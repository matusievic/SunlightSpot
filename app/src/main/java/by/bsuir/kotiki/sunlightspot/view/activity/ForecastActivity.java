package by.bsuir.kotiki.sunlightspot.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import by.bsuir.kotiki.sunlightspot.R;
import by.bsuir.kotiki.sunlightspot.model.location.LocationManager;
import by.bsuir.kotiki.sunlightspot.model.settings.SettingsManager;
import by.bsuir.kotiki.sunlightspot.view.pager.ForecastPagerAdapter;

public class ForecastActivity extends FragmentActivity {
    private ForecastPagerAdapter forecastPagerAdapter;
    private ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forecast);
        SettingsManager.getInstance(getApplicationContext()).loadSettings();
        LocationManager.getInstance(getApplicationContext());

        forecastPagerAdapter = new ForecastPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        viewPager.setAdapter(forecastPagerAdapter);
        viewPager.setCurrentItem(1);
    }
}
