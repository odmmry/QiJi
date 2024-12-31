package com.example.myapplication.util;

import android.widget.Toast;

import com.example.myapplication.MainApp;

public class ToastUtil {

    public static void showToast(String content) {
        Toast.makeText(MainApp.getInstance(), content, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String content) {
        Toast.makeText(MainApp.getInstance(), content, Toast.LENGTH_LONG).show();
    }
}
