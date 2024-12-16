package com.example.myapplication.bean;

public class CartBean {

    private int goodsId;

    private ShopGoodsBean goods;

    private int num;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public ShopGoodsBean getGoods() {
        return goods;
    }

    public void setGoods(ShopGoodsBean goods) {
        this.goods = goods;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
