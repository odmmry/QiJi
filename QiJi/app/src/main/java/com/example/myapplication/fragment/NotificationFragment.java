package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.activity.CompetitionDetailActivity;
import com.example.myapplication.activity.GoodsDetailActivity;
import com.example.myapplication.adapter.NotificationAdapter;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.Notification;
import com.example.myapplication.model.ShopGoods;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class NotificationFragment extends Fragment {

    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.listView);
        notificationAdapter = new NotificationAdapter(requireContext(), notificationList);
        listView.setAdapter(notificationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CompetitionDetailActivity.class);
                intent.putExtra("id", notificationList.get(position).getCompetitionId());
                startActivity(intent);
            }
        });

        loadDatas();
    }

    private void loadDatas() {
        notificationList.clear();

        // 获取当前登录的用户id
        String userId = SPUtil.getInstance().getString("user_id", "");

        DefaultRepository.getInstance().getNotificationList(userId)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getActivity())))
                .subscribe(new HttpObserver<DetailResponse<List<Notification>>>() {
                    @Override
                    public void onSucceeded(DetailResponse<List<Notification>> data) {
                        notificationList.addAll(data.getData());
                        notificationAdapter.notifyDataSetChanged();
                    }
                });
    }
}
