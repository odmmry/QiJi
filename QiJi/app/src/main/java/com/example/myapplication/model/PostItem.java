package com.example.myapplication.model;

import com.example.myapplication.util.DateUtil;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostItem {

    private String _id;

    private String content;

    @SerializedName("imgList")
    private List<String> imgList;

    @SerializedName("create_time")
    private Long createTime;

    @SerializedName("user_id")
    private String userId;

    private String nickname;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("is_thumb")
    private boolean isThumb;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isThumb() {
        return isThumb;
    }

    public void setThumb(boolean thumb) {
        isThumb = thumb;
    }

    public String getCreateTimeAsString() {
        return DateUtil.convertTimeToString(createTime);
    }
}
