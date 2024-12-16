package com.example.myapplication.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.dialog.LoadingDialog;

import java.lang.ref.WeakReference;

public class BaseActivity extends AppCompatActivity {

    private WeakReference<LoadingDialog> loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showLoading() {
        hideLoading();

        loadingDialog = new WeakReference<>(new LoadingDialog());

        loadingDialog.get().show(getSupportFragmentManager());
    }

    protected void hideLoading() {
        if (loadingDialog != null && loadingDialog.get() != null) {
            loadingDialog.get().dismiss();
        }
    }
}
