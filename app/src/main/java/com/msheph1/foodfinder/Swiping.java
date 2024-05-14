package com.msheph1.foodfinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collections;

public class Swiping extends AppCompatActivity {
    /*

    EDITED MANIFEST TO MAKE THIS FIRST PAGE CHANGE BACK AFTER TESTING





     */

    private ArrayAdapter<String> arrayAdapter;
    private ResturantAdapter arrAdapter;
    //Bundle extras;
    double ulati;
    double ulngi;
    String nextPageAPI;
    ArrayList<Resturant> likedRes;
    Handler swipingHandler= new Handler();
    ArrayList<Resturant> data;

    SwipeFlingAdapterView flingAdapterView;
    LoadingPopup loadingPopup = new LoadingPopup(Swiping.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiping);
        //extras = getIntent().getExtras();
        ListController lc = new ListController();
        lc.setResturants(ResCache.getAllResturants());
        /*
        String resStr = "";
        if(extras != null)
        {

            resStr = extras.getString("res");
        }

        if(resStr == null || resStr.equals("")) {
            Toast.makeText(Swiping.this, "No results found please edit the filters", Toast.LENGTH_LONG).show();
            //want to alert that no results where found
            return;
        }
        else {
            strToResturantArr(resStr, lc);
        }
        */

        configureHomeBackButton();
        flingAdapterView=findViewById(R.id.swipe);
        likedRes = new ArrayList<>();
        data = lc.getResturants();
        Collections.shuffle(data);
        arrAdapter = new ResturantAdapter(Swiping.this, data);
        flingAdapterView.setAdapter(arrAdapter);
        Button moreRes = findViewById(R.id.moreResults);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                data.remove(0);
                arrAdapter.notifyDataSetChanged();

            }

            @Override
            public void onLeftCardExit(Object o) {
                if(data.size() == 0 && ResCache.getNextPageKey().equals(""))
                {
                    prepNextPage(lc, likedRes);
                }
                else if(data.size() == 0) {
                    moreRes.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onRightCardExit(Object o) {



                likedRes.add((Resturant)o);
                if(data.size() == 0 && ResCache.getNextPageKey().equals(""))
                {
                    prepNextPage(lc, likedRes);
                }
                else if(data.size() == 0) {
                    moreRes.setVisibility(View.VISIBLE);
                }
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

        moreRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        loadingPopup.startLoadingDialog();
                    }
                });

                swipingHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Search search = new Search(BuildConfig.PLACES_API_KEY,lc);
                        search.nextPageResults(ResCache.getUlati(),ResCache.getUlngi());
                        if(lc.getResturants().size() != 0) {
                            moreRes.setVisibility(View.INVISIBLE);
                            ArrayList<Resturant> newData = lc.getResturants();
                            data.addAll(newData);
                            Collections.shuffle(data);
                            arrAdapter.notifyDataSetChanged();
                            swipingHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    loadingPopup.dismissDialog();
                                }
                            });
                        }
                        else {
                            swipingHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    loadingPopup.dismissDialog();
                                }
                            });
                            Toast.makeText(getApplicationContext(), "No More Results", Toast.LENGTH_LONG).show();
                        }
                    }
                });



            }
        });
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepNextPage(lc, likedRes);
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

    private void prepNextPage(ListController lc, ArrayList<Resturant> likedRes)
    {

        Intent i = new Intent(Swiping.this, PickedResult.class);
        /*
        i.putExtra("liked",lc.getResturantsStr(likedRes));
        for(int idx = 0; idx < likedRes.size(); idx++)
        {

            i.putExtra("bytearr" + idx, likedRes.get(idx).getBytearr());
        }
         */
        ResCache.setLikedResturants(likedRes);
        startActivity(i);
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
                Intent i = new Intent(Swiping.this,MainScreen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    /*
    private void strToResturantArr(String str, ListController lc)
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
        ulati = extras.getDouble("ulat");
        ulngi = extras.getDouble("ulng");
        lc.setNextPage(extras.getString("nextpage"));


        lc.setResturants(arr);
    }

     */
}