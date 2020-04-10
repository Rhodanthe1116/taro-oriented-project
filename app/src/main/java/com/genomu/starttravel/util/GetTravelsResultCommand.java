package com.genomu.starttravel.util;

import android.util.Log;

import com.google.firebase.database.Query;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GetTravelsResultCommand extends DBCommand implements DBDataSubject{
    final static String default_start = "When to start";    //R.string.start_date_btn
    final static String default_end = "When to end";    //R.string.end_date_btn
    private static final String TAG = GetTravelsResultCommand.class.getSimpleName();
    private List<DBDataObserver> observers;
    private List<DBAspect> aspects;
    private int limit;
    private String start;
    private String end;

    public GetTravelsResultCommand(HanWen hanWen,int limit) {
        super(hanWen);
        observers = new ArrayList<>();
        aspects = new ArrayList<>();
        this.limit = limit;
        this.start = default_start;
        this.end = default_end;
    }

    public GetTravelsResultCommand(HanWen hanWen,int limit,String start,String end) {
        super(hanWen);
        observers = new ArrayList<>();
        aspects = new ArrayList<>();
        this.limit = limit;
        this.start = start;
        this.end = end;
    }

    @Override
    void work() {
        for (int i = 0;i<observers.size();i++) {
            DBDataObserver observer = observers.get(i);
            Query q;
            try {
                switch (aspects.get(i)){
                    case PRICE_D:
                        q = hanWen.seekFromRaw("travels","price");
//                        q = hanWen.addRangeConstraint(q,start,end);
                        observer.update(hanWen.addLimitConstraint(q,limit,false),false);
                        break;
                    case PRICE_A:
                        q = hanWen.seekFromRaw("travels","price");
//                        q = hanWen.addRangeConstraint(q,start,end);
                        observer.update(hanWen.addLimitConstraint(q,limit,true),true);
                        break;
                    case TRAVELS:
                        q = hanWen.seekFromRaw("travels");
                        q = hanWen.addRangeConstraint(q,start,end);
                        observer.update(hanWen.addLimitConstraint(q,limit,true));
                        break;
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
