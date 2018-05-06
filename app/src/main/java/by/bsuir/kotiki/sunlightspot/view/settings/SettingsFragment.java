package by.bsuir.kotiki.sunlightspot.view.settings;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import by.bsuir.kotiki.sunlightspot.R;
import by.bsuir.kotiki.sunlightspot.entity.animal.Animal;
import by.bsuir.kotiki.sunlightspot.model.animal.AnimalStorage;
import by.bsuir.kotiki.sunlightspot.model.settings.SettingsManager;
import by.bsuir.kotiki.sunlightspot.presenter.settings.SettingsPresenter;
import by.bsuir.kotiki.sunlightspot.view.pager.ForecastPagerAdapter;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SettingsFragment extends Fragment implements SearchView.OnQueryTextListener, CheckBox.OnCheckedChangeListener, ListView.OnItemClickListener {
    private SettingsPresenter presenter;

    private SearchView locationSearchView;
    private CheckBox autoLocationCheckBox;

    private ListView resultLocationListView;
    private List<String> locations;
    private ArrayAdapter<String> resultLocationAdapter;

    private ImageButton shuniaImageButton;
    private ImageButton quipImageButton;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationSearchView = getView().findViewById(R.id.locationSearchView);
        autoLocationCheckBox = getView().findViewById(R.id.autoLocationCheckBox);
        resultLocationListView = getView().findViewById(R.id.resultLocationListView);

        shuniaImageButton = getView().findViewById(R.id.shuniaImageButton);
        shuniaImageButton.setOnClickListener(shuniaListener);
        quipImageButton = getView().findViewById(R.id.quipImageButton);
        quipImageButton.setOnClickListener(quipListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter = new SettingsPresenter(this);

        locationSearchView.setOnQueryTextListener(this);
        autoLocationCheckBox.setOnCheckedChangeListener(this);

        locations = new ArrayList<>();
        resultLocationAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, locations);
        resultLocationListView.setAdapter(resultLocationAdapter);
        resultLocationListView.setOnItemClickListener(this);

        autoLocationCheckBox.setChecked(presenter.getAutoLocation());
        if (locationSearchView.getQuery().length() == 0) {
            locationSearchView.setQuery(presenter.getLocation(), false);
        }

        Animal activeAnimal = presenter.getAnimal();
        if (activeAnimal == Animal.SHUNIA) {
            quipImageButton.setAlpha(0.5F);
        } else if (activeAnimal == Animal.QUIP) {
            shuniaImageButton.setAlpha(0.5F);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        presenter.changeLocationMode(b);
        if (b) {
            locationSearchView.setVisibility(View.INVISIBLE);
            resultLocationListView.setVisibility(View.INVISIBLE);
        } else {
            locationSearchView.setVisibility(View.VISIBLE);
            resultLocationListView.setVisibility(View.VISIBLE);
        }
        ForecastPagerAdapter.getInstance().getItem(1);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.isEmpty()) {
            locations.clear();
            return true;
        }

        List<String> result = null;
        FutureTask<List<String>> locatorTask = new FutureTask<>(() -> presenter.getLocations(s));
        new Thread(locatorTask).start();
        try {
            result = locatorTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        locations.clear();
        locations.addAll(result);

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String location = resultLocationAdapter.getItem(i);
        locationSearchView.setQuery(location, true);
        presenter.setLocation("q=" + location.substring(1, location.indexOf(',')) + "&");
        resultLocationListView.setVisibility(View.INVISIBLE);
    }

    private ImageButton.OnClickListener shuniaListener = view -> {
        if (presenter.getAnimal().toString().toLowerCase().equals("shunia")) {
            return;
        }

        shuniaImageButton.setAlpha(1F);
        quipImageButton.setAlpha(0.5F);

        presenter.setAnimal(Animal.SHUNIA);
        ForecastPagerAdapter.getInstance().getItem(1);
    };

    private ImageButton.OnClickListener quipListener = view -> {
        if (presenter.getAnimal().toString().toLowerCase().equals("quip")) {
            return;
        }

        shuniaImageButton.setAlpha(0.5F);
        quipImageButton.setAlpha(1F);

        presenter.setAnimal(Animal.QUIP);
        ForecastPagerAdapter.getInstance().getItem(1);
    };
}
