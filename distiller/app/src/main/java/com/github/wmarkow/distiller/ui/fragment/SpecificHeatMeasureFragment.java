package com.github.wmarkow.distiller.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.di.components.ApplicationComponent;
import com.github.wmarkow.distiller.di.components.DaggerSpecificHeatMeasureFragmentComponent;
import com.github.wmarkow.distiller.di.components.SpecificHeatMeasureFragmentComponent;
import com.github.wmarkow.distiller.di.modules.FragmentPresentersModule;
import com.github.wmarkow.distiller.di.modules.UseCasesModule;
import com.github.wmarkow.distiller.domain.calc.CondenserCalc;
import com.github.wmarkow.distiller.domain.calc.LVEWEquilibriumCalc;
import com.github.wmarkow.distiller.domain.calc.OutOfRangeException;
import com.github.wmarkow.distiller.domain.calc.SeaWaterFlowCalc;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.ui.DistillerDataChartView;
import com.github.wmarkow.distiller.ui.DistillerDataTextView;
import com.github.wmarkow.distiller.ui.DistillerDataViewIf;
import com.github.wmarkow.distiller.ui.presenter.DistillerDataPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpecificHeatMeasureFragment extends Fragment implements DistillerDataViewIf {
    private final static String TAG = "SpecificHeatMeasureFrag";

    @BindView(R.id.distillerDataTextView)
    DistillerDataTextView distillerDataTextView;

    @BindView(R.id.distillerDataChartView)
    DistillerDataChartView distillerDataChartView;

    @BindView(R.id.specificHeatTextView)
    TextView specificHeatTextView;

    @Inject
    DistillerDataPresenter distillerDataPresenter;

    private boolean measureInProgress = false;
    private List<DistillerDataEntity> measureData = new ArrayList<>();
    private Double t0 = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_specific_heat_measure, container, false);

        ButterKnife.bind(this, root);

        final ApplicationComponent applicationComponent = DistillerApplication.getDistillerApplication().getApplicationComponent();
        SpecificHeatMeasureFragmentComponent component = DaggerSpecificHeatMeasureFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .fragmentPresentersModule(new FragmentPresentersModule())
                .useCasesModule(new UseCasesModule())
                .build();
        component.inject(this);

        // uncomment the line below if you want to use a fake distiller data presenter
        //distillerDataPresenter = new DistillerFakeDataPresenter();
        distillerDataPresenter.setView(this);

        distillerDataTextView.hideBoilerTemp();
        distillerDataTextView.hideCondensateStrength();
        distillerDataTextView.hideCondensationSpeed();
        distillerDataTextView.hideHeaderTemp();

        distillerDataChartView.removeBoilerTemp();
        distillerDataChartView.removeHeaderTemp();

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
    public void showNewDistillerData(List<DistillerDataEntity> distillerData) {
        Log.i(TAG, "New distiller data arrived.");

        distillerDataTextView.showNewDistillerData(distillerData);
        distillerDataChartView.showNewDistillerData(distillerData);

        if(measureInProgress) {
            // need to collect incoming distiller data and calculate specific heat
            measureData.addAll(distillerData);
            if(measureData.size() > 0 && t0 == null) {
                // store the initial water temperature
                t0 = distillerData.get(0).hotWaterTemp;
            }
        }

        // calculate specific heat
        try {
            Double specificHeat = calculateSpecificHeat(t0, measureData);

            if(specificHeat == null) {
                specificHeatTextView.setText("Unknown");
            } else {
                specificHeatTextView.setText(String.valueOf(specificHeat));
            }

        } catch (OutOfRangeException e) {
            Log.e(TAG, e.getMessage(), e);

            specificHeatTextView.setText("Water flow error");
        }
    }

    @Override
    public void setXRangeResolutionSeconds(int xRangeResolutionSeconds) {

    }

    @Override
    public void setXRangeVisibleSpanSeconds(int xRangeVisibleSpanSeconds) {

    }

    @OnClick(R.id.buttonStart)
    public void onButtonStartClicked() {
        measureData.clear();
        measureInProgress = true;
    }

    @OnClick(R.id.buttonStop)
    public void onButtonStopClicked() {
        measureInProgress = false;
    }

    private Double calculateSpecificHeat(double t0, List<DistillerDataEntity> measureData) throws OutOfRangeException {
        double sum = 0;
        CondenserCalc condenserCalc = new CondenserCalc(new LVEWEquilibriumCalc());
        SeaWaterFlowCalc flowCalc = new SeaWaterFlowCalc();

        // end temperature
        double tk = -1;

        if(measureData.size() <= 1) {
            return null;
        }

        for (int q = 0; q < measureData.size() - 1; q++) {
            DistillerDataEntity first = measureData.get(q);
            DistillerDataEntity second = measureData.get(q + 1);

            double tIn = second.coldWaterTemp;
            double tOut = second.hotWaterTemp;
            tk = tOut;
            double flow = flowCalc.calculateWaterFlow(second.waterRpm);
            double dtMillis = (second.utcTimestampMillis - first.utcTimestampMillis);
            double dt = dtMillis / 1000.0;

            sum += condenserCalc.calculateCoolingEnergy(tIn, tOut, flow, dt);
        }

        double waterMass = 0.45; //in kg
        double result = sum / waterMass / Math.abs(tk - t0);

        return result;
    }
}