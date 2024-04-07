package com.msheph1.foodfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class ResturantAdapter extends ArrayAdapter<Resturant> {

    public ResturantAdapter(Context context, List<Resturant> resturants)
    {
        super(context, 0, resturants);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Resturant res = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.resturantcard2, parent, false);
        }

        //grab ids and edit text

        return convertView;
    }


}
