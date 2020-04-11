package com.genomu.starttravel.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genomu.starttravel.Order;
import com.genomu.starttravel.OrderAdapter;
import com.genomu.starttravel.TravelAdapter;
import com.genomu.starttravel.travel_data.Travel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdersDBObserver implements DBDataObserver {

    private static final String TAG = OrdersDBObserver.class.getSimpleName();
    private RecyclerView recyclerView;
    private Activity activity;
    private ProgressBar bar;


    public OrdersDBObserver(RecyclerView recyclerView, Activity activity, ProgressBar bar){
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.bar = bar;
    }

    @Override
    public void update() {

    }


    @Override
    public void update(Query query) {

    }


    @Override
    public void update(Task task) {
        bar.setVisibility(View.VISIBLE);
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);
                    List<Order> orders = user.getOrders();
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    OrderAdapter adapter = new OrderAdapter(activity, getResolvedList(orders));
                    recyclerView.setAdapter(adapter);
                    bar.setVisibility(View.GONE);
                }else{
                    Log.w(TAG, "onComplete: ", task.getException());
                }
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
        });
    }

    @Override
    public void update(String msg) {

    }

}
