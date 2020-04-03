package com.genomu.starttravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genomu.starttravel.travel_data.TravelCode;
import com.genomu.starttravel.travel_data.TravelCodeParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TravelHandlerActivity extends AppCompatActivity {

    private static final String TAG = TravelHandlerActivity.class.getSimpleName();
    private String jsonString;
    private List<TravelCode> travelCodeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_handler);
        TravelCodeParser travelCodeParser = new TravelCodeParser(this);
        travelCodeList = travelCodeParser.getParsedList();
        RecyclerView recyclerView = findViewById(R.id.TH_recyler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TravelCodeAdapter travelCodeAdapter = new TravelCodeAdapter();
        recyclerView.setAdapter(travelCodeAdapter);
    }


    public class TravelCodeAdapter extends RecyclerView.Adapter<TravelCodeAdapter.TravelCodeViewHolder>{
        @NonNull
        @Override
        public TravelCodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.row_travel,parent,false);
            return new TravelCodeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TravelCodeViewHolder holder, int position) {
            TravelCode travelCode = travelCodeList.get(position);
            holder.name.setText(travelCode.getTravelCodeName());
            holder.code.setText(travelCode.getTravelCode()+"");
        }

        @Override
        public int getItemCount() {
            return travelCodeList.size();
        }

        public class TravelCodeViewHolder extends RecyclerView.ViewHolder{
            TextView name;
            TextView code;
            public TravelCodeViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.code_name_row_travel);
                code = itemView.findViewById(R.id.code_row_travel);
            }
        }
    }
}
