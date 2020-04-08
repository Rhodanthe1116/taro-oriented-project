package com.genomu.starttravel.util;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genomu.starttravel.Order;
import com.genomu.starttravel.TravelAdapter;
import com.genomu.starttravel.travel_data.Travel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TravelsDBObserver implements DBDataObserver {
    private RecyclerView recyclerView;
    private Activity activity;

    public TravelsDBObserver(RecyclerView recyclerView, Activity activity) {
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @Override
    public void update() {

    }

    @Override
    public void update(DatabaseReference reference) {

    }

    @Override
    public void update(Query query) {
        update(query,true);
    }

    @Override
    public void update(Query query, final boolean isAscending) {
        recyclerView.setVisibility(View.GONE);
        //waiting UI show here
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Travel> travelList = new ArrayList<Travel>();
                for(DataSnapshot dataValues:dataSnapshot.getChildren()){
                    Travel travel = dataValues.getValue(Travel.class);
                    travelList.add(travel);
                }
                if(!isAscending){
                    Collections.reverse(travelList);
                }
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                TravelAdapter adapter = new TravelAdapter(activity, travelList,true);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
