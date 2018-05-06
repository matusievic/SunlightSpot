package by.bsuir.kotiki.sunlightspot.presenter.settings;

import android.support.v4.app.Fragment;

import java.util.List;

import by.bsuir.kotiki.sunlightspot.entity.animal.Animal;
import by.bsuir.kotiki.sunlightspot.model.location.LocationManager;
import by.bsuir.kotiki.sunlightspot.model.location.LocationProvider;
import by.bsuir.kotiki.sunlightspot.model.settings.SettingsManager;
import by.bsuir.kotiki.sunlightspot.presenter.Presenter;
import by.bsuir.kotiki.sunlightspot.view.settings.SettingsFragment;

public final class SettingsPresenter implements Presenter {
    private final SettingsFragment fragment;
    private final LocationManager locationManager;
    private final LocationProvider locationProvider;
    private final SettingsManager settingsManager;

    public SettingsPresenter(Fragment fragment) {
        this.fragment = (SettingsFragment) fragment;
        this.locationManager = LocationManager.getInstance(fragment.getActivity().getApplicationContext());
        this.locationManager.setPresenter(this);
        this.locationProvider = LocationProvider.getInstance();
        settingsManager = SettingsManager.getInstance();
    }

    public void changeLocationMode(boolean auto) {
        settingsManager.setAutoLocation(auto);
        settingsManager.saveSettings();
    }

    public List<String> getLocations(String param) throws Exception {
        return locationProvider.getLocations(param);
    }

    public Animal getAnimal() {
        return settingsManager.getAnimal();
    }

    public void setAnimal(Animal animal) {
        settingsManager.setAnimal(animal);
        settingsManager.saveSettings();
    }

    @Override
    public void displayErrorMessage(String message) {}

    public boolean getAutoLocation() {
        return settingsManager.isAutoLocation();
    }

    public void setLocation(String location) {
        settingsManager.setLocation(location);
        settingsManager.saveSettings();
    }
}
