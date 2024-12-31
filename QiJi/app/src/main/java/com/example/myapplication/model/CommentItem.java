package com.example.myapplication.model;

import com.example.myapplication.util.DateUtil;

public class CommentItem {

    private String postId;

    private String content;

    private Long createTime;

    private String userId;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTimeAsString() {
        return DateUtil.convertTimeToString(createTime);
    }
}
