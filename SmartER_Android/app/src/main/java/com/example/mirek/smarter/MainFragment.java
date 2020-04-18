package com.example.mirek.smarter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;


import android.database.Cursor;
import android.database.SQLException;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.format.DateFormat;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import static com.example.mirek.smarter.RestClient.findByUsername;

/**
 * Created by Mirek on 30/04/2018.
 */

public class MainFragment extends Fragment {
    View vMain;
    TextView currentDatetime, currentTemperature, welcome, feedback;
    ImageView image;
    Intent intent;
    String username;
    int time;
    int usageId;
    protected DBManager dbManager;
    SimpleDateFormat sdf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        intent = getActivity().getIntent();
        username = intent.getStringExtra("username");
        currentDatetime = (TextView)vMain.findViewById(R.id.current_datetime);
        currentTemperature = (TextView)vMain.findViewById(R.id.current_temperature);
        welcome = (TextView)vMain.findViewById(R.id.tv_welcome);
        feedback = (TextView)vMain.findViewById(R.id.tv_feedback);
        image = (ImageView)vMain.findViewById(R.id.image);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        new TimeThread().start();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return findByUsername(username);
            }
            @Override
            protected void onPostExecute(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    JSONObject res = jsonObject.getJSONObject("resid");
                    final String resid = res.getString("resid");
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            return RestClient.findUsageByResid(resid);
                        }
                        @Override
                        protected void onPostExecute(String response) {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-1);
                                Double usage = jsonObject.getDouble("fridgeusage") + jsonObject.getDouble("acusage") + jsonObject.getDouble("wmusage");
                                if (usage < 1.5){
                                    image.setImageResource(R.drawable.positive);
                                    feedback.setText("Well done! You saved a lot!");
                                } else {
                                    image.setImageResource(R.drawable.negative);
                                    feedback.setText("You used too much! Please save!");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute();

                    String fname = res.getString("fname");
                    welcome.setText("Welcome, " + fname + "!");
                    String address = res.getString("address");
                    Geocoder mGeocoder = new Geocoder(getActivity());
                    List<Address> location = mGeocoder.getFromLocationName(address, 1);
                    if (location.size() != 0) {
                        Double latitude = location.get(0).getLatitude();
                        Double longtitude = location.get(0).getLongitude();
                        final String suburb = location.get(0).getLocality();
                        Weather.placeIdTask asyncTask = new Weather.placeIdTask(new Weather.AsyncResponse() {
                            public void processFinish(String weather_city, String weather_temperature) {

                                currentTemperature.setText(suburb + "  " + weather_temperature + "C");

                            }
                        });
                        asyncTask.execute(latitude.toString(), longtitude.toString());
                    } else {
                        Weather.placeIdTask asyncTask = new Weather.placeIdTask(new Weather.AsyncResponse() {
                            public void processFinish(String weather_city, String weather_temperature) {

                                currentTemperature.setText("Melbourne " + weather_temperature + "C");

                            }
                        });
                        asyncTask.execute("-37.8136276", "144.9630576");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
        return vMain;
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd hh:mm:ss", sysTime);
                    currentDatetime.setText(sysTimeStr);
                    break;
                default:
                    break;
            }
        }
    };
}