package com.openclassrooms.realestatemanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class DisplayDetailedPropertyFragment extends Fragment implements OnMapReadyCallback { //OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {
    private static final String TAG = "TestDetailedFragment";

    public DisplayDetailedPropertyFragment() {
        // Required empty public constructor
    }

    TextView mRowId;
    TextView mAddress;
    TextView mType;
    TextView mSurface;
    TextView mPrice;
    TextView mRoomsNumber;
    TextView mDescription;
    TextView mStatus;
    TextView mDateBegin;
    TextView mDateEnd;
    TextView mRealEstateAgent;
    Button mBtnSave;
    Button mBtnRestore;
    Button mBtnNew;
    Button mBtnAddPhoto;

    Property currentProperty = null;

    List<Photo> photos;
    RecyclerView photosRecyclerView;

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
        if (currentProperty == null) {
            Log.i(TAG, "DisplayDetailedPropertyFragment.onMapReady property not set");
            return;
        }
        LatLng location = getLocationFromAddress(mView.getContext(), currentProperty.getAddress());
        Log.i(TAG, "DisplayDetailedPropertyFragment.onMapReady address = " +currentProperty.getAddress()+ " location = " +location);
        if (location != null) {
            googleMap.addMarker(new MarkerOptions()
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
        mBtnSave = v.findViewById(R.id.detailed_property_button_save);
        mBtnSave.setOnClickListener(this::propertySave);
        mBtnRestore = v.findViewById(R.id.detailed_property_button_restore);
        mBtnRestore.setOnClickListener(this::propertyRestore);
        mBtnNew = v.findViewById(R.id.detailed_property_button_new);
        mBtnNew.setOnClickListener(this::propertyNew);
        mBtnAddPhoto = v.findViewById(R.id.detailed_property_button_add_photo);
        mBtnAddPhoto.setOnClickListener(this::photoNew);

        mView = v;
        initialization(null);

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
    }

    // TODO to be move in utils ?
    public static LatLng getLocationFromAddress(Context context,String strAddress) {
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
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception e) {
            Log.d(TAG,"DisplayDetailedPropertyFragment.getLocationFromAddress Exception", e);
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
        Log.i(TAG,"DisplayDetailedPropertyFragment.propertySave Id = " + currentProperty.getId());
        if (currentProperty.getId() == 0) {
            v.getContext().getContentResolver().insert(MyContentProvider.CONTENT_PROPERTY_URI, values);
            String[] projection = {
                    PropertiesDb.KEY_PROPERTYROWID
            };
            Uri uri = Uri.parse(MyContentProvider.CONTENT_PROPERTY_URI.toString());
            Cursor cursor =  v.getContext().getContentResolver().query(uri, projection, null, null, null);
            if (cursor == null) {
                return;
            }
            cursor.moveToLast();
            currentProperty.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROWID)));
            cursor.close();
        } else {
            Uri uri = Uri.parse(MyContentProvider.CONTENT_PROPERTY_URI.toString() + "/" + currentProperty.getId());
            v.getContext().getContentResolver().update(uri , values, null, null);
        }

        for (Photo photo : photos) {
            ContentValues photoValues = new ContentValues();
            photoValues.put(PropertiesDb.KEY_PHOTOIMAGE, Photo.getBytesFromBitmap(photo.getImage()));
            photoValues.put(PropertiesDb.KEY_PHOTODESCRIPTION, photo.getDescription());
            photoValues.put(PropertiesDb.KEY_PHOTOPROPERTYID, currentProperty.getId());
            if (photo.getId() == 0) {
                v.getContext().getContentResolver().insert(MyContentProvider.CONTENT_PHOTO_URI, photoValues);
                String[] projection = {
                        PropertiesDb.KEY_PHOTOROWID
                };
                Uri uri = Uri.parse(MyContentProvider.CONTENT_PHOTO_URI.toString());
                Cursor cursor =  v.getContext().getContentResolver().query(uri, projection, null, null, null);
                if (cursor == null) {
                    return;
                }
                cursor.moveToLast();
                photo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOROWID)));
                cursor.close();
            } else {
                Uri uri = Uri.parse(MyContentProvider.CONTENT_PHOTO_URI.toString() + "/" + photo.getId());
                v.getContext().getContentResolver().update(uri , photoValues, null, null);
            }
        }

        initialization(currentProperty);
        if (getActivity() instanceof MainActivity) ((MainActivity)getActivity()).initializePropertiesList();

        createNotification(mView, currentProperty.getAddress());
    }

    public void createNotification(View view, String m) {
        Intent i = new Intent(getActivity(), NotificationBroadcast.class);
        i.putExtra("message",m);
        if (getActivity() != null) getActivity().sendBroadcast(i);
    }

    private void propertyRestore(View v) { initialization(currentProperty); }

    private void propertyNew(View v) { initialization(null); }

    private void photoNew(View v) {
        Log.i(TAG,"DisplayDetailedPropertyFragment.photoNew");
        getPhoto.launch("image/*");
    }

    private final ActivityResultLauncher<String> getPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri==null) {
                        Log.i(TAG,"DisplayDetailedPropertyFragment.onActivityResult uri = null");
                        Photo photo = new Photo(null, getString(R.string.unknown), (currentProperty == null) ? 0 : currentProperty.getId());
                        if (photos == null) photos = new ArrayList<>();
                        photos.add(photo);
                        initializePhotosList();
                        return;
                    }
                    Picasso.get().load(uri.toString()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Log.i(TAG,"DisplayDetailedPropertyFragment.onActivityResult.onBitmapLoaded bitmap: " + ((bitmap==null) ? "null": "not null"));
                            Bitmap scaledBitmap = null;
                            if (bitmap != null) {
                                int h = bitmap.getHeight();
                                int w = bitmap.getWidth();
                                final double sizeMax = 256.;
                                Log.i(TAG, "DisplayDetailedPropertyFragment.onActivityResult.onBitmapLoaded initial bitmap scale = (" + w + "," + h + ")");
                                if ((h > sizeMax) && (w > sizeMax)) {
                                    double t = (h > w) ? (sizeMax / w) : (sizeMax / h);
                                    Log.i(TAG, "DisplayDetailedPropertyFragment.onActivityResult.onBitmapLoaded bitmap scaling = (" + w + "," + h + ")*" + t);
                                    h = (int) Math.round(h * t);
                                    w = (int) Math.round(w * t);
                                }
                                scaledBitmap = Bitmap.createScaledBitmap(bitmap, w, h, false);
                            }
                            Photo photo = new Photo(scaledBitmap, getString(R.string.unknown), (currentProperty == null) ? 0 : currentProperty.getId());
                            if (photos == null) photos = new ArrayList<>();
                            photos.add(photo);
                            initializePhotosList();
                        }
                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            Log.d(TAG,"DisplayDetailedPropertyFragment.onActivityResult.onBitmapLoaded exception ", e);
                        }
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {}
                    });
                }
            });

    private void initialization(Property property) {
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

        photos = ((property == null) || (property.getId() == 0))
                ? new ArrayList<>() // clear the list of photos
                : readPhotosFromDb(property.getId());
        initializePhotosList();
        if (map != null) onMapReady(map);
    }

    public void initializePhotosList() {
        Log.i(TAG, "DisplayDetailedPropertyFragment.initializePhotosList");
        photosRecyclerView = mView.findViewById(R.id.list_photos);
        photosRecyclerView.setLayoutManager(new LinearLayoutManager(photosRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        photosRecyclerView.setAdapter(new MyPhotosRecyclerViewAdapter(photos));
    }

    // TODO should move to a ViewModel ?
    private List<Photo> readPhotosFromDb(int propertyId) {
        Log.i(TAG, "DisplayDetailedPropertyFragment.readPhotosFromDb");
        List<Photo> photos = new ArrayList<>();

        // Read again and checks these records
        String[] projection = {
                PropertiesDb.KEY_PHOTOROWID,
                PropertiesDb.KEY_PHOTOIMAGE,
                PropertiesDb.KEY_PHOTODESCRIPTION,
                PropertiesDb.KEY_PHOTOPROPERTYID
        };
        Uri uri = Uri.parse(MyContentProvider.CONTENT_PHOTO_URI.toString());
        Context context = mView.getContext();
        Cursor cursor =  context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            Log.i(TAG, "DisplayDetailedPropertyFragment.readPhotosFromDb cursor null");
            return photos;
        }
        Log.i(TAG, "DisplayDetailedPropertyFragment.readPhotosFromDb cursor.getCount = " +cursor.getCount());
        cursor.moveToFirst();
        for (int i=0; i < cursor.getCount(); i=i+1) {
            if (propertyId == cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID))) {
                Photo photo = new Photo(
                        Photo.getBitmapFromBytes(cursor.getBlob(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOIMAGE))),
                        cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTODESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID)));
                photo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOROWID)));
                photos.add(photo);
                Log.i(TAG, "DisplayDetailedPropertyFragment.readPhotosFromDb read property " + photo.getDescription()+ " (" +photo.getPropertyId()+ ")");
            }
            cursor.moveToNext();
        }
        cursor.close();
        Log.i(TAG, "DisplayDetailedPropertyFragment.readPhotosFromDb properties.size = " +photos.size());
        return photos;
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
}