package com.openclassrooms.realestatemanager.model;

import android.util.Log;

import com.openclassrooms.realestatemanager.MoneyTextWatcher;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Property {
    private static final String TAG = "TestProperty";

    private int _id;
    private String address;
    private String type;
    private int price;
    private int surface;
    private int roomsNumber;
    private String description;
    public enum Status { FREE, SOLD }
    private Status status;
    private Date dateBegin;
    private Date dateEnd;
    private String realEstateAgent;
    private String pointsOfInterest;

    public Property() {
        this._id = 0; // will be set by SQLite
    }

    public Property(String address, String type, int surface, int price, int roomsNumber, String description, Status status, Date dateBegin, Date dateEnd, String realEstateAgent) {
        this._id = 0; // will be set by SQLite
        this.address = address;
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.roomsNumber = roomsNumber;
        this.description = description;
        this.status = status;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.realEstateAgent = realEstateAgent;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public int getRoomsNumber() {
        return roomsNumber;
    }

    public void setRoomsNumber(int roomsNumber) {
        this.roomsNumber = roomsNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getRealEstateAgent() {
        return realEstateAgent;
    }

    public void setRealEstateAgent(String realEstateAgent) { this.realEstateAgent = realEstateAgent; }

    public String getPointsOfInterest() { return pointsOfInterest; }

    public void setPointsOfInterest(String pointsOfInterest) { this.pointsOfInterest = pointsOfInterest; }

    public static Property.Status convertPropertyStatusString(String s) {
        return ((s==null) || (!s.equals("sold"))) ? Property.Status.FREE : Property.Status.SOLD;
    }

    public static String convertPropertyStatus(Property.Status s) {
        return ((s==null) || (!s.equals(Property.Status.SOLD))) ? "free" : "sold";
    }
    private final static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static Date convertDateString(String s) {
        try {
            if (s==null) return null;
            return dateFormat.parse(s);
        }
        catch (Exception e) {
            Log.e(TAG, "Property.convertDateString() ConvertDate exception " + e);
            return null;
        }
    }

    public static String convertDate(Date d) {
        try {
            if (d==null) return null;
            return dateFormat.format(d);
        }
        catch (Exception e) {
            Log.e(TAG, "Property.convertDate() ConvertDate exception " + e);
            return null;
        }
    }

    public static int convertSurfaceString(String s) {
        try {
            return Integer.parseInt(s.split(" *m²")[0]);
        }
        catch(Exception e) {
            Log.e(TAG, "Property.convertSurfaceString() exception " + e);
            return 0;
        }
    }

    public static String convertSurface(int s) { return s + " m²"; }

    public static BigDecimal convertPriceString(String s) {
        return MoneyTextWatcher.parseCurrencyValue(s);
/*
        try {
            return Integer.parseInt(s.split(" *\\$")[0]);
        }
        catch(Exception e) {
            Log.e(TAG, "Property.convertPriceString() exception " + e);
            return 0;
        }
*/
    }

    public static String convertPrice(BigDecimal p) { return String.valueOf(p); }

    public static int convertRoomsNumberString(String s) {
        try {
            return Integer.parseInt(s);
        }
        catch(Exception e) {
            Log.e(TAG, "Property.convertRoomsNumberString() exception " + e);
            return 0;
        }
    }
}
