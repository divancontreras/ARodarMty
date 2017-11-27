package com.example.divan.arodarmty;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by Divan on 11/14/2017.
 */

public class RodarMtyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
