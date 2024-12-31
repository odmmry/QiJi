package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activity.CartActivity;
import com.example.myapplication.activity.ChangeUserInfoActivity;
import com.example.myapplication.activity.MyPostsActivity;
import com.example.myapplication.activity.OrderListActivity;
import com.example.myapplication.activity.RideLogActivity;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.ShopGoods;
import com.example.myapplication.model.UserInfo;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.SPUtil;

import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class ProfileFragment extends Fragment {

    private ImageView ivAvatar;
    private TextView tvNickname;
    
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvNickname = view.findViewById(R.id.tvNickname);

        view.findViewById(R.id.llRideLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), RideLogActivity.class));
            }
        });
        view.findViewById(R.id.llPosts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), MyPostsActivity.class));
            }
        });
        view.findViewById(R.id.llOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), OrderListActivity.class));
            }
        });
        view.findViewById(R.id.llCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), CartActivity.class));
            }
        });
        view.findViewById(R.id.llSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), ChangeUserInfoActivity.class));
            }
        });
    }

    private void loadData() {
        String userId = SPUtil.getInstance().getString("user_id", "");

        DefaultRepository.getInstance().getUserInfo(userId)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getActivity())))
                .subscribe(new HttpObserver<DetailResponse<UserInfo>>() {
                    @Override
                    public void onSucceeded(DetailResponse<UserInfo> data) {
                        if (data.getData().getAvatarUrl().isEmpty()) {
                            Glide.with(ivAvatar)
                                    .load(R.drawable.user)
                                    .into(ivAvatar);
                        } else {
                            Glide.with(ivAvatar)
                                    .load(data.getData().getAvatarUrl())
                                    .into(ivAvatar);
                        }


                        tvNickname.setText(data.getData().getNickname());
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();
    }
}
