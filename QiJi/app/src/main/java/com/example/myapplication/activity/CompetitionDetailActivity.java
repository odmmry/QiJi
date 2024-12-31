package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.Competition;
import com.example.myapplication.model.PostItem;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.Constant;
import com.example.myapplication.util.DateUtil;
import com.example.myapplication.util.SPUtil;
import com.example.myapplication.util.ToastUtil;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class CompetitionDetailActivity extends BaseActivity {
    private Competition detail;

    private ImageView imageView;
    private TextView tvName;
    private TextView tvDate;
    private TextView tvAddress;
    private TextView tvDesc;
    private TextView tvSignUp;
    private TextView tvNavRide;
    private TextView tvViewRanking;
    private TextView tvTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_detail);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView = findViewById(R.id.imageView);
        tvName = findViewById(R.id.tvName);
        tvDate = findViewById(R.id.tvDate);
        tvAddress = findViewById(R.id.tvAddress);
        tvDesc = findViewById(R.id.tvDesc);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvNavRide = findViewById(R.id.tvNavRide);
        tvViewRanking = findViewById(R.id.tvViewRanking);
        tvTips = findViewById(R.id.tvTips);


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail != null && detail.getAllowPost()) {


                    // 获取当前登录的用户id
                    String userId = SPUtil.getInstance().getString("user_id", "");


                    if (detail.getTypes().equals("1")) {
                        DefaultRepository.getInstance().signUpCompetition1(detail.get_id(), userId)
                                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                                .subscribe(new HttpObserver<BaseResponse>() {
                                    @Override
                                    public void onSucceeded(BaseResponse data) {
                                        hideLoading();

                                        ToastUtil.showToast("报名成功");

                                        loadData();
                                    }

                                    @Override
                                    public boolean onFailed(BaseResponse data, Throwable e) {
                                        hideLoading();

                                        return false;
                                    }
                                });
                    } else {
                        DefaultRepository.getInstance().signUpCompetition2(detail.get_id(), userId)
                                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                                .subscribe(new HttpObserver<BaseResponse>() {
                                    @Override
                                    public void onSucceeded(BaseResponse data) {
                                        hideLoading();

                                        ToastUtil.showToast("报名成功");

                                        loadData();
                                    }

                                    @Override
                                    public boolean onFailed(BaseResponse data, Throwable e) {
                                        hideLoading();

                                        return false;
                                    }
                                });
                    }



                } else {
                    ToastUtil.showToast("报名失败，请检查以下情形：数据不存在、已报名过或活动已结束");
                }
            }
        });
        tvNavRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail != null && detail.getAllowRide()) {
                    Intent intent = new Intent(CompetitionDetailActivity.this, RideActivity.class);
                    intent.putExtra("mode", detail.get_id());
                    startActivity(intent);
                } else {
                    ToastUtil.showToast("操作失败，请检查以下情形：已存在骑行记录、不在比赛时间内或者未报名");
                }
            }
        });
        tvViewRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompetitionDetailActivity.this, CompetitionRankingActivity.class);
                intent.putExtra("competition_id", detail.get_id());
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        showLoading();

        // 获取当前登录的用户id
        String userId = SPUtil.getInstance().getString("user_id", "");

        DefaultRepository.getInstance().getCompetitionDetail(getIntent().getStringExtra("id"), userId)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<Competition>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Competition> data) {
                        hideLoading();

                        detail = data.getData();

                        Glide.with(getHostActivity())
                                .load(detail.getImageUrl())
                                .into(imageView);

                        tvName.setText(detail.getName());
                        tvDate.setText("日期：" + DateUtil.convertTimestampToDateString(detail.getStartDate()) + " 至 " + DateUtil.convertTimestampToDateString(detail.getEndDate()));
                        tvAddress.setText("地点：" + detail.getAddress());
                        tvDesc.setText("描述：" + detail.getDesc());


                        if (detail.getTypes().equals("1")) {
                            tvNavRide.setVisibility(View.VISIBLE);
                            tvViewRanking.setVisibility(View.VISIBLE);
                            tvTips.setVisibility(View.GONE);
                        } else {
                            tvNavRide.setVisibility(View.GONE);
                            tvViewRanking.setVisibility(View.GONE);
                            tvTips.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public boolean onFailed(DetailResponse<Competition> data, Throwable e) {

                        hideLoading();

                        return false;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }
}