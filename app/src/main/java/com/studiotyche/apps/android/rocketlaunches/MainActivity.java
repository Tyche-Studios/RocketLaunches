package com.studiotyche.apps.android.rocketlaunches;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Rocket> rockets = null;
    RocketAdapter adapter = null;

    ListView listView = null;
    ProgressBar progressBar = null;
    TextView textView = null;

    RequestQueue queue = null;

    String url = "https://launchlibrary.net/1.1/launch";

    JSONArray results = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        rockets = new ArrayList<Rocket>();
        adapter = new RocketAdapter(this, rockets);

        textView = (TextView) findViewById(R.id.tvStatus);
        progressBar = (ProgressBar) findViewById(R.id.pbFetchingLaunches);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url.toLowerCase(), (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            results = response.getJSONArray("launches");
                            if (results != null) {
                                for (int i = 0; i < results.length(); i++) {

                                    Rocket rocket = new Rocket(results.getJSONObject(i).getString("id"),
                                            results.getJSONObject(i).getString("name"),
                                            results.getJSONObject(i).getString("net"),
                                            results.getJSONObject(i).getString("tbdtime"));
                                    rockets.add(rocket);
                                }
                                adapter.notifyDataSetChanged();
                                textView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText(error.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });

        queue.add(request);
    }
}
