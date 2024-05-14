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
        ListController lc = new ListController();
        lc.setResturants(ResCache.getAllResturants());
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
}