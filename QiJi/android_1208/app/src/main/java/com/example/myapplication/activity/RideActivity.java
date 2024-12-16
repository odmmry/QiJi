package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;

public class RideActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        findViewById(R.id.llStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}