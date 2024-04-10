package com.msheph1.foodfinder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;


public class Filters extends AppCompatActivity {
    /**
     * ADD ERROR CHECKING TO THE FILTERS
     */
    String apiKey;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextInputEditText latitext;
    TextInputEditText longitext;
    Slider main_slider;
    TextView main_sliderVal;
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
        configureSearchButton(search, lc);
        configureLocButton();





        main_slider = findViewById(R.id.slider);
        main_sliderVal = findViewById(R.id.distanceValue);
        main_slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                double rounded = Math.floor(value * 10)/10;
                main_sliderVal.setText("Maximum Distance: " + rounded + " Miles");
            }
        });


    }

    private void configureLocButton()
    {

        latitext = (TextInputEditText) findViewById(R.id.latitudeEditText);
        longitext = (TextInputEditText) findViewById(R.id.longitudeEditText);
        Button btn = findViewById(R.id.locbtn);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();

            }
        });
    }

    private void getLastLocation(){
        if(ContextCompat.checkSelfPermission(Filters.this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null)
                    {
                        Geocoder geocoder = new Geocoder(Filters.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            latitext.setText(String.valueOf(addresses.get(0).getLatitude()));
                            longitext.setText(String.valueOf(addresses.get(0).getLongitude()));
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
        else
        {
            //ask permission
            ActivityCompat.requestPermissions(Filters.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode== 100) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void configureSearchButton(Search search, ListController lc){
        Button btn = findViewById(R.id.btn);
        TextInputEditText latitext = (TextInputEditText) findViewById(R.id.latitudeEditText);
        TextInputEditText longitext = (TextInputEditText) findViewById(R.id.longitudeEditText);

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
                double lati = Double.parseDouble(latitext.getText().toString());
                double lngi = Double.parseDouble(longitext.getText().toString());
                //distance = Integer.parseInt(distancetext.getText().toString());
                minprice = Integer.parseInt(minpricetext.getText().toString());
                maxprice =Integer.parseInt(maxpricetext.getText().toString());
                open = opencheckbox.isChecked();


                /*
                add fetching results pop up
                btn.setEnabled(false);

                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                btn.setEnabled(true);
                            }
                        });
                    }
                }, 1000);
                */


                //search.getResults(lati,lngi,distance,minprice,maxprice,open);
                Intent i = new Intent(Filters.this, Swiping.class);
                i.putExtra("res",lc.getResturantsStr(lc.getResturants()));
                //bitmaps

                for(int idx = 0; idx < lc.getResturants().size(); idx++)
                {
                    i.putExtra("bytearr" + idx, lc.getResturants().get(idx).getBytearr());
                }
                startActivity(i);

            }
        });
    }

}



