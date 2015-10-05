package com.studiotyche.apps.android.rocketlaunches;

/**
 * Created by edwn112 on 05-10-2015.
 */

/**
 * {"id":259,"name":"Long March 2D | Jilin-1","net":"October 7, 2015 04:15:00 UTC","tbdtime":0}
 */
public class Rocket {
    String id = null;
    String name = null;
    String date = null;
    String tbdTime = null;

    public Rocket(String id, String name, String date, String tbdTime) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.tbdTime = tbdTime;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getTbdtime() {
        return tbdTime;
    }

}
