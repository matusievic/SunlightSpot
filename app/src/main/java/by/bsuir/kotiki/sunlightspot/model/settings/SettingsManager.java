package by.bsuir.kotiki.sunlightspot.model.settings;

import android.content.Context;
import android.content.SharedPreferences;

import by.bsuir.kotiki.sunlightspot.entity.animal.Animal;

public final class SettingsManager {
    private static SettingsManager instance;
    private final Context context;

    private final String settingsFile = "by.bsuir.kotiki.sunlightspot.settings";
    private final String animalParam = "animal";
    private final String autoLocationParam = "auto";
    private final String locationParam = "location";

    private Animal animal;
    private boolean autoLocation;
    private String location;

    private SettingsManager(Context context) {
        this.context = context;
        loadSettings();
    }

    public void loadSettings() {
        SharedPreferences preferences = context.getSharedPreferences(settingsFile, Context.MODE_PRIVATE);
        animal = Animal.valueOf(preferences.getString(animalParam, "SHUNIA"));
        autoLocation = preferences.getBoolean(autoLocationParam, true);
        location = preferences.getString(locationParam, "q=Navahrudak&");
    }

    public void saveSettings() {
        SharedPreferences preferences = context.getSharedPreferences(settingsFile, Context.MODE_PRIVATE);
        preferences.edit().putString(animalParam, animal.toString()).putBoolean(autoLocationParam, autoLocation).putString(locationParam, location).apply();
    }

    public static SettingsManager getInstance(Context context) {
        instance = new SettingsManager(context);
        return instance;
    }

    public static SettingsManager getInstance() {
        return instance;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public boolean isAutoLocation() {
        return autoLocation;
    }

    public void setAutoLocation(boolean autoLocation) {
        this.autoLocation = autoLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
