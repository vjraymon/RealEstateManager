package com.openclassrooms.realestatemanager;

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
/*
    public static DisplayDetailedPropertyFragment newInstance(String param1, String param2) {
        DisplayDetailedPropertyFragment fragment = new DisplayDetailedPropertyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
*/
    }

    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.display_detailed_property_fragment, container, false);
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

        mView = v;
        initialization(null);
        return v;
    }

    private void initialization(Property property) {
        mAddress.setText(((property == null) || (property.getAddress() == null))
                ? "Address unknown"
                : "Address : " +property.getAddress());
        mType.setText(((property == null) || (property.getType() == null))
                ? "Type unknown"
                : "Type : " +property.getType());
        mSurface.setText((property == null)
                ? "Surface unknown"
                : "Surface : " +property.getSurface()+ " m²");
        mPrice.setText((property == null)
                ? "Price unknown"
                : "Price : " +property.getPrice()+ " $");
        mRoomsNumber.setText((property == null)
                ? "Rooms number unknown"
                : "Rooms number : " +property.getRoomsNumber());
        mDescription.setText(((property == null) || (property.getDescription() == null))
                ? "Description unknown"
                : "Description : " +property.getDescription());
        mStatus.setText(((property == null) || (property.getStatus() == null))
                ? "Status unknown"
                : "Status : " +property.getStatus());
        mDateBegin.setText(((property == null) || (Property.convertDate(property.getDateBegin()) == null))
                ? "Date begin unknown"
                : "Date begin : " +Property.convertDate(property.getDateBegin()));
        mDateEnd.setText(((property == null) || (Property.convertDate(property.getDateEnd()) == null))
                ? "Date end unknown"
                : "Date end : " +Property.convertDate(property.getDateEnd()));
        mRealEstateAgent.setText(((property == null) || (property.getRealEstateAgent() == null))
                ? "Real estate agent unknown"
                : "Real estate agent name : " +property.getRealEstateAgent());
        if (property != null) initializePhotosList(property.getId());
    }

    List<Photo> photos;
    RecyclerView recyclerView;

    private void initializePhotosList(int propertyId) {
        Log.i(TAG, "MainActivity.initializePhotosList");
        if (propertyId != 0) {
            photos = readPhotosFromDb(propertyId);

            recyclerView = mView.findViewById(R.id.list_photos);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(new MyPhotosRecyclerViewAdapter(photos));
        }
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