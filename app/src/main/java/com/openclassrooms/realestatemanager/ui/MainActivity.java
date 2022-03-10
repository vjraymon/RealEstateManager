package com.openclassrooms.realestatemanager.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.MyFilter;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.repository.MyContentProvider;
import com.openclassrooms.realestatemanager.repository.PropertiesDb;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "TestMainActivity";

    RecyclerView recyclerView;
    private MyFilter myFilter = new MyFilter();

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
//        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

//        this.configureTextViewMain();
//        this.configureTextViewQuantity();
 /*
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        initializePropertiesList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.properties_map) {
            launchMap();
            return true;
        }
        if (item.getItemId() == R.id.financing_simulation) {
            final FinancingSimulation dialog = new FinancingSimulation(this);
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.filter_clear) {
            myFilter = new MyFilter();
            initializePropertiesList();
            return true;
        }
        if (item.getItemId() == R.id.filter_min_price) {
            CustomDialogInt.FullNameListener listener = fullName -> {
                myFilter.setMinimumPrice(fullName!=null, (fullName==null) ? 0 : Integer.parseInt(fullName));
                initializePropertiesList();
            };
            final CustomDialogInt dialog = new CustomDialogInt(this, listener, this.getString(R.string.filter_min_price),
                    myFilter.getMinimumPricePresence(), myFilter.getMinimumPrice());
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.filter_max_price) {
            CustomDialogInt.FullNameListener listener = fullName -> {
                myFilter.setMaximumPrice(fullName!=null, (fullName==null) ? 0 : Integer.parseInt(fullName));
                initializePropertiesList();
            };
            final CustomDialogInt dialog = new CustomDialogInt(this, listener, this.getString(R.string.filter_max_price),
                    myFilter.getMaximumPricePresence(), myFilter.getMaximumPrice());
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.filter_min_surface) {
            CustomDialogInt.FullNameListener listener = fullName -> {
                myFilter.setMinimumSurface(fullName!=null, (fullName==null) ? 0 : Integer.parseInt(fullName));
                initializePropertiesList();
            };
            final CustomDialogInt dialog = new CustomDialogInt(this, listener, this.getString(R.string.filter_min_surface),
                    myFilter.getMinimumSurfacePresence(), myFilter.getMinimumSurface());
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.filter_max_surface) {
            CustomDialogInt.FullNameListener listener = fullName -> {
                myFilter.setMaximumSurface(fullName!=null, (fullName==null) ? 0 : Integer.parseInt(fullName));
                initializePropertiesList();
            };
            final CustomDialogInt dialog = new CustomDialogInt(this, listener, this.getString(R.string.filter_max_surface),
                    myFilter.getMaximumSurfacePresence(), myFilter.getMaximumSurface());
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.filter_min_number_photos) {
            CustomDialogInt.FullNameListener listener = fullName -> {
                myFilter.setMinimumNumberPhotos(fullName!=null, (fullName==null) ? 0 : Integer.parseInt(fullName));
                initializePropertiesList();
            };
            final CustomDialogInt dialog = new CustomDialogInt(this, listener, this.getString(R.string.filter_min_number_photos),
                    myFilter.getMinimumNumberPhotosPresence(), myFilter.getMinimumNumberPhotos());
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.filter_sector) {
            CustomDialogString.FullNameListener listener = fullName -> {
                myFilter.setSector(fullName!=null, fullName);
                initializePropertiesList();
            };
            final CustomDialogString dialog = new CustomDialogString(this, listener, this.getString(R.string.filter_sector),
                    myFilter.getSectorPresence(), myFilter.getSector());
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.filter_date_begin) {
            CustomDialogInt.FullNameListener listener = fullName -> {
                myFilter.setDateBegin(fullName!=null, (fullName==null) ? 0 : Integer.parseInt(fullName));
                initializePropertiesList();
            };
            final CustomDialogInt dialog = new CustomDialogInt(this, listener, this.getString(R.string.filter_date_begin),
                    myFilter.getDateBeginPresence(), myFilter.getDateBegin());
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.filter_date_end) {
            CustomDialogInt.FullNameListener listener = fullName -> {
                myFilter.setDateEnd(fullName!=null, (fullName==null) ? 0 : Integer.parseInt(fullName));
                initializePropertiesList();
            };
            final CustomDialogInt dialog = new CustomDialogInt(this, listener, this.getString(R.string.filter_date_end),
                    myFilter.getDateEndPresence(), myFilter.getDateEnd());
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.filter_point_of_interest) {
            CustomDialogInt.FullNameListener listener = fullName -> {
                Log.i(TAG, "MainActivity.onOptionsItemSelected R.id.filter_point_of_interest");
                myFilter.setPointOfInterest(fullName!=null, (fullName==null) ? 0 : Integer.parseInt(fullName));
                initializePropertiesList();
            };
            final CustomDialogInt dialog = new CustomDialogInt(this, listener, this.getString(R.string.filter_point_of_interest),
                    myFilter.getPointOfInterestPresence(), myFilter.getPointOfInterest());
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.home) {
            finish();
        }
        return true;
    }

    public void launchMap() {
        Log.i(TAG, "MainActivity.launchMap");
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(intent);
    }

    private void configureTextViewMain(){
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity(){
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        this.textViewQuantity.setText(String.valueOf(quantity));
    }

    List<Property> properties;

    public void initializePropertiesList() {
        Log.i(TAG, "MainActivity.initializePropertiesList");
        properties = readPropertiesFromDb(this);
        properties = myFilter.apply(getApplicationContext(), properties);

        recyclerView = findViewById(R.id.list_properties);
        Context context = getApplicationContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyPropertiesRecyclerViewAdapter(properties));
    }

    // TODO should move to a ViewModel ?
    public static List<Property> readPropertiesFromDb(@NonNull Context context) {
        Log.i(TAG, "MainActivity.readPropertiesFromDb");
        List<Property> properties = new ArrayList<>();

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
        Cursor cursor =  context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            Log.i(TAG, "MainActivity.readPropertiesFromDb cursor null");
            return properties;
        }
        Log.i(TAG, "MainActivity.readPropertiesFromDb cursor.getCount = " +cursor.getCount());
        cursor.moveToFirst();
        for (int i=0; i < cursor.getCount(); i=i+1) {
            Property property = new Property(
                    cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYTYPE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYSURFACE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYPRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROOMSNUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDESCRIPTION)),
                    Property.convertPropertyStatusString(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYSTATUS))),
                    Property.convertDateString(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEBEGIN))),
                    Property.convertDateString(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYDATEEND))),
                    cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYREALESTATEAGENT)));
            property.setPointsOfInterest(cursor.getString(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYPOINTSOFINTEREST)));
            property.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROWID)));
            properties.add(property);
            Log.i(TAG, "MainActivity.readPropertiesFromDb read property " +property.getAddress()+
                    " (" +cursor.getInt(cursor.getColumnIndexOrThrow(PropertiesDb.KEY_PROPERTYROWID))+ ")");
            cursor.moveToNext();
        }
        cursor.close();
        Log.i(TAG, "MainActivity.readPropertiesFromDb properties.size = " +properties.size());
        return properties;
    }
}
