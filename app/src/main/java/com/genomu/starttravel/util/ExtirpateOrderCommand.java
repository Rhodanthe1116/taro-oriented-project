package com.genomu.starttravel.util;

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

import java.util.ArrayList;
import java.util.List;

public class ExtirpateOrderCommand extends DBCommand{

    private final static String TAG = ExtirpateOrderCommand.class.getSimpleName();
    private List<Order> orders;
    private String orderUID;
    private LoadingDialog dialog;

    public ExtirpateOrderCommand(HanWen hanWen,String orderUID,LoadingDialog dialog) {
        super(hanWen);
        this.orderUID = orderUID;
        this.dialog = dialog;
    }

    @Override
    void work() {
        hanWen.secureUser(UserAuth.getInstance().getUserUID());
        hanWen.seekFromUser().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  if(task.isSuccessful()){
                      User user = task.getResult().toObject(User.class);
                      assert user != null;
                      orders = user.getOrders();
                      for(int i = 1;i<orders.size();i++){
                          Order order = orders.get(i);
                          if(order.getOrderUID().equals(orderUID)){
                              orders.remove(i);
                              updateTravels(order);
                          }
                      }
                      hanWen.sproutOnUser("orders",orders,dialog);
                  }
            }
        });
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
                          Log.d(TAG, "onComplete: "+purchased);
                          DocumentReference reference = snapshot.getReference();
                          reference.update("purchased",purchased-(order.getAdult()+order.getKid()));
                      }
                  }
            }
        });
    }
}
