package com.msheph1.foodfinder;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;


public class MainActivity extends AppCompatActivity {

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

        //my house
        double lat = 41.406646;
        double lng = -87.828035;
        //sarahs
        double lats = 41.709426;
        double lngs = -87.758101;
        //isu
        double lati = 40.507574;
        double lngi = -88.985315;
        ListController lc = new ListController();
        Search search = new Search(apiKey, lc);


        Button btn = findViewById(R.id.btn);
        TextInputEditText distancetext = (TextInputEditText) findViewById(R.id.distanceEditText);
        TextInputEditText minpricetext = (TextInputEditText) findViewById(R.id.minPriceEditText);
        TextInputEditText maxpricetext = (TextInputEditText) findViewById(R.id.maxPriceEditText);
        CheckBox opencheckbox = (CheckBox) findViewById(R.id.openCheckBox);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view){
                int distance;
                int minprice;
                int maxprice;
                boolean open;
                distance = Integer.parseInt(distancetext.getText().toString());
                minprice = Integer.parseInt(minpricetext.getText().toString());
                maxprice =Integer.parseInt(maxpricetext.getText().toString());
                open = opencheckbox.isChecked();
                Log.i("MainActivity","distance set is = : " + distance);
                Log.i("MainActivity","minprice set is = : " + minprice);
                Log.i("MainActivity","maxprice set is = : " + maxprice);
                Log.i("MainActivity","checkbox set is = : " + open);
                search.getResults(lati,lngi,distance,minprice,maxprice,open);
            }
        });



    }
}



