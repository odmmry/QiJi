package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.CompetitionDetailActivity;
import com.example.myapplication.activity.CreateCompetitionActivity;
import com.example.myapplication.activity.CreatePostActivity;
import com.example.myapplication.activity.PostsDetailActivity;
import com.example.myapplication.adapter.CompetitionAdapter;
import com.example.myapplication.adapter.PostsAdapter;
import com.example.myapplication.bean.CompetitionBean;
import com.example.myapplication.bean.PostBean;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView1;
    private CompetitionAdapter competitionAdapter;
    private List<CompetitionBean> competitionBeanList = new ArrayList<>() {{
        for (int i = 0; i < 10; i++) {
            add(new CompetitionBean());
        }
    }};

    private RecyclerView recyclerView2;
    private PostsAdapter postsAdapter;
    private List<PostBean> postBeanList = new ArrayList<>() {{
        for (int i = 0; i < 10; i++) {
            PostBean postBean = new PostBean();
            postBean.setId(i + 1);
            postBean.setContent("内容内容 " + i);
            postBean.setUserId(1);
            postBean.setUserNickname("用户昵称");
            postBean.setDatetime("2024-12-03 15:00");
            postBean.setImages(List.of("https://cdn.uviewui.com/uview/album/1.jpg", "https://cdn.uviewui.com/uview/album/2.jpg"));
            add(postBean);
        }
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.tvNavCreateCompetition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), CreateCompetitionActivity.class));
            }
        });
        view.findViewById(R.id.tvNavCreatePost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), CreatePostActivity.class));
            }
        });


        recyclerView1 = view.findViewById(R.id.recyclerView1);
        competitionAdapter = new CompetitionAdapter(competitionBeanList);
        recyclerView1.setAdapter(competitionAdapter);
        competitionAdapter.setOnClickListener(new PostsAdapter.OnClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent intent = new Intent(requireContext(), CompetitionDetailActivity.class);
                startActivity(intent);
            }
        });


        recyclerView2 = view.findViewById(R.id.recyclerView2);
        postsAdapter = new PostsAdapter(postBeanList);
        recyclerView2.setAdapter(postsAdapter);
        postsAdapter.setOnClickListener(new PostsAdapter.OnClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent intent = new Intent(requireContext(), PostsDetailActivity.class);
                startActivity(intent);
            }
        });

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        LinearLayout ll1 = view.findViewById(R.id.ll1);
        LinearLayout ll2 = view.findViewById(R.id.ll2);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        ll1.setVisibility(View.VISIBLE);
                        ll2.setVisibility(View.GONE);
                        break;
                    case 1:
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(1).select();
    }
}
