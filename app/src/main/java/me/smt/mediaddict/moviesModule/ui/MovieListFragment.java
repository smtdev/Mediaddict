package me.smt.mediaddict.moviesModule.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.smt.mediaddict.R;
import me.smt.mediaddict.common.model.dataAccess.TMDbRepositoryAPI;
import me.smt.mediaddict.common.utils.Constants;
import me.smt.mediaddict.moviesModule.adapter.MoviesAdapter;
import me.smt.mediaddict.moviesModule.model.Genre;
import me.smt.mediaddict.moviesModule.model.Movie;
import me.smt.mediaddict.moviesModule.model.dataAccess.action.OnMoviesClickCallback;
import me.smt.mediaddict.moviesModule.model.dataAccess.get.OnGetGenresCallback;
import me.smt.mediaddict.moviesModule.model.dataAccess.get.OnGetMoviesCallback;

public class MovieListFragment extends Fragment {

    @BindView(R.id.srl_fragment_movie_list)
    SwipeRefreshLayout srlFragmentMovieList;
    @BindView(R.id.rv_fragment_movie_list)
    RecyclerView rvFragmentMovieList;

    private MoviesAdapter adapter;
    private TMDbRepositoryAPI mTMDbRepositoryAPI;

    private List<Genre> movieGenres;
    private String sortBy = Constants.POPULAR;

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

    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTMDbRepositoryAPI = TMDbRepositoryAPI.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        /*
         * Recoger parámetros enviados por el fragment anterior
         * https://developer.android.com/training/basics/fragments/pass-data-between
         */
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            sortBy = bundle.getString("movieFilter");
        }

        ButterKnife.bind(this, view);

        rvFragmentMovieList = view.findViewById(R.id.rv_fragment_movie_list);
        rvFragmentMovieList.setHasFixedSize(true);

        rvFragmentMovieList.setLayoutManager(new LinearLayoutManager(getContext()));

        initRecyclerViewAndScrolling(sortBy);

        getGenres(sortBy);

        srlFragmentMovieList.setOnRefreshListener(() -> {
            initRecyclerViewAndScrolling(sortBy);
            srlFragmentMovieList.setRefreshing(false);
            srlFragmentMovieList.setColorSchemeColors(
                    requireActivity().getResources().getColor(R.color.colorAccent),
                    requireActivity().getResources().getColor(R.color.text_light_blue)
            );
        });
        // Inflar el diseño del fragment
        return view;
    }

    private void initRecyclerViewAndScrolling(String sortByFilter) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvFragmentMovieList.setLayoutManager(manager);
        rvFragmentMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                Log.d("DentroDelOnScrolledML", "dentro del onScrolled");

                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1, sortByFilter);
                    }
                }
            }
        });
    }

    private void getGenres(String sortByFilter) {
        mTMDbRepositoryAPI.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getMovies(currentPage, sortByFilter);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getMovies(int page, String sortByFilter) {
        isFetchingMovies = true;
        mTMDbRepositoryAPI.getMovies(page, sortByFilter, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                Log.d("MovieListFragment", "Current Page = " + page);
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, movieGenres, callback);
                    rvFragmentMovieList.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearMovies();
                    }
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback callback = (movie, movieImageView) -> {
        Intent intent = new Intent(MovieListFragment.this.getContext(), MovieDetailsActivity.class);

        intent.putExtra(Constants.MOVIE_ID, movie.getId());
        /*intent.putExtra(Constants.MOVIE_TITLE, movie.getTitle());
        intent.putExtra(Constants.MOVIE_BACKDROP, movie.getBackdrop());
        intent.putExtra(Constants.MOVIE_RATING, movie.getRating());
        intent.putExtra(Constants.MOVIE_OVERVIEW, movie.getOverview());
        intent.putExtra(Constants.MOVIE_POSTERPATH, movie.getPosterPath());
        intent.putExtra(Constants.MOVIE_RELEASE_DATE, movie.getReleaseDate());
        intent.putExtra(Constants.MOVIE_GENRES_ID, TextUtils.join(",", movie.getGenreIds()));*/

        MovieListFragment.this.startActivity(intent);
    };

    public void showError() {
        /*
         * Context from: https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
         */
        Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.error_message_loading_movies_panel, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }
}