package me.smt.mediaddict.moviesModule.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import me.smt.mediaddict.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    private ListPreference mListPreference;

    static final String PREFS_NAME = "defaults";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(PREFS_NAME);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onStop() {
        super.onStop();
        // mostrar la Toolbar
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // ocultar la Toolbar
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ocultar la Toolbar principal al iniciar View
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        
        // Recoger parámetros enviados por el fragment anterior
        Bundle bundle = this.getArguments();

        if (bundle != null) {

        }

        /*mListPreference = (ListPreference)  getPreferenceManager().findPreference("preference_key");
        mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // your code here
                return false;
            }
        });*/

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
