package com.example.tisseo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListPhysicPoint extends ArrayAdapter<StopTrip> {

    private LayoutInflater mInflater;
    private ArrayList<StopTrip> stops ;

    private int mViewResourceId;

    public ListPhysicPoint(Context context, int textViewResourceId, ArrayList<StopTrip> stops) {
        super(context, textViewResourceId, stops);
        this.stops = stops;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        StopTrip stop = stops.get(position);

        if (stop != null) {
            TextView destination = (TextView) convertView.findViewById(R.id.list_ps_dest);
            TextView line = (TextView) convertView.findViewById(R.id.list_ps_line);
            TextView hour = (TextView) convertView.findViewById(R.id.list_ps_hour);

            if (destination != null) {
                destination.setText((stop.getDestination()));
            }
            if (line != null) {
                line.setText((stop.getLigne()));
            }
            if (hour != null) {
                // make data to display hours
                hour.setText((stop.getHour_id()));
            }

        }
        return convertView;
    }
}
