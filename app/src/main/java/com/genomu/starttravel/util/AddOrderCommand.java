package com.genomu.starttravel.util;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.genomu.starttravel.LoadingDialog;
import com.genomu.starttravel.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.genomu.starttravel.util.ReviseOrderCommand.updateOrderPool;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    void work() {
        hanWen.secureUser(UID);
        try {
            hanWen.seekFromUser().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    User user = task.getResult().toObject(User.class);
                    final List orders = user.getOrders();
                    updateTravels();
                    orders.add(order);

                    try {
                        hanWen.sproutOnUser("orders",orders,dialog);
                    } catch (CommandException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (CommandException e) {
            e.printStackTrace();
        }

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
                        int updatePur = purchased+order.getAdult()+order.getKid();
                        DocumentReference reference = snapshot.getReference();
                        goOrderPool(updatePur, reference);
                        reference.update("purchased",updatePur);
                    }
                }
            }
        });
    }

    private void goOrderPool(final int updatePur, DocumentReference reference) {
        Map<String,Object> map = new HashMap<>();
        map.put("purchased",updatePur);
        map.put("travel_id",reference.getId());
        map.put("user_id",UID);
        hanWen.rawSeek("orders",order.getOrderUID()).set(map);
        updateOrderPool(updatePur, reference);
    }

}
