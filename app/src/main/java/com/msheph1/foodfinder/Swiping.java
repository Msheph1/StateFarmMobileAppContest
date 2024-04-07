package com.msheph1.foodfinder;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Swiping extends AppCompatActivity {

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
        strToResturantArr(resStr, lc);

        TextView test = (TextView) findViewById(R.id.testtext);
        test.setText(resStr);
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