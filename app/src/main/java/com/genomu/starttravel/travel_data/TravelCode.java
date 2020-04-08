package com.genomu.starttravel.travel_data;

import org.json.JSONException;
import org.json.JSONObject;

public class TravelCode extends TravelData{
    private String travel_code_name;
    private int travel_code;
    private String country;

    public TravelCode(String travel_code_name, int travel_code,String country) {
        this.travel_code_name = travel_code_name;
        this.travel_code = travel_code;
        this.country = country;
    }

    public TravelCode(JSONObject jsonObject) {
        try {
            travel_code_name = jsonObject.getString("travel_code_name");
            travel_code = jsonObject.getInt("travel_code");
            this.country = jsonObject.getString("country");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String getTravelCodeName() {
        return travel_code_name;
    }

    public void setTravelCodeName(String travel_code_name) {
        this.travel_code_name = travel_code_name;
    }

    public int getTravelCode() {
        return travel_code;
    }

    public void setTravelCode(int travel_code) {
        this.travel_code = travel_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
