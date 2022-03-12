package com.openclassrooms.realestatemanager.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.PlaceType;
import com.openclassrooms.realestatemanager.NotificationBroadcast;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.event.DeletePhotoEvent;
import com.openclassrooms.realestatemanager.event.DisplayDetailedPropertyEvent;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.repository.MyContentProvider;
import com.openclassrooms.realestatemanager.repository.NearbySearch;
import com.openclassrooms.realestatemanager.repository.PropertiesDb;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayDetailedPropertyFragment extends Fragment implements OnMapReadyCallback { //OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {
    private static final String TAG = "TestDetailedFragment";

    public DisplayDetailedPropertyFragment() {
        Log.i(TAG, "DisplayDetailedPropertyFragment.constructor by default");
        // Required empty public constructor
    }

    TextView mRowId;
    EditText mAddress;
    EditText mType;
    EditText mSurface;
    EditText mPrice;
    EditText mRoomsNumber;
    EditText mDescription;
    EditText mStatus;
    EditText mDateBegin;
    EditText mDateEnd;
    EditText mRealEstateAgent;
    TextView mPointsOfInterest;
    Button mBtnSave;
    Button mBtnRestore;
    Button mBtnNew;
    Button mBtnAddPhoto;
    Button mBtnTakePhoto;

    Property currentProperty = null;

    List<Photo> currentPhotos;
    List<Photo> updatedPhotos;
    RecyclerView photosRecyclerView;

    List<PointOfInterest> pointsOfInterest;
    int counterOfResearch;
    StringBuilder builder;

    GoogleMap map;
    private final static int DEFAULT_ZOOM = 13;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        if (currentProperty == null) {
            Log.i(TAG, "DisplayDetailedPropertyFragment.onMapReady property not set");
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 1));
            return;
        }
        updateMap(currentProperty.getAddress());
    }

    private void updateMap(String s) {
        LatLng location = getLocationFromAddress(mView.getContext(), s);
        Log.i(TAG, "DisplayDetailedPropertyFragment.onMapReady address = " + s + " location = " + location);
        if (location == null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 1));
        } else {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.latitude, location.longitude))
                    .title("Marker"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
        }
    }

    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.display_detailed_property_fragment, container, false);
        mRowId = v.findViewById(R.id.detailed_property_rowid);
        mAddress = v.findViewById(R.id.detailed_property_address);
        mType = v.findViewById(R.id.detailed_property_type);
        mSurface = v.findViewById(R.id.detailed_property_surface);
        mPrice = v.findViewById(R.id.detailed_property_price);
        mRoomsNumber = v.findViewById(R.id.detailed_property_rooms_number);
        mDescription = v.findViewById(R.id.detailed_property_description);
        mStatus = v.findViewById(R.id.detailed_property_status);
        mDateBegin = v.findViewById(R.id.detailed_property_date_begin);
        mDateEnd = v.findViewById(R.id.detailed_property_date_end);
        mRealEstateAgent = v.findViewById(R.id.detailed_property_real_estate_agent);
        mPointsOfInterest = v.findViewById(R.id.detailed_property_points_of_interest);
        mBtnSave = v.findViewById(R.id.detailed_property_button_save);
        mBtnSave.setOnClickListener(this::propertySave);
        mBtnRestore = v.findViewById(R.id.detailed_property_button_restore);
        mBtnRestore.setOnClickListener(this::propertyRestore);
        mBtnNew = v.findViewById(R.id.detailed_property_button_new);
        mBtnNew.setOnClickListener(this::propertyNew);
        mBtnAddPhoto = v.findViewById(R.id.detailed_property_button_add_photo);
        mBtnAddPhoto.setOnClickListener(this::photoNew);
        mBtnTakePhoto = v.findViewById(R.id.detailed_property_button_take_photo);
        mBtnTakePhoto.setOnClickListener(this::photoTake);

        mView = v;
        if ((v.getContext()!=null) && (getArguments()!=null)) {
            int rowId = getArguments().getInt("rowKey");
            Log.i(TAG, "DisplayDetailedPropertyFragment.onCreateView rowId = (" +rowId+ ")");
            currentProperty = MapsFragment.getPropertyByRowId(v.getContext(), rowId);
        }
        Log.i(TAG, "DisplayDetailedPropertyFragment.onCreateView initialization("
                +((currentProperty==null) ? "null" : currentProperty.getAddress())+ ")");
        initialization(currentProperty);
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).initializePropertiesList();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "DisplayDetailedPropertyFragment.onViewCreated");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.miniMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                updateMap(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    // TODO to be move in utils ?
    public static LatLng getLocationFromAddress(Context context, String strAddress) {
        LatLng p1 = null;

        try {
            Geocoder coder = new Geocoder(context);
            List<Address> address;
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if ((address == null) || (address.size() < 1)) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception e) {
            Log.d(TAG, "DisplayDetailedPropertyFragment.getLocationFromAddress Exception", e);
        }

        return p1;
    }

    private void propertySave(View v) {
        if (currentProperty == null) currentProperty = new Property();
        currentProperty.setAddress(mAddress.getText().toString());
        currentProperty.setType(mType.getText().toString());
        currentProperty.setSurface(Property.convertSurfaceString(mSurface.getText().toString()));
        currentProperty.setPrice(Property.convertPriceString(mPrice.getText().toString()));
        currentProperty.setRoomsNumber(Property.convertRoomsNumberString(mRoomsNumber.getText().toString()));
        currentProperty.setDescription(mDescription.getText().toString());
        currentProperty.setStatus(Property.convertPropertyStatusString(mStatus.getText().toString()));
        currentProperty.setDateBegin(Property.convertDateString(mDateBegin.getText().toString()));
        currentProperty.setDateEnd(Property.convertDateString(mDateEnd.getText().toString()));
        currentProperty.setRealEstateAgent(mRealEstateAgent.getText().toString());
        currentProperty.setPointsOfInterest(mPointsOfInterest.getText().toString());
        ContentValues values = new ContentValues();
        values.put(PropertiesDb.KEY_PROPERTYADDRESS, currentProperty.getAddress());
        values.put(PropertiesDb.KEY_PROPERTYTYPE, currentProperty.getType());
        values.put(PropertiesDb.KEY_PROPERTYSURFACE, currentProperty.getSurface());
        values.put(PropertiesDb.KEY_PROPERTYPRICE, currentProperty.getPrice());
        values.put(PropertiesDb.KEY_PROPERTYROOMSNUMBER, currentProperty.getRoomsNumber());
        values.put(PropertiesDb.KEY_PROPERTYDESCRIPTION, currentProperty.getDescription());
        values.put(PropertiesDb.KEY_PROPERTYSTATUS, Property.convertPropertyStatus(currentProperty.getStatus()));
        values.put(PropertiesDb.KEY_PROPERTYDATEBEGIN, Property.convertDate(currentProperty.getDateBegin()));
        values.put(PropertiesDb.KEY_PROPERTYDATEEND, Property.convertDate(currentProperty.getDateEnd()));
        values.put(PropertiesDb.KEY_PROPERTYREALESTATEAGENT, currentProperty.getRealEstateAgent());
        values.put(PropertiesDb.KEY_PROPERTYPOINTSOFINTEREST, currentProperty.getPointsOfInterest());
        Log.i(TAG, "DisplayDetailedPropertyFragment.propertySave Id = " + currentProperty.getId());
        if (currentProperty.getId() == 0) {
            v.getContext().getContentResolver().insert(MyContentProvider.CONTENT_PROPERTY_URI, values);
            String[] projection = {
                    PropertiesDb.KEY_PROPERTYROWID
            };
            Uri uri = Uri.parse(MyContentProvider.CONTENT_PROPERTY_URI.toString());
            Cursor cursor = v.getContext().getContentResolver().query(uri, projection, null, null, null);
            if (cursor == null) {
                return;
            }
            cursor.moveToLast();
            currentProperty.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROWID)));
            cursor.close();
        } else {
            Uri uri = Uri.parse(MyContentProvider.CONTENT_PROPERTY_URI.toString() + "/" + currentProperty.getId());
            v.getContext().getContentResolver().update(uri, values, null, null);
        }

        // Delete the photo records
        for (Photo photo : currentPhotos) if ((photo!=null) &&(photo.getId()!=0)){
            Uri uriPhotoDelete = Uri.parse(MyContentProvider.CONTENT_PHOTO_URI.toString() + "/" + photo.getId());
            v.getContext().getContentResolver().delete(uriPhotoDelete, null, null);
        }
        // Insert again all the photos
        currentPhotos = updatedPhotos ;
        if (currentPhotos!=null) for (Photo photo : currentPhotos) if (photo!= null) {
            ContentValues photoValues = new ContentValues();
            photoValues.put(PropertiesDb.KEY_PHOTOIMAGE, Photo.getBytesFromBitmap(photo.getImage()));
            photoValues.put(PropertiesDb.KEY_PHOTODESCRIPTION, photo.getDescription());
            photoValues.put(PropertiesDb.KEY_PHOTOPROPERTYID, currentProperty.getId());
 //           if (photo.getId() == 0) {
                v.getContext().getContentResolver().insert(MyContentProvider.CONTENT_PHOTO_URI, photoValues);
                String[] projection = {
                        PropertiesDb.KEY_PHOTOROWID
                };
                Uri uri = Uri.parse(MyContentProvider.CONTENT_PHOTO_URI.toString());
                Cursor cursor = v.getContext().getContentResolver().query(uri, projection, null, null, null);
                if (cursor == null) {
                    return;
                }
                cursor.moveToLast();
                photo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOROWID)));
                cursor.close();
//           } else {
//               Uri uri = Uri.parse(MyContentProvider.CONTENT_PHOTO_URI.toString() + "/" + photo.getId());
//               v.getContext().getContentResolver().insert(uri, photoValues);
//            }
        }

        initialization(currentProperty);
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).initializePropertiesList();

        createNotification(mView, currentProperty.getAddress());
    }

    public void createNotification(View view, String m) {
        Intent i = new Intent(getActivity(), NotificationBroadcast.class);
        i.putExtra("message", m);
        if (getActivity() != null) getActivity().sendBroadcast(i);
    }

    private void propertyRestore(View v) {
        initialization(currentProperty);
    }

    private void propertyNew(View v) {
        initialization(null);
    }

    private void photoNew(View v) {
        Log.i(TAG, "DisplayDetailedPropertyFragment.photoNew");
        getPhoto.launch("image/*");
    }

    private final ActivityResultLauncher<String> getPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    Log.i(TAG, "DisplayDetailedPropertyFragment.ActivityResultCallback uri " + ((uri == null) ? "null" : uri.toString()));
                    if (uri == null) {
                        Log.i(TAG, "DisplayDetailedPropertyFragment.onActivityResult uri = null");
                        Photo photo = new Photo(null, getString(R.string.unknown), (currentProperty == null) ? 0 : currentProperty.getId());
                        if (updatedPhotos == null) {
                            updatedPhotos = new ArrayList<>();
                            for (Photo i: currentPhotos) if (i!=null) { updatedPhotos.add(i); }
                        }
                        updatedPhotos.add(photo);
                        initializePhotosList();
                        return;
                    }
                    Bitmap bitmap = null;
                    try {
                        if (getContext()!=null) bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri));
                    } catch (Exception e) {
                        Log.e(TAG, "DisplayDetailedPropertyFragment.onActivityResult exception : ", e);
                    }
                    Log.i(TAG, "DisplayDetailedPropertyFragment.onActivityResult bitmap: " + ((bitmap == null) ? "null" : "not null"));
                    Photo photo = new Photo(scaledBitmap(bitmap), getString(R.string.unknown), (currentProperty == null) ? 0 : currentProperty.getId());
                    if (updatedPhotos == null) {
                        updatedPhotos = new ArrayList<>();
                        for (Photo i: currentPhotos) if (i!=null) { updatedPhotos.add(i); }
                    }
                    updatedPhotos.add(photo);
                    initializePhotosList();
                }
            });

    private void photoTake(View v) {
        Log.i(TAG, "DisplayDetailedPropertyFragment.photoTake");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
           takePhoto.launch(intent);
//        }
    }

    private final ActivityResultLauncher<Intent> takePhoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null
                            && result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null
                            && result.getData().getExtras() != null
                            && result.getData().getExtras().get("data") != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        Log.i(TAG, "DisplayDetailedPropertyFragment.onActivityResult.onBitmapLoaded bitmap: " + ((bitmap == null) ? "null" : "not null"));
                        Photo photo = new Photo(scaledBitmap(bitmap), getString(R.string.unknown), (currentProperty == null) ? 0 : currentProperty.getId());
                        if (updatedPhotos == null) {
                            updatedPhotos = new ArrayList<>();
                            for (Photo i: currentPhotos) if (i!=null) { updatedPhotos.add(i); }
                        }
                        updatedPhotos.add(photo);
                        initializePhotosList();
                    }
                }
            });

    private Bitmap scaledBitmap(Bitmap bitmap) {
        if (bitmap==null) return null;
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();
        final double sizeMax = 256.;
                            Log.i(TAG,"DisplayDetailedPropertyFragment.onActivityResult.onBitmapLoaded initial bitmap scale = ("+w +","+h +")");
                            if((h >sizeMax)&&(w >sizeMax))

        {
            double t = (h > w) ? (sizeMax / w) : (sizeMax / h);
            Log.i(TAG, "DisplayDetailedPropertyFragment.onActivityResult.onBitmapLoaded bitmap scaling = (" + w + "," + h + ")*" + t);
            h = (int) Math.round(h * t);
            w = (int) Math.round(w * t);
        }
        return Bitmap.createScaledBitmap(bitmap,w,h,false);
    }

    public void initialization(Property property) {
        Log.i(TAG, "DisplayDetailedPropertyFragment.initialization " + ((property==null) ? "null" : property.getAddress()));
        currentProperty = property;
        mRowId.setText(((property == null) || (property.getId() == 0))
                ? getString(R.string.modify) + ((property == null) ? "" : "(0)")
                : String.format(getString(R.string.display_modify), property.getId()));
        mAddress.setText(((property == null) || (property.getAddress() == null))
                ? getString(R.string.unknown)
                : property.getAddress());
        mType.setText(((property == null) || (property.getType() == null))
                ? getString(R.string.unknown)
                : property.getType());
        mSurface.setText((property == null)
                ? getString(R.string.unknown)
                : Property.convertSurface(property.getSurface()));
        mPrice.setText((property == null)
                ? getString(R.string.unknown)
                : Property.convertPrice(property.getPrice()));
        mRoomsNumber.setText((property == null)
                ? getString(R.string.unknown)
                : String.valueOf(property.getRoomsNumber()));
        mDescription.setText(((property == null) || (property.getDescription() == null))
                ? getString(R.string.unknown)
                : property.getDescription());
        mStatus.setText(((property == null) || (property.getStatus() == null))
                ? getString(R.string.unknown)
                : Property.convertPropertyStatus(property.getStatus()));
        mDateBegin.setText(((property == null) || (Property.convertDate(property.getDateBegin()) == null))
                ? getString(R.string.unknown)
                : Property.convertDate(property.getDateBegin()));
        mDateEnd.setText(((property == null) || (Property.convertDate(property.getDateEnd()) == null))
                ? getString(R.string.unknown)
                : Property.convertDate(property.getDateEnd()));
        mRealEstateAgent.setText(((property == null) || (property.getRealEstateAgent() == null))
                ? getString(R.string.unknown)
                : property.getRealEstateAgent());

        currentPhotos = ((property == null) || (property.getId() == 0))
                ? new ArrayList<>() // clear the list of photos
                : Utils.readPhotosFromDb(mView.getContext(), property.getId());
        updatedPhotos =  ((property == null) || (property.getId() == 0))
                ? new ArrayList<>() // clear the list of photos
                : Utils.readPhotosFromDb(mView.getContext(), property.getId());

        initializePhotosList();

        if (map!=null) onMapReady(map);

        Log.i(TAG, "DisplayDetailedPropertyFragment.initialization property.getPointsOfInterest(): " + ((property == null) ? "null" : property.getPointsOfInterest()));
        if (property == null) mPointsOfInterest.setText("");
        else if ((property.getPointsOfInterest()!=null) && !property.getPointsOfInterest().isEmpty())
            mPointsOfInterest.setText(property.getPointsOfInterest());
        else {
            mPointsOfInterest.setText("");
            if (mView.getContext()!=null) {
                pointsOfInterest = new ArrayList<>();
                LatLng location = getLocationFromAddress(mView.getContext(), property.getAddress());
                NearbySearch nearbySearch = new NearbySearch();
                if (location != null) {
                    builder = new StringBuilder();
                    counterOfResearch = 3;
                    nearbySearch.run(mView.getContext(), location.latitude, location.longitude, PlaceType.PRIMARY_SCHOOL, this::getPoi);
                    nearbySearch.run(mView.getContext(), location.latitude, location.longitude, PlaceType.SECONDARY_SCHOOL, this::getPoi);
                    nearbySearch.run(mView.getContext(), location.latitude, location.longitude, PlaceType.STORE, this::getPoi);
                }
            }
        }

    }

    private void getPoi(List<HashMap<String, String>> h) {
        Log.i(TAG, "DisplayDetailedPropertyFragment.initialization number of POI = " + h.size());
        for (HashMap<String, String> l : h) {
            PointOfInterest p = new PointOfInterest(
                    null,
                    l.get("name"),
                    currentProperty.getId());
            builder.append(l.get("name"));
            builder.append(" -- ");
            pointsOfInterest.add(p); // TODO check if the point of interest is not redundant
            Log.i(TAG, "DisplayDetailedPropertyFragment.initialization POI = " + p.getName());
        }
        counterOfResearch--;
        if (counterOfResearch <= 0) mPointsOfInterest.setText(builder);
    }

    public void initializePhotosList() {
        Log.i(TAG, "DisplayDetailedPropertyFragment.initializePhotosList");
        photosRecyclerView = mView.findViewById(R.id.list_photos);
        photosRecyclerView.setLayoutManager(new LinearLayoutManager(photosRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        photosRecyclerView.setAdapter(new MyPhotosRecyclerViewAdapter(updatedPhotos));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDisplayDetailedProperty(DisplayDetailedPropertyEvent event) {
        if (event.property != null)
        {
            Log.i(TAG, "DisplayDetailedPropertyFragment.onDisplayDetailedProperty property = " + event.property.getAddress());
            initialization(event.property);
        }
    }

    @Subscribe
    public void onDeletePhoto(DeletePhotoEvent event) {
        Log.i(TAG, "DisplayDetailedPropertyFragment.onDeletePhoto position = " + event.position);
        CustomConfirm.ConfirmListener listener = () -> {
            updatedPhotos.remove(event.position);
            DisplayDetailedPropertyFragment.this.initializePhotosList();
        };
        final CustomConfirm dialog = new CustomConfirm(getContext(), listener, getString(R.string.confirm_suppress_photo));
        dialog.show();
    }
}