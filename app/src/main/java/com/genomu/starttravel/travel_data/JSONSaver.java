package com.genomu.starttravel.travel_data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class JSONSaver {
    private final static int JSON_INDENT = 2;
    private List list;
    private static OutputStreamWriter writer;
    public JSONSaver(List list){
        this.list = list;
    }
    public void save(TravelCode code) throws JSONException, IOException {
        writer = new OutputStreamWriter(new FileOutputStream(new File("generated.json")),"UTF-8");
        JSONArray array = new JSONArray();
        for(int i = 0;i<list.size();i++){
            TravelCode travelCode = (TravelCode) list.get(i);
            JSONObject obj = new JSONObject();
            obj.put("travel_code_name",travelCode.getTravelCodeName());
            obj.put("travel_code",travelCode.getTravelCode());
            obj.put("country",travelCode.getCountry());
            array.put(obj);
        }
        String jsonString = formatJson(array.toString());
        writer.write(jsonString);
        writer.flush();
    }

    public static String formatJson(String json) {
        String formatted = "";
        if (json == null || json.length() == 0) {
            return formatted;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jo = new JSONObject(json);
                formatted = jo.toString(JSON_INDENT);
            } else if (json.startsWith("[")) {
                JSONArray ja = new JSONArray(json);
                formatted = ja.toString(JSON_INDENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatted;
    }
}
