package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.repository.MyContentProvider;
import com.openclassrooms.realestatemanager.repository.PropertiesDb;

import org.junit.Test;

public class DeleteDbForTest {

    @Test
    public void insertProperty2RecordsPhotos5Records() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());

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
