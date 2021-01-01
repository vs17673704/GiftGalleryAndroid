package com.android.nasr.model;

public class ItemModel {

    String itemid;
    String itemname;
    String itemprice;

    public ItemModel(String itemid, String itemname, String itemprice) {
        this.itemid = itemid;
        this.itemname = itemname;
        this.itemprice = itemprice;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }
}
