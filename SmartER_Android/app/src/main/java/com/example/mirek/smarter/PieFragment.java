package com.example.mirek.smarter;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Mirek on 1/05/2018.
 */

public class PieFragment extends Fragment {
    View vPieChart;
    private EditText editText;
    private Button button;
    private PieChart pieChart;
    private Intent intent;
    private String username;
    private String date;
    private String resid;
    String[] xData = {"fridge","air conditioner","washing machine"};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vPieChart = inflater.inflate(R.layout.fragment_pie, container, false);
        pieChart = (PieChart) vPieChart.findViewById(R.id.pie_chart);
        editText = (EditText) vPieChart.findViewById(R.id.et_date);
        button = (Button) vPieChart.findViewById(R.id.bt_pie);
        intent = getActivity().getIntent();
        username = intent.getStringExtra("username");
        editText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
        date = editText.getText().toString();
        button.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        final ArrayList<PieEntry> yEntity = new ArrayList<>();
                        final ArrayList<String> xEntity = new ArrayList<>();
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                return RestClient.findByUsername(username);
                            }
                            @Override
                            protected void onPostExecute(String response) {
                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    final JSONObject res = jsonObject.getJSONObject("resid");
                                    resid = res.getString("resid");
                                    new AsyncTask<String, Void, String>() {
                                        @Override
                                        protected String doInBackground(String... params) {
                                            return RestClient.getDailyusage(resid,date);
                                        }
                                        protected void onPostExecute(String response) {
                                            try {
                                                JSONArray jsonArray = new JSONArray(response);
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                final String[] data = new String[3];
                                                data[0] = jsonObject.getString("fridge");
                                                data[1] = jsonObject.getString("aircon");
                                                data[2] = jsonObject.getString("washingmachine");
                                                for(int i =0;i<3;i++)
                                                {
                                                    yEntity.add(new PieEntry(Float.parseFloat(data[i]),xData[i]));
                                                    xEntity.add(xData[i]);
                                                }
                                                PieDataSet pieDataSet = new PieDataSet(yEntity,"appliance list");
                                                pieDataSet.setSliceSpace(2);
                                                pieDataSet.setValueTextSize(12);
                                                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                                pieChart.setHoleRadius(30f);
                                                pieChart.setTransparentCircleRadius(0);
                                                pieChart.setDrawHoleEnabled(true);
                                                pieChart.setRotationAngle(90);
                                                pieChart.setRotationEnabled(true);
                                                pieChart.setUsePercentValues(true);
                                                pieChart.setCenterText("Daily_Usage");
                                                Legend mLegend = pieChart.getLegend();
                                                mLegend.setXEntrySpace(10f);
                                                mLegend.setYEntrySpace(5f);
                                                pieChart.animateXY(1000,1000);
                                                PieData pieData = new PieData(pieDataSet);
                                                pieChart.setUsePercentValues(true);
                                                pieChart.setData(pieData);

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
                }
        );

        return vPieChart;
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;
                if (monthOfYear >= 10) {
                    if (dayOfMonth >= 10)
                        editText.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    else
                        editText.setText(year + "-" + monthOfYear + "-0" + dayOfMonth);
                }
                else {
                    if (dayOfMonth >= 10)
                        editText.setText(year + "-0" + monthOfYear + "-" + dayOfMonth);
                    else
                        editText.setText(year + "-0" + monthOfYear + "-0" + dayOfMonth);
                }

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH ), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void PieChart(final String id){

    }
}
