package com.cqu.accountbook;

public class Card {
    private final String name;
    private final int imageId;

    public Card(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
