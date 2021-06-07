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
    public void showDistillerData(DistillerData distillerData) {
        systemUpTimeTextView.setText(formatSystemUpTime(distillerData.systemUpTime));

        coldWaterTempTextView.setText(String.valueOf(String.format("%.2f", distillerData.coldWaterTemp)));
        hotWaterTempTextView.setText(String.valueOf(String.format("%.2f", distillerData.hotWaterTemp)));
        waterFlowTextView.setText(String.valueOf(String.format("%.2f", distillerData.waterRpm)));

        headerTempTextView.setText(String.valueOf(String.format("%.2f", distillerData.headerTemp)));

        boilerTempTextView.setText(String.valueOf(String.format("%.2f", distillerData.boilerTemp)));

        SeaWaterFlowCalc waterFlowCalc = new SeaWaterFlowCalc();
        Double waterFlowInM3PerS = null;
        try {
            waterFlowInM3PerS = waterFlowCalc.calculateWaterFlow(distillerData.waterRpm);
            double waterFlowInLPerH = waterFlowInM3PerS * 1000 * 3600;
            CondenserCalc condenserCalc = new CondenserCalc();
            double condenserPowerInW = condenserCalc.calculateCoolingPower(distillerData.coldWaterTemp, distillerData.hotWaterTemp, waterFlowInM3PerS);
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
            LVEWEquilibrium equilibrium = ec.calculateEquilibrium(distillerData.headerTemp);

            EthanolSolutionCalc esc = new EthanolSolutionCalc();
            double volConcentration = esc.calculateVolumeConcentration(equilibrium.ethanolLiquidMoleFraction, distillerData.headerTemp);
            condensateStrengthTextView.setText(String.format("%.2f", volConcentration));
        } catch (OutOfRangeException e) {
            Log.e(TAG, e.getMessage(), e);
            condensateStrengthTextView.setText("ERROR");
        }

        try {
            // calculate condensation speed
            if(waterFlowInM3PerS != null) {
                CondenserCalc cc = new CondenserCalc();
                CondensationSpeed cSpeed = cc.calculateCondensationSpeed(distillerData.coldWaterTemp, distillerData.hotWaterTemp, waterFlowInM3PerS, distillerData.headerTemp);
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
