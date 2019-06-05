package com.example.tisseo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class ListStop extends ArrayAdapter<Stop> {

    private LayoutInflater mInflater;
    private ArrayList<Stop> stops ;

    private int mViewResourceId;

    public ListStop(Context context, int textViewResourceId, ArrayList<Stop> stops) {
        super(context, textViewResourceId, stops);
        this.stops = stops;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        Stop stop = stops.get(position);

        if (stop != null) {
            TextView name = (TextView) convertView.findViewById(R.id.name_stop);
            TextView city = (TextView) convertView.findViewById(R.id.city_stop);

            if (name != null) {
                name.setText((stop.getName()));
            }
            if (city != null) {
                city.setText((stop.getCity()));
            }

        }
        return convertView;
    }
}
