package com.networks.coffee;

public class TableModel {

    private String type;
    private int places;
    private Boolean status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPlaces() {
        return places;
    }

    public TableModel(String type, int places, Boolean status) {
        this.type = type;
        this.places = places;
        this.status = status;
    }

    public TableModel() {

    }

    public Boolean getStatus() {
        return status;
    }
}
