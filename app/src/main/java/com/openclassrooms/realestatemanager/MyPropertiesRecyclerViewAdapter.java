package com.openclassrooms.realestatemanager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
//        public final ImageView mPhoto;
        public TextView mAddress;

        public ViewHolder(View v) {
            super(v);
//            mPhoto = v.findViewById(R.id.joined_workmate_photo);
            mAddress = v.findViewById(R.id.property_list_address);
        }
        @NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(TAG, "MyPropertiesRecyclerViewAdapter.onBindViewHolder position = " + position + " : " + properties.get(position).getAddress());
        holder.mAddress.setText(properties.get(position).getAddress());

//        String p = workmates.get(position).getPhotoUrl();
//        if (p != null) {
//            Uri uri = Uri.parse(p);
//            Picasso.with(holder.mPhoto.getContext()).load(uri).into(holder.mPhoto);
//        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "MyPropertiesRecyclerViewAdapter.getItemCount properties.size() = " + this.properties.size());
        return properties.size();
    }
}