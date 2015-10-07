package com.studiotyche.apps.android.rocketlaunches;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by edwn112 on 07-10-2015.
 */
public class SharedPreference {

    public static final String PREFS_NAME = "ROCKET";
    public static final String PREFS_LIST = "ROCKET_LIST";

    SharedPreferences settings = null;
    SharedPreferences.Editor editor = null;

    public SharedPreference() {
        super();
    }

    public void saveRockets(Context context, List<Rocket> rockets) {

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonList = gson.toJson(rockets);
        editor.putString(PREFS_LIST, jsonList);
        editor.commit();
    }

    public void addRocket(Context context, Rocket rocket) {
        List<Rocket> rockets = getRockets(context);

        if (rockets == null)
            rockets = new ArrayList<Rocket>();
        rockets.add(rocket);

        saveRockets(context, rockets);
    }

    public void removeRocket(Context context, Rocket rocket) {
        ArrayList<Rocket> rockets = getRockets(context);

        if (rockets != null) {
            rockets.remove(rocket);
            saveRockets(context, rockets);
        }
    }

    public void removeRockets(Context context) {
        ArrayList<Rocket> rockets = new ArrayList<Rocket>();

        saveRockets(context, rockets);
    }

    public ArrayList<Rocket> getRockets(Context context) {
        List<Rocket> rockets;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(PREFS_LIST)) {
            String jsonList = settings.getString(PREFS_LIST, null);

            Gson gson = new Gson();
            Rocket[] rocketsList = gson.fromJson(jsonList,
                    Rocket[].class);

            rockets = Arrays.asList(rocketsList);
            rockets = new ArrayList<Rocket>(rockets);
        } else
            return null;

        return (ArrayList<Rocket>) rockets;
    }
}
