package com.example.myapplication;

import android.app.Application;

public class MainApp extends Application {

    private static MainApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static MainApp getInstance() {
        return instance;
    }
}
