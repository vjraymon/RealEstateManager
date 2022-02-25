package com.openclassrooms.realestatemanager;

public class Photo {
//    private static final String TAG = "TestPhoto";

    private int _id;
    private String description;
    private int propertyId;

    public Photo(String description, int propertyId) {
        this._id = 0; // To be set by SQLite
        this.description = description;
        this.propertyId = propertyId;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
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
}

