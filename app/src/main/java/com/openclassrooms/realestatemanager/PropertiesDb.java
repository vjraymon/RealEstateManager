package com.openclassrooms.realestatemanager;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PropertiesDb {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_PROPERTYADDRESS= "address";
    public static final String KEY_PROPERTYTYPE = "type";
    public static final String KEY_PROPERTYPRICE = "price";
    public static final String KEY_PROPERTYSURFACE = "surface";

    private static final String TAG = "PropertiesDb";
    public static final String SQLITE_TABLE = "Property";

    private static final String TABLE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " ( "
            + KEY_ROWID + " integer PRIMARY KEY autoincrement, "
                    + KEY_PROPERTYADDRESS + ", "
                    + KEY_PROPERTYTYPE + ", "
                    + KEY_PROPERTYPRICE + ", "
                    + KEY_PROPERTYSURFACE + ", "
                    + "UNIQUE (" + KEY_PROPERTYADDRESS + "));";

    public static void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "TABLE_CREATE");
        db.execSQL(TABLE_CREATE);

    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgradin database from " +oldVersion+ " to "
        +newVersion+ " which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " +SQLITE_TABLE);
        onCreate(db);
    }
}
