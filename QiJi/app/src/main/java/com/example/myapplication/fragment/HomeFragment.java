package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.BaseActivity;
import com.example.myapplication.activity.CompetitionDetailActivity;
import com.example.myapplication.activity.CreateCompetitionActivity;
import com.example.myapplication.activity.CreatePostActivity;
import com.example.myapplication.activity.PostsDetailActivity;
import com.example.myapplication.activity.TabbarActivity;
import com.example.myapplication.adapter.CompetitionAdapter;
import com.example.myapplication.adapter.PostsAdapter;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.Competition;
import com.example.myapplication.model.PostItem;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.model.response.LoginResponse;
import com.example.myapplication.util.SPUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;

    private RecyclerView recyclerView1;
    private CompetitionAdapter competitionAdapter;
    private List<Competition> competitionList = new ArrayList<>();

    private RecyclerView recyclerView2;
    private PostsAdapter postsAdapter;
    private List<PostItem> postList = new ArrayList<>();

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
        competitionAdapter = new CompetitionAdapter(competitionList);
        recyclerView1.setAdapter(competitionAdapter);
        competitionAdapter.setOnClickListener(new CompetitionAdapter.OnClickListener() {
            @Override
            public void onItemClick(String id) {
                Intent intent = new Intent(requireContext(), CompetitionDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });


        recyclerView2 = view.findViewById(R.id.recyclerView2);
        postsAdapter = new PostsAdapter(postList);
        recyclerView2.setAdapter(postsAdapter);
        postsAdapter.setOnClickListener(new PostsAdapter.OnClickListener() {
            @Override
            public void onItemClick(String id) {
                Intent intent = new Intent(requireContext(), PostsDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        tabLayout = view.findViewById(R.id.tabLayout);
        LinearLayout ll1 = view.findViewById(R.id.ll1);
        LinearLayout ll2 = view.findViewById(R.id.ll2);
        tabLayout.getTabAt(1).select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        ll1.setVisibility(View.VISIBLE);
                        ll2.setVisibility(View.GONE);

                        loadCompetitionList();
                        break;
                    case 1:
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);

                        loadPostList();
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
    }

    private void loadCompetitionList() {
        competitionList.clear();

        getHostActivity().showLoading();

        DefaultRepository.getInstance().getCompetitionList()
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<List<Competition>>>() {
                    @Override
                    public void onSucceeded(DetailResponse<List<Competition>> data) {
                        if (getHostActivity() != null) {
                            getHostActivity().hideLoading();
                        }

                        competitionList.addAll(data.getData());
                        competitionAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onFailed(DetailResponse<List<Competition>> data, Throwable e) {

                        if (getHostActivity() != null) {
                            getHostActivity().hideLoading();
                        }

                        return false;
                    }
                });

    }

    private void loadPostList() {
        postList.clear();

        getHostActivity().showLoading();

        DefaultRepository.getInstance().getPostList()
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<List<PostItem>>>() {
                    @Override
                    public void onSucceeded(DetailResponse<List<PostItem>> data) {
                        if (getHostActivity() != null) {
                            getHostActivity().hideLoading();
                        }

                        postList.addAll(data.getData());
                        postsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onFailed(DetailResponse<List<PostItem>> data, Throwable e) {

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

        switch (tabLayout.getSelectedTabPosition()) {
            case 0:
                loadCompetitionList();
                break;
            case 1:
                loadPostList();
                break;
        }
    }
}
