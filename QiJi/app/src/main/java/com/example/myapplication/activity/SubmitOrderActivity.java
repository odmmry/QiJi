package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.ShopGoods;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.util.Constant;
import com.example.myapplication.util.SPUtil;
import com.example.myapplication.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class SubmitOrderActivity extends BaseActivity {

    private List<ShopGoods> shopGoodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        LinearLayout llGoodsContainer = findViewById(R.id.llGoodsContainer);
        TextView tvCardTitle = findViewById(R.id.tvCardTitle);
        EditText etName = findViewById(R.id.etName);
        EditText etPhone = findViewById(R.id.etPhone);
        EditText etDetail = findViewById(R.id.etDetail);

        shopGoodsList = new Gson().fromJson(getIntent().getStringExtra("goods"), new TypeToken<List<ShopGoods>>(){}.getType());;
        tvCardTitle.setText(String.format("共%d件商品", shopGoodsList.size()));
        int childCount = llGoodsContainer.getChildCount();
        for (int i = childCount - 1; i > 1; i--) {
            llGoodsContainer.removeViewAt(i);
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        for (ShopGoods shopGoods : shopGoodsList) {
            View listItemView = inflater.inflate(R.layout.item_submit_order_goods, llGoodsContainer, false);
            ImageView imageView = listItemView.findViewById(R.id.imageView);
            TextView tvName = listItemView.findViewById(R.id.tvName);
            TextView tvPrice = listItemView.findViewById(R.id.tvPrice);

            Glide.with(this)
                    .load(shopGoods.getImgList().get(0))
                    .into(imageView);
            tvName.setText(shopGoods.getName());
            tvPrice.setText("¥" + shopGoods.getPrice());

            llGoodsContainer.addView(listItemView);
        }



        findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String detail = etDetail.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || detail.isEmpty()) {
                    ToastUtil.showToast("请输入收货信息");
                    return;
                }

                // 获取当前登录的用户id
                String userId = SPUtil.getInstance().getString("user_id", "");


                if (getIntent().getStringExtra(Constant.NAV_SUBMIT_ORDER_TYPE).equals(Constant.NAV_SUBMIT_ORDER_TYPE_1)) {
                    // 直接下单

                    DefaultRepository.getInstance().submitOrder(shopGoodsList.get(0).get_id(), name, phone, detail, userId)
                            .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                            .subscribe(new HttpObserver<BaseResponse>() {
                                @Override
                                public void onSucceeded(BaseResponse data) {
                                    hideLoading();

                                    ToastUtil.showToast("下单成功");


                                    startActivityAfterFinishThis(OrderListActivity.class);
                                }

                                @Override
                                public boolean onFailed(BaseResponse data, Throwable e) {
                                    hideLoading();

                                    return false;
                                }
                            });
                } else {
                    // 购物车下单

                    DefaultRepository.getInstance().submitOrder(name, phone, detail, userId)
                            .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                            .subscribe(new HttpObserver<BaseResponse>() {
                                @Override
                                public void onSucceeded(BaseResponse data) {
                                    hideLoading();

                                    ToastUtil.showToast("下单成功");


                                    startActivityAfterFinishThis(OrderListActivity.class);
                                }

                                @Override
                                public boolean onFailed(BaseResponse data, Throwable e) {
                                    hideLoading();

                                    return false;
                                }
                            });
                }
            }
        });
    }
}