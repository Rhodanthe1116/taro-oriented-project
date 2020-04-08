package com.genomu.starttravel.util;

public interface DBDataSubject {
    public void attach(DBDataObserver observer,DBAspect aspect);
    public void detach(DBDataObserver observer);
}
