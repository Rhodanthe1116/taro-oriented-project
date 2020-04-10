package com.genomu.starttravel.travel_data;

import android.content.Context;

import com.genomu.starttravel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TravelCodeParser extends DataParser {

    private List<TravelCode> travelCodeList;

    public TravelCodeParser(Context context) {
        super(context);
        travelCodeList = new ArrayList<TravelCode>();
    }

    public List<TravelCode> getParsedList() {
        setUpInput();
        parseGSON();
        return travelCodeList;
    }


    @Override
    public void parseGSON() {
        Gson gson = new Gson();
        travelCodeList = gson.fromJson(jsonString,new TypeToken<ArrayList<TravelCode>>(){}.getType());
    }

    @Override
    public void parseJSON() {
        try {
            JSONArray array = new JSONArray(jsonString);
            for(int i = 0;i<array.length();i++){
                JSONObject jsonObject = array.getJSONObject(i);
                travelCodeList.add(new TravelCode(jsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpInput() {
        InputStream is = context.getResources().openRawResource(R.raw.travel_code_a_1_4);
        try {
            jsonString = readTextFile(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
