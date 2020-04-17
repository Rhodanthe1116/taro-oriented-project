package com.genomu.starttravel.util;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.genomu.starttravel.LoadingDialog;
import com.genomu.starttravel.Order;
import com.genomu.starttravel.UserAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ReviseOrderCommand extends DBCommand {
    private static final String TAG = ReviseOrderCommand.class.getSimpleName();
    private List<Order> orders;
    private Order targetOrder;
    private LoadingDialog dialog;
    private int[] amount;
    private Activity activity;
    public ReviseOrderCommand(HanWen hanWen,Activity activity, Order targetOrder, LoadingDialog dialog,int[] amount) {
        super(hanWen);
        this.activity = activity;
        this.dialog = dialog;
        this.targetOrder = targetOrder;
        this.amount = amount;
    }

    @Override
    void work() {
        hanWen.secureUser(UserAuth.getInstance().getUserUID());
        try {
            hanWen.seekFromUser().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        User user = task.getResult().toObject(User.class);
                        assert user != null;
                        orders = user.getOrders();
                        for(int i = 1;i<orders.size();i++){
                            Order order = orders.get(i);
                            if(order.equals(targetOrder)){
                                orders.remove(i);
                                updateTravels(order);
                            }
                        }
                        orders.add(new Order(targetOrder,amount));
                        try {
                            hanWen.sproutOnUser("orders",orders,dialog);
                        } catch (CommandException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (CommandException e) {
            e.printStackTrace();
        }
    }

    private void updateTravels(final Order order) {
        hanWen.seekFromTravels().whereEqualTo("product_key",order.getTravel().getProduct_key())
                .whereEqualTo("start_date",order.getTravel().getStart_date())
                .whereEqualTo("upper_bound",order.getTravel().getUpper_bound())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult().getDocuments()){
                        int purchased = snapshot.get("purchased",Integer.class);
                        DocumentReference reference = snapshot.getReference();
                        int change = amount[0]+amount[1]-(order.getAdult()+order.getKid());
                        if(change+purchased<=order.getTravel().getUpper_bound()){
                            reference.update("purchased",purchased+change);
                        }else {
                            try {
                                throw new CommandException(CommandException.reasons.INPUT_INVALID,activity);
                            } catch (CommandException e) {
                                e.getExceptionDialog().show();
                            }
                        }
                    }
                }
            }
        });
    }
}
