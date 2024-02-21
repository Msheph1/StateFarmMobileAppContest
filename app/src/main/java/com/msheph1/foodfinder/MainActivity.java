package com.msheph1.foodfinder;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {




    PlacesClient placesClient;
 String apiKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //look into google nearby search advanced
        // place details
        //place photos
        apiKey = BuildConfig.PLACES_API_KEY;

        if(TextUtils.isEmpty(apiKey) || apiKey.equals("DEFAULT_API_KEY"))
        {
            Log.e("Places test","No api key");
            finish();
            return;
        }



        Log.i("MainActivity","Before calling");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Log.i("MainActivity",getNearbyRes());
                parseRes();
            }
        }).start();





    }

    private String getNearbyRes()
    {
        //https://developers.google.com/maps/documentation/places/web-service/search-nearby#json
        try {
            String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
            String keyword = "cruise";
            String location = "-33.8670522,151.1957362";
            int radius = 1500;
            String type = "restaurant";

            String urlParameters = String.format("keyword=%s&location=%s&radius=%d&type=%s&key=%s",
                    URLEncoder.encode(keyword, "UTF-8"),
                    URLEncoder.encode(location, "UTF-8"),
                    radius,
                    URLEncoder.encode(type, "UTF-8"),
                    URLEncoder.encode(apiKey, "UTF-8"));

            URL url = new URL(baseUrl + "?" + urlParameters);
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
            Log.i("MainActivity", "printing the whole thing\n" + response.toString());
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

    private ArrayList<ArrayList<String>> parseRes()
    {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        //String unparsed = getNearbyRes();
        try {
            Object obj = new JSONParser().parse(getNearbyRes());
            JSONObject jo = (JSONObject) obj;
            String results = (String) jo.get("Results");
            Log.i("MainActivity", results);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return res;
    }








}



