package com.github.wmarkow.distiller.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.domain.calc.OutOfRangeException;
import com.github.wmarkow.distiller.domain.calc.SeaWaterFlowCalc;
import com.github.wmarkow.distiller.domain.model.DistillerData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistillerDataChartView extends RelativeLayout implements DistillerDataViewIf {
    private final static String TAG = "DistillerDataChartView";

    private final static String COLD_WATER_TEMP_DATA_SET_LABEL = "Cold water";
    private final static String HOT_WATER_TEMP_DATA_SET_LABEL = "Hot water";
    private final static String BOILER_TEMP_DATA_SET_LABEL = "Boiler";
    private final static String HEADER_TEMP_DATA_SET_LABEL = "Header";
    private final static String WATER_FLOW_DATA_SET_LABEL = "Water flow";

    @BindView(R.id.chart)
    LineChart chart;

    private LineData data = new LineData();
    protected Typeface tfLight = Typeface.DEFAULT;

    public DistillerDataChartView(Context context) {
        super(context);

        inflate(context);
    }

    public DistillerDataChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        inflate(context);
    }

    @Override
    public void showDistillerData(DistillerData distillerData) {
        LineData data = chart.getData();

        // Warning: the chart library has issues when x value is to big (like ine milliseconds since epoch)
        // The chart doesn't look good then.
        // It works ok when a number of seconds since last midnight is used.
        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int millis = calendar.get(Calendar.MILLISECOND);
        float x = seconds + 60 * minutes +  3600 * hour24hrs;

        ILineDataSet coldWaterTempDataSet = data.getDataSetByLabel(COLD_WATER_TEMP_DATA_SET_LABEL, false);
        coldWaterTempDataSet.addEntry(new Entry(x, (float)distillerData.coldWaterTemp));
        ILineDataSet hotWaterTempDataSet = data.getDataSetByLabel(HOT_WATER_TEMP_DATA_SET_LABEL, false);
        hotWaterTempDataSet.addEntry(new Entry(x, (float)distillerData.hotWaterTemp));
        ILineDataSet boilerTempDataSet = data.getDataSetByLabel(BOILER_TEMP_DATA_SET_LABEL, false);
        if(boilerTempDataSet != null) {
            boilerTempDataSet.addEntry(new Entry(x, (float) distillerData.boilerTemp));
        }
        ILineDataSet headerTempDataSet = data.getDataSetByLabel(HEADER_TEMP_DATA_SET_LABEL, false);
        if(headerTempDataSet != null) {
            headerTempDataSet.addEntry(new Entry(x, (float) distillerData.headerTemp));
        }

        ILineDataSet waterFlowDataSet = data.getDataSetByLabel(WATER_FLOW_DATA_SET_LABEL, false);
        try {
            float waterFlowInLPerH = calculateWaterFlow(distillerData.waterRpm);

            waterFlowDataSet.addEntry(new Entry(x, waterFlowInLPerH));
        } catch (OutOfRangeException e) {
            waterFlowDataSet.addEntry(new Entry(x, -1.0f));
        }


        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        // limit the number of visible entries
        chart.setVisibleXRangeMaximum(30);
        // chart.setVisibleYRange(30, AxisDependency.LEFT);

        // move to the latest entry
        chart.moveViewToX(x);
    }

    public void removeBoilerTemp() {
        LineData data = chart.getData();
        ILineDataSet dataSet = data.getDataSetByLabel(BOILER_TEMP_DATA_SET_LABEL, false);
        data.removeDataSet(dataSet);
    }

    public void removeHeaderTemp() {
        LineData data = chart.getData();
        ILineDataSet dataSet = data.getDataSetByLabel(HEADER_TEMP_DATA_SET_LABEL, false);
        data.removeDataSet(dataSet);
    }

    private void inflate(Context context) {
        inflate(context, R.layout.view_distiller_data_chart, this);

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        // add empty data
        chart.setData(data);
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tfLight);
        l.setTextColor(Color.BLACK);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTypeface(tfLight);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setLabelRotationAngle(-25);
        xAxis.setEnabled(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float secondsSinceMidnight) {

                int hour24hrs = (int)(secondsSinceMidnight / 3600.0f);
                int minutes = (int)((secondsSinceMidnight - 3600 * hour24hrs) / 60);
                int seconds = (int)(secondsSinceMidnight - 3600 * hour24hrs - 60 * minutes);

                return String.format("%02d:%02d:%02d", hour24hrs, minutes, seconds);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(110f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setLabelCount(12, true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value == 110f)
                {
                    return "\u00B0" + "C";
                }
                return String.format("%.0f", value);
            }
        });

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(true);
        rightAxis.setTypeface(tfLight);
        rightAxis.setTextColor(Color.BLACK);
        rightAxis.setAxisMaximum(110f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setLabelCount(12, true);
        rightAxis.setDrawGridLines(true);
        rightAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value == 110f)
                {
                    return "l/h";
                }
                return String.format("%.0f", value);
            }
        });

        // create data sets
        LineData data = chart.getData();
        ILineDataSet coldWaterTempDataSet = createColdWaterDataSet();
        data.addDataSet(coldWaterTempDataSet);
        ILineDataSet hotWaterTempDataSet = createHotWaterDataSet();
        data.addDataSet(hotWaterTempDataSet);

        ILineDataSet kegTempDataSet = createBoilerDataSet();
        data.addDataSet(kegTempDataSet);

        ILineDataSet headerTempDataSet = createHeaderDataSet();
        data.addDataSet(headerTempDataSet);

        ILineDataSet waterFlowDataSet = createWaterFlowDataSet();
        data.addDataSet(waterFlowDataSet);
    }

    private LineDataSet createDefaultDataSet(String label) {
        LineDataSet set = new LineDataSet(null, label);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(2f);
        set.setCircleRadius(3f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private LineDataSet createColdWaterDataSet() {
        LineDataSet set =createDefaultDataSet(COLD_WATER_TEMP_DATA_SET_LABEL);

        return set;
    }

    private LineDataSet createHotWaterDataSet() {
        LineDataSet set = createDefaultDataSet(HOT_WATER_TEMP_DATA_SET_LABEL);
        set.setColor(Color.rgb(255,255,0));

        return set;
    }

    private LineDataSet createBoilerDataSet() {
        LineDataSet set = createDefaultDataSet(BOILER_TEMP_DATA_SET_LABEL);
        set.setColor(Color.rgb(255,0,0));

        return set;
    }

    private LineDataSet createHeaderDataSet() {
        LineDataSet set = createDefaultDataSet(HEADER_TEMP_DATA_SET_LABEL);
        set.setColor(Color.rgb(255,165,0));

        return set;
    }

    private LineDataSet createWaterFlowDataSet() {
        LineDataSet set = createDefaultDataSet(WATER_FLOW_DATA_SET_LABEL);
        set.setColor(Color.rgb(0,0,0));
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);

        return set;
    }

    private float calculateWaterFlow(double waterRpm) throws OutOfRangeException {
        SeaWaterFlowCalc waterFlowCalc = new SeaWaterFlowCalc();

        double waterFlowInM3PerS = waterFlowCalc.calculateWaterFlow(waterRpm);
        double waterFlowInLPerH = waterFlowInM3PerS * 1000 * 3600;

        return (float)waterFlowInLPerH;
    }
}
