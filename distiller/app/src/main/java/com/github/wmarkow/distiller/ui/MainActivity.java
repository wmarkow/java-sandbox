package com.github.wmarkow.distiller.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.di.components.ApplicationComponent;
import com.github.wmarkow.distiller.di.components.DaggerMainActivityComponent;
import com.github.wmarkow.distiller.di.components.MainActivityComponent;
import com.github.wmarkow.distiller.di.modules.ActivityPresentersModule;
import com.github.wmarkow.distiller.ui.presenter.ConnectivityPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ConnectivityViewIf {
    private final String TAG = "MainActivity";

    private AppBarConfiguration mAppBarConfiguration;

    @BindView(R.id.foregroundServiceSwitch)
    Switch foregroundServiceSwitch;
    @BindView(R.id.bluetoothFloatingActionButton)
    FloatingActionButton fab;
    @BindView(R.id.bluetoothProgressBar)
    ProgressBar bluetoothProgressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Inject
    ConnectivityPresenter connectivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);
        // make bluetooth progress bar invisible
        bluetoothProgressBar.setVisibility(View.INVISIBLE);

        setSupportActionBar(toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_data_browser, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        final ApplicationComponent applicationComponent = ((DistillerApplication) this.getApplication()).getApplicationComponent();
        MainActivityComponent mainActivityConnectivityComponent = DaggerMainActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityPresentersModule(new ActivityPresentersModule())
                .build();
        mainActivityConnectivityComponent.inject(this);
        connectivityPresenter.setView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        connectivityPresenter.pause();

        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus) {
            // https://github.com/JorgeCastilloPrz/FABProgressCircle/issues/29#issuecomment-252837864
            // FABProgressCircle throws NPE when called from onResume() (probably not all graphic resources are loaded/created
            // when onResume() is called).
            // Need to call it from this method as it behaves like onResume()
            connectivityPresenter.resume();
        }
    }

    @OnClick(R.id.foregroundServiceSwitch)
    public void foregroundServiceSwitchClicked() {
        connectivityPresenter.enableForegroundService(foregroundServiceSwitch.isChecked());
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void requestPermissions(@NonNull String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions,0);
    }

    @Override
    public void showDistillerConnected()  {
        fab.setEnabled(false);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ic_bluetooth_connected)));
        bluetoothProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showDistillerDisconnected() {
        fab.setEnabled(false);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ic_bluetooth_disconnected)));
        bluetoothProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showDistillerConnecting() {
        fab.setEnabled(false);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ic_bluetooth_connecting)));
        bluetoothProgressBar.setIndeterminate(true);
        bluetoothProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDistillerIndicatorDisabled() {
        showDistillerDisconnected();
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ic_bluetooth_icon_disabled)));
    }

    @Override
    public void showDistillerSwitchChecked(boolean checked) {
        foregroundServiceSwitch.setChecked(checked);
    }

    @Override
    public void showMessage(String message) {
        // do nothing
    }

    @Override
    public void showRefresh() {
        // do nothing
    }

    @Override
    public void hideRefresh() {
        // do nothing
    }
}