package com.example.myapplication.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreatePostRequest {

    private String content;

    @SerializedName("imgList")
    private List<String> imgList;

    private String user_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }
}
