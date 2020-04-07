package com.genomu.starttravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.travel_data.TravelCode;
import com.genomu.starttravel.travel_data.TravelParser;

import java.util.List;

public class TravelHandlerActivity extends AppCompatActivity {

    private static final String TAG = TravelHandlerActivity.class.getSimpleName();
    private String jsonString;
    private List<TravelCode> travelCodeList;
    private List<Travel> travelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_travel_handler);
        TravelParser travelParser = new TravelParser(this);
        travelList = travelParser.getParsedList();
//        TravelCodeParser travelCodeParser = new TravelCodeParser(this);
//        travelCodeList = travelCodeParser.getParsedList();
        RecyclerView recyclerView = findViewById(R.id.TH_recyler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TravelAdapter travelAdapter = new TravelAdapter(this,travelList,true);
//        TravelCodeAdapter travelCodeAdapter = new TravelCodeAdapter();
        recyclerView.setAdapter(travelAdapter);
    }


    public class TravelCodeAdapter extends RecyclerView.Adapter<TravelCodeAdapter.TravelCodeViewHolder>{
        @NonNull
        @Override
        public TravelCodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.row_travel_code,parent,false);
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
