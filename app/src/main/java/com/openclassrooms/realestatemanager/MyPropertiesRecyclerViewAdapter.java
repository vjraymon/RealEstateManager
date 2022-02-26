package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyPropertiesRecyclerViewAdapter extends RecyclerView.Adapter<MyPropertiesRecyclerViewAdapter.ViewHolder>{
    private static String TAG = "TestPropertyList";

    private final List<Property> properties;

    public MyPropertiesRecyclerViewAdapter(List<Property> properties) {
        this.properties = properties;
        for (Property i : properties) {
            Log.i(TAG, "MyPropertiesRecyclerViewAdapter property " + i.getAddress());
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.property, viewGroup, false);
        return new ViewHolder(v);
    }
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Property property;
        public ImageView mPhoto;
        public TextView mType;
        public TextView mRowId;
        public TextView mAddress;
        public TextView mPrice;

        public ViewHolder(View mView) {
            super(mView);
            mPhoto = mView.findViewById(R.id.property_list_image);
            mType = mView.findViewById(R.id.property_list_type);
            mAddress = mView.findViewById(R.id.property_list_address);
            mRowId = mView.findViewById(R.id.property_list_rowid);
            mPrice = mView.findViewById(R.id.property_list_price);
            mView.setOnClickListener(v -> {
                Log.i(TAG, "MyPropertiesRecyclerViewAdapter.ViewHolder click on an element " + property.getAddress());
//                view.setEnabled(false);
                EventBus.getDefault().post(new DisplayDetailedPropertyEvent(property));
            });
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(TAG, "DisplayDetailedPropertyFragment.onBindViewHolder position = " + position + " : " + properties.get(position).getAddress());
        holder.property =  properties.get(position);
        holder.mType.setText(properties.get(position).getType());
        holder.mAddress.setText(properties.get(position).getAddress());
        holder.mRowId.setText("(" +properties.get(position).getId()+ ")");
        holder.mPrice.setText(Property.convertPrice(properties.get(position).getPrice()));

        List<Photo> photos = readPhotosFromDb(properties.get(position).getId(), holder.mPhoto.getContext());
        Bitmap bitmap = null;
        if ((photos.size() > 0) && (photos.get(0) != null)) bitmap = photos.get(0).getImage();
        if (bitmap == null) {
            Log.i(TAG, "DisplayDetailedPropertyFragment.onBindViewHolder position = " + position + " : image null");
            bitmap = Photo.getBitmapFromVectorDrawable(holder.mPhoto.getContext(), R.drawable.ic_launcher_background);
        }
        holder.mPhoto.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "MyPropertiesRecyclerViewAdapter.getItemCount properties.size() = " + this.properties.size());
        return properties.size();
    }

    // TODO should move to a ViewModel ?
    private List<Photo> readPhotosFromDb(int propertyId, Context context) {
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
}