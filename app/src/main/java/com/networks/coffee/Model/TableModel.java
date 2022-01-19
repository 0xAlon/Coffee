package com.networks.coffee.Model;

public class TableModel {

    private String documentId;
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

    public TableModel(String type, int places, Boolean status, String documentId) {
        this.type = type;
        this.places = places;
        this.status = status;
        this.documentId = documentId;
    }

    public TableModel() {

    }

    public Boolean getStatus() {
        return status;
    }

    public String getDocumentId() {
        return documentId;
    }
}

