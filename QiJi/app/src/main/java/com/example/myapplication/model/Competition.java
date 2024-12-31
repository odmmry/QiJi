package com.example.myapplication.model;

public class Competition {

    private String _id;

    private String name;

    private String imageUrl;

    private String address;

    private String desc;

    private Long startDate;

    private Long endDate;


    // 如果已经报名或者比赛截止就返回false
    private Boolean allowPost;

    // 如果已经有骑行记录或者不在比赛时间内或者未报名返回false
    private Boolean allowRide;

    private String types;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Boolean getAllowPost() {
        return allowPost;
    }

    public void setAllowPost(Boolean allowPost) {
        this.allowPost = allowPost;
    }

    public Boolean getAllowRide() {
        return allowRide;
    }

    public void setAllowRide(Boolean allowRide) {
        this.allowRide = allowRide;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
