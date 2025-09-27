package com.aviral.batball.dataObjects;

public class Product {

    private int imgsrc;

    private String name;

    public Product(int imgsrc,String name){
        this.imgsrc=imgsrc;
        this.name=name;
    }

    public String getName(){return this.name;}
    public int getImgsrc(){return this.imgsrc;}

}
