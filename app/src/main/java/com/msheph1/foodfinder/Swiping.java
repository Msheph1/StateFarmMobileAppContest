package com.msheph1.foodfinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Swiping extends AppCompatActivity {
    /*

    EDITED MANIFEST TO MAKE THIS FIRST PAGE CHANGE BACK AFTER TESTING





     */
    private ArrayAdapter<String> arrayAdapter;
    private ResturantAdapter arrAdapter;
    List<Resturant> data;
    SwipeFlingAdapterView flingAdapterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiping);


        Bundle extras = getIntent().getExtras();
        String resStr = "";
        if(extras != null)
        {

            resStr = extras.getString("res");
        }
        ListController lc = new ListController();
        if(resStr == null || resStr.equals("")) {
            Toast.makeText(Swiping.this, "No results found please edit the filters", Toast.LENGTH_LONG).show();
            //want to alert that no results where found
            return;
        }
        else {
            strToResturantArr(resStr, lc);
        }

        flingAdapterView=findViewById(R.id.swipe);
        ArrayList<Resturant> likedRes = new ArrayList<>();

        ArrayList<Resturant> data = lc.getResturants();
        Collections.shuffle(data);
        arrAdapter = new ResturantAdapter(Swiping.this, data);
        flingAdapterView.setAdapter(arrAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                data.remove(0);
                arrAdapter.notifyDataSetChanged();
                if(data.size() == 0)
                {
                    Intent i = new Intent(Swiping.this, PickedResult.class);
                    i.putExtra("liked",lc.getResturantsStr(likedRes));
                    startActivity(i);
                }
            }

            @Override
            public void onLeftCardExit(Object o) {


            }

            @Override
            public void onRightCardExit(Object o) {

                likedRes.add((Resturant)o);
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

        Button nextpage = findViewById(R.id.nextpagebtn);
        Button like = findViewById(R.id.like);
        Button dislike = findViewById(R.id.dislike);

        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Swiping.this, PickedResult.class);
                i.putExtra("liked",lc.getResturantsStr(likedRes));
                startActivity(i);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingAdapterView.getTopCardListener().selectRight();
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingAdapterView.getTopCardListener().selectLeft();
            }
        });
    }


    private void strToResturantArr(String str, ListController lc)
    {
        String[] resturants = str.split("//");
        ArrayList<Resturant> arr = new ArrayList<>();
        for(int i = 0; i < resturants.length; i++)
        {
            String[] info = resturants[i].split(",,,");
            Log.i("Swiping", info.toString());
            Resturant temp = new Resturant(info[0], Long.parseLong(info[1]),Double.parseDouble(info[2]),Double.parseDouble(info[3]),info[4],Boolean.parseBoolean(info[5]),info[6]);
            arr.add(temp);
        }
        lc.setResturants(arr);
    }
}