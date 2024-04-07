package com.msheph1.foodfinder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PickedResult extends AppCompatActivity {


    int randomRes= -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picked_result);


        Bundle extras = getIntent().getExtras();
        String resStr = "";
        if(extras != null)
        {

            resStr = extras.getString("liked");
            Log.i("str", resStr);
        }
        ListController lc = new ListController();
        if(resStr == null || resStr.equals("")) {
            Toast.makeText(PickedResult.this, "No liked resturants found please try again", Toast.LENGTH_LONG).show();
            return;
        } else {
            Log.i("str", "running");
            strToLikedResturantArr(resStr, lc);
        }
        ArrayList<Resturant> liked = lc.getLikedResturants();
        for(int a = 0; a < liked.size(); a++)
        {
            Log.i("PICKED", liked.get(a).toString());
        }

        Button genNew = findViewById(R.id.genNewBtn);
        selectResturant(liked);
        genNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectResturant(liked);
            }
        });

    }


    private void selectResturant(ArrayList<Resturant> arr)
    {
        int index = randomResturants(arr.size());
        while(arr.size() > 1 && index == randomRes)
        {
            index = randomResturants(arr.size());
        }
        randomRes = index;
        TextView resName = findViewById(R.id.resNameText);
        TextView price = findViewById(R.id.priceText);
        TextView rating = findViewById(R.id.ratingText);
        TextView distance = findViewById(R.id.distanceText);
        TextView address = findViewById(R.id.addressText);
        TextView open = findViewById(R.id.openText);
        resName.setText(arr.get(index).getName());
        price.setText(arr.get(index).getPrice());
        String ratingStr = arr.get(index).getRating() + "/5";
        rating.setText(ratingStr);
        String distanceStr = arr.get(index).getDistance() + " Miles";
        distance.setText(distanceStr);
        address.setText(arr.get(index).getAddress());
        open.setText(String.valueOf(arr.get(index).getOpen()));


    }




    private int randomResturants(int size){
        return (int)Math.floor(Math.random() * size);
    }


    private void strToLikedResturantArr(String str, ListController lc)
    {
        String[] resturants = str.split("//");
        ArrayList<Resturant> arr = new ArrayList<>();
        for(int i = 0; i < resturants.length; i++)
        {
            String[] info = resturants[i].split(",,,");
            Resturant temp = new Resturant(info[0], info[1],Double.parseDouble(info[2]),Double.parseDouble(info[3]),info[4],info[5],info[6]);
            arr.add(temp);
        }
        lc.setLikedResturants(arr);
    }
}