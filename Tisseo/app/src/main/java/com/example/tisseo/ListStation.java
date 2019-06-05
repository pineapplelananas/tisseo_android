package com.example.tisseo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class ListStation extends ArrayAdapter<Station> {

    private LayoutInflater mInflater;
    private ArrayList<Station> stations ;
    ///ArrayList<HashMap<String, String>> stations;

    private int mViewResourceId;

    public ListStation(Context context, int textViewResourceId, ArrayList<Station> stations) {
        super(context, textViewResourceId, stations);
        this.stations = stations;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        Station station = stations.get(position);

        if (station != null) {
            TextView name = (TextView) convertView.findViewById(R.id.name_station);
            TextView line_number = (TextView) convertView.findViewById(R.id.line_number_station);
            TextView direction= (TextView) convertView.findViewById(R.id.direction_station);

            if (name != null) {
                name.setText((station.getName()));
            }
            if (line_number != null) {
                line_number.setText((station.getLine_number()));
            }
            if (direction != null) {
                direction.setText((station.getDirection()));

            }
        }
        return convertView;
    }
}
