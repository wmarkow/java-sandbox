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
import com.github.wmarkow.distiller.domain.model.DistillerData;
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
    @BindView(R.id.coldWaterTempTextView)
    TextView coldWaterTempTextView;
    @BindView(R.id.hotWaterTempTextView)
    TextView hotWaterTempTextView;
    @BindView(R.id.waterFlowTextView)
    TextView waterFlowTextView;
    @BindView(R.id.waterFlowTextView2)
    TextView waterFlowTextView2;
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

        systemUpTimeTextView.setText(formatSystemUpTime(latestData.utcTimestampMillis));

        coldWaterTempTextView.setText(String.valueOf(String.format("%.2f", latestData.coldWaterTemp)));
        hotWaterTempTextView.setText(String.valueOf(String.format("%.2f", latestData.hotWaterTemp)));
        waterFlowTextView.setText(String.valueOf(String.format("%.2f", latestData.waterRpm)));

        headerTempTextView.setText(String.valueOf(String.format("%.2f", latestData.headerTemp)));

        boilerTempTextView.setText(String.valueOf(String.format("%.2f", latestData.boilerTemp)));

        SeaWaterFlowCalc waterFlowCalc = new SeaWaterFlowCalc();
        Double waterFlowInM3PerS = null;
        try {
            waterFlowInM3PerS = waterFlowCalc.calculateWaterFlow(latestData.waterRpm);
            double waterFlowInLPerH = waterFlowInM3PerS * 1000 * 3600;
            CondenserCalc condenserCalc = new CondenserCalc();
            double condenserPowerInW = condenserCalc.calculateCoolingPower(latestData.coldWaterTemp, latestData.hotWaterTemp, waterFlowInM3PerS);
            waterFlowTextView2.setText(String.format("%.2f", waterFlowInLPerH));
            condenserPowerTextView.setText(String.format("%.2f", condenserPowerInW));
        } catch (OutOfRangeException e) {
            Log.e(TAG, e.getMessage(), e);
            waterFlowTextView2.setText("ERROR");
            condensationSpeedTextView.setText("ERROR");
        }

        try {
            // calculate condensate strength
            LVEWEquilibriumCalc ec = new LVEWEquilibriumCalc();
            LVEWEquilibrium equilibrium = ec.calculateEquilibrium(latestData.headerTemp);

            EthanolSolutionCalc esc = new EthanolSolutionCalc();
            double volConcentration = esc.calculateVolumeConcentration(equilibrium.ethanolLiquidMoleFraction, latestData.headerTemp);
            condensateStrengthTextView.setText(String.format("%.2f", volConcentration));
        } catch (OutOfRangeException e) {
            Log.e(TAG, e.getMessage(), e);
            condensateStrengthTextView.setText("ERROR");
        }

        try {
            // calculate condensation speed
            if(waterFlowInM3PerS != null) {
                CondenserCalc cc = new CondenserCalc();
                CondensationSpeed cSpeed = cc.calculateCondensationSpeed(latestData.coldWaterTemp, latestData.hotWaterTemp, waterFlowInM3PerS, latestData.headerTemp);
                double condensationSpeedInLPerMin = cSpeed.speedInLPerSec * 1000 * 60;
                condensationSpeedTextView.setText(String.format("%.2f", condensationSpeedInLPerMin));
            }
        } catch (OutOfRangeException e) {
            Log.e(TAG, e.getMessage(), e);
            condensationSpeedTextView.setText("ERROR");
        }
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
