package com.msheph1.foodfinder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class Swiping extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter;
    List<String> data;
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
        //strToResturantArr(resStr, lc);

        //TextView test = (TextView) findViewById();
        //test.setText(resStr);
        flingAdapterView=findViewById(R.id.swipe);

        data = new ArrayList<>();
        data.add("avantis");
        data.add("labamba");
        data.add("culvers");
        data.add("medici");

        arrayAdapter = new ArrayAdapter<>(Swiping.this, R.layout.resturantcard, R.id.resNameText, data);
        flingAdapterView.setAdapter(arrayAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                data.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(Swiping.this, "left", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(Swiping.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

        flingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClicked(int i, Object o) {
                    Toast.makeText(Swiping.this, "data is " + data.get(i), Toast.LENGTH_SHORT).show();
                }
        });
        Button like = findViewById(R.id.like);
        Button dislike = findViewById(R.id.dislike);

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