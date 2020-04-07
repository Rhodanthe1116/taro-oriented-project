package com.genomu.starttravel.util;

import androidx.annotation.NonNull;

import com.genomu.starttravel.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

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
        hanWen.seekFromUser("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long idx = dataSnapshot.getChildrenCount();
                hanWen.seekFromUser("orders").child(Long.toString(idx)).setValue(order);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
