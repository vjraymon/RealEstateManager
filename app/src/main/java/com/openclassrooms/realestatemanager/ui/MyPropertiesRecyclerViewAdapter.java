package com.openclassrooms.realestatemanager.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.event.DisplayDetailedPropertyEvent;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MyPropertiesRecyclerViewAdapter extends RecyclerView.Adapter<MyPropertiesRecyclerViewAdapter.ViewHolder>{
    private final static String TAG = "TestPropertyList";

    private final List<Property> properties;

    private CardView currentCardView = null;
    private TextView currentPrice = null;

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
        public final CardView mCardView;
        public final ImageView mPhoto;
        public final TextView mType;
        public final TextView mRowId;
        public final TextView mAddress;
        public final TextView mPrice;

        public ViewHolder(View mView) {
            super(mView);
            mCardView = mView.findViewById(R.id.property_card_view);
            mCardView.setCardBackgroundColor(Color.WHITE);
            mPhoto = mView.findViewById(R.id.property_list_image);
            mType = mView.findViewById(R.id.property_list_type);
            mAddress = mView.findViewById(R.id.property_list_address);
            mRowId = mView.findViewById(R.id.property_list_rowid);
            mPrice = mView.findViewById(R.id.property_list_price);
            mView.setOnClickListener(v -> {
                Log.i(TAG, "MyPropertiesRecyclerViewAdapter.ViewHolder click on an element " + property.getAddress());
//                view.setEnabled(false);
                if (currentCardView!=null) currentCardView.setCardBackgroundColor(Color.WHITE);
                if (currentPrice!=null) currentPrice.setTextColor(Color.RED);
                currentCardView = mCardView;
                currentPrice = mPrice;
                currentCardView.setCardBackgroundColor(Color.RED);
                currentPrice.setTextColor(Color.WHITE);
                EventBus.getDefault().post(new DisplayDetailedPropertyEvent(property));
            });
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(TAG, "DisplayDetailedPropertyFragment.onBindViewHolder position = " + position + " : " + properties.get(position).getAddress());
        holder.mCardView.setCardBackgroundColor(Color.WHITE);
        holder.property =  properties.get(position);
        holder.mType.setText(properties.get(position).getType());
        holder.mAddress.setText(properties.get(position).getAddress());
        holder.mRowId.setText("(" +properties.get(position).getId()+ ")");
        holder.mPrice.setText(Property.convertPrice(properties.get(position).getPrice()));

        List<Photo> photos = Utils.readPhotosFromDb(holder.mPhoto.getContext(), properties.get(position).getId());
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
}