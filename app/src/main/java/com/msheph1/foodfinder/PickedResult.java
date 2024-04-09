package com.msheph1.foodfinder;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PickedResult extends AppCompatActivity {


    int randomRes= -1;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picked_result);


        extras = getIntent().getExtras();
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

        Button genNew = findViewById(R.id.genNewBtn);
        selectResturant(liked);
        genNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectResturant(liked);
            }
        });

        Button listBtn = findViewById(R.id.viewAllBtn);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PickedResult.this, LikedListed.class);
                i.putExtra("liked",lc.getResturantsStr(liked));
                for(int idx = 0; idx < lc.getLikedResturants().size(); idx++)
                {
                    i.putExtra("bytearr" + idx, lc.getLikedResturants().get(idx).getBytearr());
                }
                startActivity(i);

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

        for(int i = 0; i< arr.size(); i++)
        {
            byte[] bytearr = (byte[]) extras.getByteArray("bytearr" + i);
            arr.get(i).setBytearr(bytearr);
            arr.get(i).setBitmap(BitmapFactory.decodeByteArray(bytearr, 0, bytearr.length));
        }
        lc.setLikedResturants(arr);
    }
}