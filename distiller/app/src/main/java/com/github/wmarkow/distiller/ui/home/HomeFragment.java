package com.github.wmarkow.distiller.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.di.components.ApplicationComponent;
import com.github.wmarkow.distiller.di.components.DaggerHomeFragmentComponent;
import com.github.wmarkow.distiller.di.components.HomeFragmentComponent;
import com.github.wmarkow.distiller.di.modules.PresentersModule;
import com.github.wmarkow.distiller.domain.calc.CondensationSpeed;
import com.github.wmarkow.distiller.domain.calc.CondenserCalc;
import com.github.wmarkow.distiller.domain.calc.EthanolSolutionCalc;
import com.github.wmarkow.distiller.domain.calc.LVEWEquilibrium;
import com.github.wmarkow.distiller.domain.calc.LVEWEquilibriumCalc;
import com.github.wmarkow.distiller.domain.calc.OutOfRangeException;
import com.github.wmarkow.distiller.domain.calc.SeaWaterFlowCalc;
import com.github.wmarkow.distiller.domain.model.DistillerData;
import com.github.wmarkow.distiller.ui.DistillerDataChartView;
import com.github.wmarkow.distiller.ui.DistillerDataViewIf;
import com.github.wmarkow.distiller.ui.presenter.DistillerDataPresenter;
import com.github.wmarkow.distiller.ui.presenter.DistillerFakeDataPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements DistillerDataViewIf {
    private final static String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;

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

    @BindView(R.id.distillerDataChartView)
    DistillerDataChartView chart;

    @Inject
    DistillerDataPresenter distillerDataPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);

        final ApplicationComponent applicationComponent = DistillerApplication.getDistillerApplication().getApplicationComponent();
        HomeFragmentComponent homeFragmentComponent = DaggerHomeFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .presentersModule(new PresentersModule())
                .build();
        homeFragmentComponent.inject(this);

        // uncomment the line below if you want to use a fake distiller data presenter
        distillerDataPresenter = new DistillerFakeDataPresenter();

        distillerDataPresenter.setView(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.distillerDataPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.distillerDataPresenter.pause();
    }

    @Override
    public void showDistillerData(DistillerData distillerData) {
        Log.i(TAG, "New distiller data arrived.");

        systemUpTimeTextView.setText(formatSystemUpTime(distillerData.systemUpTime));

        coldWaterTempTextView.setText(String.valueOf(String.format("%.2f", distillerData.coldWaterTemp)));
        hotWaterTempTextView.setText(String.valueOf(String.format("%.2f", distillerData.hotWaterTemp)));
        waterFlowTextView.setText(String.valueOf(String.format("%.2f", distillerData.waterRpm)));

        headerTempTextView.setText(String.valueOf(String.format("%.2f", distillerData.headerTemp)));

        boilerTempTextView.setText(String.valueOf(String.format("%.2f", distillerData.boilerTemp)));

        SeaWaterFlowCalc waterFlowCalc = new SeaWaterFlowCalc();
        try {
            double waterFlowInM3PerS = waterFlowCalc.calculateWaterFlow(distillerData.waterRpm);
            double waterFlowInLPerH = waterFlowInM3PerS * 1000 * 3600;
            CondenserCalc condenserCalc = new CondenserCalc();
            double condenserPowerInW = condenserCalc.calculateCoolingPower(distillerData.coldWaterTemp, distillerData.hotWaterTemp, waterFlowInM3PerS);
            waterFlowTextView2.setText(String.format("%.2f", waterFlowInLPerH));
            condenserPowerTextView.setText(String.format("%.2f", condenserPowerInW));

            // calculate condensate strength
            LVEWEquilibriumCalc ec = new LVEWEquilibriumCalc();
            LVEWEquilibrium equilibrium = ec.calculateEquilibrium(distillerData.headerTemp);

            EthanolSolutionCalc esc = new EthanolSolutionCalc();
            double volConcentration = esc.calculateVolumeConcentration(equilibrium.ethanolLiquidMoleFraction, distillerData.headerTemp);
            condensateStrengthTextView.setText(String.format("%.2f", volConcentration));

            // calculate condensation speed
            CondenserCalc cc = new CondenserCalc();
            CondensationSpeed cSpeed = cc.calculateCondensationSpeed(distillerData.coldWaterTemp, distillerData.hotWaterTemp, waterFlowInM3PerS, distillerData.headerTemp);
            double condensationSpeedInLPerMin = cSpeed.speedInLPerSec * 1000 * 60;
            condensationSpeedTextView.setText(String.format("%.2f", condensationSpeedInLPerMin));
        } catch (OutOfRangeException e) {
            Log.e(TAG, e.getMessage(), e);
            waterFlowTextView2.setText("ERROR");
            condenserPowerTextView.setText("ERROR");
            condensateStrengthTextView.setText("ERROR");
            condensationSpeedTextView.setText("ERROR");
        }



        this.chart.addDistillerData(distillerData);
    }

    public static String formatSystemUpTime(long systemUpTimeInMillis) {
        long seconds = systemUpTimeInMillis / 1000;
        return String.format( "%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }
}