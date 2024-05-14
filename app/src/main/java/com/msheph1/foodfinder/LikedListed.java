package com.msheph1.foodfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LikedListed extends AppCompatActivity {

    //Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_listed);
        ListController lc = new ListController();
        lc.setLikedResturants(ResCache.getLikedResturants());
        /*
        extras = getIntent().getExtras();
        String resStr = "";
        if(extras != null)
        {

            resStr = extras.getString("liked");
        }

        if(resStr == null || resStr.equals("")) {
            Toast.makeText(LikedListed.this, "No liked resturants found please try again", Toast.LENGTH_LONG).show();
            return;
        } else {
            strToLikedResturantArr(resStr, lc);
        }
         */
        if(lc.getLikedResturants().size() == 0) {
            Toast.makeText(LikedListed.this, "No liked resturants found please try again", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<Resturant> liked = lc.getLikedResturants();
        redoList(liked);
        Button distBtn = findViewById(R.id.distanceBtn);
        Button priceBtn = findViewById(R.id.priceBtn);
        Button ratingBtn = findViewById(R.id.ratingBtn);
        distBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByDistance(liked);
            }
        });
        priceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPrice(liked);
            }
        });
        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByRating(liked);
            }
        });

        configureHomeBackButton();

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
                Intent i = new Intent(LikedListed.this,MainScreen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    private void redoList(ArrayList<Resturant> arr)
    {
        ResturantAdapter arrAdapter = new ResturantAdapter(LikedListed.this,arr);
        ListView lv = (ListView) findViewById(R.id.listId);
        lv.setAdapter(arrAdapter);
    }

    private void sortByDistance(ArrayList<Resturant> arr)
    {
        for(int i =0; i<arr.size(); i++)
        {
            int min = i;
            for(int j =i +1; j< arr.size(); j++)
            {
                if(arr.get(min).getDistance() > arr.get(j).getDistance())
                {
                    min = j;
                }
            }
            Resturant temp = arr.get(i);
            arr.set(i, arr.get(min));
            arr.set(min, temp);
        }
        redoList(arr);
    }
    private void sortByPrice(ArrayList<Resturant> arr)
    {
        for(int i =0; i<arr.size(); i++)
        {
            int min = i;
            for(int j =i +1; j< arr.size(); j++)
            {
                if((arr.get(min).getPrice().compareTo(arr.get(j).getPrice())) > 0)
                {
                    min = j;
                }
            }
            Resturant temp = arr.get(i);
            arr.set(i, arr.get(min));
            arr.set(min, temp);
        }
        redoList(arr);
    }
    private void sortByRating(ArrayList<Resturant> arr)
    {
        for(int i =0; i<arr.size(); i++)
        {
            int max = i;
            for(int j =i +1; j< arr.size(); j++)
            {
                if((arr.get(max).getRating() < arr.get(j).getRating()))
                {
                    max = j;
                }
            }
            Resturant temp = arr.get(i);
            arr.set(i, arr.get(max));
            arr.set(max, temp);
        }
        redoList(arr);
    }



    /*
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

        for(int i = 0; i< arr.size(); i++)
        {
            byte[] bytearr = (byte[]) extras.getByteArray("bytearr" + i);
            arr.get(i).setBytearr(bytearr);
            arr.get(i).setBitmap(BitmapFactory.decodeByteArray(bytearr, 0, bytearr.length));
        }
        lc.setLikedResturants(arr);

    }
     */
}