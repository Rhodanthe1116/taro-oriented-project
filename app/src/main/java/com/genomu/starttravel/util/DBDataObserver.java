package com.genomu.starttravel.util;

import com.google.firebase.database.DatabaseReference;

public interface DBDataObserver {
    void update();
    void update(DatabaseReference reference);
}
