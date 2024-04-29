package com.max.finalproj;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ContactController {

    
    @GetMapping({"/index","/"})
    public String showContactForm(){
        return "redirect:/html/index2.html";
    }


    @PostMapping("html/results")
    public String sendContactForm(HttpServletRequest request, Model model) {
        //get params
        Double lati = Double.parseDouble(request.getParameter("lati"));
        Double lngi = Double.parseDouble(request.getParameter("longi"));
        int distance= Integer.parseInt(request.getParameter("dist"));
        int minPrice = Integer.parseInt(request.getParameter("minprice"));
        int maxPrice = Integer.parseInt(request.getParameter("maxprice"));
        boolean open = Boolean.parseBoolean(request.getParameter("open"));


        ArrayList<Resturant> res = convertToResturantArr(lati, lngi, getResturants(lati, lngi, distance, minPrice, maxPrice, open));
        Collections.shuffle(res);
        for(int i = 0; i< res.size(); i++)
        {
            res.get(i).setId(i);
        }
        model.addAttribute("resturants", res);


        
        
        return "newResults";

    }

    private String getResturants(double lati, double lngi, int distance, int minprice, int maxprice, boolean open)
    {
        String apiKey = BuildConfig.PLACES_API_KEY;
        try {
            String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
            String keyword = "";
            String type = "restaurant";
            String location = lati + ", " + lngi;
            String urlParameters = String.format("keyword=%s&location=%s&radius=%d&type=%s&maxprice=%d&minprice=%d&opennow=%b&key=%s",
                    URLEncoder.encode(keyword, "UTF-8"),
                    URLEncoder.encode(location, "UTF-8"),
                    distance,
                    URLEncoder.encode(type, "UTF-8"),
                    maxprice,
                    minprice,
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

    private ArrayList<Resturant> convertToResturantArr(double ulat, double ulng, String str)
    {
        String apiKey = BuildConfig.PLACES_API_KEY;
        ArrayList<Resturant> res = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(str);
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
                double distance = calcDistance(ulat, ulng, latitude, longitude) / 1.61;
                distance = Math.floor(distance * 100) / 100;

                //photos/html_attributions/
                JSONArray photos = tempobj.containsKey("photos") ? (JSONArray) tempobj.get("photos") : null;
                String photoref = "";
                byte[] bytearr = null;
                if(photos != null)
                {
                    photoref = ((JSONObject) photos.get(0)).containsKey("photo_reference") ? (String) ((JSONObject) photos.get(0)).get("photo_reference") : null;
                    String baseurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=854&maxheight=480&photo_reference=" + photoref + "&key=" + apiKey;
                    try {
                        URL url = new URL(baseurl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                        connection.disconnect();
                        bytearr = out.toByteArray();


                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    

                }
                String encoded = Base64.getEncoder().encodeToString(bytearr);
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


                String pricestr = "";
                if(price > 0)
                {
                    for(int monies = 0; monies < price; monies++)
                    {
                        pricestr += "$";
                    }
                }
                if(!permClosed) {
                    Resturant temp = new Resturant(name, pricestr, rating, distance, address, openstr, encoded);
                    res.add(temp);
                }


            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return res;
    }


    private double calcDistance(double lati, double lngi, double latitude, double longitude)
    {

        double Eradius = 6371;
        double dLat = (lati - latitude) * (Math.PI/180);
        double dLng = (lngi - longitude) * (Math.PI/180);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lati *(Math.PI/180)) * Math.cos(latitude * (Math.PI/180)) * Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return Eradius * c;

    }
}


