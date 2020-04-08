package com.genomu.starttravel.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class HanWen {
    public static final boolean[] flag = {false};
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference = database.getReference("users");
    private DatabaseReference userReference;


    void rawSet(String key,List rawList){
        database.getReference("raw").child(key).setValue(rawList);
    }

    void secureUser(String UID){
        usersReference.child(UID).child("UID").setValue(UID);
        this.userReference = usersReference.child(UID);
        //for any command,please call this method first to secure user
    }

    void secureOrderListOnUser(){
        userReference.child("orders").child("0").child("travel").child("title").setValue("dummy");
        //for order list setting command,please call this method before sprouting
    }

    void sproutOnUser(String Key, String value){
        userReference.child(Key).setValue(value);
    }

    void sproutOnUser(String Key,int value){
        userReference.child(Key).setValue(value);
    }

    void sproutOnUser(String key, List value){
        if(value.size()>0) {
            userReference.child(key).setValue(value);
        }
    }

    DatabaseReference seekFromUser(String key){
        return userReference.child(key);
    }

    Query seekFromRawAtFirst(String key, int limit){
        return database.getReference("raw").child(key).orderByKey().limitToFirst(limit);
    }

    Query seekFromRawAtLast(String key, int limit){
        return database.getReference("raw").child(key).orderByKey().limitToLast(limit);
    }

    Query seekFromRawAtFirst(String key, String orderRefKey, int limit){
        return database.getReference("raw").child(key).orderByChild(orderRefKey).limitToFirst(limit);
    }

    Query seekFromRawAtLast(String key, String orderRefKey, int limit){
        return database.getReference("raw").child(key).orderByChild(orderRefKey).limitToLast(limit);
    }

}
