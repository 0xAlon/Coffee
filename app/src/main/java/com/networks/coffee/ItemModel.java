package com.networks.coffee;

import androidx.annotation.DrawableRes;

public class ItemModel {

    private String name;
    @DrawableRes
    private int imageRes;
    private String overview;
    private String url;
    private String price;

    public ItemModel(){}

    public ItemModel(String name, String overview, String price, String url) {
        this.name = name;
        this.overview = overview;
        this.price = price;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DrawableRes
    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(@DrawableRes int imageRes) {
        this.imageRes = imageRes;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
