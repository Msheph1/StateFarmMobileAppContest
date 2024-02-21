package com.msheph1.foodfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;




import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


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


        Places.initializeWithNewPlacesApiEnabled(getApplicationContext(), apiKey);
        placesClient = Places.createClient(this);
        Log.i("MainActivity","Before calling");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getNearbyRes();
            }
        }).start();





    }
/*
    private void getNearbyRes()
    {
        try {
            Log.i("MainActivity","testing output in the method");
            URL url = new URL("https://places.googleapis.com/v1/places:searchNearby");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Goog-Api-Key", apiKey);
            connection.setRequestProperty("X-Goog-FieldMask", "places.displayName");
            connection.setDoOutput(true);
            String jsonPayload = "{\"includedTypes\": [\"restaurant\"], \"maxResultCount\": 10, \"locationRestriction\": {\"circle\": {\"center\": {\"latitude\": 37.7937, \"longitude\": -122.3965}, \"radius\": 500.0}}}";
            Log.i("MainActivity","connection attempt");
            try(OutputStream outputStream = connection.getOutputStream())
            {
                byte[] input = jsonPayload.getBytes("utf-8");
                outputStream.write(input,0,input.length);
            }

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())))
            {
                StringBuilder response = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null)
                {
                    response.append(line);
                }
                Log.i("MainActivity","printing the whole thing\n" + response.toString());
            }

            connection.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
 */

    private void getNearbyRes()
    {
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



            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null)
            {
                response.append(line);
            }
            reader.close();
            Log.i("MainActivity","printing the whole thing\n" + response.toString());


            connection.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }






}



