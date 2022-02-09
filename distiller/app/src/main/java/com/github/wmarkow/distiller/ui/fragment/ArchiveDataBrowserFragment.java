package com.github.wmarkow.distiller.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.di.components.ApplicationComponent;
import com.github.wmarkow.distiller.di.components.DaggerArchiveDataBrowserFragmentComponent;
import com.github.wmarkow.distiller.di.components.ArchiveDataBrowserFragmentComponent;
import com.github.wmarkow.distiller.di.modules.FragmentPresentersModule;
import com.github.wmarkow.distiller.di.modules.UseCasesModule;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.ui.DistillerDataChartView;
import com.github.wmarkow.distiller.ui.DistillerDataTextView;
import com.github.wmarkow.distiller.ui.DistillerDataViewIf;
import com.github.wmarkow.distiller.ui.MVPViewIf;
import com.github.wmarkow.distiller.ui.presenter.ArchiveDataBrowserPresenter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArchiveDataBrowserFragment extends Fragment implements DistillerDataViewIf, MVPViewIf, SeekBar.OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener {
    private final static String TAG = "DataBrowserFragment";

    private final static int MIN_TIME_SPAN_SECONDS = 60;
    private final static int MAX_TIME_SPAN_SECONDS = 3600;

    private Integer year = null;
    private Integer month = null;
    private Integer dayOfMonth = null;

    @BindView(R.id.distillerDataTextView)
    DistillerDataTextView distillerDataTextView;

    @BindView(R.id.distillerDataChartView)
    DistillerDataChartView distillerDataChartView;

    @BindView(R.id.timeSpanSeekBar)
    SeekBar timeSpanSeekBar;

    @BindView(R.id.chooseDateButton)
    Button chooseDateButton;

    @Inject
    ArchiveDataBrowserPresenter archiveDataBrowserPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_archive_data_browser, container, false);

        ButterKnife.bind(this, root);

        final ApplicationComponent applicationComponent = DistillerApplication.getDistillerApplication().getApplicationComponent();
        ArchiveDataBrowserFragmentComponent dataBrowserFragmentComponent = DaggerArchiveDataBrowserFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .useCasesModule(new UseCasesModule())
                .fragmentPresentersModule(new FragmentPresentersModule())
                .build();
        dataBrowserFragmentComponent.inject(this);

        archiveDataBrowserPresenter.setView(this);

        timeSpanSeekBar.setMin(MIN_TIME_SPAN_SECONDS);
        timeSpanSeekBar.setMax(MAX_TIME_SPAN_SECONDS);
        timeSpanSeekBar.setProgress(MIN_TIME_SPAN_SECONDS);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        timeSpanSeekBar.setOnSeekBarChangeListener(this);
        distillerDataChartView.setFollowLatestEntry(true);
        this.archiveDataBrowserPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        timeSpanSeekBar.setOnSeekBarChangeListener(null);
        this.archiveDataBrowserPresenter.pause();
    }

    @OnClick(R.id.chooseDateButton)
    public void onChooseDateButtonClicked() {
        Log.i(TAG, "Button clicked");

        if(year == null) {
            Calendar mCalendar = Calendar.getInstance();
            year = mCalendar.get(Calendar.YEAR);
            month = mCalendar.get(Calendar.MONTH);
            dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        }

        // month in DatePicker is from 0 to 11
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this, year, month - 1, dayOfMonth);
        datePickerDialog.show();
    }

    @Override
    public void showNewDistillerData(List<DistillerDataEntity> newData) {
        if(newData.isEmpty()) {
            this.showMessage("No data found!");
        }

        distillerDataTextView.showNewDistillerData(newData);
        distillerDataChartView.showNewDistillerData(newData);
    }

    @Override
    public void setXRangeResolutionSeconds(int resolutionInSeconds) {
        distillerDataChartView.setXRangeResolutionSeconds(resolutionInSeconds);
    }

    @Override
    public void setXRangeVisibleSpanSeconds(int spanInSeconds) {
        distillerDataChartView.setXRangeVisibleSpanSeconds(spanInSeconds);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void hideRefresh() {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.i(TAG, "Date chosen");

        this.year = year;
        // month in DatePicker is from 0 to 11
        this.month = month + 1;
        this.dayOfMonth = dayOfMonth;

        chooseDateButton.setText(String.format("%04d-%02d-%02d", year, this.month, dayOfMonth));

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("UTC")).withYear(year).withMonth(this.month).withDayOfMonth(dayOfMonth);

        archiveDataBrowserPresenter.readDistillerData(dateTime);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.i(TAG, String.format("New time span is %s", progress));
        distillerDataChartView.setXRangeVisibleSpanSeconds(progress);
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
