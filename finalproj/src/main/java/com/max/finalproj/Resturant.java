package com.max.finalproj;



public class Resturant {

    private String name;
    private String price;
    private double rating;
    private double distance;
    private String address;
    private String open;
    private String photo;
    private byte[] bytearr;

    public Resturant(String name, String price, double rating, double distance, String address, String open, String photo)
    {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.distance = distance;
        this.address = address;
        this.open = open;
        this.photo = photo;
    }

    
    public String getName()
    {
        return name;
    }

    public String getPrice()
    {
        return price;
    }

    public double getRating()
    {
        return rating;
    }

    public double getDistance()
    {
        return distance;
    }

    public String getAddress()
    {
        return address;
    }

    public String getOpen()
    {
        return open;
    }

    public String getPhoto()
    {
        return photo;
    }

    public byte[] getBytearr() {
        return bytearr;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setOpen(String open) {
        this.open = open;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setBytearr(byte[] ba){
        this.bytearr = ba;
    }

}
