package com.msheph1.foodfinder;

import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Search {
    //implementing the api
    private final String apiKey;
    private ListController lc;

    public Search(String apiKey, ListController lc)
    {
        this.apiKey = apiKey;
        this.lc = lc;
    }



    /*grabs all resturants nearby by uisng the google places API

       @param ulat  user latitude
       @param ulng user longitude
       @return String a json string

        */
    private String getNearbyRes(double ulat, double ulng, int dist, int minp, int maxp, boolean open)
    {
        try {
            String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
            String keyword = "";
            String type = "restaurant";
            String location = ulat + ", " + ulng;
            String urlParameters = String.format("keyword=%s&location=%s&radius=%d&type=%s&maxprice=%d&minprice=%d&opennow=%b&key=%s",
                    URLEncoder.encode(keyword, "UTF-8"),
                    URLEncoder.encode(location, "UTF-8"),
                    dist,
                    URLEncoder.encode(type, "UTF-8"),
                    maxp,
                    minp,
                    open,
                    URLEncoder.encode(apiKey, "UTF-8"));

            URL url = new URL(baseUrl + "?" + urlParameters);
            //URL url = new URL("");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //trying buffer method
            int bufferSize = 8192;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            char[] buffer = new char[bufferSize];
            int bytesRead;
            while((bytesRead = reader.read(buffer)) != -1)
            {
                response.append(buffer, 0, bytesRead);
            }
            reader.close();
            connection.disconnect();
            return response.toString();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "err";
    }

    /*calls getNearbyRes to grab all resturants and parses the json file to grab the information needed such as
    Name, Price, Rating, Location, Image, OpenNow
    then creates a new resturant object and saves it to the arraylist

   @param ulat  user latitude
   @param ulng user longitude
   @return ArrayList<Resturant> a list of all the resturants in teh nearby area



    */
    private ArrayList<Resturant> parseRes(double ulat, double ulng, String unparsed)
    {
        ArrayList<Resturant> res = new ArrayList<>();
        Log.i("MainActivity", unparsed);
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(unparsed);
            JSONObject json = (JSONObject) obj;
            JSONArray resarr = (JSONArray) json.get("results");
            int resturantCount = 0;
            for(int i = 0; i< resarr.size(); i++)
            {
                JSONObject tempobj = (JSONObject) resarr.get(i);
                //geometry/ location/ lat lng
                JSONObject geometry = tempobj.containsKey("geometry") ? (JSONObject) tempobj.get("geometry") : null;
                double latitude = 0;
                double longitude = 0;
                if(geometry != null)
                {
                    JSONObject cords = geometry.containsKey("location") ? (JSONObject) geometry.get("location") : null;
                    if(cords != null)
                    {
                        latitude = cords.containsKey("lat") ? (double) cords.get("lat") : 300;
                        longitude = cords.containsKey("lng") ? (double) cords.get("lng") : 300;
                    }
                }
                //may want to switch to 2(sqrt((dist^2)/2)
                double distance = calcDistance(ulat, ulng, latitude, longitude) / 1.61;
                distance = Math.floor(distance * 100) / 100;

                //photos/html_attributions/
                JSONArray photos = tempobj.containsKey("photos") ? (JSONArray) tempobj.get("photos") : null;
                String photoref = "";
                if(photos != null)
                {
                    photoref = ((JSONObject) photos.get(0)).containsKey("photo_reference") ? (String) ((JSONObject) photos.get(0)).get("photo_reference") : null;
                }
                //name
                //price_level
                //rating
                String name = tempobj.containsKey("name") ? (String) tempobj.get("name") : "N/A";
                long price = tempobj.containsKey("price_level") ? (long) tempobj.get("price_level") : -1;
                double rating = tempobj.containsKey("rating") ? ((Number) tempobj.get("rating")).doubleValue() : -1;
                String address = tempobj.containsKey("vicinity") ? (String) tempobj.get("vicinity") : "N/A";
                //opening_hours/ open_now
                boolean open = false;
                JSONObject openinghours = tempobj.containsKey("opening_hours") ? (JSONObject) tempobj.get("opening_hours") : null;
                if(openinghours != null)
                {
                    open = (boolean) openinghours.get("open_now");
                }
                String openstr = open ? "Yes" : "No";
                boolean permClosed = false;
                //may want to change this to check for temp close
                permClosed =  tempobj.containsKey("permanently_closed") ? (boolean) tempobj.get("permanently_closed") : false;

                Log.i("MainActivity", i + "\tName = " + name + "\nPrice = " + price + "\nrating = " + rating + "\nlat = " +
                        latitude + "\nlng = " + longitude +  "\ndistance = " + distance + "\naddress = " + address  + "\nopen? = " + openstr + "\nphoto ref = " + photoref + "\n");
                String pricestr = "";
                if(price > 0)
                {
                    for(int str = 0; str < price; str++)
                    {
                        pricestr += "$";
                    }
                }
                if(!permClosed) {
                    res.add(new Resturant(name, pricestr, rating, distance, address, openstr, photoref));
                    resturantCount++;
                }


            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return res;
    }

    /*creates a new thread to call parseRes which grabs all the resturants in the local area and adds them to the array
    and waits for the thread to join back *** May want to change this due to cause pause***
    Stores the found resturants in the resturant array
    @param ulat  user latitude
    @param ulng user longitude



     */
    public void getResults(double ulat, double ulng, int dist, int minp, int maxp, Boolean open)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String unparsed = getNearbyRes(ulat, ulng,dist,minp,maxp,open);
                lc.setResturants(parseRes(ulat,ulng, unparsed));

            }
        });
        thread.start();
        try {
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    private double calcDistance(double ulat, double ulng, double lat, double lng)
    {

        double Eradius = 6371;
        double dLat = (ulat - lat) * (Math.PI/180);
        double dLng = (ulng - lng) * (Math.PI/180);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(ulat *(Math.PI/180)) * Math.cos(lat * (Math.PI/180)) * Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return Eradius * c;

    }

    //Prints all the resturants in the array
    public void printResList()
    {

        for(int i = 0; i< lc.getResturants().size(); i++)
        {
            Log.i("MainActivity", lc.getResturants().get(i).toString());
            return;
        }
        Log.i("MainActivity", "The List is empty");
    }


}
