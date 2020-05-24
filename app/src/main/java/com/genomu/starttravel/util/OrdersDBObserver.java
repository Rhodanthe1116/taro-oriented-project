package com.genomu.starttravel.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.genomu.starttravel.Order;
import com.genomu.starttravel.OrderAdapter;
import com.genomu.starttravel.R;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.genomu.starttravel.util.TravelStateOffice.ALREADY_END;
import static com.genomu.starttravel.util.TravelStateOffice.LACK;
import static com.genomu.starttravel.util.TravelStateOffice.NOT_YET_START;

public class OrdersDBObserver implements DBDataObserver {

    private static final String TAG = OrdersDBObserver.class.getSimpleName();
    private RecyclerView recyclerView;
    private Activity activity;
    private OrderAdapter adapter;


    public OrdersDBObserver(RecyclerView recyclerView, Activity activity){
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @Override
    public void update() {

    }


    @Override
    public void update(Query query) {

    }


    @Override
    public void update(Task task) {
        List<Order> skes = getSkes();
        adapter = new OrderAdapter(activity,skes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
        final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView).adapter(adapter).load(R.layout.row_skeleton_travel).show();
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);
                    final List<Order> orders = user.getOrders();
                    culling(orders);
                    skeletonScreen.hide();
                    adapter = new OrderAdapter(activity, orders);
                    recyclerView.setAdapter(adapter);

                }else{
                    Log.w(TAG, "onComplete: ", task.getException());
                }
            }

        });
    }

    private ArrayList<Order> getSkes() {
        ArrayList<Order> arrayList = new ArrayList<>();
        for(int i = 0;i<10;i++){
            arrayList.add(new Order());
        }
        return arrayList;
    }

    private void culling(List<Order> orders) {
        orders.remove(0);
//        for (int i = orders.size()-1;i>=0;i--){
//            Order order = orders.get(i);
//            Travel travel = order.getTravel();
//            try {
//                TravelStateOffice office = new TravelStateOffice(travel);
//                if (office.isCancelled()){
//                    orders.remove(i);
//                    Log.d(TAG, "culling: "+travel.getTitle());
//                }else if(office.getState()==ALREADY_END){
//                    orders.remove(i);
//                    Log.d(TAG, "culling: "+travel.getTitle());
//                }
//            } catch (ParseException e) {
//                Log.w(TAG, "culling: ", e);
//            }
//        }

    }

    @Override
    public void update(String msg) {

    }

}
