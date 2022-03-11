package com.openclassrooms.realestatemanager;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.repository.MyContentProvider;
import com.openclassrooms.realestatemanager.repository.PropertiesDb;

import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyFilterTest {

    private Context initialization() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());

        // Empty the Database at beginning for sustainable tests
        // Delete the photo records
        Uri uriPhotoDelete = Uri.parse(MyContentProvider.CONTENT_PHOTO_URI.toString());
        appContext.getContentResolver().delete(uriPhotoDelete, null, null);

        // Read again and checks there is no more record
        String[] projectionPhotoDelete = {
                PropertiesDb.KEY_PHOTOROWID,
                PropertiesDb.KEY_PHOTODESCRIPTION,
                PropertiesDb.KEY_PHOTOPROPERTYID
        };
        Cursor cursorPhotoDelete =  appContext.getContentResolver().query(uriPhotoDelete, projectionPhotoDelete, null, null, null);
        assertNotNull(cursorPhotoDelete);
        cursorPhotoDelete.moveToFirst();
        assertEquals(0, cursorPhotoDelete.getCount());

        // Delete the properties records
        Uri uriDelete = Uri.parse(MyContentProvider.CONTENT_PROPERTY_URI.toString());
        appContext.getContentResolver().delete(uriDelete, null, null);

        // Read again and checks there is no more record
        String[] projectionDelete = {
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
        Cursor cursorDelete =  appContext.getContentResolver().query(uriDelete, projectionDelete, null, null, null);
        assertNotNull(cursorDelete);
        cursorDelete.moveToFirst();
        assertEquals(0, cursorDelete.getCount());

        return appContext;
    }

    private void insertPhoto(int propertyRowId, String description) {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());
        // Insert 1st record
        ContentValues values = new ContentValues();
        values.put(PropertiesDb.KEY_PHOTOPROPERTYID, propertyRowId);
        values.put(PropertiesDb.KEY_PHOTODESCRIPTION, description);
        appContext.getContentResolver().insert(MyContentProvider.CONTENT_PHOTO_URI, values);
    }

    private int[] initialization2records5photos(Context appContext) {
        // Insert 1st record
        ContentValues values = new ContentValues();
        values.put(PropertiesDb.KEY_PROPERTYADDRESS, "Vanves");
        values.put(PropertiesDb.KEY_PROPERTYTYPE, "Loft");
        values.put(PropertiesDb.KEY_PROPERTYSURFACE, 100);
        values.put(PropertiesDb.KEY_PROPERTYPRICE, 700000);
        values.put(PropertiesDb.KEY_PROPERTYROOMSNUMBER, 3);
        values.put(PropertiesDb.KEY_PROPERTYDESCRIPTION, "xxxxxx");
        values.put(PropertiesDb.KEY_PROPERTYSTATUS, "free");
        values.put(PropertiesDb.KEY_PROPERTYDATEBEGIN, "01/01/2022");
        values.put(PropertiesDb.KEY_PROPERTYDATEEND, "");
        values.put(PropertiesDb.KEY_PROPERTYREALESTATEAGENT, "Caroline");
        values.put(PropertiesDb.KEY_PROPERTYPOINTSOFINTEREST, "Lycée Michelet");
        appContext.getContentResolver().insert(MyContentProvider.CONTENT_PROPERTY_URI, values);

        // Insert 2nd record
        values = new ContentValues();
        values.put(PropertiesDb.KEY_PROPERTYADDRESS, "Paris");
        values.put(PropertiesDb.KEY_PROPERTYTYPE, "House");
        values.put(PropertiesDb.KEY_PROPERTYSURFACE, 120);
        values.put(PropertiesDb.KEY_PROPERTYPRICE, 1000000);
        values.put(PropertiesDb.KEY_PROPERTYROOMSNUMBER, 6);
        values.put(PropertiesDb.KEY_PROPERTYDESCRIPTION, "yyyy");
        values.put(PropertiesDb.KEY_PROPERTYSTATUS, "sold");
        values.put(PropertiesDb.KEY_PROPERTYDATEBEGIN, "01/12/2021");
        values.put(PropertiesDb.KEY_PROPERTYDATEEND, "22/02/2022");
        values.put(PropertiesDb.KEY_PROPERTYREALESTATEAGENT, "Jack");
        values.put(PropertiesDb.KEY_PROPERTYPOINTSOFINTEREST, "");
        appContext.getContentResolver().insert(MyContentProvider.CONTENT_PROPERTY_URI, values);

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
        Cursor cursor =  appContext.getContentResolver().query(uri, projection, null, null, null);
        assertNotNull(cursor);
        cursor.moveToFirst();
        assertEquals(2, cursor.getCount());
        assertEquals("Vanves", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYADDRESS)));
        assertEquals("Loft", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYTYPE)));
        assertEquals(100, cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYSURFACE)));
        assertEquals(700000, cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYPRICE)));
        assertEquals(3, cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROOMSNUMBER)));
        assertEquals("xxxxxx", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDESCRIPTION)));
        assertEquals("free", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYSTATUS)));
        assertEquals("01/01/2022", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEBEGIN)));
        assertEquals("", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEEND)));
        assertEquals("Caroline", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYREALESTATEAGENT)));

        // Insert 3 photos
        int propertyId1 = cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROWID));
        insertPhoto(propertyId1, "kitchen");
        insertPhoto(propertyId1, "bedroom");
        insertPhoto(propertyId1, "dinningroom");

        cursor.moveToNext();
        assertEquals("Paris", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYADDRESS)));
        assertEquals("House", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYTYPE)));
        assertEquals(120, cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYSURFACE)));
        assertEquals(1000000, cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYPRICE)));
        assertEquals(6, cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROOMSNUMBER)));
        assertEquals("yyyy", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDESCRIPTION)));
        assertEquals("sold", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYSTATUS)));
        assertEquals("01/12/2021", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEBEGIN)));
        assertEquals("22/02/2022", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEEND)));
        assertEquals("Jack", cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYREALESTATEAGENT)));

        // Insert 2 photos
        int propertyId2 = cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROWID));
        insertPhoto(propertyId2, "facade");
        insertPhoto(propertyId2, "livingroom");

        // check the photos
        String[] projectionPhoto = {
                PropertiesDb.KEY_PHOTOROWID,
                PropertiesDb.KEY_PHOTODESCRIPTION,
                PropertiesDb.KEY_PHOTOPROPERTYID
        };
        Uri uriPhoto = Uri.parse(MyContentProvider.CONTENT_PHOTO_URI.toString());
        Cursor cursorPhoto =  appContext.getContentResolver().query(uriPhoto, projectionPhoto, null, null, null);
        assertNotNull(cursorPhoto);
        cursorPhoto.moveToFirst();
        assertEquals(5, cursorPhoto.getCount());
        assertEquals("kitchen", cursorPhoto.getString(cursorPhoto.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTODESCRIPTION)));
        assertEquals(propertyId1, cursorPhoto.getInt(cursorPhoto.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID)));
        cursorPhoto.moveToNext();
        assertEquals("bedroom", cursorPhoto.getString(cursorPhoto.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTODESCRIPTION)));
        assertEquals(propertyId1, cursorPhoto.getInt(cursorPhoto.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID)));
        cursorPhoto.moveToNext();
        assertEquals("dinningroom", cursorPhoto.getString(cursorPhoto.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTODESCRIPTION)));
        assertEquals(propertyId1, cursorPhoto.getInt(cursorPhoto.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID)));
        cursorPhoto.moveToNext();
        assertEquals("facade", cursorPhoto.getString(cursorPhoto.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTODESCRIPTION)));
        assertEquals(propertyId2, cursorPhoto.getInt(cursorPhoto.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID)));
        cursorPhoto.moveToNext();
        assertEquals("livingroom", cursorPhoto.getString(cursorPhoto.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTODESCRIPTION)));
        assertEquals(propertyId2, cursorPhoto.getInt(cursorPhoto.getColumnIndexOrThrow(PropertiesDb.KEY_PHOTOPROPERTYID)));

        return new int[] {propertyId1, propertyId2};
    }

    @Test
    public void noFilters() {
        Context appContext = initialization();
        initialization2records5photos(appContext);
        MyFilter myFilter = new MyFilter();
        List<Property> properties = Utils.readPropertiesFromDb(appContext);
        properties = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(properties);
        assertEquals(2,properties.size());
    }

    @Test
    public void addressFilters()
    {
        Context appContext = initialization();
        initialization2records5photos(appContext);
        MyFilter myFilter = new MyFilter();
        List<Property> properties = Utils.readPropertiesFromDb(appContext);
        myFilter.setSector(false, "");
        List<Property> propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
        myFilter.setSector(true,"vanves");
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(1,propertiesFiltered.size());
        assertEquals("Vanves", propertiesFiltered.get(0).getAddress());
    }

    @Test
    public void priceFilters()
    {
        Context appContext = initialization();
        initialization2records5photos(appContext);
        MyFilter myFilter = new MyFilter();
        List<Property> properties = Utils.readPropertiesFromDb(appContext);
        myFilter.setMinimumPrice(false, 0);
        myFilter.setMaximumPrice(false, 0);
        List<Property> propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
        myFilter.setMinimumPrice(true,600000);
        myFilter.setMaximumPrice(true,650000);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(0,propertiesFiltered.size());
        myFilter.setMinimumPrice(true,700000);
        myFilter.setMaximumPrice(true,750000);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(1,propertiesFiltered.size());
        assertEquals("Vanves", propertiesFiltered.get(0).getAddress());
        myFilter.setMinimumPrice(true,1000000);
        myFilter.setMaximumPrice(true,1050000);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(1,propertiesFiltered.size());
        assertEquals("Paris", propertiesFiltered.get(0).getAddress());
        myFilter.setMinimumPrice(true,600000);
        myFilter.setMaximumPrice(true,1050000);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
    }

    @Test
    public void surfaceFilters()
    {
        Context appContext = initialization();
        initialization2records5photos(appContext);
        MyFilter myFilter = new MyFilter();
        List<Property> properties = Utils.readPropertiesFromDb(appContext);
        myFilter.setMinimumSurface(false, 0);
        myFilter.setMaximumSurface(false, 0);
        List<Property> propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
        myFilter.setMinimumSurface(true,80);
        myFilter.setMaximumSurface(true,90);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(0,propertiesFiltered.size());
        myFilter.setMinimumSurface(true,100);
        myFilter.setMaximumSurface(true,110);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(1,propertiesFiltered.size());
        assertEquals("Vanves", propertiesFiltered.get(0).getAddress());
        myFilter.setMinimumSurface(true,120);
        myFilter.setMaximumSurface(true,130);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(1,propertiesFiltered.size());
        assertEquals("Paris", propertiesFiltered.get(0).getAddress());
        myFilter.setMinimumSurface(true,90);
        myFilter.setMaximumSurface(true,120);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
    }

    @Test
    public void numberPhotosFilters()
    {
        Context appContext = initialization();
        initialization2records5photos(appContext);
        MyFilter myFilter = new MyFilter();
        List<Property> properties = Utils.readPropertiesFromDb(appContext);
        myFilter.setMinimumNumberPhotos(false, 0);
        List<Property> propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
        myFilter.setMinimumNumberPhotos(true,4);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(0,propertiesFiltered.size());
        myFilter.setMinimumNumberPhotos(true,3);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(1,propertiesFiltered.size());
        assertEquals("Vanves", propertiesFiltered.get(0).getAddress());
        myFilter.setMinimumNumberPhotos(true,2);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
    }

    private int delayToDate(Date date) {
        return (int) TimeUnit.DAYS.convert((new Date()).getTime() - date.getTime(), TimeUnit.MILLISECONDS);
    }

    @Test
    public void dateBeginFilters()
    {
        Context appContext = initialization();
        initialization2records5photos(appContext);
        MyFilter myFilter = new MyFilter();
        List<Property> properties = Utils.readPropertiesFromDb(appContext);
        myFilter.setDateBegin(false, 0);
        List<Property> propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
        myFilter.setDateBegin(true, delayToDate(properties.get(0).getDateBegin()));
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(1,propertiesFiltered.size());
        myFilter.setDateBegin(true, delayToDate(properties.get(1).getDateBegin()));
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
        myFilter.setDateBegin(true,delayToDate(new Date()));
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(0,propertiesFiltered.size());
    }

    @Test
    public void dateEndFilters()
    {
        Context appContext = initialization();
        initialization2records5photos(appContext);
        MyFilter myFilter = new MyFilter();
        List<Property> properties = Utils.readPropertiesFromDb(appContext);
        myFilter.setDateEnd(false, 0);
        List<Property> propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
        myFilter.setDateEnd(true, delayToDate(properties.get(1).getDateEnd()));
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(1,propertiesFiltered.size());
        myFilter.setDateEnd(true,delayToDate(new Date()));
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(0,propertiesFiltered.size());
    }

    @Test
    public void pointsOfInterestFilters()
    {
        Context appContext = initialization();
        initialization2records5photos(appContext);
        MyFilter myFilter = new MyFilter();
        List<Property> properties = Utils.readPropertiesFromDb(appContext);
        myFilter.setPointOfInterest(false, 0);
        List<Property> propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(2,propertiesFiltered.size());
        myFilter.setPointOfInterest(true, 0);
        propertiesFiltered = myFilter.apply(getApplicationContext(), properties);
        assertNotNull(propertiesFiltered);
        assertEquals(1,propertiesFiltered.size());
    }
}