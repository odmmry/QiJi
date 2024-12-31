package com.example.myapplication.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateGoodsRequest {

    private String name;

    private String price;

    private String desc;

    @SerializedName("imgList")
    private List<String> imgList;

    @SerializedName("user_id")
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
