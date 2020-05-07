package com.genomu.starttravel.util;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.genomu.starttravel.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class NameDBObserver implements DBDataObserver {
    private static final String TAG = NameDBObserver.class.getSimpleName();
    private TextView name;
    private TextView membership;

    public NameDBObserver(TextView name,TextView membership){
        this.name = name;
        this.membership = membership;
    }

    @Override
    public void update() {
    }

    @Override
    public void update(Query query) {

    }


    @Override
    public void update(Task task) {
        name.setTextSize(10.0f);
        name.setText("從資料庫取得姓名 ...");
        membership.setText("判斷中...");
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);
                    List<Order> orders = user.getOrders();
                    rankMember(orders);
                    String userName = user.getName();
                    name.setText(userName);
                    name.setTextSize(22.0f);
                }else{
                    Log.w(TAG, "onComplete: ", task.getException());
                }
            }
        });
    }

    private void rankMember(List<Order> orders) {
        int total = 0;
        for(Order order:orders){
            int price = order.getTravel().getPrice();
            total+=order.getTotal();
        }
        Log.d(TAG, "rankMember: "+total);
        if(total>=800000){
            membership.setText("至尊會員");
        }else if(total>=200000){
            membership.setText("尊榮會員");
        }else if(total>=100000){
            membership.setText("鑽石會員");
        }else if(total>=80000){
            membership.setText("黃金會員");
        }else if(total>=50000){
            membership.setText("白銀會員");
        }else {
            membership.setText("基本會員");
        }
    }

    @Override
    public void update(String msg) {

    }
}
