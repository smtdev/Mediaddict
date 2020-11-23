package me.smt.mediaddict.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.smt.mediaddict.R;
import me.smt.mediaddict.module.GlideApp;

/**
 * Clase que se encarga de mostrar información del usuario.
 * @author Sergio Martín Teruel
 * @version 1.0
 * @see Fragment
 **/
public class UserProfileFragment extends Fragment {

    @BindView(R.id.tb_user_profile)
    Toolbar tbUserProfile;
    @BindView(R.id.ctl_user_profile)
    CollapsingToolbarLayout ctlUserProfile;
    @BindView(R.id.fab_user_profile)
    FloatingActionButton fabUserProfile;
    @BindView(R.id.fab_settings)
    FloatingActionButton fabSettings;
    @BindView(R.id.iv_user_profile)
    ImageView ivUserProfile;
    @BindView(R.id.abl_user_profile)
    AppBarLayout ablUserProfile;
    @BindView(R.id.tv_user_profile_username)
    AppCompatTextView tvUserProfileUsername;
    @BindView(R.id.constly_movie_detail)
    ConstraintLayout constlyMovieDetail;
    @BindView(R.id.bt_user_profile_close_session)
    AppCompatButton btUserProfileCloseSession;
    @BindView(R.id.tv_user_profile_email)
    AppCompatTextView tvUserProfileEmail;
    @BindView(R.id.cl_user_profile)
    CoordinatorLayout clUserProfile;

    /**
     * Atributo que crea una instancia de FirebaseAuth.
     */
    private FirebaseAuth mFirebaseAuth;

    /**
     * Constructor de la clase.
     */
    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Método llamado al iniciar la creación del Fragment.
     * @param savedInstanceState estado de la instancia.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ocultar la Toolbar principal al iniciar View
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }

    /**
     * Método llamado para que el Fragment cree una instancia de la
     * vista a inflar.
     * @param inflater vista a inflar.
     * @param container contenedor de vistas.
     * @param savedInstanceState estado de la instancia.
     * @return la vista.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ButterKnife.bind(this, view);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        if (user != null) {
            loadUserProfileInformation(user.getDisplayName(), user.getEmail(), user.getPhotoUrl());
        }

        return view;
    }

    /**
     * Método que se encarga de cargar los datos del usuario loggeado.
     * @param userName  nombre de usuario.
     * @param email     email de usuario.
     * @param photoUrl  imagen del usuario.
     */
    public void loadUserProfileInformation(String userName, String email, Uri photoUrl) {
        tvUserProfileUsername.setText(userName);
        tvUserProfileEmail.setText(email);

        GlideApp.with(requireContext())
                .load(photoUrl + "?type=large")
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(ivUserProfile);

        /*GlideApp.with(requireContext())
                .load(R.drawable.profile)
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(ivUserProfile);*/
    }

    /**
     * Método llamado cuando se ha creado la Activity del Fragment y se ha
     * instanciado la jerarquía de vistas del fragmento.
     * @param savedInstanceState estado de la instancia.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
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
        // mostrar la Toolbar
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }


    /**
     * Método para manejar los clics de los elementos asociados al @OnClick.
     * @param view
     */
    @OnClick({R.id.fab_user_profile, R.id.fab_settings, R.id.bt_user_profile_close_session})
    public void onClick(View view) {
        Bundle bundle = new Bundle();

        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setArguments(bundle);

        switch (view.getId()) {
            case R.id.fab_user_profile:
                Snackbar.make(constlyMovieDetail, "Próximamente...", Snackbar.LENGTH_SHORT)
                        .show();
                break;
            case R.id.fab_settings:
                requireActivity().getSupportFragmentManager().beginTransaction()
                        // ((ViewGroup)getView().getParent()).getId() -> es el id del fragment actual
                        .replace(((ViewGroup) requireView().getParent()).getId(), settingsFragment, "FragmentPreferences")
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.bt_user_profile_close_session:
                AuthUI.getInstance()
                        .signOut(requireContext())
                        .addOnCompleteListener(task -> {
                            // user is now signed out
                            Intent i = new Intent(requireActivity(), LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        });
                break;
        }
    }
}