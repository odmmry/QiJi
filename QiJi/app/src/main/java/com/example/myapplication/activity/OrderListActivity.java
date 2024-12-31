package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class OrderListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        orderAdapter = new OrderAdapter(list);
        recyclerView.setAdapter(orderAdapter);

        loadList();
    }

    private void loadList() {
        list.clear();

        showLoading();

        // 获取当前登录的用户id
        String userId = SPUtil.getInstance().getString("user_id", "");


        DefaultRepository.getInstance().gerOrderList(userId)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<List<Order>>>() {
                    @Override
                    public void onSucceeded(DetailResponse<List<Order>> data) {
                        hideLoading();

                        list.addAll(data.getData());
                        orderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onFailed(DetailResponse<List<Order>> data, Throwable e) {

                        hideLoading();

                        return false;
                    }
                });
    }
}