package com.msheph1.foodfinder;

import java.util.ArrayList;

public class ListController {

    private ArrayList<Resturant> resturantArrayList;
    private ArrayList<Resturant> likedList;

    public ListController()
    {
        resturantArrayList = new ArrayList<>();
        likedList = new ArrayList<>();
    }


    public void setResturants(ArrayList<Resturant> arr)
    {
        this.resturantArrayList = arr;
    }

    public ArrayList<Resturant> getResturants()
    {
        return this.resturantArrayList;
    }




}
