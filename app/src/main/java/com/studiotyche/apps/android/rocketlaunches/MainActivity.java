package com.studiotyche.apps.android.rocketlaunches;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String PREFSNAME = "FIRST_BOOT";

    SharedPreferences.Editor editor = null;
    SharedPreferences prefs = null;

    static ArrayList<Rocket> rocketsLocal = null, rocketsGlobal = null;
    static RocketAdapter adapterLocal = null, adapterGlobal = null;

    static ListView listView = null;

    ProgressBar progressBar = null;

    TextView textView = null;

    static RequestQueue queue = null;

    String url = "https://launchlibrary.net/1.1/launch/next/10";

    private static ScheduleClient scheduleClient;

    static Calendar cal = null;

    static SharedPreference sharedPreference = null;

    static Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        prefs = getSharedPreferences(PREFSNAME, MODE_PRIVATE);

        sharedPreference = new SharedPreference();

        cal = Calendar.getInstance();

        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        queue = Volley.newRequestQueue(this);

        rocketsLocal = new ArrayList<>();
        adapterLocal = new RocketAdapter(this, rocketsLocal);

        rocketsGlobal = new ArrayList<>();
        adapterGlobal = new RocketAdapter(this, rocketsGlobal);

        adapterLocal.setLocalTime(true);
        adapterGlobal.setLocalTime(false);

        textView = (TextView) findViewById(R.id.tvStatus);
        progressBar = (ProgressBar) findViewById(R.id.pbFetchingLaunches);

        listView = (ListView) findViewById(R.id.listView);

        if (prefs.getBoolean("ISTHISAFIRSTBOOT", true)) {
            editor = getSharedPreferences(PREFSNAME, MODE_PRIVATE).edit();
            editor.putBoolean("ISTHISAFIRSTBOOT", false);
            editor.commit();

            listView.setAdapter(adapterGlobal);

            makeRequest();

        } else {
            textView.setVisibility(View.GONE);

            SharedPreferences pref =
                    PreferenceManager.getDefaultSharedPreferences(context);

            if (pref.getBoolean("localTime", false)) {
                setLocalTime();
            } else {
                setGlobalTime();
            }

        }

    }

    public static void setLocalTime() {

        ArrayList<Rocket> rockets = sharedPreference.getRockets(context);

        rocketsLocal.clear();
        adapterLocal.clear();
        rocketsLocal.addAll(rockets);
        adapterLocal.notifyDataSetChanged();
        listView.setAdapter(adapterLocal);
    }

    public static void setGlobalTime() {
        ArrayList<Rocket> rockets = sharedPreference.getRockets(context);

        rocketsGlobal.clear();
        adapterGlobal.clear();
        rocketsGlobal.addAll(rockets);
        adapterGlobal.notifyDataSetChanged();
        listView.setAdapter(adapterGlobal);
    }

    protected void onStop() {
        if (scheduleClient != null) {
            scheduleClient.doUnbindService();
            super.onStop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {

            textView.setVisibility(View.GONE);

            makeRequest();
        }


        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeRequest() {
        progressBar.setVisibility(View.VISIBLE);

        rocketsGlobal.clear();
        rocketsLocal.clear();

        adapterGlobal.clear();
        adapterLocal.clear();

        sharedPreference.removeRockets(context);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url.toLowerCase(), (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("launches");
                            if (results != null) {
                                for (int i = 0; i < results.length(); i++) {

                                    Rocket rocket = new Rocket(results.getJSONObject(i).getString("id"),
                                            results.getJSONObject(i).getString("name"),
                                            results.getJSONObject(i).getString("net"),
                                            results.getJSONObject(i).getString("tbdtime"),
                                            results.getJSONObject(i).getString("netstamp"),
                                            results.getJSONObject(i).getString("wsstamp"));

                                    rocketsLocal.add(rocket);
                                    rocketsGlobal.add(rocket);
                                    sharedPreference.addRocket(context, rocket);
                                }
                                adapterLocal.notifyDataSetChanged();
                                adapterGlobal.notifyDataSetChanged();

                                textView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);


                                SharedPreferences prefs =
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                if (prefs.getBoolean("notifications", false)) {
                                    unsetAlarm();
                                    setAlarm(sharedPreference.getRockets(MainActivity.context)
                                            .get(0).getWsstamp());
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Poor or No Network Connection");
                        progressBar.setVisibility(View.GONE);
                    }
                });

        queue.add(request);
    }

    public static void setAlarm(String time) {

        cal.setTimeInMillis(Long.parseLong(time + "000") - (15 * 60 * 1000));

        if (scheduleClient != null)
            scheduleClient.setAlarmForNotification(cal, true);

    }

    public static void unsetAlarm() {

        if (scheduleClient != null)
            scheduleClient.setAlarmForNotification(cal, false);
    }

}
