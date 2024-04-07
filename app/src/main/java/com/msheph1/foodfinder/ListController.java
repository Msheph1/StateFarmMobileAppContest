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

    public String getResturantsStr(ArrayList<Resturant> arr)
    {
        String str = "";
        for(int i = 0; i < arr.size(); i++)
        {
            str += arr.get(i).getInfo() + "//";
        }
        return str;
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
