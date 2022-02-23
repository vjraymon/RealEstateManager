package com.openclassrooms.realestatemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());
    }

    @Test
    public void insertProperty1Record() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());

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

        // Inserts a new record
        appContext.getContentResolver().insert(MyContentProvider.CONTENT_PROPERTY_URI, values);

        // Read again and checks this record
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
                PropertiesDb.KEY_PROPERTYREALESTATEAGENT
        };
        Uri uri = Uri.parse(MyContentProvider.CONTENT_PROPERTY_URI.toString());
        Cursor cursor =  appContext.getContentResolver().query(uri, projection, null, null, null);
        assertNotNull(cursor);
        cursor.moveToFirst();
        assertEquals(1, cursor.getCount());
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

        //updates the record
        ContentValues valuesUpdate = new ContentValues();
        valuesUpdate.put(PropertiesDb.KEY_PROPERTYADDRESS, "Vanves");
        valuesUpdate.put(PropertiesDb.KEY_PROPERTYTYPE, "Flat");
        valuesUpdate.put(PropertiesDb.KEY_PROPERTYSURFACE, 100);
        valuesUpdate.put(PropertiesDb.KEY_PROPERTYPRICE, 700000);
        appContext.getContentResolver().update(MyContentProvider.CONTENT_PROPERTY_URI, valuesUpdate, null, null);

        // Read again and checks this record
        String[] projectionUpdate = {
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
                PropertiesDb.KEY_PROPERTYREALESTATEAGENT
        };
        Uri uriUpdate = Uri.parse(MyContentProvider.CONTENT_PROPERTY_URI.toString());
        Cursor cursorUpdate =  appContext.getContentResolver().query(uriUpdate, projectionUpdate, null, null, null);
        assertNotNull(cursorUpdate);
        cursorUpdate.moveToFirst();
        assertEquals(1, cursorUpdate.getCount());
        assertEquals("Vanves", cursorUpdate.getString(cursorUpdate.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYADDRESS)));
        assertEquals("Flat", cursorUpdate.getString(cursorUpdate.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYTYPE)));
        assertEquals(100, cursorUpdate.getInt(cursorUpdate.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYSURFACE)));
        assertEquals(700000, cursorUpdate.getInt(cursorUpdate.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYPRICE)));
        assertEquals(3, cursorUpdate.getInt(cursorUpdate.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROOMSNUMBER)));
        assertEquals("xxxxxx", cursorUpdate.getString(cursorUpdate.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDESCRIPTION)));
        assertEquals("free", cursorUpdate.getString(cursorUpdate.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYSTATUS)));
        assertEquals("01/01/2022", cursorUpdate.getString(cursorUpdate.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEBEGIN)));
        assertEquals("", cursorUpdate.getString(cursorUpdate.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEEND)));
        assertEquals("Caroline", cursorUpdate.getString(cursorUpdate.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYREALESTATEAGENT)));

        // Delete the record
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
                PropertiesDb.KEY_PROPERTYREALESTATEAGENT
        };
        Cursor cursorDelete =  appContext.getContentResolver().query(uriDelete, projectionDelete, null, null, null);
        assertNotNull(cursorDelete);
        cursorDelete.moveToFirst();
        assertEquals(0, cursorDelete.getCount());

    }

    @Test
    public void insertProperty2Records() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());

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
                PropertiesDb.KEY_PROPERTYREALESTATEAGENT
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

        // Delete the record
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
                PropertiesDb.KEY_PROPERTYREALESTATEAGENT
        };
        Cursor cursorDelete =  appContext.getContentResolver().query(uriDelete, projectionDelete, null, null, null);
        assertNotNull(cursorDelete);
        cursorDelete.moveToFirst();
        assertEquals(0, cursorDelete.getCount());
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

    @Test
    public void insertProperty2RecordsPhotos5Records() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());

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
                PropertiesDb.KEY_PROPERTYREALESTATEAGENT
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
                PropertiesDb.KEY_PROPERTYREALESTATEAGENT
        };
        Cursor cursorDelete =  appContext.getContentResolver().query(uriDelete, projectionDelete, null, null, null);
        assertNotNull(cursorDelete);
        cursorDelete.moveToFirst();
        assertEquals(0, cursorDelete.getCount());
    }
}
