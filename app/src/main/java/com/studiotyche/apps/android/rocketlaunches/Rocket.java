package com.studiotyche.apps.android.rocketlaunches;

import java.text.SimpleDateFormat;

/**
 * Created by edwn112 on 05-10-2015.
 */

public class Rocket {
    String id = null;
    String name = null;
    String date = null;
    String localDate = null;
    String tbdTime = null;
    String netstamp = null;
    String wsstamp = null;

    public Rocket(String id, String name, String date, String tbdTime, String netstamp, String wsstamp) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.tbdTime = tbdTime;
        this.netstamp = netstamp;
        this.wsstamp = wsstamp;

        this.localDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
                .format(new java.util.Date(Long.parseLong(netstamp + "000")));

    }

    public String getName() {

        return name;
    }
    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {

        return id;
    }

    public String getTbdtime() {

        return tbdTime;
    }

    public String getLocalDate() {

        return localDate;
    }

    public int getNetstamp() {
        return Integer.parseInt(netstamp);
    }

    public String getWsstamp() {
        return wsstamp;
    }
}
