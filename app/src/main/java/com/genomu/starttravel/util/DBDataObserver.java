package com.genomu.starttravel.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;

public interface DBDataObserver {
    void update();
    void update(Query query);
    void update(Task task);
    void update(String msg);
}
