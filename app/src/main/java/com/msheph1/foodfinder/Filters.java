package com.msheph1.foodfinder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
    Handler filtersHandler= new Handler();
    Slider mainSlider;
    TextView mainSliderVal;
    LoadingPopup loadingPopup = new LoadingPopup(Filters.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiKey = BuildConfig.PLACES_API_KEY;

        if(TextUtils.isEmpty(apiKey) || apiKey.equals("DEFAULT_API_KEY"))
        {
            Log.e("Places test","No api key");
            finish();
            return;
        }
        mainSlider = findViewById(R.id.slider);
        mainSliderVal = findViewById(R.id.distanceValue);

        ListController lc = new ListController();
        Search search = new Search(apiKey, lc);
        configureSearchButton(search, lc);
        configureLocButton();
        configureHomeBackButton();






        mainSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                double rounded = Math.floor(value * 10)/10;
                mainSliderVal.setText("Maximum Distance: " + rounded + " Miles");
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        String[] prices = getResources().getStringArray(R.array.prices);
        ArrayAdapter<String> pricesAdapter = new ArrayAdapter<>(this,R.layout.dropdown_item,prices);
        AutoCompleteTextView minPriceTextView =  findViewById(R.id.minPrice);
        AutoCompleteTextView maxPriceTextView =  findViewById(R.id.maxPrice);
        minPriceTextView.setAdapter(pricesAdapter);
        maxPriceTextView.setAdapter(pricesAdapter);
        filtersHandler.post(new Runnable() {
            @Override
            public void run() {
                loadingPopup.dismissDialog();

            }
        });
    }



    private void configureHomeBackButton(){

        Button backbtn = findViewById(R.id.backbtn);
        Button homebtn = findViewById(R.id.homebtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Filters.this,MainScreen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
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
        AutoCompleteTextView minPriceTextView =  findViewById(R.id.minPrice);
        AutoCompleteTextView maxPriceTextView =  findViewById(R.id.maxPrice);
        CheckBox opencheckbox = (CheckBox) findViewById(R.id.openCheckBox);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view){




                int distance;
                int minprice;
                int maxprice;
                boolean open;
                int meters = (int) Math.floor(mainSlider.getValue() * 1.61 * 1000);
                double lati;
                if(latitext.getText() == null || latitext.getText().toString().equals(""))
                {
                    lati = -1000;
                }else{
                    lati = Double.parseDouble(latitext.getText().toString());
                }


                double lngi;
                if(longitext.getText() == null || longitext.getText().toString().equals(""))
                {
                    lngi = -1000;
                }else{
                    lngi = Double.parseDouble(longitext.getText().toString());
                }

                if((lati < -181 || lati > 180) || (lngi < -181 || lngi > 181))
                {

                    Toast.makeText(getApplicationContext(), "Invalid Longitude and Latitude Please Try Again", Toast.LENGTH_LONG).show();
                    return;
                }

                distance = meters;

                minprice = Integer.parseInt(minPriceTextView.getText().toString());
                maxprice =Integer.parseInt(maxPriceTextView.getText().toString());
                open = opencheckbox.isChecked();




                ResCache.setUlati(lati);
                ResCache.setUlngi(lngi);
                //checking to make sure all filter requirements are set
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        loadingPopup.startLoadingDialog();
                    }
                });






                filtersHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        search.getResults(lati,lngi,distance,minprice,maxprice,open);

                        if(lc.getResturants().size() != 0) {

                            Intent i = new Intent(Filters.this, Swiping.class);
                            startActivity(i);
                            Log.i("make it", "6");

                        }
                        else {
                            filtersHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    loadingPopup.dismissDialog();
                                }
                            });
                            Toast.makeText(getApplicationContext(), "0 Results where found with those filters please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });






            }
        });
    }

}



