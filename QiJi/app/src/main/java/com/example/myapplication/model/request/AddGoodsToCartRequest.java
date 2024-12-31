package com.example.myapplication.model.request;

import com.google.gson.annotations.SerializedName;

public class AddGoodsToCartRequest {

    @SerializedName("shop_goods_id")
    private String shopGoodsId;
    @SerializedName("user_id")
    private String userId;

    public String getShopGoodsId() {
        return shopGoodsId;
    }

    public void setShopGoodsId(String shopGoodsId) {
        this.shopGoodsId = shopGoodsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
