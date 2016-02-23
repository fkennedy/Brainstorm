package com.example.michellewang.brainstorm;

import com.firebase.client.Firebase;
import android.app.Application;

/**
 * Created by Victor on 2/21/2016.
 */
public class AppLifeApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
