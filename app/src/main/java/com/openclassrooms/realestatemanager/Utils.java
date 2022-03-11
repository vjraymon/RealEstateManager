package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.repository.MyContentProvider;
import com.openclassrooms.realestatemanager.repository.PropertiesDb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {
private final static String TAG = "TstUtils";

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    public static int convertEuroToDollar(int euros){ return (int) Math.round(euros / 0.812); }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */

    public static Boolean isInternetAvailable(Context context){
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    public static boolean isOnLine(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return ((capabilities != null)
                    && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)));
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo networkInfo : info)
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
            return false;
        }
    }

    public static List<Property> readPropertiesFromDb(@NonNull Context context) {
        Log.i(TAG, "MainActivity.readPropertiesFromDb");
        List<Property> properties = new ArrayList<>();

        // Read again and checks these records
        String[] projection = {
                PropertiesDb.KEY_PROPERTYROWID,
                PropertiesDb.KEY_PROPERTYADDRESS,
                PropertiesDb.KEY_PROPERTYTYPE,
                PropertiesDb.KEY_PROPERTYSURFACE,
                PropertiesDb.KEY_PROPERTYPRICE,
                PropertiesDb.KEY_PROPERTYROOMSNUMBER,
                PropertiesDb.KEY_PROPERTYDESCRIPTION,
                PropertiesDb.KEY_PROPERTYSTATUS,
                PropertiesDb.KEY_PROPERTYDATEBEGIN,
                PropertiesDb.KEY_PROPERTYDATEEND,
                PropertiesDb.KEY_PROPERTYREALESTATEAGENT,
                PropertiesDb.KEY_PROPERTYPOINTSOFINTEREST
        };
        Uri uri = Uri.parse(MyContentProvider.CONTENT_PROPERTY_URI.toString());
        Cursor cursor =  context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            Log.i(TAG, "Utils.readPropertiesFromDb cursor null");
            return properties;
        }
        Log.i(TAG, "Utils.readPropertiesFromDb cursor.getCount = " +cursor.getCount());
        cursor.moveToFirst();
        for (int i=0; i < cursor.getCount(); i=i+1) {
            Property property = new Property(
                    cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYTYPE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYSURFACE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYPRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROOMSNUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDESCRIPTION)),
                    Property.convertPropertyStatusString(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYSTATUS))),
                    Property.convertDateString(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEBEGIN))),
                    Property.convertDateString(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEEND))),
                    cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYREALESTATEAGENT)));
            property.setPointsOfInterest(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYPOINTSOFINTEREST)));
            property.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROWID)));
            properties.add(property);
            Log.i(TAG, "Utils.readPropertiesFromDb read property " +property.getAddress()+
                    " (" +cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROWID))+ ")");
            cursor.moveToNext();
        }
        cursor.close();
        Log.i(TAG, "Utils.readPropertiesFromDb properties.size = " +properties.size());
        return properties;
    }

    public static List<Photo> readPhotosFromDb(@NonNull Context context, int propertyId) {
        Log.i(TAG, "Utils.readPhotosFromDb");
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
            Log.i(TAG, "Utils.readPhotosFromDb cursor null");
            return photos;
        }
        Log.i(TAG, "Utils.readPhotosFromDb cursor.getCount = " +cursor.getCount());
        cursor.moveToFirst();
        for (int i=0; i < cursor.getCount(); i=i+1) {
            if (propertyId == cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID))) {
                Photo photo = new Photo(
                        Photo.getBitmapFromBytes(cursor.getBlob(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOIMAGE))),
                        cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTODESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID)));
                photo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOROWID)));
                photos.add(photo);
                Log.i(TAG, "Utils.readPhotosFromDb read property " + photo.getDescription()+ " (" +photo.getPropertyId()+ ")");
            }
            cursor.moveToNext();
        }
        cursor.close();
        Log.i(TAG, "Utils.readPhotosFromDb properties.size = " +photos.size());
        return photos;
    }
/*
    public static List<Photo> readPhotosFromDb(int propertyId, Context context) {
        Log.i(TAG, "Utils.readPhotosFromDb");
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
            Log.i(TAG, "Utils.readPhotosFromDb cursor null");
            return photos;
        }
        Log.i(TAG, "Utils.readPhotosFromDb cursor.getCount = " +cursor.getCount());
        cursor.moveToFirst();
        for (int i=0; i < cursor.getCount(); i=i+1) {
            if (propertyId == cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID))) {
                Photo photo = new Photo(
                        Photo.getBitmapFromBytes(cursor.getBlob(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOIMAGE))),
                        cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTODESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID)));
                photo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOROWID)));
                photos.add(photo);
                Log.i(TAG, "Utils.readPhotosFromDb read property " + photo.getDescription()+ " (" +photo.getPropertyId()+ ")");
            }
            cursor.moveToNext();
        }
        cursor.close();
        Log.i(TAG, "Utils.readPhotosFromDb properties.size = " +photos.size());
        return photos;
    }
*/
}

