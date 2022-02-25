package com.openclassrooms.realestatemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class DisplayDetailedPropertyFragment extends Fragment {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void propertyRestore(View v) { initialization(currentProperty); }

    private void propertyNew(View v) { initialization(null); }

    private void photoNew(View v) {
        Photo photo = new Photo(getString(R.string.unknown), (currentProperty == null) ? 0 : currentProperty.getId());
        if (photos == null) photos = new ArrayList<>();
        photos.add(photo);
        initializePhotosList();
    }

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
    }

    private void initializePhotosList() {
        Log.i(TAG, "MainActivity.initializePhotosList");
        photosRecyclerView = mView.findViewById(R.id.list_photos);
        photosRecyclerView.setLayoutManager(new LinearLayoutManager(photosRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        photosRecyclerView.setAdapter(new MyPhotosRecyclerViewAdapter(photos));
    }

    // TODO should move to a ViewModel ?
    private List<Photo> readPhotosFromDb(int propertyId) {
        Log.i(TAG, "MainActivity.readPhotosFromDb");
        List<Photo> photos = new ArrayList<>();

        // Read again and checks these records
        String[] projection = {
                PropertiesDb.KEY_PHOTOROWID,
                PropertiesDb.KEY_PHOTODESCRIPTION,
                PropertiesDb.KEY_PHOTOPROPERTYID
        };
        Uri uri = Uri.parse(MyContentProvider.CONTENT_PHOTO_URI.toString());
        Context context = mView.getContext();
        Cursor cursor =  context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            Log.i(TAG, "MainActivity.readPhotosFromDb cursor null");
            return photos;
        }
        Log.i(TAG, "MainActivity.readPhotosFromDb cursor.getCount = " +cursor.getCount());
        cursor.moveToFirst();
        for (int i=0; i < cursor.getCount(); i=i+1) {
            if (propertyId == cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID))) {
                Photo photo = new Photo(
                        cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTODESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID)));
                photo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOROWID)));
                photos.add(photo);
                Log.i(TAG, "MainActivity.readPhotosFromDb read property " + photo.getDescription()+ " (" +photo.getPropertyId()+ ")");
            }
            cursor.moveToNext();
        }
        cursor.close();
        Log.i(TAG, "MainActivity.readPhotosFromDb properties.size = " +photos.size());
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