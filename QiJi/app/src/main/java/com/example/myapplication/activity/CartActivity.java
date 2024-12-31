package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.Cart;
import com.example.myapplication.model.ShopGoods;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.Constant;
import com.example.myapplication.util.SPUtil;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class CartActivity extends BaseActivity {

    private CartAdapter cartAdapter;
    private List<Cart> list = new ArrayList<>();

    private TextView tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        cartAdapter = new CartAdapter(list);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.setOnClickListener(new CartAdapter.OnClickListener() {
            @Override
            public void onDeleteClick(String id) {
                showLoading();

                DefaultRepository.getInstance().delGoodsForCart(id)
                        .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                        .subscribe(new HttpObserver<BaseResponse>() {
                            @Override
                            public void onSucceeded(BaseResponse data) {
                                hideLoading();

                                int index = -1;
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).get_id().equals(id)) {
                                        index = i;
                                        break;
                                    }
                                }
                                if (index > -1) {
                                    list.remove(index);
                                    cartAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public boolean onFailed(BaseResponse data, Throwable e) {
                                hideLoading();

                                return false;
                            }
                        });
            }
        });

        findViewById(R.id.tvOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, SubmitOrderActivity.class);

                intent.putExtra(Constant.NAV_SUBMIT_ORDER_TYPE, Constant.NAV_SUBMIT_ORDER_TYPE_2);


                List<ShopGoods> shopGoodsList = new ArrayList<>();
                for (Cart cart : list) {
                    ShopGoods shopGoods = new ShopGoods();
                    shopGoods.set_id(cart.getShopGoodsId());
                    shopGoods.setName(cart.getName());
                    shopGoods.setPrice(cart.getPrice());
                    shopGoods.setImgList(cart.getImgList());

                    shopGoodsList.add(shopGoods);
                }

                intent.putExtra("goods", new Gson().toJson(shopGoodsList));


                startActivity(intent);
            }
        });
    }

    private void loadList() {
        list.clear();

        showLoading();

        // 获取当前登录的用户id
        String userId = SPUtil.getInstance().getString("user_id", "");


        DefaultRepository.getInstance().getCartList(userId)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<List<Cart>>>() {
                    @Override
                    public void onSucceeded(DetailResponse<List<Cart>> data) {
                        hideLoading();

                        list.addAll(data.getData());
                        cartAdapter.notifyDataSetChanged();



                        BigDecimal totalPrice = new BigDecimal("0");
                        for (Cart cart : list) {
                            BigDecimal goodsPrice = new BigDecimal(cart.getPrice());
                            totalPrice = totalPrice.add(goodsPrice);
                        }

                        tvTotalPrice.setText("¥" + totalPrice.setScale(2, RoundingMode.HALF_UP));
                    }

                    @Override
                    public boolean onFailed(DetailResponse<List<Cart>> data, Throwable e) {

                        hideLoading();

                        return false;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadList();
    }
}