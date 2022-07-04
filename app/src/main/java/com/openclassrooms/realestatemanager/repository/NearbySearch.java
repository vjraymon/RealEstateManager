package com.openclassrooms.realestatemanager.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.maps.model.PlaceType;
import com.openclassrooms.realestatemanager.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class NearbySearch {
    private final static String TAG = "TestNearbySearch";

    public NearbySearch() {
    }

    public interface Callback {
        void call(List<HashMap<String, String>> h);
    }

    private Callback callback;

    public void run(Context contextApp, double latitude, double longitude, PlaceType type, Callback callback) {
        Log.i(TAG, "NearbySearch.run (" + latitude + ", " + longitude + ")");
        this.callback = callback;
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" +latitude+ "," +longitude+
                "&radius=1000" +
                "&types=" +type+
                "&sensor=true" +
                "&key=" + contextApp.getString(R.string.google_maps_key);
        Executors.newSingleThreadExecutor().execute(() -> {

            Handler mainHandler = new Handler(Looper.getMainLooper());

            //sync calculations
            String data = null;
            try {
                data = downloadUrl(url);
            } catch (IOException e) {
                Log.e(TAG, "PlaceTask.doInBackground exception ", e);
            }
            Log.i(TAG, "PlaceTask.doInBackground data = " +((data==null) ? "null" : data));
//            return data;

            String finalData = data;
            mainHandler.post(() -> parserTaskExecute(finalData));

        });
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

    void parserTaskExecute(String data) {
        Executors.newSingleThreadExecutor().execute(() -> {

            Handler mainHandler = new Handler(Looper.getMainLooper());

            //sync calculations
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String,String>> mapList = null;
            JSONObject object;
            try {
                object = new JSONObject(data);
                mapList = (List<HashMap<String, String>>) jsonParser.parseResult(object);
            } catch (JSONException e) {
                Log.e(TAG, "ParserTask.doInBackground exception ", e);
            }

            if (mapList == null) return;
            List<HashMap<String, String>> finalMapList = mapList;
            mainHandler.post(() -> {
                for (int i = 0; i< finalMapList.size(); i = i + 1) {
                    HashMap<String, String> hashMapList = finalMapList.get(i);
                    Log.i(TAG, "ParserTask.onPostExecute name = " +hashMapList.get("name"));
                }
                callback.call(finalMapList);
            });

        });
    }
}