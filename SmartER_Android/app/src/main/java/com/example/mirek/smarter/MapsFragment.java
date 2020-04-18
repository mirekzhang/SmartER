package com.example.mirek.smarter;

import android.app.Fragment;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapquest.mapping.MapQuestAccountManager;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


/**
 * Created by Mirek on 26/04/2018.
 */

public class MapsFragment extends Fragment {
    private MapboxMap mMapboxMap;
    private MapView mMapView;
    private Spinner spinner;
    private String usageType;
    private View vMaps;
    private Intent intent;
    private String username;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMaps = inflater.inflate(R.layout.fragment_maps, container, false);
        MapboxAccountManager.start(getActivity().getApplicationContext());
        mMapView = (MapView) vMaps.findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);
        spinner = (Spinner)vMaps.findViewById(R.id.sp_maps);
        usageType = spinner.getSelectedItem().toString();
        intent = getActivity().getIntent();
        username = intent.getStringExtra("username");
        spinner.setOnItemSelectedListener(new OnItemSelectedListener());
        return vMaps;
    }

    private void myPlace() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return RestClient.findByUsername(username);
            }
            @Override
            protected void onPostExecute(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    JSONObject res = jsonObject.getJSONObject("resid");
                    String address = res.getString("address");
                    Geocoder mGeocoder = new Geocoder(getActivity());
                    List<Address> location = mGeocoder.getFromLocationName(address, 1);
                    final Double latitude = location.get(0).getLatitude();
                    final Double longitude = location.get(0).getLongitude();
                    final LatLng myPlace = new LatLng(latitude, longitude);
                    mMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(MapboxMap mapboxMap) {
                            mMapboxMap = mapboxMap;
                            mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace,11));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(myPlace);
                            markerOptions.title("My house");
                            mapboxMap.addMarker(markerOptions);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void allPlacesHourly() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return RestClient.findAllResident();
            }
            @Override
            protected void onPostExecute(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String address = jsonObject.getString("address");
                        final String resid = jsonObject.getString("resid");
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                return RestClient.findUsageByResid(resid);
                            }
                            @Override
                            protected void onPostExecute(String response) {
                                try {
                                    JSONArray jsonArray1 = new JSONArray(response);
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(jsonArray1.length()-1);
                                    final String lastDate = jsonObject1.getString("date").substring(0, 10);
                                    new AsyncTask<Void, Void, String>() {
                                        @Override
                                        protected String doInBackground(Void... params) {
                                            return RestClient.getHourlyOrDailyUsage(resid, lastDate, "hourly");
                                        }
                                        @Override
                                        protected void onPostExecute(String response) {
                                            try {
                                                JSONArray jsonArray2 = new JSONArray(response);
                                                JSONObject jsonObject2 = jsonArray2.getJSONObject(jsonArray2.length()-1);
                                                final String hourlyUsage = jsonObject2.getString("totalusage");
                                                Geocoder mGeocoder = new Geocoder(getActivity());
                                                List<Address> location = mGeocoder.getFromLocationName(address, 1);
                                                final Double latitude = location.get(0).getLatitude();
                                                final Double longitude = location.get(0).getLongitude();
                                                final LatLng eachPlace = new LatLng(latitude, longitude);
                                                mMapView.getMapAsync(new OnMapReadyCallback() {
                                                    @Override
                                                    public void onMapReady(MapboxMap mapboxMap) {
                                                        mMapboxMap = mapboxMap;
                                                        MarkerOptions markerOptions = new MarkerOptions();
                                                        markerOptions.position(eachPlace);
                                                        markerOptions.snippet("Hourly usage: " + hourlyUsage + "kWh.");
                                                        mapboxMap.addMarker(markerOptions);
                                                        if (Double.valueOf(hourlyUsage) < 1.5)
                                                        {
                                                            IconFactory iconFactory = IconFactory.getInstance(getActivity());
                                                            Icon icon = iconFactory.fromResource(R.drawable.green_marker);
                                                            mapboxMap.addMarker(markerOptions).setIcon(icon);
                                                        }
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.execute();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void allPlacesDaily() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return RestClient.findAllResident();
            }
            @Override
            protected void onPostExecute(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String address = jsonObject.getString("address");
                        final String resid = jsonObject.getString("resid");
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                return RestClient.findUsageByResid(resid);
                            }
                            @Override
                            protected void onPostExecute(String response) {
                                try {
                                    JSONArray jsonArray1 = new JSONArray(response);
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(jsonArray1.length()-1);
                                    final String lastDate = jsonObject1.getString("date").substring(0, 10);
                                    new AsyncTask<Void, Void, String>() {
                                        @Override
                                        protected String doInBackground(Void... params) {
                                            return RestClient.getHourlyOrDailyUsage(resid, lastDate, "daily");
                                        }
                                        @Override
                                        protected void onPostExecute(String response) {
                                            try {
                                                JSONArray jsonArray2 = new JSONArray(response);
                                                JSONObject jsonObject2 = jsonArray2.getJSONObject(jsonArray2.length()-1);
                                                final String dailyUsage = jsonObject2.getString("totalusage");
                                                Geocoder mGeocoder = new Geocoder(getActivity());
                                                List<Address> location = mGeocoder.getFromLocationName(address, 1);
                                                final Double latitude = location.get(0).getLatitude();
                                                final Double longitude = location.get(0).getLongitude();
                                                final LatLng eachPlace = new LatLng(latitude, longitude);
                                                mMapView.getMapAsync(new OnMapReadyCallback() {
                                                    @Override
                                                    public void onMapReady(MapboxMap mapboxMap) {
                                                        mMapboxMap = mapboxMap;
                                                        MarkerOptions markerOptions = new MarkerOptions();
                                                        markerOptions.position(eachPlace);
                                                        markerOptions.snippet("Daily usage: " + dailyUsage + "kWh.");
                                                        mapboxMap.addMarker(markerOptions);
                                                        if (Double.valueOf(dailyUsage) < 21)
                                                        {
                                                            IconFactory iconFactory = IconFactory.getInstance(getActivity());
                                                            Icon icon = iconFactory.fromResource(R.drawable.green_marker);
                                                            mapboxMap.addMarker(markerOptions).setIcon(icon);
                                                        }
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.execute();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @Override
    public void onResume()
    { super.onResume(); mMapView.onResume(); }

    @Override
    public void onPause()
    { super.onPause(); mMapView.onPause(); }

    @Override
    public void onDestroy()
    { super.onDestroy(); mMapView.onDestroy(); }

    @Override
    public void onSaveInstanceState(Bundle outState)
    { super.onSaveInstanceState(outState); mMapView.onSaveInstanceState(outState); }

    private class OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            usageType = adapterView.getItemAtPosition(i).toString();
            switch (usageType){
                case "Hourly":
                    myPlace();
                    allPlacesHourly();
                    break;
                case "Daily":
                    myPlace();
                    allPlacesDaily();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }
}