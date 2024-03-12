package com.msheph1.foodfinder;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.net.PlacesClient;


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

        
        String location = "41.422937, -87.741847";
        Search search = new Search(apiKey);
        search.getResults(location);

        Log.i("MainActivity", "made it passed get results call");
        search.printResList();

    }
}



