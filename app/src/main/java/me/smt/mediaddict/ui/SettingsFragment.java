package me.smt.mediaddict.ui;

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

/**
 * Clase que se encarga de mostrar el menú de configuración de la
 * aplicación.
 * @author Sergio Martín Teruel
 * @version 1.0
 * @see PreferenceFragmentCompat
 **/
public class SettingsFragment extends PreferenceFragmentCompat {

    /**
     * Atributo que hace referencia a una lista de preferencias.
     */
    private ListPreference mListPreference;

    /**
     * Atributo que hace referencia al nombre de las prefs almacenadas.
     */
    static final String PREFS_NAME = "defaults";

    /**
     * Método llamado durante el OnCreate para proporcionar preferencias
     * de este Fragment.
     * @param savedInstanceState estado de la instancia.
     * @param rootKey rootKey.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(PREFS_NAME);
        addPreferencesFromResource(R.xml.preferences);
    }

    /**
     * Método que se llama cuando el Fragment no va a volver a abrirse más.
     */
    @Override
    public void onStop() {
        super.onStop();
        // mostrar la Toolbar
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }

    /**
     * Método que se llama cuando el Fragment es visible para el usuario y está activo.
     */
    @Override
    public void onResume() {
        super.onResume();

        // ocultar la Toolbar
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }

    /**
     * Método que se llama cuando el Fragment ya no está en uso.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }

    /**
     * Método llamado al iniciar la creación del Fragment.
     * @param savedInstanceState estado de la instancia.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Método llamado para que el Fragment cree una instancia de la
     * vista a inflar.
     * @param inflater vista a inflar.
     * @param container contenedor de vistas.
     * @param savedInstanceState estado de la isntancia.
     * @return la vista.
     */
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
