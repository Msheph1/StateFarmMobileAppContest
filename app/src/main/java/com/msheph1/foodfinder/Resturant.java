package com.msheph1.foodfinder;

public class Resturant {

    private String name;
    private String price;
    private String photo;
    private String rating;
    private String distance;
    private boolean open;

    public Resturant(String name, String price, String photo, String rating, String distance, boolean open)
    {
        this.name = name;
        this.price = price;
        this.photo = photo;
        this.rating = rating;
        this.distance = distance;
        this.open = open;
    }

}
