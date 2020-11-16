package me.sergiomartin.tvshowmovietracker.moviesModule.ui;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.common.model.dataAccess.TMDbRepositoryAPI;
import me.sergiomartin.tvshowmovietracker.common.utils.Constants;
import me.sergiomartin.tvshowmovietracker.moviesModule.adapter.MoviesAdapter;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.data.MoviesDbHelper;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.action.OnMoviesClickCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetMoviesCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeListFragment extends Fragment {

    //@BindView(R.id.home_list_indicator_tabLayout)
    //TabLayout homeIndicatorTabLayout;
    @BindView(R.id.rv_home_popular_movies_recyclerview)
    RecyclerView rvHomePopularMoviesRecyclerview;
    @BindView(R.id.rv_home_toprated_movies_recyclerview)
    RecyclerView rvHomeTopratedMoviesRecyclerview;
    @BindView(R.id.rv_home_upcoming_movies_recyclerview)
    RecyclerView rvHomeUpcomingMoviesRecyclerview;
    @BindView(R.id.btn_home_list_popular_viewall_textview)
    AppCompatButton btnHomeListPopularViewallTextview;
    @BindView(R.id.btn_home_list_toprated_viewall_textview)
    AppCompatButton btnHomeListTopratedViewallTextview;
    @BindView(R.id.btn_home_list_upcoming_viewall_textview)
    AppCompatButton btnHomeListUpcomingViewallTextview;
    @BindView(R.id.pb_home_list)
    ProgressBar pbHomeList;

    private MoviesDbHelper moviesDbHelper;
    private MoviesAdapter adapter;
    private TMDbRepositoryAPI mTMDbRepositoryAPI;

    private List<Movie> savedMovieList;

    private String sortBy;
    private boolean isFavoriteChecked = false;
    /**
     * Mediante esta variable indicamos en qué página inicializa
     * el listado extraido de la API. Cada vez que se haga scroll al 50%
     * del listado de películas, se incrementará +1
     */
    private int currentPage = 1;

    public HomeListFragment() {
        // Required empty public constructor
    }

    public static HomeListFragment newInstance() {
        return new HomeListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // para poder filtrar por el botón de búsqueda
        setHasOptionsMenu(true);

        mTMDbRepositoryAPI = TMDbRepositoryAPI.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_list, container, false);

        ButterKnife.bind(this, view);

        rvHomePopularMoviesRecyclerview = view.findViewById(R.id.rv_home_popular_movies_recyclerview);
        rvHomeTopratedMoviesRecyclerview = view.findViewById(R.id.rv_home_toprated_movies_recyclerview);
        rvHomeUpcomingMoviesRecyclerview = view.findViewById(R.id.rv_home_upcoming_movies_recyclerview);

        rvHomePopularMoviesRecyclerview.setVisibility(View.GONE);
        rvHomeTopratedMoviesRecyclerview.setVisibility(View.GONE);
        rvHomeUpcomingMoviesRecyclerview.setVisibility(View.GONE);

        initRecyclerView();
        //getMovies(currentPage, sortBy);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SearchView searchView = (SearchView) item.getActionView();

        /**
         * Info sacada de: https://guides.codepath.com/android/Extended-ActionBar-Guide#adding-searchview-to-actionbar
         */
        // Modificando el icono de búsqueda del SearchView de la AppBar
        int searchImgId = androidx.appcompat.R.id.search_button;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_baseline_search_24);

        ImageView searchClose = searchView.findViewById(R.id.search_close_btn);
        searchClose.setColorFilter(Color.WHITE);

        // Cambiando el style al SearchView
        int searchEditId = androidx.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            et.setTextColor(requireActivity().getBaseContext().getResources().getColor(R.color.colorAccent, requireActivity().getBaseContext().getTheme()));
            et.setBackgroundColor(requireActivity().getBaseContext().getResources().getColor(R.color.gray_800, requireActivity().getBaseContext().getTheme()));
        } else {
            et.setTextColor(requireActivity().getBaseContext().getResources().getColor(R.color.colorAccent));
            et.setBackgroundColor(requireActivity().getBaseContext().getResources().getColor(R.color.gray_800));
        }

        et.setHint(R.string.searchview_text);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                // Here is where we are going to implement the filter logic
                return true;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displaySortedMovieList(rvHomePopularMoviesRecyclerview);
        displaySortedMovieList(rvHomeTopratedMoviesRecyclerview);
        displaySortedMovieList(rvHomeUpcomingMoviesRecyclerview);
    }

    private void initRecyclerView() {

        // inicializando ArrayList de películas marcadas como favoritas
        savedMovieList = new ArrayList<>();

        // Configuración del RecyclerView
        rvHomePopularMoviesRecyclerview.setHasFixedSize(true);
        rvHomeTopratedMoviesRecyclerview.setHasFixedSize(true);
        rvHomeUpcomingMoviesRecyclerview.setHasFixedSize(true);
        //rvHomePopularMoviesRecyclerview.setAdapter(null);

        LinearLayoutManager managerPopular = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager managerToprated = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager managerUpcoming = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvHomePopularMoviesRecyclerview.setLayoutManager(managerPopular);
        rvHomeTopratedMoviesRecyclerview.setLayoutManager(managerToprated);
        rvHomeUpcomingMoviesRecyclerview.setLayoutManager(managerUpcoming);
    }

    private void getHomeMovies(int page, String sortBy, RecyclerView rv) {
        currentPage = 1;
        mTMDbRepositoryAPI.getMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                Log.d("FragmentHome-getMovies", "Current Page = " + page);
                adapter = new MoviesAdapter(movies, callback);
                rv.setAdapter(adapter);
                adapter.appendMovies(movies);
                currentPage = page;
                pbHomeList.setVisibility(View.GONE);
                rvHomePopularMoviesRecyclerview.setVisibility(View.VISIBLE);
                rvHomeTopratedMoviesRecyclerview.setVisibility(View.VISIBLE);
                rvHomeUpcomingMoviesRecyclerview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback callback = (movie, moviePosterImageView) -> {
        isFavoriteChecked = false;
        /*
         * Enviar información entre activities y fragments para manejarla y mostrarla
         */
        Intent intent = new Intent(HomeListFragment.this.getContext(), MovieDetailsActivity.class);

        /*
         * Recuperando películas guardadas para saber si ya están marcadas como favoritas
         * y mantener marcado el fab de MovieDetailsActivity
         */
        getFavoriteMovies();

        for(Movie savedMovie : savedMovieList) {
            if(savedMovie.getId() == movie.getId()) {
                isFavoriteChecked = true;
            }
        }

        intent.putExtra(Constants.MOVIE_ID, movie.getId());
        intent.putExtra(Constants.MOVIE_TITLE, movie.getTitle());
        intent.putExtra(Constants.MOVIE_THUMBNAIL, movie.getBackdrop());
        intent.putExtra(Constants.MOVIE_RATING, movie.getRating());
        intent.putExtra(Constants.MOVIE_SUMMARY, movie.getOverview());
        intent.putExtra(Constants.MOVIE_POSTERPATH, movie.getPosterPath());
        intent.putExtra("movie_favorite_status", isFavoriteChecked);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                HomeListFragment.this.getActivity(),
                moviePosterImageView,
                "fromHomeToMovieDetails"
        );
        HomeListFragment.this.startActivity(intent, options.toBundle());
    };


    @SuppressLint("NonConstantResourceId")
    // se añade ya que los resource ids no son finals desde Gradle 5.0 y hay que evitar usarlo en switchs
    private void displaySortedMovieList(RecyclerView rv) {
        currentPage = 1; // Volvemos al inicio cada vez que entremos en una de las listas

        switch (rv.getId()) {
            case R.id.rv_home_popular_movies_recyclerview:
                sortBy = Constants.POPULAR;
                break;
            case R.id.rv_home_toprated_movies_recyclerview:
                sortBy = Constants.TOP_RATED;
                break;
            case R.id.rv_home_upcoming_movies_recyclerview:
                sortBy = Constants.UPCOMING;
                break;
            /**
             * Cambiar vista entre lista y póster
             * https://stackoverflow.com/questions/45456601/switch-between-layouts-on-list-to-grid-recyclerview
             */
            /*case R.id.switch_view:
                //getActivity().invalidateOptionsMenu();
                boolean isSwitched = adapter.toggleItemViewType();
                //mRecyclerView.setLayoutManager(isSwitched ? new LinearLayoutManager(this) : new GridLayoutManager(this, 2));
                adapter.notifyDataSetChanged();
                return true;*/
        }
        getHomeMovies(currentPage, sortBy, rv);
    }

    public void showError() {
        /**
         * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
         */
        Snackbar.make(getActivity().findViewById(android.R.id.content), "Error 1", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }

    @OnClick({R.id.btn_home_list_popular_viewall_textview, R.id.btn_home_list_toprated_viewall_textview, R.id.btn_home_list_upcoming_viewall_textview})
    public void onClick(View view) {
        String sortFilter = null;
        // Enviar parámetros para que reciba la info el nuevo fragment
        // https://stackoverflow.com/a/40949016/1552146
        Bundle bundle = new Bundle();

        MovieListFragment movieList = new MovieListFragment();
        movieList.setArguments(bundle);
        switch (view.getId()) {
            case R.id.btn_home_list_popular_viewall_textview:
                sortFilter = Constants.POPULAR;
                break;
            case R.id.btn_home_list_toprated_viewall_textview:
                sortFilter = Constants.TOP_RATED;
                break;
            case R.id.btn_home_list_upcoming_viewall_textview:
                sortFilter = Constants.UPCOMING;
                break;
        }
        bundle.putString("movieFilter", sortFilter);

        getActivity().getSupportFragmentManager().beginTransaction()
                // ((ViewGroup)getView().getParent()).getId() -> es el id del fragment actual
                .replace(((ViewGroup) getView().getParent()).getId(), movieList, "FragmentMovieListFiltered")
                .addToBackStack(null)
                .commit();
    }

    /*
     * Recuperar en un nuevo hilo la lista de películas almacenadas en la SQLite
     */
    private void getFavoriteMovies() {
        moviesDbHelper = new MoviesDbHelper(requireActivity().getApplicationContext());

        savedMovieList.clear();
        savedMovieList.addAll(moviesDbHelper.getSavedMovies());
    }
}
