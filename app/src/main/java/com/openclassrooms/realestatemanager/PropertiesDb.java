package com.openclassrooms.realestatemanager;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PropertiesDb {
    private static final String TAG = "PropertiesDb";

    public static final String KEY_PROPERTYROWID = "_id";
    public static final String KEY_PROPERTYADDRESS= "address";
    public static final String KEY_PROPERTYTYPE = "type";
    public static final String KEY_PROPERTYPRICE = "price";
    public static final String KEY_PROPERTYSURFACE = "surface";
    public static final String KEY_PROPERTYROOMSNUMBER = "roomsnumber";
    public static final String KEY_PROPERTYDESCRIPTION = "description";
    public static final String KEY_PROPERTYSTATUS = "status";
    public static final String KEY_PROPERTYDATEBEGIN = "datebegin";
    public static final String KEY_PROPERTYDATEEND = "dateend";
    public static final String KEY_PROPERTYREALESTATEAGENT = "realestateagent";

    public static final String SQLITE_PROPERTIES_TABLE = "Property";

    private static final String TABLE_PROPERTIES_CREATE =
            "CREATE TABLE if not exists " + SQLITE_PROPERTIES_TABLE + " ( "
            + KEY_PROPERTYROWID + " integer PRIMARY KEY autoincrement, "
                    + KEY_PROPERTYADDRESS + " text, "
                    + KEY_PROPERTYTYPE + " text, "
                    + KEY_PROPERTYPRICE + " integer, "
                    + KEY_PROPERTYSURFACE + " integer, "
                    + KEY_PROPERTYROOMSNUMBER + " integer, "
                    + KEY_PROPERTYDESCRIPTION + " text, "
                    + KEY_PROPERTYSTATUS + " text, "
                    + KEY_PROPERTYDATEBEGIN + " text, "
                    + KEY_PROPERTYDATEEND + " text, "
                    + KEY_PROPERTYREALESTATEAGENT + " text"
//                    + "UNIQUE (" + KEY_PROPERTYADDRESS + ")"
                    +");";

    public static final String KEY_PHOTOROWID = "_id";
    public static final String KEY_PHOTODESCRIPTION = "description";
    public static final String KEY_PHOTOPROPERTYID = "propertyid";

    public static final String SQLITE_PHOTOS_TABLE = "Photo";

    private static final String TABLE_PHOTOS_CREATE =
            "CREATE TABLE if not exists " + SQLITE_PHOTOS_TABLE + " ( "
                    + KEY_PHOTOROWID + " integer PRIMARY KEY autoincrement, "
                    + KEY_PHOTODESCRIPTION + " text, "
                    + KEY_PHOTOPROPERTYID + " integer references " + SQLITE_PROPERTIES_TABLE + "("+KEY_PROPERTYROWID+")"
                    + ");";

    public static void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "TABLE_PROPERTIES_CREATE");
        db.execSQL(TABLE_PROPERTIES_CREATE);
        Log.i(TAG, "TABLE_PHOTOS_CREATE");
        db.execSQL(TABLE_PHOTOS_CREATE);

    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrade database from " +oldVersion+ " to "
        +newVersion+ " which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_PROPERTIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_PHOTOS_TABLE);
        onCreate(db);
    }
}
