package com.genomu.starttravel.util;

import android.content.Context;

public class TravelImageSubject implements DBDataSubject {
    private Context context;

    public TravelImageSubject(Context context) {
        this.context = context;
    }

    @Override
    public void attach(DBDataObserver observer, DBAspect aspect) {

    }

    @Override
    public void detach(DBDataObserver observer) {

    }
}
