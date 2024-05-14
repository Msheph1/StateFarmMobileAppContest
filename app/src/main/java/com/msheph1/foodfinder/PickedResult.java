package com.msheph1.foodfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

        ListController lc = new ListController();
        lc.setLikedResturants(ResCache.getLikedResturants());
        if(lc.getLikedResturants().size() == 0) {
            Toast.makeText(PickedResult.this, "No liked resturants found please try again", Toast.LENGTH_LONG).show();
            TextView resname = findViewById(R.id.resName);
            TextView price = findViewById(R.id.price);
            TextView rating = findViewById(R.id.rating);
            TextView distance = findViewById(R.id.distance);
            TextView address = findViewById(R.id.address);
            TextView open = findViewById(R.id.open);
            TextView resnameText = findViewById(R.id.resNameText);
            TextView priceText = findViewById(R.id.priceText);
            TextView ratingText = findViewById(R.id.ratingText);
            TextView distanceText = findViewById(R.id.distanceText);
            TextView addressText = findViewById(R.id.addressText);
            TextView openText = findViewById(R.id.openText);
            TextView title = findViewById(R.id.textView3);
            resname.setText("");
            price.setText("");
            rating.setText("");
            distance.setText("");
            address.setText("");
            open.setText("");
            resnameText.setText("");
            priceText.setText("");
            ratingText.setText("");
            distanceText.setText("");
            addressText.setText("");
            openText.setText("");
            title.setText("No Liked Resturants Please Try Again");

        } else {

            //strToLikedResturantArr(resStr, lc);
        }
        ArrayList<Resturant> liked = lc.getLikedResturants();

        Button genNew = findViewById(R.id.genNewBtn);
        if(liked.size() > 0)
            selectResturant(liked);
        genNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(liked.size() > 0)
                    selectResturant(liked);
            }
        });

        Button listBtn = findViewById(R.id.viewAllBtn);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(liked.size() > 0) {

                    Intent i = new Intent(PickedResult.this, LikedListed.class);
                    startActivity(i);
                }

            }
        });

        configureHomeBackButton();

    }


    private void selectResturant(ArrayList<Resturant> arr)
    {
        int index = randomResturants(arr.size());
        while(arr.size() > 1 && index == randomRes)
        {
            index = randomResturants(arr.size());
        }
        randomRes = index;
        ImageView img = findViewById(R.id.imgView);
        TextView resName = findViewById(R.id.resNameText);
        TextView price = findViewById(R.id.priceText);
        TextView rating = findViewById(R.id.ratingText);
        TextView distance = findViewById(R.id.distanceText);
        TextView address = findViewById(R.id.addressText);
        TextView open = findViewById(R.id.openText);
        img.setImageBitmap(arr.get(index).getBitmap());
        resName.setText(arr.get(index).getName());
        price.setText(arr.get(index).getPrice());
        String ratingStr = arr.get(index).getRating() + "/5";
        rating.setText(ratingStr);
        String distanceStr = arr.get(index).getDistance() + " Miles";
        distance.setText(distanceStr);
        address.setText(arr.get(index).getAddress());
        open.setText(String.valueOf(arr.get(index).getOpen()));


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
                Intent i = new Intent(PickedResult.this,MainScreen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
    private int randomResturants(int size){
        return (int)Math.floor(Math.random() * size);
    }
}