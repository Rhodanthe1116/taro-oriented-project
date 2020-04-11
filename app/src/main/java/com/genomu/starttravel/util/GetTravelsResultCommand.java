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
    final static String default_place = "Place";    //R.string.place
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
        this.place = default_place;
        this.isRanged = false;
    }

    public GetTravelsResultCommand(HanWen hanWen,int limit,String start,String end) {
        super(hanWen);
        observers = new ArrayList<>();
        aspects = new ArrayList<>();
        this.limit = limit;
        this.start = start;
        this.end = end;
        this.place = default_place;
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
                if(!place.equals(default_place)){
                    Query q = parseCodes();
                    switch (aspects.get(i)){
                        case PRICE_D:
                            q = hanWen.addRangeConstraint(q,start,end);
                            q = hanWen.orderBy(q,"start_date",true);
                            observer.update(hanWen.addLimitConstraint(q,limit,false));
                            break;
                        case PRICE_A:
                            q = hanWen.addRangeConstraint(q,start,end);
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


    private Query parseCodes(){
        switch (place){
            case "Asia":
                return hanWen.seekFromTravels("travel_code",433,41,405);
            case "Africa":
                return hanWen.seekFromTravels("travel_code",81,84,100);
            case "North America":
                return hanWen.seekFromTravels("travel_code",85,43,92,873,409,77);
            case "Oceania":
                return hanWen.seekFromTravels("travel_code",50,45,47,408,407,96,867);
            case "Europe":
                return hanWen.seekFromTravels("travel_code",395,452,413,44,101,393);
            case "South America":
                return hanWen.seekFromTravels("travel_code",57,95);
            default:
                return hanWen.seekFromTravels("travel_code",396);
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
