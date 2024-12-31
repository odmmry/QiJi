package com.example.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;
import com.example.myapplication.util.Constant;
import com.example.myapplication.util.DateUtil;
import com.example.myapplication.util.ToastUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Ride2Activity extends BaseActivity {

    private int selectedTarget = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride2);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView tvTarget = findViewById(R.id.tvTarget);

        findViewById(R.id.tvSettingTarget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Integer> targetOptions = new LinkedHashMap<>();
                targetOptions.put("5分钟", 5);
                targetOptions.put("10分钟", 10);
                targetOptions.put("15分钟", 15);
                targetOptions.put("30分钟", 30);
                targetOptions.put("60分钟", 60);

//                final String[] options = {"5分钟", "10分钟", "15分钟", "30分钟", "45分钟", "60分钟"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Ride2Activity.this);
                builder.setTitle("设置骑行目标");

                String[] options = targetOptions.keySet().toArray(new String[0]);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedTarget = targetOptions.get(options[which]);

                        tvTarget.setText(DateUtil.convertMinutesToFormat(selectedTarget));
                    }
                });

                builder.show();
            }
        });


        findViewById(R.id.tvStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTarget == 0) {
                    ToastUtil.showToast("请选择目标");
                    return;
                }


                Intent intent = new Intent(Ride2Activity.this, RideActivity.class);
                intent.putExtra("mode", Constant.RIDE_MODE_2);
                intent.putExtra("selectedTarget", selectedTarget);
                startActivity(intent);

                finish();
            }
        });
    }
}