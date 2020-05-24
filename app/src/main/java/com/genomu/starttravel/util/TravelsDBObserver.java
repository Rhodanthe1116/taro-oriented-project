package com.genomu.starttravel.util;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonAdapter;
import com.ethanhua.skeleton.SkeletonScreen;
import com.genomu.starttravel.R;
import com.genomu.starttravel.TravelAdapter;
import com.genomu.starttravel.travel_data.Travel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TravelsDBObserver implements DBDataObserver {
    private static final String TAG = TravelsDBObserver.class.getSimpleName();
    private RecyclerView recyclerView;
    private Activity activity;
    private boolean isRanged;
    private String end;
    private TravelAdapter adapter;

    public TravelsDBObserver(RecyclerView recyclerView, Activity activity) {
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.isRanged = false;
        this.end = "2022年4月23日 星期五";
    }

    @Override
    public void update() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void update(Query query) {
        List<Travel> skes = getSkes();
        adapter = new TravelAdapter(activity,skes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
         final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView).adapter(adapter).load(R.layout.row_skeleton_travel).show();
        //waiting UI show here
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Travel> travelList = new ArrayList<Travel>();
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Travel travel = document.toObject(Travel.class);
                        int jud = 0;
                        try {
                            DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            jud = sdf.parse(travel.getEnd_date()).compareTo(format.parse(end));
                            Log.d(TAG, travel.getTitle()+" is not after "+(jud<1));
                        } catch (ParseException e) {
                            Log.w(TAG, "onComplete: ", e);
                        }
                        if(jud<1 &&!travel.getTitle().equals("dummy")){
                            travelList.add(travel);
                        }
                    }
                    if(travelList.size()>0){
                        skeletonScreen.hide();
                        adapter = new TravelAdapter(activity, travelList);
                        recyclerView.setAdapter(adapter);


                    }else {
                        try {
                            throw new CommandException(CommandException.reasons.NO_RESULT,activity);
                        } catch (CommandException e) {
                            e.getExceptionDialog().show();

                        }
                    }
                }else{
                    Log.w(TAG, "onComplete: failed ", task.getException());
                }
            }
        });
    }

    private ArrayList<Travel> getSkes(){
        ArrayList<Travel> arrayList = new ArrayList<Travel>();
        for(int i = 0;i<10;i++){
            arrayList.add(Travel.dummy);
        }
        return arrayList;
    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void update(String msg) {
        //both start and end are picked
        isRanged = true;
        end = msg;
    }
}
