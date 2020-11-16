package me.sergiomartin.tvshowmovietracker.moviesModule.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sergiomartin.tvshowmovietracker.BuildConfig;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.common.utils.Constants;
import me.sergiomartin.tvshowmovietracker.moviesModule.adapter.MoviesAdapter;

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

    // MENUUUUUU

    /**
     * Métodos para ordenar en base a un item de menú
     * No utilizado actualmente
     */
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_filter:
                showSortMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.app_bar_filter));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // Every time we sort, we need to go back to page 1

                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy = Constants.POPULAR;
                        //getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = Constants.TOP_RATED;
                        //getMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        sortBy = Constants.UPCOMING;
                        //getMovies(currentPage);
                        return true;

                     // Cambiar vista entre lista y póster
                     // https://stackoverflow.com/questions/45456601/switch-between-layouts-on-list-to-grid-recyclerview

                    case R.id.switch_view:
                        supportInvalidateOptionsMenu();
                        boolean isSwitched = adapter.toggleItemViewType();
                        //mRecyclerView.setLayoutManager(isSwitched ? new LinearLayoutManager(this) : new GridLayoutManager(this, 2));
                        adapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }
        });

        sortMenu.inflate(R.menu.movies_sort_menu);
        sortMenu.show();
    }*/


    /*private void showError() {
        Snackbar.make(mainConstraintLayout, R.string.error_network_message, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }*/


}