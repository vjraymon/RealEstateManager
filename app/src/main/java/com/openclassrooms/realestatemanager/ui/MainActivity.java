package com.openclassrooms.realestatemanager.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.MyFilter;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.event.DisplayDetailedPropertyEvent;
import com.openclassrooms.realestatemanager.model.Property;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "TestMainActivity";

    RecyclerView recyclerView;
    private MyFilter myFilter = new MyFilter();

    private TextView textViewMain;
    private TextView textViewQuantity;
    private SlidingPaneLayout slidingPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
//        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

//        this.configureTextViewMain();
//        this.configureTextViewQuantity();
        slidingPaneLayout = findViewById(R.id.sliding_pane_layout);
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
            CustomDialogString.FullNameListener listener = fullName -> {
                myFilter.setDateBegin(fullName!=null, (fullName==null) ? null : Property.convertDateString(fullName));
                initializePropertiesList();
            };
            final CustomDialogString dialog = new CustomDialogString(this, listener, this.getString(R.string.filter_date_begin),
                    myFilter.getDateBeginPresence(), Property.convertDate(myFilter.getDateBegin()));
            dialog.show();
            return true;
        }
        if (item.getItemId() == R.id.filter_date_end) {
            CustomDialogString.FullNameListener listener = fullName -> {
                myFilter.setDateEnd(fullName!=null, (fullName==null) ? null : Property.convertDateString(fullName));
                initializePropertiesList();
            };
            final CustomDialogString dialog = new CustomDialogString(this, listener, this.getString(R.string.filter_date_end),
                    myFilter.getDateEndPresence(), Property.convertDate(myFilter.getDateEnd()));
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
        properties = Utils.readPropertiesFromDb(this);
        if (properties.isEmpty()) slidingPaneLayout.open();
        properties = myFilter.apply2(getApplicationContext());
//        properties = myFilter.apply(getApplicationContext(), properties);

        recyclerView = findViewById(R.id.list_properties);
        Context context = getApplicationContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyPropertiesRecyclerViewAdapter(properties));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDisplayDetailedProperty(DisplayDetailedPropertyEvent event) {
        if (event.property != null)
        {
            Log.i(TAG, "DisplayDetailedPropertyFragment.onDisplayDetailedProperty property = " + event.property.getAddress());
            slidingPaneLayout.open();
        }
    }
}
