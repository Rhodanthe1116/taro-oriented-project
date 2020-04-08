package com.genomu.starttravel.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public interface DBDataObserver {
    void update();
    void update(DatabaseReference reference);
    void update(Query query);
    void update(Query query,boolean isAscending);
}
