package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.CreateGoodsActivity;
import com.example.myapplication.activity.GoodsDetailActivity;
import com.example.myapplication.adapter.ShopGoodsAdapter;
import com.example.myapplication.bean.ShopGoodsBean;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment {

    private GridView gridView;
    private ShopGoodsAdapter shopGoodsAdapter;
    private List<ShopGoodsBean> list = new ArrayList<>() {{
        for (int i = 0; i < 10; i++) {
            ShopGoodsBean shopGoodsBean = new ShopGoodsBean();
            add(shopGoodsBean);
        }
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);
        return view;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridView = view.findViewById(R.id.gridView);
        shopGoodsAdapter = new ShopGoodsAdapter(requireContext(), list);
        gridView.setAdapter(shopGoodsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(requireContext(), GoodsDetailActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), CreateGoodsActivity.class));
            }
        });
    }
}
