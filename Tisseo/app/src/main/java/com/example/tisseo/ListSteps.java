package com.example.tisseo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListSteps extends ArrayAdapter<String> {

    private LayoutInflater mInflater;
    private ArrayList<String> steps ;

    private int mViewResourceId;

    public ListSteps(Context context, int textViewResourceId, ArrayList<String> steps) {
        super(context, textViewResourceId, steps);
        this.steps = steps;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        String stop = steps.get(position);

        if (stop != null) {
            TextView name = (TextView) convertView.findViewById(R.id.step_name);


            if (name != null) {
                // make data to display hours
                name.setText(stop);
            }

        }
        return convertView;
    }
}
