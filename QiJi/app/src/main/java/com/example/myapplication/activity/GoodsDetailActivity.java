package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.PostItem;
import com.example.myapplication.model.ShopGoods;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.Constant;
import com.example.myapplication.util.SPUtil;
import com.example.myapplication.util.ToastUtil;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class GoodsDetailActivity extends BaseActivity {

    private ShopGoods detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        Banner banner = findViewById(R.id.banner);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvDesc = findViewById(R.id.tvDesc);



        showLoading();

        DefaultRepository.getInstance().getGoodsDetail(getIntent().getStringExtra("id"))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<ShopGoods>>() {
                    @Override
                    public void onSucceeded(DetailResponse<ShopGoods> data) {
                        hideLoading();

                        detail = data.getData();


                        banner.setAdapter(new BannerImageAdapter<String>(detail.getImgList()) {
                                    @Override
                                    public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                                        Glide.with(holder.itemView)
                                                .load(data)
                                                .into(holder.imageView);
                                    }
                                })
                                .addBannerLifecycleObserver(GoodsDetailActivity.this)
                                .setIndicator(new CircleIndicator(GoodsDetailActivity.this));


                        tvPrice.setText("¥" + detail.getPrice());
                        tvName.setText(detail.getName());
                        tvDesc.setText(detail.getDesc());
                    }

                    @Override
                    public boolean onFailed(DetailResponse<ShopGoods> data, Throwable e) {

                        hideLoading();

                        return false;
                    }
                });


        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tvAddToCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();

                // 获取当前登录的用户id
                String userId = SPUtil.getInstance().getString("user_id", "");


                DefaultRepository.getInstance().addGoodsToCart(detail.get_id(), userId)
                        .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                        .subscribe(new HttpObserver<BaseResponse>() {
                            @Override
                            public void onSucceeded(BaseResponse data) {
                                hideLoading();

                                ToastUtil.showToast("加入购物车成功");
                            }

                            @Override
                            public boolean onFailed(BaseResponse data, Throwable e) {
                                hideLoading();

                                return false;
                            }
                        });
            }
        });

        findViewById(R.id.tvBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsDetailActivity.this, SubmitOrderActivity.class);
                intent.putExtra(Constant.NAV_SUBMIT_ORDER_TYPE, Constant.NAV_SUBMIT_ORDER_TYPE_1);
                intent.putExtra("goods", new Gson().toJson(List.of(
                        detail
                )));
                startActivity(intent);
            }
        });
    }
}