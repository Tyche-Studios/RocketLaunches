package com.studiotyche.apps.android.rocketlaunches;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by edwn112 on 06-10-2015.
 */

public class SettingsActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SettingsFragment extends PreferenceFragment {

        boolean localTime = false;
        boolean notifications = false;

        SharedPreference preference = null;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings);

            preference = new SharedPreference();

            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());

            prefs.registerOnSharedPreferenceChangeListener(
                    new SharedPreferences.OnSharedPreferenceChangeListener() {
                        @Override
                        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                            if (key.equals("localTime")) {
                                localTime = sharedPreferences.getBoolean(key, false);

                                if (localTime) {
                                    MainActivity.setLocalTime();
                                } else {
                                    MainActivity.setGlobalTime();
                                }

                            }

                            if (key.equals("notifications")) {
                                notifications = sharedPreferences.getBoolean(key, false);

                                if (notifications) {
                                    MainActivity.setAlarm(preference.getRockets(MainActivity.context)
                                            .get(0).getWsstamp());
                                } else
                                    MainActivity.unsetAlarm();
                            }
                        }
                    }
            );
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

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}