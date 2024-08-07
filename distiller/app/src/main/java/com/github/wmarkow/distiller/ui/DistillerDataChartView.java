package com.github.wmarkow.distiller.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.domain.calc.EthanolSolutionCalc;
import com.github.wmarkow.distiller.domain.calc.LVEWEquilibrium;
import com.github.wmarkow.distiller.domain.calc.LVEWEquilibriumCalc;
import com.github.wmarkow.distiller.domain.calc.OutOfRangeException;
import com.github.wmarkow.distiller.domain.calc.SeaWaterFlowCalc;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistillerDataChartView extends RelativeLayout implements DistillerDataViewIf {
    private final static String TAG = "DistillerDataChartView";

    private final static String COLD_WATER_TEMP_DATA_SET_LABEL = "Cold water";
    private final static String HOT_WATER_TEMP_DATA_SET_LABEL = "Hot water";
    private final static String BOILER_TEMP_DATA_SET_LABEL = "Boiler";
    private final static String HEADER_TEMP_DATA_SET_LABEL = "Header";
    private final static String WATER_FLOW_DATA_SET_LABEL = "Water flow";
    private final static String ALCOHOL_STRENGTH_DATA_SET_LABEL = "%vol";

    @BindView(R.id.chart)
    LineChart chart;

    private LineData data = new LineData();
    protected Typeface tfLight = Typeface.DEFAULT;
    private int xRangeResolutionSeconds = 2;
    private int xRangeVisibleSpanSeconds = 120;
    private boolean dataAvailable = false;
    private boolean followLatestEntry = true;
    /***
     * the chart library has issues when X value is to big (like milliseconds since epoch).
     * The variable below is used as a local startup epoch to offset the long values of epoch seconds.
     */
    private Long startupMidnightSecondsUtc = null;

    public DistillerDataChartView(Context context) {
        super(context);

        inflate(context);
    }

    public DistillerDataChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        inflate(context);
    }

    @Override
    public void showNewDistillerData(List<DistillerDataEntity> distillerData) {
        if(distillerData.size() == 0) {
            return;
        }

        LineData data = chart.getData();

        // Warning: the chart library has issues when x value is to big (like milliseconds since epoch)
        // The chart doesn't look good then.
        // It works ok when a number of seconds since last midnight is used.
        // TODO: improve this because the graph may not work nice when operating at midnight
        if(startupMidnightSecondsUtc == null) {
            long firstDataMillis = distillerData.get(0).utcTimestampMillis;
            Instant instant = Instant.ofEpochMilli(firstDataMillis);

            startupMidnightSecondsUtc = (long)(ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).withHour(0).withMinute(0).withSecond(0).toInstant().toEpochMilli() / 1000.0);
        }
        float todaySecondsLocal = startupMidnightSecondsUtc;

        for(DistillerDataEntity distillerDataEntity : distillerData) {

            float secondsSinceMidnightUtc = (long)(distillerDataEntity.utcTimestampMillis / 1000.0) - startupMidnightSecondsUtc;
            if(secondsSinceMidnightUtc < 0) {
                continue;
            }
            ZoneOffset localZoneOffset = ZonedDateTime.now().getOffset();
            todaySecondsLocal = secondsSinceMidnightUtc + localZoneOffset.getTotalSeconds();

            if(distillerDataEntity.coldWaterTemp != null) {
                ILineDataSet coldWaterTempDataSet = data.getDataSetByLabel(COLD_WATER_TEMP_DATA_SET_LABEL, false);
                coldWaterTempDataSet.addEntry(new Entry(todaySecondsLocal, distillerDataEntity.coldWaterTemp.floatValue(), distillerDataEntity));
            }
            if(distillerDataEntity.hotWaterTemp != null) {
                ILineDataSet hotWaterTempDataSet = data.getDataSetByLabel(HOT_WATER_TEMP_DATA_SET_LABEL, false);
                hotWaterTempDataSet.addEntry(new Entry(todaySecondsLocal, distillerDataEntity.hotWaterTemp.floatValue(), distillerDataEntity));
            }
            if(distillerDataEntity.boilerTemp != null) {
                ILineDataSet boilerTempDataSet = data.getDataSetByLabel(BOILER_TEMP_DATA_SET_LABEL, false);
                if (boilerTempDataSet != null) {
                    boilerTempDataSet.addEntry(new Entry(todaySecondsLocal, distillerDataEntity.boilerTemp.floatValue(), distillerDataEntity));
                }
            }
            if(distillerDataEntity.headerTemp != null) {
                ILineDataSet headerTempDataSet = data.getDataSetByLabel(HEADER_TEMP_DATA_SET_LABEL, false);
                if (headerTempDataSet != null) {
                    headerTempDataSet.addEntry(new Entry(todaySecondsLocal, distillerDataEntity.headerTemp.floatValue(), distillerDataEntity));
                }
            }

            ILineDataSet waterFlowDataSet = data.getDataSetByLabel(WATER_FLOW_DATA_SET_LABEL, false);
            try {
                float waterFlowInLPerH = calculateWaterFlow(distillerDataEntity.waterRpm);

                waterFlowDataSet.addEntry(new Entry(todaySecondsLocal, waterFlowInLPerH, distillerDataEntity));
            } catch (OutOfRangeException e) {
                waterFlowDataSet.addEntry(new Entry(todaySecondsLocal, -1.0f, distillerDataEntity));
            }

            ILineDataSet alcoholStrengthDataSet = data.getDataSetByLabel(ALCOHOL_STRENGTH_DATA_SET_LABEL, false);
            try {
                // calculate condensate strength
                if(distillerDataEntity.headerTemp != null)
                {
                    // TODO: add extended model min temp support
                    LVEWEquilibriumCalc ec = new LVEWEquilibriumCalc();
                    LVEWEquilibrium equilibrium = ec.calculateEquilibrium(distillerDataEntity.headerTemp);
                    EthanolSolutionCalc esc = new EthanolSolutionCalc();
                    double volConcentration = esc.calculateVolumeConcentration(equilibrium.ethanolVaporMoleFraction, distillerDataEntity.headerTemp);

                    alcoholStrengthDataSet.addEntry(new Entry(todaySecondsLocal, (float) volConcentration, distillerDataEntity));
                }
            } catch (OutOfRangeException e) {
                // silently fail
            }
        }

        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        // limit the number of visible entries
        chart.setVisibleXRange(0, xRangeVisibleSpanSeconds / xRangeResolutionSeconds);
        dataAvailable = true;

        if(followLatestEntry) {
            chart.moveViewToX(todaySecondsLocal);
        }
    }

    @Override
    public void setXRangeResolutionSeconds(int xRangeResolutionSeconds) {
        this.xRangeResolutionSeconds = xRangeResolutionSeconds;
    }

    @Override
    public void setXRangeVisibleSpanSeconds(int xRangeVisibleSpanSeconds) {
        this.xRangeVisibleSpanSeconds = xRangeVisibleSpanSeconds;

        if(dataAvailable) {
            chart.setVisibleXRange(0, xRangeVisibleSpanSeconds / xRangeResolutionSeconds);
            chart.invalidate();
        }
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

    public void setFollowLatestEntry(boolean followLatestEntry) {
        this.followLatestEntry = followLatestEntry;
    }

    public void setOnChartValueSelectedListener(DistillerDataChartViewListener listener) {
        if(listener == null) {
            chart.setOnChartValueSelectedListener(null);

            return;
        }

        chart.setOnChartValueSelectedListener(new DistillerDataChartViewListenerDecorator(listener));
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
        xAxis.setLabelCount(10, true);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(true);
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
                return String.format("%.2f", value);
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

        ILineDataSet alcoholStrengthDataSet = createAlcoholStrengthDataSet();
        data.addDataSet(alcoholStrengthDataSet);
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

    private LineDataSet createAlcoholStrengthDataSet() {
        LineDataSet set = createDefaultDataSet(ALCOHOL_STRENGTH_DATA_SET_LABEL);
        set.setColor(Color.rgb(0,0,0));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return set;
    }

    private float calculateWaterFlow(double waterRpm) throws OutOfRangeException {
        SeaWaterFlowCalc waterFlowCalc = new SeaWaterFlowCalc();

        double waterFlowInM3PerS = waterFlowCalc.calculateWaterFlow(waterRpm);
        double waterFlowInLPerH = waterFlowInM3PerS * 1000 * 3600;

        return (float)waterFlowInLPerH;
    }

    private class DistillerDataChartViewListenerDecorator implements OnChartValueSelectedListener {

        private DistillerDataChartViewListener decorated;

        DistillerDataChartViewListenerDecorator(DistillerDataChartViewListener decorated) {
            this.decorated = decorated;
        }

        @Override
        public void onValueSelected(Entry e, Highlight h) {
            decorated.onValueSelected((DistillerDataEntity)e.getData());
        }

        @Override
        public void onNothingSelected() {
            // do nothing
        }
    }
}
