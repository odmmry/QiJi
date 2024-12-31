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
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.activity.BaseActivity;
import com.example.myapplication.activity.CreateGoodsActivity;
import com.example.myapplication.activity.GoodsDetailActivity;
import com.example.myapplication.adapter.ShopGoodsAdapter;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.PostItem;
import com.example.myapplication.model.ShopGoods;
import com.example.myapplication.model.response.DetailResponse;

import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class ShoppingFragment extends Fragment {

    private GridView gridView;
    private ShopGoodsAdapter shopGoodsAdapter;
    private List<ShopGoods> list = new ArrayList<>();

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
                intent.putExtra("id", list.get(position).get_id());
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

    private void loadList() {
        list.clear();

        getHostActivity().showLoading();

        DefaultRepository.getInstance().getGoodsList()
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<List<ShopGoods>>>() {
                    @Override
                    public void onSucceeded(DetailResponse<List<ShopGoods>> data) {
                        if (getHostActivity() != null) {
                            getHostActivity().hideLoading();
                        }

                        list.addAll(data.getData());
                        shopGoodsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onFailed(DetailResponse<List<ShopGoods>> data, Throwable e) {

                        if (getHostActivity() != null) {
                            getHostActivity().hideLoading();
                        }

                        return false;
                    }
                });
    }

    public BaseActivity getHostActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadList();
    }
}
