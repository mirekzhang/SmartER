package com.example.mirek.smarter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mirek on 26/04/2018.
 */

public class ReportFragment extends Fragment{
    private View vReport;
    private Spinner selectReport;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);
        selectReport = (Spinner) vReport.findViewById(R.id.select_report);
        String choice = selectReport.getSelectedItem().toString();
        switch (choice) {
            case "Line Chart":
                lineChart();
                break;
            case "Pie Chart":
                //pieChart();
                break;
            case "Bar Chart":
                //barChart();
                break;
        }
        return vReport;
    }

    public void lineChart(){
        LineChart chart =(LineChart)vReport.findViewById(R.id.line_chart);
        List<Entry> entries = new ArrayList<Entry>();
//to display five values, and later formatter is used so years will not have decimal values
        float[] xAxis = {0f,1f,2f,3f,4f};
        float[] yAxis = {100, 200, 150, 320, 470};
        for (int i=0; i<xAxis.length; i++){
            entries.add(new Entry(xAxis[i], yAxis[i]));
        }
//implementing IAxisValueFormatter interface to show year values not as float/decimal
        final String[] years = new String[] { "2014", "2015", "2016", "2017","2018" };
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return years[(int)value];
            }
        };
        LineDataSet dataSet = new LineDataSet(entries, "This is Demo");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        XAxis xAxisFromChart = chart.getXAxis();
        xAxisFromChart.setDrawAxisLine(true);
        xAxisFromChart.setValueFormatter(formatter);
// minimum axis-step (interval) is 1,if no, the same value will be displayed multiple times
        xAxisFromChart.setGranularity(1f);
        xAxisFromChart.setPosition(XAxis.XAxisPosition.BOTTOM);
    }
}
