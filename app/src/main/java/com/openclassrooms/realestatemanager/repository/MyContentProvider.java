package com.openclassrooms.realestatemanager.repository;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {
    private MyDatabaseHelper dbHelper;

    private static final int ALL_PROPERTIES = 1;
    private static final int SINGLE_PROPERTY = 2;
    private static final int ALL_PHOTOS = 3;
    private static final int SINGLE_PHOTO = 4;
    private static final int JOIN_PHOTOS = 5;

    private static final String AUTHORITY = "com.openclassrooms.realestatemanager.contentprovider";

    public static final Uri CONTENT_PROPERTY_URI =
        Uri.parse("content://" +AUTHORITY+ "/properties");

    public static final Uri CONTENT_PHOTO_URI =
            Uri.parse("content://" +AUTHORITY+ "/photos");

    public static final Uri CONTENT_JOIN_URI =
            Uri.parse("content://" +AUTHORITY+ "/join");

    private final static UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "properties", ALL_PROPERTIES);
        uriMatcher.addURI(AUTHORITY, "properties/#", SINGLE_PROPERTY);
        uriMatcher.addURI(AUTHORITY, "photos", ALL_PHOTOS);
        uriMatcher.addURI(AUTHORITY, "photos/#", SINGLE_PHOTO);
        uriMatcher.addURI(AUTHORITY, "join", JOIN_PHOTOS);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case ALL_PROPERTIES:
                queryBuilder.setTables(PropertiesDb.SQLITE_PROPERTIES_TABLE);
                break;
            case SINGLE_PROPERTY:
                queryBuilder.setTables(PropertiesDb.SQLITE_PROPERTIES_TABLE);
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(PropertiesDb.KEY_PROPERTYROWID +"="+ id);
                break;
            case ALL_PHOTOS:
                queryBuilder.setTables(PropertiesDb.SQLITE_PHOTOS_TABLE);
                break;
            case SINGLE_PHOTO:
                queryBuilder.setTables(PropertiesDb.SQLITE_PHOTOS_TABLE);
                String id2 = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(PropertiesDb.KEY_PHOTOROWID + "=" + id2);
                break;
            case JOIN_PHOTOS:
                String s1 = PropertiesDb.SQLITE_PROPERTIES_TABLE;
                if ((selection!=null) && !selection.isEmpty()) {
                    s1 = "( SELECT * FROM " +PropertiesDb.SQLITE_PROPERTIES_TABLE+ " WHERE " +selection+ ")";
                }
                String s = "SELECT * "
                        + ", COUNT(" +PropertiesDb.SQLITE_PHOTOS_TABLE+ "." +PropertiesDb.KEY_PHOTOPROPERTYID+ ") as itemCount"
                        + " FROM " +s1+ " as intermediateList"
                        + " LEFT OUTER JOIN " +PropertiesDb.SQLITE_PHOTOS_TABLE
                        + " ON " + "intermediateList._id = " +PropertiesDb.SQLITE_PHOTOS_TABLE+ "." +PropertiesDb.KEY_PHOTOPROPERTYID
                        + " GROUP BY " + "intermediateList._id"
                        + " HAVING itemCount >= " +sortOrder;
            return db.rawQuery(s, selectionArgs);

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_PROPERTIES:
                return "vnd.android.cursor.dir/vnd.com.openclassrooms.realestatemanager.contentprovider.properties";
            case SINGLE_PROPERTY:
                return "vnd.android.cursor.item/vnd.com.openclassrooms.realestatemanager.contentprovider.properties";
            case ALL_PHOTOS:
                return "vnd.android.cursor.dir/vnd.com.openclassrooms.realestatemanager.contentprovider.photos";
            case SINGLE_PHOTO:
                return "vnd.android.cursor.item/vnd.com.openclassrooms.realestatemanager.contentprovider.photos";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_PROPERTIES:
                long id = db.insert(PropertiesDb.SQLITE_PROPERTIES_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(CONTENT_PROPERTY_URI +"/"+ id);
            case ALL_PHOTOS:
                long id2 = db.insert(PropertiesDb.SQLITE_PHOTOS_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(CONTENT_PHOTO_URI +"/"+ id2);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
     }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String s;
        switch (uriMatcher.match(uri)) {
            case ALL_PROPERTIES:
                s = PropertiesDb.SQLITE_PROPERTIES_TABLE;
                break;
            case SINGLE_PROPERTY:
                s = PropertiesDb.SQLITE_PROPERTIES_TABLE;
                String id = uri.getPathSegments().get(1);
                selection = PropertiesDb.KEY_PROPERTYROWID +"="+ id
                + (!TextUtils.isEmpty(selection) ? "AND (" +selection+ ')' : "");
                break;
            case ALL_PHOTOS:
                s = PropertiesDb.SQLITE_PHOTOS_TABLE;
                break;
            case SINGLE_PHOTO:
                s = PropertiesDb.SQLITE_PHOTOS_TABLE;
                String id2 = uri.getPathSegments().get(1);
                selection = PropertiesDb.KEY_PHOTOROWID +"="+ id2
                        + (!TextUtils.isEmpty(selection) ? "AND (" +selection+ ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(s, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String s;
        switch (uriMatcher.match(uri)) {
            case ALL_PROPERTIES:
                s = PropertiesDb.SQLITE_PROPERTIES_TABLE;
                break;
            case SINGLE_PROPERTY:
                s = PropertiesDb.SQLITE_PROPERTIES_TABLE;
                String id = uri.getPathSegments().get(1);
                selection = PropertiesDb.KEY_PROPERTYROWID +"="+ id
                        + (!TextUtils.isEmpty(selection) ? "AND (" +selection+ ')' : "");
                break;
            case ALL_PHOTOS:
                s = PropertiesDb.SQLITE_PHOTOS_TABLE;
                break;
            case SINGLE_PHOTO:
                s = PropertiesDb.SQLITE_PHOTOS_TABLE;
                String id2 = uri.getPathSegments().get(1);
                selection = PropertiesDb.KEY_PHOTOROWID +"="+ id2
                        + (!TextUtils.isEmpty(selection) ? "AND (" +selection+ ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(s, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
