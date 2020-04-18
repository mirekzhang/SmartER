package com.example.mirek.smarter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mirek on 1/05/2018.
 */

public class LineFragment extends Fragment {
    View vLineChart;
    private Spinner spinner;
    private LineChart lineChart;
    private Intent intent;
    private String username;
    private String resid;
    private List<ILineDataSet> lineDataSets = new ArrayList<>();
    String thedDate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vLineChart = inflater.inflate(R.layout.fragment_line, container, false);
        lineChart = (LineChart)vLineChart.findViewById(R.id.line_chart);
        spinner = (Spinner)vLineChart.findViewById(R.id.sp_line);
        intent = getActivity().getIntent();
        username = intent.getStringExtra("username");
        spinner.setOnItemSelectedListener(new OnItemSelectedListener());
        return vLineChart;
    }

    private class OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String usageType = adapterView.getItemAtPosition(i).toString();
            switch (usageType){
                case "Hourly":
                    lineChartHourly();
                    break;
                case "Daily":
                    lineChartDaily();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public void lineChartHourly(){
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
                    final JSONObject res = jsonObject.getJSONObject("resid");
                    resid = res.getString("resid");
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
                                thedDate = jsonObject1.getString("date").substring(0, 10);
                                new AsyncTask<Void, Void, String>() {
                                    @Override
                                    protected String doInBackground(Void... params) {
                                        return RestClient.getHourlyOrDailyUsage(resid, thedDate, "hourly");
                                    }
                                    @Override
                                    protected void onPostExecute(String response) {
                                        String[] allData = new String[24];
                                        JSONArray jsonArray2 = null;
                                        try {
                                            jsonArray2 = new JSONArray(response);
                                            for (int i = 0; i <jsonArray2.length(); i++){
                                                JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                                                String iUsage = jsonObject2.getString("totalusage");
                                                int hour = Integer.valueOf(jsonObject2.getString("hours"));
                                                String temp = jsonObject2.getString("temperature");
                                                String hourlyData = iUsage + "," + temp + "," + String.valueOf(hour);
                                                allData[hour] = hourlyData;
                                            }
                                            setData(allData,"hourly");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.execute();
                            } catch (JSONException e) {
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

    /**
     * get the usage data
     */
    public void lineChartDaily(){
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
                    final JSONObject res = jsonObject.getJSONObject("resid");
                    resid = res.getString("resid");
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
                                thedDate = jsonObject1.getString("date").substring(0, 10);
                                final String month = thedDate.substring(0, 7);
                                    new AsyncTask<Void, Void, String[]>() {
                                        @Override
                                        protected String[] doInBackground(Void... params) {
                                            String[] dailyData = new String[31];
                                            for (int i=1; i<31; i++){
                                                if (i < 10)
                                                    thedDate = month+"-0"+String.valueOf(i);
                                                else
                                                    thedDate = month+"-"+String.valueOf(i);
                                                try {
                                                    JSONArray jsonArray2 = new JSONArray(RestClient.getHourlyOrDailyUsage(resid, thedDate, "daily"));
                                                    if (jsonArray2.length() != 0){
                                                        JSONObject jsonObject2 = jsonArray2.getJSONObject(0);
                                                        String dUsage = jsonObject2.getString("totalusage");
                                                        String temp = jsonObject2.getString("temperature");
                                                        dailyData[i-1] = dUsage + "," + temp + "," + thedDate.substring(8, 10);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            return dailyData;
                                        }
                                        protected void onPostExecute(String[] response) {
                                                setData(response,"daily");
                                        }
                                    }.execute();
                            } catch (JSONException e) {
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

    public void setData(String[] data, String option) {
        List<Entry> usage_values = new ArrayList<Entry>();
        List<Entry> temp_values = new ArrayList<Entry>();
        lineDataSets.clear();
        String[] index = new String[data.length];
        int j =0;
        for(int i=0; i<data.length; i++)
        {
            String[] real_data = data[i].split(",");
            if(!real_data[0].equalsIgnoreCase("0.0"))
            {
                index[j] = String.valueOf(Integer.valueOf(real_data[2]));
                float y = Float.parseFloat(real_data[0]);
                float tem_y = Float.parseFloat(real_data[1]);
                usage_values.add(new Entry(Float.valueOf(index[j]),y));
                temp_values.add(new Entry(Float.valueOf(index[j]),tem_y));
                j++;
            }
        }
        LineDataSet dataSet = new LineDataSet(usage_values,thedDate+" Usage (kWh)");
        LineDataSet dataSet1 = new LineDataSet(temp_values,thedDate+" Temperature (℃)");
        if(option.equalsIgnoreCase("daily"))
        {
            dataSet.setLabel(thedDate.substring(0,7)+" Usage (kWh)");
            dataSet1.setLabel(thedDate.substring(0,7)+"  Temperature (℃)");
        }
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet1.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet.setColor(Color.BLUE);
        dataSet1.setColor(Color.BLACK);
        lineDataSets.add(dataSet);
        lineDataSets.add(dataSet1);
        LineData lineData = new LineData(lineDataSets);
        lineChart.setData(lineData);
        lineChart.animateY(1000);
        XAxis xAxisFromChart = lineChart.getXAxis();
        xAxisFromChart.setDrawAxisLine(true);
        xAxisFromChart.setGranularity(1f);
        xAxisFromChart.setPosition(XAxis.XAxisPosition.BOTTOM);
    }
}
