package com.msheph1.foodfinder;

public class Resturant {

    private String name;
    private long price;
    private String photo;
    private double rating;
    private String distance;
    private boolean open;

    public Resturant(String name, int price, double rating, String distance, boolean open)
    {
        this.name = name;
        this.price = price;
        this.photo = photo;
        this.rating = rating;
        this.distance = distance;
        this.open = open;
    }

    public Resturant(String name, long price, double rating)
    {
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    /*overrides toString method in order to correctly format resturants based on Name Price and Rating

    @return String   formatted output for resturant



    */
    @Override
    public String toString()
    {
        return "Name = " + name + "\nPrice = " + price + "\nrating = " + rating + "\n";
    }

}
