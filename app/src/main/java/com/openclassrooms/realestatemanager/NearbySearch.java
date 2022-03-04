package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.maps.GaeRequestHandler;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NearbySearch {
//    public static GeoApiContext context = null;
    private final static String TAG = "TestNearbySearch";

    public NearbySearch() {
 //       if (context == null) context = new GeoApiContext.Builder(new GaeRequestHandler.Builder())
 //               .apiKey("AIzaSyC8ss3iAuvyEXqnHVTpfSCCcn_sUQ7tQMo")
 //               .build();
    }
    public void run(Context contextApp, double lattitude, double longitude) {
        Log.i(TAG, "NearbySearch.run (" + lattitude + ", " + longitude + ")");
 /*       LatLng location = new LatLng(lattitude, longitude);
        PlacesSearchResponse response = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder(new GaeRequestHandler.Builder())
                .apiKey("AIzaSyC8ss3iAuvyEXqnHVTpfSCCcn_sUQ7tQMo")
                .build();
        Log.i(TAG, "NearbySearch.run call PlacesApi.nearbySearchQuery(context, location)");
        NearbySearchRequest request = PlacesApi.nearbySearchQuery(context, location)
                .radius(5000)
                .rankby(RankBy.PROMINENCE)
                .keyword("cruise")
                .language("en")
                .type(PlaceType.RESTAURANT);

        try {
            Log.i(TAG, "NearbySearch.run call execute request");
            response = request.await();
        } catch (Exception e) {
            Log.e(TAG, "NearbySearch.run exception ", e);
            return null;
        } finally {
            Log.i(TAG, "NearbySearch.run number of response = " + ((response == null) ? "null" :
                    " results = " +((response.results == null) ? "null" : response.results.length)));
//            for (int i = 0; i < request.results.length; i++) {
//                Log.i(TAG, "NearbySearch.run name= " +request.results[i].name);
//            }
            return response;
        }

*/
/*
// Async
        PlacesApi.nearbySearchQuery(context, location)
                .radius(50000)
                .rankby(RankBy.PROMINENCE)
                .keyword("cruise")
                .language("en")
                .type(PlaceType.RESTAURANT)
                .setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
                    @Override
                    public void onResult(PlacesSearchResponse result) {
                        Log.i(TAG, "NearbySearch.run number of request = " + ((result == null) ? "null" : result.toString()));
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "NearbySearch.run async exception ", e);
                    }
                });

*/
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" +lattitude+ "," +longitude+
                "&radius=5000" +
                "&types=" +PlaceType.RESTAURANT+
                "&sensor=true" +
                "&key=" + contextApp.getString(R.string.google_maps_key);
        new PlaceTask().execute(url);
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... strings) {
        String data = null;
        try {
            data = downloadUrl(strings[0]);
        } catch (IOException e) {
            Log.e(TAG, "PlaceTask.doInBackground exception ", e);
        }
        Log.i(TAG, "PlaceTask.doInBackground data = " +((data==null) ? "null" : data));
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        new ParserTask().execute(s);
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line=reader.readLine()) != null) {
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        return data;
    }
}

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String,String>> mapList = null;
            JSONObject object;
            try {
                object = new JSONObject(strings[0]);
                mapList = (List<HashMap<String, String>>) jsonParser.parseResult(object);
            } catch (JSONException e) {
                Log.e(TAG, "ParserTask.doInBackground exception ", e);
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            for (int i=0; i<hashMaps.size(); i = i + 1) {
                HashMap<String, String> hashMapList = hashMaps.get(i);
                Log.i(TAG, "ParserTask.onPostExecute name = " +hashMapList.get("name"));
            }
        }
    }
}