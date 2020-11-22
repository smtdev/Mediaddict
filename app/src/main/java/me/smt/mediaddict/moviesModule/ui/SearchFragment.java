package me.smt.mediaddict.moviesModule.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.smt.mediaddict.R;
import me.smt.mediaddict.common.model.dataAccess.TMDbRepositoryAPI;
import me.smt.mediaddict.common.utils.CommonUtils;
import me.smt.mediaddict.common.utils.Constants;
import me.smt.mediaddict.moviesModule.adapter.SearchAdapter;
import me.smt.mediaddict.moviesModule.model.Movie;
import me.smt.mediaddict.moviesModule.model.dataAccess.action.OnMoviesClickCallback;
import me.smt.mediaddict.moviesModule.model.dataAccess.action.OnSearchMovieCallback;

public class SearchFragment extends Fragment {

    @BindView(R.id.rv_fragment_search)
    RecyclerView rvFragmentSearch;
    @BindView(R.id.srl_fragment_search)
    SwipeRefreshLayout srlFragmentSearch;
    @BindView(R.id.mcl_fragment_search)
    CoordinatorLayout mclFragmentSearch;

    private TMDbRepositoryAPI mTMDbRepositoryAPI;
    private SearchAdapter searchAdapter;
    private String query = "";

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

    public SearchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {

        return new SearchFragment();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(String.format("Búsqueda: %s", query));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTMDbRepositoryAPI = TMDbRepositoryAPI.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Recoger parámetros enviados por el fragment anterior
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            query = bundle.getString("queryValue");
            Log.d("QueryValue", "La búsqueda es: " + query);
        }

        ButterKnife.bind(this, view);

        rvFragmentSearch = view.findViewById(R.id.rv_fragment_search);
        rvFragmentSearch.setHasFixedSize(true);

        initRecyclerViewAndScrolling(query);

        getSearchedMovieList(currentPage, query);
        srlFragmentSearch.setOnRefreshListener(() -> {
            initRecyclerViewAndScrolling(query);
            srlFragmentSearch.setRefreshing(false);
            srlFragmentSearch.setColorSchemeColors(
                    requireActivity().getResources().getColor(R.color.colorAccent),
                    requireActivity().getResources().getColor(R.color.text_light_blue)
            );
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(String.format("Búsqueda: %s", query));
    }

    private void initRecyclerViewAndScrolling(String queryFilter) {
        int mNoOfColumns = CommonUtils.calculateNoOfColumns(requireContext(), 140);

        final LinearLayoutManager manager = new GridLayoutManager(getContext(), mNoOfColumns);
        rvFragmentSearch.setLayoutManager(manager);
        rvFragmentSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getSearchedMovieList(currentPage + 1, queryFilter);
                    }
                }
            }
        });
    }

    private void getSearchedMovieList(int page, String query) {
        isFetchingMovies = true;
        mTMDbRepositoryAPI.searchMovies(page, query, new OnSearchMovieCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies, String query) {
                if (searchAdapter == null) {
                    searchAdapter = new SearchAdapter(movies, callback);
                    rvFragmentSearch.setAdapter(searchAdapter);
                } else {
                    if (page == 1) {
                        searchAdapter.clearMovies();
                    }
                    searchAdapter.appendResultMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {
                showError();
            }

            @Override
            public void onEmptyResult() {
                Snackbar.make(mclFragmentSearch, R.string.search_fragment_no_results_message, Snackbar.LENGTH_LONG)
                        .setAnchorView(mclFragmentSearch)
                        .show();
            }
        });
    }

    OnMoviesClickCallback callback = (movie, movieImageView) -> {
        Intent intent = new Intent(SearchFragment.this.getContext(), MovieDetailsActivity.class);
        intent.putExtra(Constants.MOVIE_ID, movie.getId());
        /*intent.putExtra(Constants.MOVIE_TITLE, movie.getTitle());
        intent.putExtra(Constants.MOVIE_BACKDROP, movie.getBackdrop());
        intent.putExtra(Constants.MOVIE_RATING, movie.getRating());
        intent.putExtra(Constants.MOVIE_OVERVIEW, movie.getOverview());
        intent.putExtra(Constants.MOVIE_POSTERPATH, movie.getPosterPath());
        intent.putExtra(Constants.MOVIE_RELEASE_DATE, movie.getReleaseDate());*/

        SearchFragment.this.startActivity(intent);
    };

    public void showError() {
        /**
         * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
         */
        Snackbar.make(mclFragmentSearch, R.string.error_message_loading_search_movie, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }

}