package com.openclassrooms.realestatemanager.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.io.ByteArrayOutputStream;

public class Photo {
//    private static final String TAG = "TestPhoto";

    private int _id;
    private Bitmap image;
    private String description;
    private int propertyId;

    public Photo(Bitmap image, String description, int propertyId) {
        this._id = 0; // To be set by SQLite
        this.image = image;
        this.description = description;
        this.propertyId = propertyId;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable==null) return null;
 //       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
 //           drawable = (DrawableCompat.wrap(drawable)).mutate();
 //       }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    // convert from bitmap to byte array
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getBitmapFromBytes(byte[] image) {
        if (image == null) return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}

