package me.smt.mediaddict.ui;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.smt.mediaddict.R;

/**
 * Clase principal que se encarga de cargar el layout donde
 * ocurrirán todas las acciones en la aplicación.
 * @author Sergio Martín Teruel
 * @version 1.0
 * @see AppCompatActivity
 **/
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.mainConstraintLayout)
    ConstraintLayout mainConstraintLayout;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    /**
     * Método llamado al iniciar la creación de la Activity.
     * @param savedInstanceState estado de la instancia.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mainToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        openFragment(new HomeListFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.app_bar_home:
                    openFragment(new HomeListFragment());
                    return true;
                /*case R.id.app_bar_tvshow:
                    openFragment(new FragmentShowList());
                    return true;*/
                /*case R.id.app_bar_movie:
                    openFragment(new MovieListFragment());
                    return true;*/
                case R.id.app_bar_fav:
                    openFragment(new FavoritesFragment());
                    return true;
                case R.id.app_bar_profile:
                    openFragment(new UserProfileFragment());
                    return true;
            }
            return false;
        });
    }

    /**
     * Método que se utiliza para abrir los diferentes Fragments de la
     * aplicación sobre el mismo FrameLayout.
     * @param fragment fragmento a abrir.
     */
    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}