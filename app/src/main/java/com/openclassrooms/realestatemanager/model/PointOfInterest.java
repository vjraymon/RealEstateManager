package com.openclassrooms.realestatemanager.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointOfInterest {
    private int _id;
    private String googleId;
    private String name;
    private int propertyId;

    public PointOfInterest(String googleId, String name, int propertyId) {
        _id = 0; // set by SQLite
        this.googleId = googleId;
        this.name = name;
        this.propertyId = propertyId;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

}
