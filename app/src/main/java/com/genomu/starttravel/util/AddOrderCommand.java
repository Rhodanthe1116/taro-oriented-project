package com.genomu.starttravel.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.genomu.starttravel.LoadingDialog;
import com.genomu.starttravel.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AddOrderCommand extends DBCommand {
    private static final String TAG = AddOrderCommand.class.getSimpleName();
    private String UID;
    private Order order;
    private LoadingDialog dialog;
    public AddOrderCommand(HanWen hanWen, String UID, Order order, LoadingDialog dialog) {
        super(hanWen);
        this.UID = UID;
        this.order = order;
        this.dialog = dialog;
    }

    @Override
    void work() {
        hanWen.secureUser(UID);
        hanWen.seekFromUser().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                final List orders = user.getOrders();
                orders.add(order);
                updateTravels();
                hanWen.sproutOnUser("orders",orders,dialog);

            }
        });

    }

    private void updateTravels() {
        hanWen.seekFromTravels()
                .whereEqualTo("product_key",order.getTravel().getProduct_key())
                .whereEqualTo("start_date",order.getTravel().getStart_date())
                .whereEqualTo("upper_bound",order.getTravel().getUpper_bound())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot : task.getResult().getDocuments()){
                        int purchased = snapshot.get("purchased",Integer.class);
                        DocumentReference reference = snapshot.getReference();
                        reference.update("purchased",purchased+order.getAdult()+order.getKid());
                    }
                }
            }
        });
    }
}
