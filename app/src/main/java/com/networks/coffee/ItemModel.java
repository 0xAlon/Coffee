package com.networks.coffee;

import androidx.annotation.DrawableRes;

public class ItemModel {

    private String name;
    @DrawableRes
    private int imageRes;
    private String overview;
    private String url;
    private String price;
    private String count;
    private Boolean today;


    private Boolean popular;
    private Boolean alcoholic;
    private int total_count = 0;
    private String documentId;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public ItemModel() {
    } // Default

    public ItemModel(String name, String overview, String price, String url, String count,Boolean alcoholic,Boolean today, Boolean popular,String documentId) {
        this.name = name;
        this.overview = overview;
        this.price = price;
        this.url = url;
        this.count = count;
        this.alcoholic=alcoholic;
        this.today = today;
        this.popular = popular;
        this.documentId=documentId;
    }

    public Boolean getToday() {
        return today;
    }

    public void setToday(Boolean today) {
        this.today = today;
    }

    public Boolean getPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }


    public Boolean getAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(Boolean alcoholic) {
        this.alcoholic = alcoholic;
    }
}
