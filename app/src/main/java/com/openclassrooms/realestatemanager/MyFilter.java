package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.repository.MyContentProvider;
import com.openclassrooms.realestatemanager.repository.PropertiesDb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyFilter {
    private final static String TAG = "TestMyFilter";

    private boolean filter_minimum_price_presence = false;
    private int filter_minimum_price = 0;
    public void setMinimumPrice(boolean filter_minimum_price_presence, int filter_minimum_price) {
        this.filter_minimum_price_presence = filter_minimum_price_presence;
        this.filter_minimum_price = filter_minimum_price;
    }
    public boolean getMinimumPricePresence() { return filter_minimum_price_presence; }
    public int getMinimumPrice() { return filter_minimum_price; }
    private boolean filter_maximum_price_presence = false;
    private int filter_maximum_price = 0;
    public void setMaximumPrice(boolean filter_maximum_price_presence, int filter_maximum_price) {
        this.filter_maximum_price_presence = filter_maximum_price_presence;
        this.filter_maximum_price = filter_maximum_price;
    }
    public boolean getMaximumPricePresence() { return filter_maximum_price_presence; }
    public int getMaximumPrice() { return filter_maximum_price; }

    private boolean filter_minimum_surface_presence = false;
    private int filter_minimum_surface = 0;
    public void setMinimumSurface(boolean filter_minimum_surface_presence, int filter_minimum_surface) {
        this.filter_minimum_surface_presence = filter_minimum_surface_presence;
        this.filter_minimum_surface = filter_minimum_surface;
    }
    public boolean getMinimumSurfacePresence() { return filter_minimum_surface_presence; }
    public int getMinimumSurface() { return filter_minimum_surface; }
    private boolean filter_maximum_surface_presence = false;
    private int filter_maximum_surface = 0;
    public void setMaximumSurface(boolean filter_maximum_surface_presence, int filter_maximum_surface) {
        this.filter_maximum_surface_presence = filter_maximum_surface_presence;
        this.filter_maximum_surface = filter_maximum_surface;
    }
    public boolean getMaximumSurfacePresence() { return filter_maximum_surface_presence; }
    public int getMaximumSurface() { return filter_maximum_surface; }

    private boolean filter_minimum_number_photos_presence = false;
    private int filter_minimum_number_photos = 0;
    public void setMinimumNumberPhotos(boolean filter_minimum_number_photos_presence, int filter_minimum_number_photos) {
        this.filter_minimum_number_photos_presence = filter_minimum_number_photos_presence;
        this.filter_minimum_number_photos = filter_minimum_number_photos;
    }
    public boolean getMinimumNumberPhotosPresence() { return filter_minimum_number_photos_presence; }
    public int getMinimumNumberPhotos() { return filter_minimum_number_photos; }

    private boolean filter_sector_presence = false;
    private String filter_sector = null;
    public void setSector(boolean filter_sector_presence, String filter_sector) {
        this.filter_sector_presence = filter_sector_presence;
        this.filter_sector = filter_sector;
    }
    public boolean getSectorPresence() { return filter_sector_presence; }
    public String getSector() { return filter_sector; }

    private boolean filter_date_begin_presence = false;
    private Date filter_date_begin = null;
    public void setDateBegin(boolean filter_date_begin_presence, Date filter_date_begin) {
        this.filter_date_begin_presence = filter_date_begin_presence;
        this.filter_date_begin = filter_date_begin;
    }
    public boolean getDateBeginPresence() { return filter_date_begin_presence; }
    public Date getDateBegin() { return filter_date_begin; }

    private boolean filter_date_end_presence = false;
    private Date filter_date_end = null;
    public void setDateEnd(boolean filter_date_end_presence, Date filter_date_end) {
        this.filter_date_end_presence = filter_date_end_presence;
        this.filter_date_end = filter_date_end;
    }
    public boolean getDateEndPresence() { return filter_date_end_presence; }
    public Date getDateEnd() { return filter_date_end; }

    private boolean filter_point_of_interest_presence = false;
    private int filter_point_of_interest = 0;
    public void setPointOfInterest(boolean filter_point_of_interest_presence, int filter_point_of_interest) {
        this.filter_point_of_interest_presence = filter_point_of_interest_presence;
        this.filter_point_of_interest = filter_point_of_interest;
    }
    public boolean getPointOfInterestPresence() { return filter_point_of_interest_presence; }
    public int getPointOfInterest() { return filter_point_of_interest; }

    private String selection = "";
    private String[] selectionArgs = {};

    public String getSelection() { return selection; }
    public String[] getSelectionArgs() { return selectionArgs; }

    public List<Property> apply2(Context context) {
        selection = "";
        List<String> filtersValues = new ArrayList<String>();
        Log.i(TAG, "MyFilter.apply2");
        if (filter_minimum_price_presence) {
            if (!selection.isEmpty()) selection = selection + " AND ";
            selection = selection + PropertiesDb.KEY_PROPERTYPRICE + ">=?";
            filtersValues.add(Integer.toString(filter_minimum_price));
        }
        if (filter_maximum_price_presence) {
            if (!selection.isEmpty()) selection = selection + " AND ";
            selection = selection + PropertiesDb.KEY_PROPERTYPRICE + "<=?";
            filtersValues.add(Integer.toString(filter_maximum_price));
        }
        if (filter_minimum_surface_presence) {
            if (!selection.isEmpty()) selection = selection + " AND ";
            selection = selection + PropertiesDb.KEY_PROPERTYSURFACE + ">=?";
            filtersValues.add(Integer.toString(filter_minimum_surface));
        }
        if (filter_maximum_surface_presence) {
            if (!selection.isEmpty()) selection = selection + " AND ";
            selection = selection + PropertiesDb.KEY_PROPERTYSURFACE + "<=?";
            filtersValues.add(Integer.toString(filter_maximum_surface));
        }
        if (filter_sector_presence) {
            if (!selection.isEmpty()) selection = selection + " AND ";
            selection = selection + " ( " +PropertiesDb.KEY_PROPERTYADDRESS + " LIKE ? )";
            filtersValues.add("%"+filter_sector+"%");
        }

        if ((filter_date_begin_presence)&&(Property.convertDateToDb(filter_date_begin)!=null)) {
            if (!selection.isEmpty()) selection = selection + " AND ";
            selection = selection + " ( " +PropertiesDb.KEY_PROPERTYDATEBEGIN +
                    " BETWEEN strftime('%Y-%m-%d', ?) AND strftime('%Y-%m-%d', date('now')) )";
            filtersValues.add(Property.convertDateToDb(filter_date_begin));
        }

        if ((filter_date_end_presence)&&(Property.convertDateToDb(filter_date_end)!=null)) {
            if (!selection.isEmpty()) selection = selection + " AND ";
            selection = selection +PropertiesDb.KEY_PROPERTYDATEEND+ " NOT NULL AND "
                    +PropertiesDb.KEY_PROPERTYDATEEND+ " NOT LIKE '' "
                    +"AND ( " +PropertiesDb.KEY_PROPERTYDATEEND +
                    " BETWEEN strftime('%Y-%m-%d', ?) AND strftime('%Y-%m-%d', date('now')) )";
            filtersValues.add(Property.convertDateToDb(filter_date_end));
        }

        if (filter_point_of_interest_presence) {
            if (!selection.isEmpty()) selection = selection + " AND ";
            selection = selection +PropertiesDb.KEY_PROPERTYPOINTSOFINTEREST+ " NOT LIKE '' AND " +PropertiesDb.KEY_PROPERTYPOINTSOFINTEREST+ " NOT NULL";
        }

        selectionArgs = new String[ filtersValues.size() ];
        filtersValues.toArray( selectionArgs );

        if (filter_minimum_number_photos_presence) {
            List<Property> properties = new ArrayList<>();

            Uri uri = Uri.parse(MyContentProvider.CONTENT_JOIN_URI.toString());
            // sortOrder is used here to transmit the minimum number of photo
            Cursor cursor = context.getContentResolver().query(uri, null, getSelection(), getSelectionArgs(), Integer.toString(filter_minimum_number_photos));
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
                        Property.convertDateFromDb(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEBEGIN))),
                        Property.convertDateFromDb(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEEND))),
                        cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYREALESTATEAGENT)));
                property.setPointsOfInterest(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYPOINTSOFINTEREST)));
                property.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROWID)));
                properties.add(property);
                cursor.moveToNext();
            }
            cursor.close();
            return properties;
        }

        return Utils.readPropertiesFromDbWithFilter(context, this);
    }
}
