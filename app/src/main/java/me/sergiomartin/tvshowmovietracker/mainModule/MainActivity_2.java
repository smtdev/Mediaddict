package me.sergiomartin.tvshowmovietracker.mainModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.common.model.dataAccess.TMDbRepositoryAPI;
import me.sergiomartin.tvshowmovietracker.moviesModule.adapter.MoviesAdapter;
import me.sergiomartin.tvshowmovietracker.moviesModule.fragments.FragmentMovieDetails;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Genre;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetGenresCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetMoviesCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnMoviesClickCallback;

public class MainActivity_2 extends AppCompatActivity {

    @BindView(R.id.movies_list)
    RecyclerView moviesList;
    @BindView(R.id.mainCoordinatorLayout)
    CoordinatorLayout mainCoordinatorLayout;
    @BindView(R.id.fab_app_bar_search)
    FloatingActionButton fabAppBarSearch;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    //@BindView(R.id.bottom_app_bar)
    //BottomAppBar bottomAppBarSearch;

    private MoviesAdapter adapter;
    private TMDbRepositoryAPI tmDbRepositoryAPI;
    private List<Genre> movieGenres;
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
        setContentView(R.layout.activity_main_2);
        ButterKnife.bind(this);

        moviesList.setLayoutManager(new LinearLayoutManager(this));

        tmDbRepositoryAPI = TMDbRepositoryAPI.getInstance();

        setupOnScrollListener();
        getGenres();

        //setSupportActionBar(mainToolbar);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (item -> {
                    switch (item.getItemId()) {
                        case R.id.app_bar_movie:
                            Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_LONG).show();
                            break;
                    }
                    return false;
                });

        //setSupportActionBar(bottomAppBarSearch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Añade los menús disponibles al Toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        //inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Buscar...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        //search(searchView);
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
                        getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = TMDbRepositoryAPI.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        sortBy = TMDbRepositoryAPI.UPCOMING;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });

        sortMenu.inflate(R.menu.movies_sort_menu);
        sortMenu.show();
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        moviesList.setLayoutManager(manager);
        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    private void getGenres() {
        tmDbRepositoryAPI.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getMovies(int page) {
        isFetchingMovies = true;
        tmDbRepositoryAPI.getMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                Log.d("MainActivity activity", "Current Page = " + page);
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, movieGenres, callback);
                    moviesList.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearMovies();
                    }
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;

                setTitle();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(MainActivity_2.this, FragmentMovieDetails.class);
            intent.putExtra(FragmentMovieDetails.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };

    private void setTitle() {
        switch (sortBy) {
            case TMDbRepositoryAPI.POPULAR:
                setTitle(getString(R.string.toolbar_menu_search_category_popular));
                break;
            case TMDbRepositoryAPI.TOP_RATED:
                setTitle(getString(R.string.toolbar_menu_search_category_toprated));
                break;
            case TMDbRepositoryAPI.UPCOMING:
                setTitle(getString(R.string.toolbar_menu_search_category_upcoming));
                break;
        }
    }

    public void showError() {
        Snackbar.make(mainCoordinatorLayout, R.string.error_message_loading_movies, Snackbar.LENGTH_LONG)
                .setAnchorView(fabAppBarSearch)
                .show();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottomappbar_menu, menu);
        return true;
    }*/

}