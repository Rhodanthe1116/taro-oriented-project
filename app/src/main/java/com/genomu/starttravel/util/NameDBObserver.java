package com.genomu.starttravel.util;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class NameDBObserver implements DBDataObserver {
    private static final String TAG = NameDBObserver.class.getSimpleName();
    private TextView textView;

    public NameDBObserver(TextView textView){
        this.textView = textView;
    }

    @Override
    public void update() {
    }

    @Override
    public void update(Query query) {

    }


    @Override
    public void update(Task task) {
        textView.setText("getting value from database");
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String name = task.getResult().get("name",String.class);
                    textView.setText(name);
                }else{
                    Log.w(TAG, "onComplete: ", task.getException());
                }
            }
        });
    }

    @Override
    public void update(String msg) {

    }
}
