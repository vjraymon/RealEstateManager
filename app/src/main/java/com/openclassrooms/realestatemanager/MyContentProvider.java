package com.openclassrooms.realestatemanager;

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

    private static final int ALL_COUNTRIES = 1; // TODO To rename
    private static final int SINGLE_COUNTRY = 2; // TODO To rename

    private static final String AUTHORITY = "com.openclassrooms.realestatemanager.contentprovider";

    public static final Uri CONTENT_URI =
        Uri.parse("content://" +AUTHORITY+ "/properties");

    private static UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "properties", ALL_COUNTRIES);
        uriMatcher.addURI(AUTHORITY, "properties/#", SINGLE_COUNTRY);
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
        queryBuilder.setTables(PropertiesDb.SQLITE_TABLE);

        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                break;
            case SINGLE_COUNTRY:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(PropertiesDb.KEY_ROWID +"="+ id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                return "vnd.android.cursor.dir/vnd.com.openclassrooms.realestatemanager.contentprovider.properties";
            case SINGLE_COUNTRY:
                return "vnd.android.cursor.item/vnd.com.openclassrooms.realestatemanager.contentprovider.properties";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(PropertiesDb.SQLITE_TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri.parse(CONTENT_URI +"/"+ id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                break;
            case SINGLE_COUNTRY:
                String id = uri.getPathSegments().get(1);
                selection = PropertiesDb.KEY_ROWID +"="+ id
                + (!TextUtils.isEmpty(selection) ? "AND (" +selection+ ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(PropertiesDb.SQLITE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                break;
            case SINGLE_COUNTRY:
                String id = uri.getPathSegments().get(1);
                selection = PropertiesDb.KEY_ROWID +"="+ id
                        + (!TextUtils.isEmpty(selection) ? "AND (" +selection+ ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(PropertiesDb.SQLITE_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
