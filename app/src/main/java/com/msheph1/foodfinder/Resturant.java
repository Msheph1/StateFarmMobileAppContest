package com.msheph1.foodfinder;

public class Resturant {

    private String name;
    private long price;
    private String photo;
    private double rating;
    private double distance;
    private String address;
    private boolean open;

    public Resturant(String name, long price, double rating, double distance, String address, boolean open, String photo)
    {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.distance = distance;
        this.address = address;
        this.open = open;
        this.photo = photo;
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
        return "\tName = " + name + "\nPrice = " + price + "\nrating = " + rating + "\nlat = " +
                "\ndistance = " + distance + "\naddress = " + address  + "\nopen? = " + open + "\nphoto ref = " + photo + "\n";
    }

}
