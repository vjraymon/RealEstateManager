package com.openclassrooms.realestatemanager.repository;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.PointOfInterest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyPlace {
    private static final String TAG_PLACE = "TestMyPlace";

    public enum InitPlaceStatus { INIT_TODO, INIT_ONGOING, INIT_FAILED, INIT_DONE }
    private static InitPlaceStatus initPlaceStatus = InitPlaceStatus.INIT_TODO;
    private static int counter_request_ongoing = 0;
    private final static int MAX_COUNTER_RETRIES_ON_EXCEPTION = 3;
    private static int counterRetriesOnException;

    public InitPlaceStatus getInitRestaurantsStatus() { return initPlaceStatus; }

    private PlacesClient placesClient;
    private static final int M_MAX_ENTRIES = 60;

    List<PointOfInterest> pointsOfInterest;
    int propertyId;

    public interface Callback {
        void call(List<PointOfInterest> p);
    }

    private Callback callback;

    public MyPlace(@NonNull Context context, int propertyId, Callback callback) {
        Log.i(TAG_PLACE, "MyPlace.MyPlace Places.initialize");
        this.propertyId = propertyId;
        this.callback = callback;
        boolean permission = (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        /*if (!Places.isInitialized())*/ Places.initialize(context, context.getString(R.string.google_maps_key));
        Log.i(TAG_PLACE, "MyPlace.MyPlace Places.createClient");
        placesClient = Places.createClient(context);
        initPlaceStatus = InitPlaceStatus.INIT_TODO;
        counterRetriesOnException = 0;
        pointsOfInterest = new ArrayList<>(); // TODO Initialize from SQLite
        if (permission) getPointsOfInterestFromGooglePlace();
    }

    List<PointOfInterest> getPointsOfInterest() {
        Log.i(TAG_PLACE, "MyPlace.getPointsOfInterest number POI = " +pointsOfInterest.size());
        for (PointOfInterest p : pointsOfInterest) {
            Log.i(TAG_PLACE, "MyPlace.getPointsOfInterest POI = " +p.getName());
        }
        return pointsOfInterest;
    }

    private void getPointsOfInterestFromGooglePlace() {
        Log.i(TAG_PLACE, "MyPlace.getPointsOfInterestFromGooglePlace");
        if ((initPlaceStatus == InitPlaceStatus.INIT_ONGOING) || (initPlaceStatus == InitPlaceStatus.INIT_DONE)) return;

        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME
        );

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
        Log.i(TAG_PLACE, "MyPlace.getPointsOfInterestFromGooglePlace: INIT_ONGOING");
        initPlaceStatus = InitPlaceStatus.INIT_ONGOING;
        @SuppressLint("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult = placesClient.findCurrentPlace(request);
        placeResult.addOnCompleteListener(
                (response) -> {
                    if (response.isSuccessful() && (response.getResult() != null)) {
                        FindCurrentPlaceResponse likelyPlaces = response.getResult();
                        int count = Math.min(likelyPlaces.getPlaceLikelihoods().size(), M_MAX_ENTRIES);
                        int i=0;
                        counter_request_ongoing = 0;
                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            i++;
                            if (i>count) {
                                break;
                            }
                            Log.i(TAG_PLACE, "MyPlace.getPointsOfInterestFromGooglePlace location i = " + i + " : " + placeLikelihood.getPlace().getId()+
                                    " " +placeLikelihood.getPlace().getName() );
                            PointOfInterest p = new PointOfInterest(
                                    placeLikelihood.getPlace().getId(),
                                    placeLikelihood.getPlace().getName(),
                                    propertyId);
                            pointsOfInterest.add(p); // TODO check if the point of interest is not redundant
                        }
                        callback.call(getPointsOfInterest());
                        initPlaceStatus = InitPlaceStatus.INIT_DONE;
                    } else {
                        Log.e(TAG_PLACE, "MyPlace.getPointsOfInterestFromGooglePlace incorrect response on FindCurrentPlaceResponse");
                        initPlaceStatus = InitPlaceStatus.INIT_FAILED;
                    }
                }).addOnFailureListener((exception) -> {
                    Log.e(TAG_PLACE, "MyPlace.getPointsOfInterestFromGooglePlace Error on FindCurrentPlaceResponse" + exception.getMessage());
                    initPlaceStatus = InitPlaceStatus.INIT_FAILED;
                    counterRetriesOnException = counterRetriesOnException + 1;
                    if (counterRetriesOnException < MAX_COUNTER_RETRIES_ON_EXCEPTION) {
                        Log.e(TAG_PLACE, "MyPlace.getPointsOfInterestFromGooglePlace retry from restaurantRepository");
                        getPointsOfInterestFromGooglePlace();
                    } else {
                        Log.e(TAG_PLACE, "MyPlace.getPointsOfInterestFromGooglePlace retry from mapFragment");
                    }
                });
    }
}

