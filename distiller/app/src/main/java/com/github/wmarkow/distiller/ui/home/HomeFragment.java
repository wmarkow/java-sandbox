package com.github.wmarkow.distiller.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.di.components.ApplicationComponent;
import com.github.wmarkow.distiller.di.components.DaggerHomeFragmentComponent;
import com.github.wmarkow.distiller.di.components.HomeFragmentComponent;
import com.github.wmarkow.distiller.di.modules.PresentersModule;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.ui.DistillerDataChartView;
import com.github.wmarkow.distiller.ui.DistillerDataTextView;
import com.github.wmarkow.distiller.ui.DistillerDataViewIf;
import com.github.wmarkow.distiller.ui.presenter.DistillerDataPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements DistillerDataViewIf {
    private final static String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;

    @BindView(R.id.distillerDataTextView)
    DistillerDataTextView distillerDataTextView;

    @BindView(R.id.distillerDataChartView)
    DistillerDataChartView distillerDataChartView;

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
        //distillerDataPresenter = new DistillerFakeDataPresenter();

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
    public void showNewDistillerData(List<DistillerDataEntity> distillerData) {
        Log.i(TAG, "New distiller data arrived.");

        distillerDataTextView.showNewDistillerData(distillerData);
        distillerDataChartView.showNewDistillerData(distillerData);
    }
}