package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CompetitionRankingAdapter;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.CompetitionRanking;
import com.example.myapplication.model.RideLog;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class CompetitionRankingActivity extends BaseActivity {

    private ListView listView;
    private CompetitionRankingAdapter competitionRankingAdapter;
    private List<CompetitionRanking> list = new ArrayList<>();

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_ranking);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = findViewById(R.id.listView);
        competitionRankingAdapter = new CompetitionRankingAdapter(this, list);
        listView.setAdapter(competitionRankingAdapter);

        loadList();
    }

    private void loadList() {
        list.clear();

        showLoading();

        DefaultRepository.getInstance().getCompetitionRankingList(getIntent().getStringExtra("competition_id"))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<List<CompetitionRanking>>>() {
                    @Override
                    public void onSucceeded(DetailResponse<List<CompetitionRanking>> data) {
                        hideLoading();

                        list.addAll(data.getData());
                        competitionRankingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onFailed(DetailResponse<List<CompetitionRanking>> data, Throwable e) {

                        hideLoading();

                        return false;
                    }
                });
    }
}