package com.example.work_order;

import com.google.firebase.database.FirebaseDatabase;

public class firebasePersistence extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
