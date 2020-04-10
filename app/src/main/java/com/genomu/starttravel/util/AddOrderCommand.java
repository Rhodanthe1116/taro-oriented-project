package com.genomu.starttravel.util;

import androidx.annotation.NonNull;

import com.genomu.starttravel.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class AddOrderCommand extends DBCommand {
    private String UID;
    private Order order;
    public AddOrderCommand(HanWen hanWen,String UID,Order order) {
        super(hanWen);
        this.UID = UID;
        this.order = order;
    }

    @Override
    void work() {
        hanWen.secureUser(UID);
        hanWen.seekFromUser().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                List orders = user.getOrders();
                orders.add(order);
                hanWen.sproutOnUser("orders",orders);
            }
        });

    }
}
