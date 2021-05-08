package com.github.wmarkow.distiller.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.domain.calc.CondenserCalc;
import com.github.wmarkow.distiller.domain.calc.InvalidArgumentException;
import com.github.wmarkow.distiller.domain.calc.SeaWaterFlowCalc;
import com.github.wmarkow.distiller.domain.model.DistillerData;
import com.github.wmarkow.distiller.ui.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistillerDataChartView extends RelativeLayout {
    private final static String TAG = "DistillerDataChartView";

    private final static int COLD_WATER_TEMP_DATA_SET_INDEX = 0;
    private final static int HOT_WATER_TEMP_DATA_SET_INDEX = 1;
    private final static int BOILER_TEMP_DATA_SET_INDEX = 2;
    private final static int HEADER_TEMP_DATA_SET_INDEX = 3;
    private final static int WATER_FLOW_DATA_SET_INDEX = 4;

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

    public void addDistillerData(DistillerData distillerData) {
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

        ILineDataSet coldWaterTempDataSet = data.getDataSetByIndex(COLD_WATER_TEMP_DATA_SET_INDEX);
        data.addEntry(new Entry(x, (float)distillerData.coldWaterTemp), COLD_WATER_TEMP_DATA_SET_INDEX);
        ILineDataSet hotWaterTempDataSet = data.getDataSetByIndex(HOT_WATER_TEMP_DATA_SET_INDEX);
        data.addEntry(new Entry(x, (float)distillerData.hotWaterTemp), HOT_WATER_TEMP_DATA_SET_INDEX);
        ILineDataSet boilerTempDataSet = data.getDataSetByIndex(BOILER_TEMP_DATA_SET_INDEX);
        data.addEntry(new Entry(x, (float)distillerData.boilerTemp), BOILER_TEMP_DATA_SET_INDEX);
        ILineDataSet headerTempDataSet = data.getDataSetByIndex(HEADER_TEMP_DATA_SET_INDEX);
        data.addEntry(new Entry(x, (float)distillerData.headerTemp), HEADER_TEMP_DATA_SET_INDEX);

        ILineDataSet waterFlowDataSet = data.getDataSetByIndex(WATER_FLOW_DATA_SET_INDEX);
        try {
            float waterFlowInLPerH = calculateWaterFlow(distillerData.waterRpm);

            data.addEntry(new Entry(x, waterFlowInLPerH), WATER_FLOW_DATA_SET_INDEX);
        } catch (InvalidArgumentException e) {
            data.addEntry(new Entry(x, -1.0f), WATER_FLOW_DATA_SET_INDEX);
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

    private void inflate(Context context) {
        inflate(context, R.layout.distiller_data_chart_view, this);

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
        LineDataSet set =createDefaultDataSet("Cold water");

        return set;
    }

    private LineDataSet createHotWaterDataSet() {
        LineDataSet set = createDefaultDataSet("Hot water");
        set.setColor(Color.rgb(255,255,0));

        return set;
    }

    private LineDataSet createBoilerDataSet() {
        LineDataSet set = createDefaultDataSet("Boiler");
        set.setColor(Color.rgb(255,0,0));

        return set;
    }

    private LineDataSet createHeaderDataSet() {
        LineDataSet set = createDefaultDataSet("Header");
        set.setColor(Color.rgb(255,165,0));

        return set;
    }

    private LineDataSet createWaterFlowDataSet() {
        LineDataSet set = createDefaultDataSet("Water flow");
        set.setColor(Color.rgb(0,0,0));
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);

        return set;
    }

    private float calculateWaterFlow(double waterRpm) throws InvalidArgumentException {
        SeaWaterFlowCalc waterFlowCalc = new SeaWaterFlowCalc();

        double waterFlowInM3PerS = waterFlowCalc.calculateWaterFlow(waterRpm);
        double waterFlowInLPerH = waterFlowInM3PerS * 1000 * 3600;

        return (float)waterFlowInLPerH;
    }
}
