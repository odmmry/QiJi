package com.example.myapplication.model.request;

public class SubmitOrderRequest {

    private String shopGoodsId;

    private String addressName;

    private String addressPhone;

    private String addressDetail;

    private String userId;

    public String getShopGoodsId() {
        return shopGoodsId;
    }

    public void setShopGoodsId(String shopGoodsId) {
        this.shopGoodsId = shopGoodsId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
