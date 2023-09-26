package com.example.rideredirverapplication;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rideredirverapplication.Classes.Driver;
import com.google.firebase.FirebaseApp;

public class YourApplication extends Application {
    private Driver userObj;

    public Driver getUserObj() {
        return userObj;
    }

    public void setUserObj(Driver userObj) {
        this.userObj = userObj;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Register to be informed of activities starting up
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.i("YourApplication", "Activity destroyed: " + activity.getClass().getName());
            }
        });
    }
}
