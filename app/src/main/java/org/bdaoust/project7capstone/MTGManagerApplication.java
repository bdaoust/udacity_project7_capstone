package org.bdaoust.project7capstone;


import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MTGManagerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enabling Firebase data persistence, based on solution from
        // cdkn7 (https://stackoverflow.com/questions/37448186/setpersistenceenabledtrue-crashes-app)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
