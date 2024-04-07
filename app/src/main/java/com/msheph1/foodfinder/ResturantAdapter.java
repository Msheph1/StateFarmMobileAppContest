package com.msheph1.foodfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        TextView resName = convertView.findViewById(R.id.resNameText);
        TextView price = convertView.findViewById(R.id.priceText);
        TextView rating = convertView.findViewById(R.id.ratingText);
        TextView distance = convertView.findViewById(R.id.distanceText);
        TextView address = convertView.findViewById(R.id.addressText);
        TextView open = convertView.findViewById(R.id.openText);

        resName.setText(res.getName());
        price.setText(String.valueOf(res.getPrice()));
        rating.setText(String.valueOf(res.getRating()));
        distance.setText(String.valueOf(res.getDistance()));
        address.setText(res.getAddress());
        open.setText(String.valueOf(res.getOpen()));

        return convertView;
    }


}
