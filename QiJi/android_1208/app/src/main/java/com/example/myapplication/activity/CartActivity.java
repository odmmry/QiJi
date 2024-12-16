package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.bean.CartBean;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity {

    private CartAdapter cartAdapter;
    private List<CartBean> list = new ArrayList<>() {{
        add(null);
        add(null);
        add(null);
        add(null);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        cartAdapter = new CartAdapter(list);
        recyclerView.setAdapter(cartAdapter);
    }
}