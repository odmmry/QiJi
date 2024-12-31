package com.example.myapplication;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

public class MainApp extends Application {

    private static MainApp instance;

    private static Handler mainHandler;
    static {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static MainApp getInstance() {
        return instance;
    }

    public static void runOnMainThread(Runnable runnable) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            runnable.run();
        } else {
            mainHandler.post(runnable);
        }
    }

    public static void runOnMainThread(Runnable runnable, long delayMillis) {
        if (delayMillis <= 0) {
            runOnMainThread(runnable);
        } else {
            mainHandler.postDelayed(runnable, delayMillis);
        }
    }

    public static void removeCallbackOnMainThread(Runnable runnable) {
        mainHandler.removeCallbacks(runnable);
    }
}
