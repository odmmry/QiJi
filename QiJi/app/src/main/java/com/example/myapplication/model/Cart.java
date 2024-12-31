package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cart {

    private String _id;

    @SerializedName("shop_goods_id")
    private String shopGoodsId;

    private String price;

    @SerializedName("imgList")
    private List<String> imgList;

    private String name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getShopGoodsId() {
        return shopGoodsId;
    }

    public void setShopGoodsId(String shopGoodsId) {
        this.shopGoodsId = shopGoodsId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
