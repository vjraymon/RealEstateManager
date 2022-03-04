package com.openclassrooms.realestatemanager;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private final static String TAG = "TestJsonParser";

    private HashMap<String, String> parseJsonObject(JSONObject object) {
        HashMap<String, String> dataList = new HashMap<>();
        try {
            String name = object.getString("name");
            dataList.put("name", name);
        } catch (JSONException e) {
            Log.e(TAG, "JsonParser.parseJsonObject exception", e);
        }
        return dataList;
    }

    private List<HashMap<String, String>> parseJsonArray(JSONArray jsonArray) {
        List<HashMap<String, String>> dataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                HashMap<String, String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                dataList.add(data);
            } catch (JSONException e) {
                Log.e(TAG, "JsonParser.parseJsonArray exception", e);
            }
        }
        return dataList;
    }

    public List<HashMap<String, String>> parseResult(JSONObject object) {
        JSONArray jsonArray = null;
        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            Log.e(TAG, "JsonParser.parseResult exception", e);
        }
        return (jsonArray==null) ? null : parseJsonArray(jsonArray);
    }
}
