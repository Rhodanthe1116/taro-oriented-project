package com.genomu.starttravel.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.genomu.starttravel.LoadingDialog;
import com.genomu.starttravel.Order;
import com.genomu.starttravel.travel_data.Travel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HanWen {
    public static final boolean[] flag = {false};
    private static final String TAG = HanWen.class.getSimpleName();
    private static FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference usersReference = database.collection("users");
    private DocumentReference userReference;
    private CollectionReference travelsReference = database.collection("travels");


    void rawSet(final String key, List rawList){
        for(int i = 0;i<rawList.size();i++){
            Log.d(TAG, "rawSet: "+i);
            database.collection(key).add(rawList.get(i))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "raw list successfully set on key: "+key);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
    }

    void secureUser(String UID,String name){
        secureUser(UID);
        Map<String,Object> data = new HashMap<>();
        data.put("UID",UID);
        data.put("name",name);
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Travel.dummy));
        data.put("orders",orders);
        usersReference.document(UID).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: securing user");
            }
        });
    }

    void secureUser(String UID){
        this.userReference = usersReference.document(UID);
        //call this method before accessing any user's field
    }

    void sproutOnUser(String key, String value){
        userReference.update(key,value).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "onFailure: sprouting", e);
            }
        });
    }

    void sproutOnUser(String key,int value){
        userReference.update(key,value).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "onFailure: sprouting", e);
            }
        });
    }

    void sproutOnUser(String key, List value){
        if(value.size()>0) {
            userReference.update(key,value).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "onFailure: sprouting", e);
                }
            });
        }
    }

    void sproutOnUser(String key, List value, final LoadingDialog dialog){
        if(value.size()>0) {
            userReference.update(key,value).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   dialog.dismissLoading();
                }
            });
        }
    }

    Task<DocumentSnapshot> seekFromUser(){
        return userReference.get();
    }

    CollectionReference seekFromTravels(){
        return travelsReference;
    }

    Query seekFromTravels(String field,Object... values){
        return travelsReference.whereIn(field, Arrays.asList(values));
    }

    CollectionReference seekFromRaw(String key){
        return database.collection(key);
    }

    Query seekFromRaw(String key,String field,Object... values){
        return database.collection(key).whereIn(field,Arrays.asList(values));
    }


    //Warning:aborted
    Query addRangeConstraint(Query original,String start,String end) throws ParseException {
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!start.equals(GetTravelsResultCommand.default_start)){
            if(!end.equals(GetTravelsResultCommand.default_end)){
                start = sdf.format(format.parse(start));
                end = sdf.format(format.parse(end));
                return original.whereGreaterThanOrEqualTo("start_date",start).
                        whereLessThanOrEqualTo("start_date",end).orderBy("start_date", Query.Direction.ASCENDING);
            }else {
                start = sdf.format(format.parse(start));
                return original.whereGreaterThanOrEqualTo("start_date",start).orderBy("start_date", Query.Direction.ASCENDING);
            }
        }else if(!end.equals(GetTravelsResultCommand.default_end)){
            end = sdf.format(format.parse(end));
            return original.whereLessThanOrEqualTo("end_date",end).orderBy("end_date", Query.Direction.DESCENDING);
        }
        return original;
    }



    Query orderBy(Query original,String field,boolean isAscending){
        return (isAscending)?original.orderBy(field, Query.Direction.ASCENDING):original.orderBy(field, Query.Direction.DESCENDING);
    }

    Query addLimitConstraint(Query original,int limit,boolean isFirst){
        return  (isFirst)?(original.limit(limit)):(original.limitToLast(limit));
    }

}
