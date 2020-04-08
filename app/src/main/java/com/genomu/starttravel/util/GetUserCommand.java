package com.genomu.starttravel.util;

import java.util.ArrayList;
import java.util.List;

public class GetUserCommand extends DBCommand implements DBDataSubject {
    private List<DBDataObserver> observers;
    private List<DBAspect> aspects;
    private String UID;

    @Override
    public void attach(DBDataObserver observer,DBAspect aspect) {
        observers.add(observer);
        aspects.add(aspect);
    }

    @Override
    public void detach(DBDataObserver observer) {
        int idx = observers.indexOf(observer);
        observers.remove(idx);
        aspects.remove(idx);
    }

    public GetUserCommand(HanWen hanWen,String UID) {
        super(hanWen);
        this.UID = UID;
        observers = new ArrayList<>();
        aspects = new ArrayList<>();
    }

    @Override
    void work() {
        hanWen.secureUser(UID);
        for (int i = 0;i<observers.size();i++) {
            DBDataObserver observer = observers.get(i);
            switch (aspects.get(i)){
                case NAME:
                    observer.update(hanWen.seekFromUser("name"));
                    break;
                case ORDERS:
                    observer.update(hanWen.seekFromUser("orders"));
                    break;
            }
        }
    }
}
