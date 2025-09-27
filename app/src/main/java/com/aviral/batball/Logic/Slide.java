package com.aviral.batball.Logic;

public class Slide {
    private int imageResId;
    private String text;

    public Slide(int imageResId, String text) {
        this.imageResId = imageResId;
        this.text = text;
    }

    public int getImageResId() { return imageResId; }
    public String getText() { return text; }
}
