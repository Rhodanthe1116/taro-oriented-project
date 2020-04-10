package com.genomu.starttravel.util;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class HanWen {
    public static final boolean[] flag = {false};
    private static final String TAG = HanWen.class.getSimpleName();
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

    Query seekFromRaw(String key){
        return database.getReference("raw").child(key).orderByKey();
    }
    Query seekFromRaw(String key,String orderRefKey){
        return database.getReference("raw").child(key).orderByChild(orderRefKey);
    }

    //Warning:aborted
    Query addRangeConstraint(Query original,String start,String end) throws ParseException {
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!start.equals(GetTravelsResultCommand.default_start)){
            if(!end.equals(GetTravelsResultCommand.default_end)){
                start = sdf.format(format.parse(start));
                end = sdf.format(format.parse(end));
                return original.startAt(start,"start_date").endAt(end,"end_date");
            }else {
                start = sdf.format(format.parse(start));
                return original.startAt(start,"start_date");
            }
        }else if(!end.equals(GetTravelsResultCommand.default_end)){
            end = sdf.format(format.parse(end));
            return original.endAt(end,"end_date");
        }
        return original;
    }

    Query addLimitConstraint(Query original,int limit,boolean isFirst){
        return  (isFirst)?(original.limitToFirst(limit)):(original.limitToLast(limit));
    }

}
