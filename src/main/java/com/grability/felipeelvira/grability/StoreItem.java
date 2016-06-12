package com.grability.felipeelvira.grability;


/**
 * Created by felipeelvira on 5/28/16.
 */
public class StoreItem {


    private String label;
    private String thumbnail;
    private String large;
    private String description;

    public StoreItem(String label, String thumbnail, String large, String description){

        this.label = label;
        this.thumbnail = thumbnail;
        this.large = large;
        this.description = description;

    }

    public String getLabel() {
        return label;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public String getLarge() {
        return large;
    }
    public String getDescription() {
        return description;
    }

}
