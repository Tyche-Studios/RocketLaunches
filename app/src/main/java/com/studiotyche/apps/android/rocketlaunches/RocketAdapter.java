package com.studiotyche.apps.android.rocketlaunches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by edwn112 on 05-10-2015.
 */


public class RocketAdapter extends ArrayAdapter<Rocket> {

    boolean isLocalTime = false;

    public RocketAdapter(Context context, ArrayList<Rocket> rockets) {
        super(context, 0, rockets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Rocket rockets = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rockets, parent, false);
        }

        TextView rocket = (TextView) convertView.findViewById(R.id.rocket);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        rocket.setText(rockets.getName());

        if (isLocalTime) {
            if (rockets.getNetstamp() == 0)
                date.setText(rockets.getDate());
            else
                date.setText(rockets.getLocalDate());
        } else
            date.setText(rockets.getDate());

        return convertView;
    }

    public void setLocalTime(boolean localTime) {
        this.isLocalTime = localTime;
    }
}
