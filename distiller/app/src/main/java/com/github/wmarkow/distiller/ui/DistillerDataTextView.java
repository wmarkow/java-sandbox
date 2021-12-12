package com.github.wmarkow.distiller.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.domain.calc.CondensationSpeed;
import com.github.wmarkow.distiller.domain.calc.CondenserCalc;
import com.github.wmarkow.distiller.domain.calc.EthanolSolutionCalc;
import com.github.wmarkow.distiller.domain.calc.LVEWEquilibrium;
import com.github.wmarkow.distiller.domain.calc.LVEWEquilibriumCalc;
import com.github.wmarkow.distiller.domain.calc.OutOfRangeException;
import com.github.wmarkow.distiller.domain.calc.SeaWaterFlowCalc;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistillerDataTextView extends RelativeLayout implements DistillerDataViewIf {
    private final static String TAG = "DistillerDataTextView";


    @BindView(R.id.condensationSpeedLayout)
    ViewGroup condensationSpeedLayout;
    @BindView(R.id.condensateStrengthLayout)
    ViewGroup condensateStrengthLayout;
    @BindView(R.id.boilerTempLayout)
    ViewGroup boilerTempLayout;
    @BindView(R.id.headerTempLayout)
    ViewGroup headerTempLayout;

    @BindView(R.id.systemUpTimeTextView)
    TextView systemUpTimeTextView;
    @BindView(R.id.coldHotWaterTempTextView)
    TextView coldHotWaterTempTextView;
    @BindView(R.id.waterFlowTextView)
    TextView waterFlowTextView;
    @BindView(R.id.condenserPowerTextView)
    TextView condenserPowerTextView;
    @BindView(R.id.headerTempTextView)
    TextView headerTempTextView;
    @BindView(R.id.boilerTempTextView)
    TextView boilerTempTextView;

    @BindView(R.id.condensateStrengthTextView)
    TextView condensateStrengthTextView;
    @BindView(R.id.condensationSpeedTextView)
    TextView condensationSpeedTextView;

    public DistillerDataTextView(Context context) {
        super(context);

        inflate(context);
    }

    public DistillerDataTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        inflate(context);
    }

    private void inflate(Context context) {
        inflate(context, R.layout.view_distiller_data_text, this);

        ButterKnife.bind(this);
    }

    @Override
    public void showNewDistillerData(List<DistillerDataEntity> distillerData) {

        DistillerDataEntity latestData = distillerData.get(distillerData.size() - 1);

        systemUpTimeTextView.setText(formatSystemUpTime(latestData.deviceUpTimeMillis));

        String coldWaterTempAsString = "UNAVAL";
        String hotWaterTempAsString = "UNAVAL";

        if(latestData.coldWaterTemp != null) {
            coldWaterTempAsString = String.valueOf(String.format("%.2f", latestData.coldWaterTemp));
        }
        if(latestData.hotWaterTemp != null) {
            hotWaterTempAsString = String.valueOf(String.format("%.2f", latestData.hotWaterTemp));
        }
        coldHotWaterTempTextView.setText(coldWaterTempAsString + " / " + hotWaterTempAsString);

        waterFlowTextView.setText(String.valueOf(String.format("%.2f", latestData.waterRpm)));

        if(latestData.headerTemp == null) {
            headerTempTextView.setText("UNAVAL");
        }else {
            headerTempTextView.setText(String.valueOf(String.format("%.2f", latestData.headerTemp)));
        }

        if(latestData.boilerTemp == null) {
            boilerTempTextView.setText("UNAVAL");
        }else {
            boilerTempTextView.setText(String.valueOf(String.format("%.2f", latestData.boilerTemp)));
        }

        SeaWaterFlowCalc waterFlowCalc = new SeaWaterFlowCalc();
        Double waterFlowInM3PerS = null;
        try {
                waterFlowInM3PerS = waterFlowCalc.calculateWaterFlow(latestData.waterRpm);
                double waterFlowInLPerH = waterFlowInM3PerS * 1000 * 3600;
                waterFlowTextView.setText(String.format("%.2f (%.0f)", waterFlowInLPerH, latestData.waterRpm));
                if(latestData.coldWaterTemp == null || latestData.hotWaterTemp == null) {
                    condenserPowerTextView.setText("UNAVAL");
                }else {
                    CondenserCalc condenserCalc = new CondenserCalc();
                    double condenserPowerInW = condenserCalc.calculateCoolingPower(latestData.coldWaterTemp, latestData.hotWaterTemp, waterFlowInM3PerS);
                    condenserPowerTextView.setText(String.format("%.2f", condenserPowerInW));
                }
        } catch (OutOfRangeException e) {
            Log.e(TAG, e.getMessage(), e);
            waterFlowTextView.setText(String.format("ERROR (%.0f)", latestData.waterRpm));
            condensationSpeedTextView.setText("ERROR");
        }

        try {
            // calculate condensate strength
            if(latestData.headerTemp == null)
            {
                condensateStrengthTextView.setText("UNAVAL");
            } else {
                LVEWEquilibriumCalc ec = new LVEWEquilibriumCalc();
                LVEWEquilibrium equilibrium = ec.calculateEquilibrium(latestData.headerTemp);

                EthanolSolutionCalc esc = new EthanolSolutionCalc();
                double volConcentration = esc.calculateVolumeConcentration(equilibrium.ethanolLiquidMoleFraction, latestData.headerTemp);
                condensateStrengthTextView.setText(String.format("%.2f", volConcentration));
            }
        } catch (OutOfRangeException e) {
            Log.e(TAG, e.getMessage(), e);
            condensateStrengthTextView.setText("ERROR");
        }

        try {
            // calculate condensation speed
            if(waterFlowInM3PerS == null || latestData.coldWaterTemp == null || latestData.hotWaterTemp == null || latestData.headerTemp == null) {
                condensationSpeedTextView.setText("UNAVAL");
            } else {
                CondenserCalc cc = new CondenserCalc();

                CondensationSpeed cSpeed = cc.calculateCondensationSpeed(latestData.coldWaterTemp, latestData.hotWaterTemp, waterFlowInM3PerS, latestData.headerTemp);
                double condensationSpeedInMlPerMin = cSpeed.speedInLPerSec * 1000 * 60;

                CondensationSpeed condAndCoolingSpeed = cc.calculateCondensationAndCoolingSpeed(latestData.coldWaterTemp, latestData.hotWaterTemp, waterFlowInM3PerS, latestData.headerTemp);
                double condensationAndCoolingSpeedInMlPerMin = condAndCoolingSpeed.speedInLPerSec * 1000 * 60;

                condensationSpeedTextView.setText(String.format("%.1f (%.1f)", condensationAndCoolingSpeedInMlPerMin, condensationSpeedInMlPerMin));
            }
        } catch (OutOfRangeException e) {
            Log.e(TAG, e.getMessage(), e);
            condensationSpeedTextView.setText("ERROR");
        }
    }

    @Override
    public void setXRangeResolutionSeconds(int xRangeResolutionSeconds) {

    }

    @Override
    public void setXRangeVisibleSpanSeconds(int xRangeVisibleSpanSeconds) {

    }

    public void hideHeaderTemp() {
        headerTempLayout.setVisibility(View.GONE);
    }

    public void hideBoilerTemp() {
        boilerTempLayout.setVisibility(View.GONE);
    }

    public void hideCondensateStrength() {
        condensateStrengthLayout.setVisibility(View.GONE);
    }

    public void hideCondensationSpeed() {
        condensationSpeedLayout.setVisibility(View.GONE);
    }

    private static String formatSystemUpTime(long systemUpTimeInMillis) {
        long seconds = systemUpTimeInMillis / 1000;
        return String.format( "%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }
}
