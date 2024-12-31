package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.RideLogAdapter;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.RideLog;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class RideLogActivity extends BaseActivity {

    private ListView listView;
    private RideLogAdapter rideLogAdapter;
    private List<RideLog> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_log);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = findViewById(R.id.listView);
        rideLogAdapter = new RideLogAdapter(this, list);
        listView.setAdapter(rideLogAdapter);

        loadList();
    }

    private void loadList() {
        list.clear();

        showLoading();

        // 获取当前登录的用户id
        String userId = SPUtil.getInstance().getString("user_id", "");


        DefaultRepository.getInstance().getRideLogList(userId)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<List<RideLog>>>() {
                    @Override
                    public void onSucceeded(DetailResponse<List<RideLog>> data) {
                        hideLoading();

                        list.addAll(data.getData());
                        rideLogAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onFailed(DetailResponse<List<RideLog>> data, Throwable e) {

                        hideLoading();

                        return false;
                    }
                });
    }
}