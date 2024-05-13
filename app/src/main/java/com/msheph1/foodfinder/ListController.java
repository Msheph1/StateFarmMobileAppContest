package com.msheph1.foodfinder;

import java.util.ArrayList;

public class ListController {

    private ArrayList<Resturant> resturantArrayList;
    private ArrayList<Resturant> likedList;

    private String nextPage;

    public ListController()
    {
        resturantArrayList = new ArrayList<>();
        likedList = new ArrayList<>();
        nextPage = "";
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

    public void setLikedResturants(ArrayList<Resturant> arr){ this.likedList = arr;}

    public ArrayList<Resturant> getLikedResturants(){return this.likedList;}

    public void setNextPage(String str){this.nextPage = str;}
    public String getNextPage(){return this.nextPage;}

    public String toString()
    {
        String temp = "";
        for(int i = 0; i< resturantArrayList.size(); i++ )
        {
            temp += resturantArrayList.get(i).toString();
        }
        return temp;
    }






}
