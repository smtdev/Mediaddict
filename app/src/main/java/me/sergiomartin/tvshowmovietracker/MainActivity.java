package me.sergiomartin.tvshowmovietracker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sergiomartin.tvshowmovietracker.moviesModule.fragments.FragmentHomeList;
import me.sergiomartin.tvshowmovietracker.moviesModule.fragments.FragmentMovieList;
import me.sergiomartin.tvshowmovietracker.moviesModule.fragments.FragmentShowList;
import me.sergiomartin.tvshowmovietracker.common.model.dataAccess.TMDbRepositoryAPI;
import me.sergiomartin.tvshowmovietracker.moviesModule.adapter.MoviesAdapter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private MoviesAdapter adapter;
    private String sortBy = TMDbRepositoryAPI.POPULAR;

    /**
     * Determina si está cerca la siguiente página de la API.
     * Se utiliza para evitar duplicidad y mostrar siempre
     * las mismas películas al hacer scroll
     */
    private boolean isFetchingMovies;
    /**
     * Mediante esta variable indicamos en qué página inicializa
     * el listado extraido de la API. Cada vez que se haga scroll al 50%
     * del listado de películas, se incrementará +1
     */
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //mainToolbar.setNavigationIcon(R.drawable.ic_back);
        mainToolbar.setLogo(R.drawable.ic_launcher_menu);

        openFragment(new FragmentHomeList());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.app_bar_home:
                    openFragment(new FragmentHomeList());
                    return true;

                case R.id.app_bar_tvshow:
                    openFragment(new FragmentShowList());
                    return true;

                case R.id.app_bar_movie:
                    openFragment(new FragmentMovieList());
                    return true;

                case R.id.app_bar_history:
                    //openFragment(new AccountFragment());
                    return true;

                case R.id.app_bar_profile:
                    //openFragment(new SettingsFragment());
                    return true;
            }
            return false;
        });
    }

    void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Añade los menús disponibles al Toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        // Crea barra de búsqueda
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        if (searchItem != null) {
            final SearchView searchView = (SearchView) searchItem.getActionView();

            /**
             * Info sacada de: https://guides.codepath.com/android/Extended-ActionBar-Guide#adding-searchview-to-actionbar
             */
            // Use a custom search icon for the SearchView in AppBar
            int searchImgId = androidx.appcompat.R.id.search_button;
            ImageView v = (ImageView) searchView.findViewById(searchImgId);
            v.setImageResource(R.drawable.ic_baseline_search_24);

            // Customize searchview text and hint colors
            int searchEditId = androidx.appcompat.R.id.search_src_text;
            EditText et = (EditText) searchView.findViewById(searchEditId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                et.setTextColor(getBaseContext().getResources().getColor(R.color.colorAccent, getBaseContext().getTheme()));
                et.setBackgroundColor(getBaseContext().getResources().getColor(R.color.gray_800, getBaseContext().getTheme()));
            } else {
                et.setTextColor(getBaseContext().getResources().getColor(R.color.colorAccent));
                et.setBackgroundColor(getBaseContext().getResources().getColor(R.color.gray_800));
            }

            et.setHint(R.string.searchview_text);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // use this method for auto complete search process
                    adapter.getFilter().filter(newText);
                    return true;
                }
            });
            //search(searchView);
        }
        return true;
    }

    @Override
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
                /*
                 * Every time we sort, we need to go back to page 1
                 */
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy = TMDbRepositoryAPI.POPULAR;
                        //getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = TMDbRepositoryAPI.TOP_RATED;
                        //getMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        sortBy = TMDbRepositoryAPI.UPCOMING;
                        //getMovies(currentPage);
                        return true;
                    /**
                     * Cambiar vista entre lista y póster
                     * https://stackoverflow.com/questions/45456601/switch-between-layouts-on-list-to-grid-recyclerview
                     */
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
    }

}