package com.genomu.starttravel.util;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genomu.starttravel.Order;
import com.genomu.starttravel.OrderAdapter;
import com.genomu.starttravel.TravelAdapter;
import com.genomu.starttravel.travel_data.Travel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersDBObserver implements DBDataObserver {

    private RecyclerView recyclerView;
    private Activity activity;

    public OrdersDBObserver(RecyclerView recyclerView, Activity activity){
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @Override
    public void update() {

    }

    @Override
    public void update(DatabaseReference reference) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Order>> t = new GenericTypeIndicator<List<Order>>(){};
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                OrderAdapter adapter = new OrderAdapter(activity, getResolvedList(dataSnapshot.getValue(t)));
                recyclerView.setAdapter(adapter);
            }

            private List<Order> getResolvedList(List<Order> orderList){
                List<Order> orders = new ArrayList<>();
                for(Order order:orderList) {
                    if (!order.getTravel().getTitle().equals("dummy")) {
                        orders.add(order);
                    }
                }
                return orders;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void update(Query query) {

    }

    @Override
    public void update(Query query, boolean isAscending) {

    }
}
