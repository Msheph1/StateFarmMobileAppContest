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
    private ArrayList<Resturant> arr = new ArrayList<>();
    private final String apiKey;

    public Search(String apiKey)
    {
        this.apiKey = apiKey;
    }



    /*grabs all resturants nearby by uisng the google places API

       @param ulat  user latitude
       @param ulng user longitude
       @return String a json string

        */
    private String getNearbyRes(double ulat, double ulng)
    {
        //https://developers.google.com/maps/documentation/places/web-service/search-nearby#json
        try {
            String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
            String keyword = "";
            int radius = 10000;
            String type = "restaurant";
            String location = ulat + ", " + ulng;
            String urlParameters = String.format("keyword=%s&location=%s&radius=%d&type=%s&key=%s",
                    URLEncoder.encode(keyword, "UTF-8"),
                    URLEncoder.encode(location, "UTF-8"),
                    radius,
                    URLEncoder.encode(type, "UTF-8"),
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
        //parse results get
        //name , price, ratings, photos, distance
        //for filters for the api price level cusine type distance ratings open now
        //for filters we have max price/ min price - open now value type-restuarant/food /maybe takeout filter
        return "err";
    }

    /*calls getNearbyRes to grab all resturants and parses the json file to grab the information needed such as
    Name, Price, Rating, Location, Image, OpenNow
    then creates a new resturant object and saves it to the arraylist

   @param ulat  user latitude
   @param ulng user longitude
   @return ArrayList<Resturant> a list of all the resturants in teh nearby area



    */
    private ArrayList<Resturant> parseRes(double ulat, double ulng)
    {
        ArrayList<Resturant> res = new ArrayList<>();
        String unparsed = getNearbyRes(ulat, ulng);
        Log.i("MainActivity", unparsed);
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(unparsed);
            JSONObject json = (JSONObject) obj;
            JSONArray resarr = (JSONArray) json.get("results");
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
                //opening_hours/ open_now
                boolean open = false;
                JSONObject openinghours = tempobj.containsKey("opening_hours") ? (JSONObject) tempobj.get("opening_hours") : null;
                if(openinghours != null)
                {
                    open = (boolean) openinghours.get("open_now");
                }
                boolean permclosed = false;
                permclosed =  tempobj.containsKey("permanently_closed") ? (boolean) tempobj.get("permanently_closed") : false;




                Log.i("MainActivity", i + "\tName = " + name + "\nPrice = " + price + "\nrating = " + rating + "\nlat = " +
                        latitude + "\nlng = " + longitude +  "\ndistance = " + distance + "\nopen? = " + open + "\nphoto ref = " + photoref + "\n");
                res.add(new Resturant(name, price, rating));


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
    public void getResults(double ulat, double ulng)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                arr = parseRes(ulat,ulng);

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

        for(int i = 0; i< arr.size(); i++)
        {
            Log.i("MainActivity", arr.get(i).toString());
            return;
        }
        Log.i("MainActivity", "The List is empty");
    }


}