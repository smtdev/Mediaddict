package me.smt.mediaddict.moviesModule.ui;

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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.mainConstraintLayout)
    ConstraintLayout mainConstraintLayout;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

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

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}