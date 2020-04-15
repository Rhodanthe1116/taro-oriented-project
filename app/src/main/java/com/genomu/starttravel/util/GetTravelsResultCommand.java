package com.genomu.starttravel.util;

import android.content.Intent;
import android.util.Log;


import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetTravelsResultCommand extends DBCommand implements DBDataSubject{
    final static String default_start = "When to start";    //R.string.start_date_btn
    final static String default_end = "When to end";    //R.string.end_date_btn
    private static final String TAG = GetTravelsResultCommand.class.getSimpleName();
    private List<DBDataObserver> observers;
    private List<DBAspect> aspects;
    private int limit;
    private String start;
    private boolean isRanged;

    public GetTravelsResultCommand(HanWen hanWen, int limit, String start, String end, String place) {
        super(hanWen);
        observers = new ArrayList<>();
        aspects = new ArrayList<>();
        this.limit = limit;
        this.start = start;
        this.end = end;
        this.place = place;
        this.isRanged =!start.equals(GetTravelsResultCommand.default_start)
                &&!end.equals(GetTravelsResultCommand.default_end);
    }

    private String end;
    private String place;

    public GetTravelsResultCommand(HanWen hanWen,int limit) {
        super(hanWen);
        observers = new ArrayList<>();
        aspects = new ArrayList<>();
        this.limit = limit;
        this.start = default_start;
        this.end = default_end;
        this.place = "";
        this.isRanged = false;
    }

    public GetTravelsResultCommand(HanWen hanWen,int limit,String start,String end) {
        super(hanWen);
        observers = new ArrayList<>();
        aspects = new ArrayList<>();
        this.limit = limit;
        this.start = start;
        this.end = end;
        this.place = "";
        this.isRanged =!start.equals(GetTravelsResultCommand.default_start)
                &&!end.equals(GetTravelsResultCommand.default_end);
    }

    @Override
    void work() {
        for (int i = 0;i<observers.size();i++) {
            DBDataObserver observer = observers.get(i);

            if(isRanged){
                observer.update(end);
            }

            try {
                if(!place.equals("")){
                    Query q = CodeMapping.parseCodes(place,hanWen);
                    switch (aspects.get(i)){
                        case PRICE_D:
                            q = hanWen.addRangeConstraint(q,start,end);
                            q = hanWen.orderBy(q,"price",false);
                            observer.update(hanWen.addLimitConstraint(q,limit,false));
                            break;
                        case PRICE_A:
                            q = hanWen.addRangeConstraint(q,start,end);
                            q = hanWen.orderBy(q,"price",true);
                            observer.update(hanWen.addLimitConstraint(q,limit,true));
                            break;
                        case TRAVELS:
                            observer.update(hanWen.seekFromTravels().limit(limit));
                            break;
                    }
                }else{
                    Query q = hanWen.seekFromTravels();

                    switch (aspects.get(i)){
                        case PRICE_D:
                            q = hanWen.addRangeConstraint(q,start,end);
                            q = hanWen.orderBy(q,"price",false);
                            observer.update(hanWen.addLimitConstraint(q,limit,true));
                            break;
                        case PRICE_A:
                            q = hanWen.addRangeConstraint(q,start,end);
                            q = hanWen.orderBy(q,"price",true);
                            observer.update(hanWen.addLimitConstraint(q,limit,true));
                            break;
                        case TRAVELS:
                            observer.update(hanWen.seekFromTravels().limit(limit));
                            break;
                    }
                }
            }catch (ParseException e){
                Log.w(TAG, "work: ", e);
            }

        }
    }

    @Override
    public void attach(DBDataObserver observer, DBAspect aspect) {
        observers.add(observer);
        aspects.add(aspect);
    }

    @Override
    public void detach(DBDataObserver observer) {
        int idx = observers.indexOf(observer);
        observers.remove(idx);
        aspects.remove(idx);
    }
}
