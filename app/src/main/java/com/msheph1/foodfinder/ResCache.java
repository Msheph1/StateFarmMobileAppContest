package com.msheph1.foodfinder;

import java.util.ArrayList;

public class ResCache {
    private static ArrayList<Resturant> likedResturants = new ArrayList<>();
    private static ArrayList<Resturant> allResturants = new ArrayList<>();
    private static String nextPageKey;
    private static double ulati;

    private static double ulngi;
    public static ArrayList<Resturant> getAllResturants() {
        return allResturants;
    }
    public static ArrayList<Resturant> getLikedResturants(){ return likedResturants;}

    public static void setAllResturants(ArrayList<Resturant> allResturants) {
        ResCache.allResturants = allResturants;
    }

    public static void setLikedResturants(ArrayList<Resturant> likedResturants) {
        ResCache.likedResturants = likedResturants;
    }

    public static String getNextPageKey() {
        return nextPageKey;
    }

    public static void setNextPageKey(String nextPageKey) {
        ResCache.nextPageKey = nextPageKey;
    }

    public static void setUlati(double ulati) {
        ResCache.ulati = ulati;
    }

    public static void setUlngi(double ulngi) {
        ResCache.ulngi = ulngi;
    }

    public static double getUlati() {
        return ulati;
    }

    public static double getUlngi() {
        return ulngi;
    }
}
