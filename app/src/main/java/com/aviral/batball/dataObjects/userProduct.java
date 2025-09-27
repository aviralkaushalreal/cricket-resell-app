package com.aviral.batball.dataObjects;

import java.util.ArrayList;
import java.util.List;

public class userProduct {

    public String title;
    public String cost;
    public String cat;
    public String img;
    public List<String> urls;

    public String desc;
    public String itemKey;
    public String status;


    // Proper constructor
    public userProduct(String title, String cost, String cat, String img, List<String> urls,String desc,String itemKey,String status) {
        this.title = title;
        this.cost = cost;
        this.cat = cat;
        this.desc=desc;
        this.img = img;
        this.itemKey = itemKey;
        this.status=status;
        this.urls = urls != null ? urls : new ArrayList<>();
    }

    public userProduct() {
        this.urls = new ArrayList<>();
    }
}
