package com.openclassrooms.realestatemanager;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Property {
    private static final String TAG = "TestProperty";

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
    private String realEstateEgent;

    public Property(String address, String type, int price, int surface, int roomsNumber, String description, Status status, Date dateBegin, Date dateEnd, String realEstateEgent) {
        this.address = address;
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.roomsNumber = roomsNumber;
        this.description = description;
        this.status = status;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.realEstateEgent = realEstateEgent;
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

    public String getRealEstateEgent() {
        return realEstateEgent;
    }

    public void setRealEstateEgent(String realEstateEgent) {
        this.realEstateEgent = realEstateEgent;
    }

    public static Property.Status convertPropertyStatusString(String s) {
        return ((s==null) || (s.equals("free"))) ? Property.Status.FREE : Property.Status.SOLD;
    }

    public static String convertPropertyStatus(Property.Status s) {
        return ((s==null) || (s.equals(Property.Status.FREE))) ? "free" : "sold";
    }
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
}