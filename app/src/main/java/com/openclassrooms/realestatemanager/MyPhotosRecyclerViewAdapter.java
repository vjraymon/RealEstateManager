package com.openclassrooms.realestatemanager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MyPhotosRecyclerViewAdapter extends RecyclerView.Adapter<com.openclassrooms.realestatemanager.MyPhotosRecyclerViewAdapter.ViewHolder>{
    private static String TAG = "TestPhotosList";

    private final List<Photo> photos;

    public MyPhotosRecyclerViewAdapter(List<Photo> photos) {
        this.photos = photos;
        for (Photo i : photos) {
            Log.i(TAG, "MyPhotosRecyclerViewAdapter photo " + i.getDescription());
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public com.openclassrooms.realestatemanager.MyPhotosRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.photo, viewGroup, false);
        return new com.openclassrooms.realestatemanager.MyPhotosRecyclerViewAdapter.ViewHolder(v);
    }
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Photo photo;
        //        public final ImageView mPhoto;
        public EditText mDescription;

        public ViewHolder(View mView) {
            super(mView);
//            mPhoto = v.findViewById(R.id.joined_workmate_photo);
            mDescription = mView.findViewById(R.id.photo_list_description);
            mDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    photo.setDescription(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }

    @Override
    public void onBindViewHolder(com.openclassrooms.realestatemanager.MyPhotosRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "MyPhotosRecyclerViewAdapter.onBindViewHolder position = " + position + " : " + photos.get(position).getDescription());
        holder.photo =  photos.get(position);
        holder.mDescription.setText(photos.get(position).getDescription());

//        String p = workmates.get(position).getPhotoUrl();
//        if (p != null) {
//            Uri uri = Uri.parse(p);
//            Picasso.with(holder.mPhoto.getContext()).load(uri).into(holder.mPhoto);
//        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "MyPhotosRecyclerViewAdapter.getItemCount properties.size() = " + this.photos.size());
        return photos.size();
    }
}
