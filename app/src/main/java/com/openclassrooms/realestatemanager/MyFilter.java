package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.util.Log;

import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.ui.DisplayDetailedPropertyFragment;

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
    private int filter_date_begin = 0;
    public void setDateBegin(boolean filter_date_begin_presence, int filter_date_begin) {
        this.filter_date_begin_presence = filter_date_begin_presence;
        this.filter_date_begin = filter_date_begin;
    }
    public boolean getDateBeginPresence() { return filter_date_begin_presence; }
    public int getDateBegin() { return filter_date_begin; }

    private boolean filter_date_end_presence = false;
    private int filter_date_end = 0;
    public void setDateEnd(boolean filter_date_end_presence, int filter_date_end) {
        this.filter_date_end_presence = filter_date_end_presence;
        this.filter_date_end = filter_date_end;
    }
    public boolean getDateEndPresence() { return filter_date_end_presence; }
    public int getDateEnd() { return filter_date_end; }

    private boolean filter_point_of_interest_presence = false;
    private int filter_point_of_interest = 0;
    public void setPointOfInterest(boolean filter_point_of_interest_presence, int filter_point_of_interest) {
        this.filter_point_of_interest_presence = filter_point_of_interest_presence;
        this.filter_point_of_interest = filter_point_of_interest;
    }
    public boolean getPointOfInterestPresence() { return filter_point_of_interest_presence; }
    public int getPointOfInterest() { return filter_point_of_interest; }

    public List<Property> apply(Context context, List<Property> properties) {
        List<Property> results = new ArrayList<>();
        for (Property p : properties) {
            if (p==null) continue;
            Log.i(TAG, "MyFilter.apply p = " +p.getAddress());
            if (filter_minimum_price_presence && (p.getPrice() < filter_minimum_price)) continue;
            if (filter_maximum_price_presence && (p.getPrice() > filter_maximum_price)) continue;
            if (filter_minimum_surface_presence && (p.getSurface() < filter_minimum_surface)) continue;
            if (filter_maximum_surface_presence && (p.getSurface() > filter_maximum_surface)) continue;

            List<Photo> photos = (p.getId() == 0)
                    ? new ArrayList<>() // clear the list of photos
                    : DisplayDetailedPropertyFragment.readPhotosFromDb(context, p.getId());
            if (filter_minimum_number_photos_presence && (photos.size() < filter_minimum_number_photos)) continue;

            if (filter_sector_presence) {
                if (p.getAddress() == null) continue; // Address not set
                if ((filter_sector == null) || !p.getAddress().toLowerCase().contains(filter_sector.toLowerCase())) continue;
            }
            if (filter_date_begin_presence) {
                if (p.getDateBegin() == null) continue; // date begin not set
                if (TimeUnit.DAYS.convert((new Date()).getTime() - p.getDateBegin().getTime(), TimeUnit.MILLISECONDS) > filter_date_begin) continue;
            }
            if (filter_date_end_presence) {
                if (p.getDateEnd()==null) continue; // not sold yet
                if (TimeUnit.DAYS.convert((new Date()).getTime() - p.getDateEnd().getTime(), TimeUnit.MILLISECONDS) > filter_date_end) continue;
            }
            if (filter_point_of_interest_presence) {
                Log.i(TAG, "MyFilter.apply R.id.filter_point_of_interest");
                if ((p.getPointsOfInterest() == null) || p.getPointsOfInterest().isEmpty()) continue; // TODO check the number of poi
            }

            results.add(p);
        }
        return results;
    }
}
