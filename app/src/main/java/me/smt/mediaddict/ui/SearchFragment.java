package me.smt.mediaddict.ui;

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
import me.smt.mediaddict.model.data.TMDbRepositoryAPI;
import me.smt.mediaddict.common.CommonUtils;
import me.smt.mediaddict.common.Constants;
import me.smt.mediaddict.adapter.SearchAdapter;
import me.smt.mediaddict.model.Movie;
import me.smt.mediaddict.model.dataAccess.action.OnMoviesClickCallback;
import me.smt.mediaddict.model.dataAccess.action.OnSearchMovieCallback;

/**
 * Clase que se encarga de controlar la funcionalidad
 * de la búsqueda a través del botón de búsqueda.
 * @author Sergio Martín Teruel
 * @version 1.0
 * @see Fragment
 **/
public class SearchFragment extends Fragment {

    @BindView(R.id.rv_fragment_search)
    RecyclerView rvFragmentSearch;
    @BindView(R.id.srl_fragment_search)
    SwipeRefreshLayout srlFragmentSearch;
    @BindView(R.id.mcl_fragment_search)
    CoordinatorLayout mclFragmentSearch;

    /**
     * Atributo que crea una instancia de la clase gestora de API.
     */
    private TMDbRepositoryAPI mTMDbRepositoryAPI;

    /**
     * Atributo que crea una instancia del adapter.
     */
    private SearchAdapter searchAdapter;

    /**
     * Atributo que se refiere a la búsqueda a realizar.
     */
    private String query = "";

    /**
     * Atributo que determina si está cerca la siguiente página de la API.
     * Se utiliza para evitar duplicidad y mostrar siempre las mismas películas
     * al hacer scroll.
     */
    private boolean isFetchingMovies;
    /**
     * Atributo mediante el cual indicamos en qué página inicializa
     * el listado extraido de la API. Cada vez que se haga scroll al 50%
     * del listado de películas, se incrementará +1.
     */
    private int currentPage = 1;

    /**
     * Constructor de la clase.
     */
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Método para retornar un nuevo Fragment de tipo SearchFragment.
     * @return fragment devuelto.
     */
    public static SearchFragment newInstance() {

        return new SearchFragment();
    }


    /**
     * Método que se llama cuando el Fragment no va a volver a abrirse más.
     */
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    /**
     * Método que se llama cuando el Fragment es visible para el usuario y está activo.
     */
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(String.format("Búsqueda: %s", query));
    }

    /**
     * Método que se llama cuando el Fragment ya no está en uso.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    /**
     * Método llamado al iniciar la creación del Fragment.
     * @param savedInstanceState estado de la instancia.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTMDbRepositoryAPI = TMDbRepositoryAPI.getInstance();
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

    /**
     * Método llamado inmediatamente después de que OnCreateView termine pero
     * antes de que se haya restaurado cualquier estado a la vista.
     * @param view vista.
     * @param savedInstanceState estado de la instancia.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(String.format("Búsqueda: %s", query));
    }

    /**
     * Método para instanciar el RecyclerView, cargando los datos de las
     * películas encontradas en la búsqueda.
     * @param queryFilter búsqueda realizada.
     */
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

    /**
     * Método mediante el cual se comunica la aplicación y la API esperando
     * una respuesta del servidor y actuando en consecuencia.
     * @param page página activa.
     * @param query búsqueda enviada.
     */
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

    /**
     * Método para abrir los detalles de una película al hacer clic sobre ella.
     */
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

    /**
     * Método para mostrar un Snackbar al aparecer algún error.
     * Más info: https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
     */
    public void showError() {
        Snackbar.make(mclFragmentSearch, R.string.error_message_loading_search_movie, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }
}