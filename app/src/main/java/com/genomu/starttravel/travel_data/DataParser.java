package com.genomu.starttravel.travel_data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class DataParser {

    protected String jsonString;
    protected Context context;
    public abstract void parseGSON();
    public abstract void parseJSON();
    public abstract void setUpInput();

    public String readTextFile(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = in.readLine();
        while (line!=null){
            sb.append(line);
            line = in.readLine();
        }
        return sb.toString();
    }

    public DataParser(Context context){
        this.context = context;
    }

    public String getJsonString() {
        return String.valueOf(jsonString);
    }
}
