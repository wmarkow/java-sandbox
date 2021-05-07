package com.github.wmarkow.distiller.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.domain.model.DistillerData;
import com.github.wmarkow.distiller.ui.home.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistillerDataChartView extends RelativeLayout {

    private final static int COLD_WATER_TEMP_DATA_SET_INDEX = 0;
    private final static int HOT_WATER_TEMP_DATA_SET_INDEX = 1;
    private final static int BOILER_TEMP_DATA_SET_INDEX = 2;
    private final static int HEADER_TEMP_DATA_SET_INDEX = 3;

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

        ILineDataSet coldWaterTempDataSet = data.getDataSetByIndex(COLD_WATER_TEMP_DATA_SET_INDEX);
        data.addEntry(new Entry(coldWaterTempDataSet.getEntryCount(), (float)distillerData.coldWaterTemp), COLD_WATER_TEMP_DATA_SET_INDEX);
        ILineDataSet hotWaterTempDataSet = data.getDataSetByIndex(HOT_WATER_TEMP_DATA_SET_INDEX);
        data.addEntry(new Entry(coldWaterTempDataSet.getEntryCount(), (float)distillerData.hotWaterTemp), HOT_WATER_TEMP_DATA_SET_INDEX);
        ILineDataSet boilerTempDataSet = data.getDataSetByIndex(BOILER_TEMP_DATA_SET_INDEX);
        data.addEntry(new Entry(coldWaterTempDataSet.getEntryCount(), (float)distillerData.boilerTemp), BOILER_TEMP_DATA_SET_INDEX);
        ILineDataSet headerTempDataSet = data.getDataSetByIndex(HEADER_TEMP_DATA_SET_INDEX);
        data.addEntry(new Entry(coldWaterTempDataSet.getEntryCount(), (float)distillerData.headerTemp), HEADER_TEMP_DATA_SET_INDEX);

        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        // limit the number of visible entries
        chart.setVisibleXRangeMaximum(30);
        // chart.setVisibleYRange(30, AxisDependency.LEFT);

        // move to the latest entry
        chart.moveViewToX(data.getEntryCount());
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

        XAxis xl = chart.getXAxis();
        xl.setTypeface(tfLight);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

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
    }

    private void addRandomEntry() {
        DistillerData dd = new DistillerData();
        dd.coldWaterTemp = (float) (Math.random() * 2) + 20f;
        dd.hotWaterTemp = (float) (Math.random() * 2) + 70f;
        dd.boilerTemp = (float) (Math.random() * 2) + 91f;
        dd.headerTemp = (float) (Math.random() * 0.5) + 78f;

        addDistillerData(dd);
    }

    private LineDataSet createDefaultDataSet(String label) {
        LineDataSet set = new LineDataSet(null, label);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
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
}
