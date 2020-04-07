package com.genomu.starttravel.util;

import android.widget.TextView;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class NameDBObserver implements DBDataObserver {
    private TextView textView;

    public NameDBObserver(TextView textView){
        this.textView = textView;
    }


    @Override
    public void update() {
    }

    @Override
    public void update(DatabaseReference reference) {
        textView.setText("getting value from database");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = dataSnapshot.getValue(String.class);
                    textView.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
