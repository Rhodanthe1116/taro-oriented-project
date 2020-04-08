package com.genomu.starttravel.util;

import java.util.ArrayList;
import java.util.List;

public class GetTravelsResultCommand extends DBCommand implements DBDataSubject{
    private List<DBDataObserver> observers;
    private List<DBAspect> aspects;
    private int limit;

    public GetTravelsResultCommand(HanWen hanWen,int limit) {
        super(hanWen);
        observers = new ArrayList<>();
        aspects = new ArrayList<>();
        this.limit = limit;
    }

    @Override
    void work() {
        for (int i = 0;i<observers.size();i++) {
            DBDataObserver observer = observers.get(i);
            switch (aspects.get(i)){
                case PRICE_D:
                    observer.update(hanWen.seekFromRawAtLast("travels","price",limit),false);
                    break;
                case PRICE_A:
                    observer.update(hanWen.seekFromRawAtFirst("travels","price",limit),true);
                    break;
                case TRAVELS:
                    observer.update(hanWen.seekFromRawAtFirst("travels",limit));
                    break;
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
