package com.github.wmarkow.distiller.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.di.components.ApplicationComponent;
import com.github.wmarkow.distiller.di.components.DaggerHomeFragmentComponent;
import com.github.wmarkow.distiller.di.components.HomeFragmentComponent;
import com.github.wmarkow.distiller.di.modules.FragmentPresentersModule;
import com.github.wmarkow.distiller.di.modules.UseCasesModule;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.ui.DistillerDataChartView;
import com.github.wmarkow.distiller.ui.DistillerDataTextView;
import com.github.wmarkow.distiller.ui.DistillerDataViewIf;
import com.github.wmarkow.distiller.ui.presenter.DistillerDataPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment implements DistillerDataViewIf, SeekBar.OnSeekBarChangeListener {
    private final static String TAG = "HomeFragment";
    private final static int MIN_TIME_SPAN_SECONDS = 60;
    private final static int MAX_TIME_SPAN_SECONDS = 3600;

    @BindView(R.id.distillerDataTextView)
    DistillerDataTextView distillerDataTextView;

    @BindView(R.id.distillerDataChartView)
    DistillerDataChartView distillerDataChartView;

    @BindView(R.id.timeSpanSeekBar)
    SeekBar timeSpanSeekBar;

    @BindView(R.id.followLatestEntrySwitch)
    Switch followLatestEntrySwitch;

    @BindView(R.id.extendedModelSwitch)
    Switch extendedModelSwitch;

    @BindView(R.id.minExtendedTempEditText)
    EditText minExtendedTempEditText;

    @Inject
    DistillerDataPresenter distillerDataPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);

        final ApplicationComponent applicationComponent = DistillerApplication.getDistillerApplication().getApplicationComponent();
        HomeFragmentComponent homeFragmentComponent = DaggerHomeFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .useCasesModule(new UseCasesModule())
                .fragmentPresentersModule(new FragmentPresentersModule())
                .build();
        homeFragmentComponent.inject(this);

        // uncomment the line below if you want to use a fake distiller data presenter
        //distillerDataPresenter = new DistillerFakeDataPresenter();

        distillerDataPresenter.setView(this);

        timeSpanSeekBar.setMin(MIN_TIME_SPAN_SECONDS);
        timeSpanSeekBar.setMax(MAX_TIME_SPAN_SECONDS);
        timeSpanSeekBar.setProgress(MIN_TIME_SPAN_SECONDS);
        distillerDataChartView.setXRangeVisibleSpanSeconds(MIN_TIME_SPAN_SECONDS);

        extendedModelSwitch.setChecked(false);
        minExtendedTempEditText.setEnabled(false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        timeSpanSeekBar.setOnSeekBarChangeListener(this);
        followLatestEntrySwitch.setChecked(true);
        distillerDataChartView.setFollowLatestEntry(true);
        this.distillerDataPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        timeSpanSeekBar.setOnSeekBarChangeListener(null);
        this.distillerDataPresenter.pause();
    }

    @Override
    public void showNewDistillerData(List<DistillerDataEntity> distillerData) {
        Log.i(TAG, "New distiller data arrived.");

        distillerDataTextView.showNewDistillerData(distillerData);
        distillerDataChartView.showNewDistillerData(distillerData);
    }

    @Override
    public void setXRangeResolutionSeconds(int xRangeResolutionSeconds) {
        distillerDataChartView.setXRangeResolutionSeconds(xRangeResolutionSeconds);
    }

    @Override
    public void setXRangeVisibleSpanSeconds(int xRangeVisibleSpanSeconds) {
        distillerDataChartView.setXRangeVisibleSpanSeconds(xRangeVisibleSpanSeconds);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.i(TAG, String.format("New time span is %s", progress));
        distillerDataChartView.setXRangeVisibleSpanSeconds(progress);
    }

    @OnClick(R.id.followLatestEntrySwitch)
    public void onFollowLatestEntrySwitchClicked() {
        distillerDataChartView.setFollowLatestEntry(followLatestEntrySwitch.isChecked());
    }

    @OnClick(R.id.extendedModelSwitch)
    public void onExtendedModelSwitchClicked() {
        if(extendedModelSwitch.isChecked()) {
            if(minExtendedTempEditText.getText().length() == 0) {
                minExtendedTempEditText.setText("75.3");
            }

            double minTemp = Double.parseDouble(minExtendedTempEditText.getText().toString());
            distillerDataTextView.enableExtendedModel(minTemp);
            minExtendedTempEditText.setEnabled(true);

        } else {
            distillerDataTextView.disableExtendedModel();
            minExtendedTempEditText.setEnabled(false);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // do nothing
    }
}