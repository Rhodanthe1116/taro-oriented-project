package com.genomu.starttravel.travel_data;

import android.content.Context;

import com.genomu.starttravel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TravelParser extends DataParser {

    private List<Travel> travelList;

    public TravelParser(Context context) {
        super(context);
        travelList = new ArrayList<Travel>();
    }

    public List<Travel> getParsedList(){
        setUpInput();
        parseGSON();
        return travelList;
    }

    @Override
    public void parseGSON() {
        Gson gson = new Gson();
        travelList = gson.fromJson(jsonString,new TypeToken<ArrayList<Travel>>(){}.getType());
    }

    @Override
    public void parseJSON() {
        try {
            JSONArray array = new JSONArray(jsonString);
            for(int i = 0;i<array.length();i++){
                JSONObject jsonObject = array.getJSONObject(i);
                Travel travel = new Travel(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpInput() {
        InputStream is = context.getResources().openRawResource(R.raw.travel);
        try {
            jsonString = readTextFile(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
