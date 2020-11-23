package me.smt.mediaddict.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.smt.mediaddict.R;
import me.smt.mediaddict.common.CommonUtils;
import me.smt.mediaddict.common.Constants;
import me.smt.mediaddict.common.FirebaseUtils;
import me.smt.mediaddict.adapter.MoviesAdapter;
import me.smt.mediaddict.model.Movie;
import me.smt.mediaddict.model.dataAccess.action.OnMoviesClickCallback;

/**
 * Clase que se encarga de mostrar las películas marcadas
 * como favoritos durante la interacción con la aplicación.
 * @author Sergio Martín Teruel
 * @version 1.0
 * @see Fragment
 **/
public class FavoritesFragment extends Fragment {

    @BindView(R.id.rv_fragment_favorites_list)
    RecyclerView rvFragmentFavoritesList;
    @BindView(R.id.srl_fragment_favorites_list)
    SwipeRefreshLayout srlFragmentFavoritesList;
    @BindView(R.id.mcl_fragment_favorites_list)
    CoordinatorLayout mclFragmentFavoritesList;
    @BindView(R.id.pb_favorites_fragment)
    ProgressBar pbFavoritesFragment;

    //private MoviesDbHelper moviesDbHelper;
    /**
     * Atributo que instancia un adapter.
     */
    private MoviesAdapter adapter;

    /**
     * Atributo para almacenar la lista de las películas favoritas.
     */
    private List<Movie> savedMovieList;

    /**
     * Atributo para indicar la orientación del layout.
     */
    private int viewLayoutType;

    /**
     * Constructor de la clase.
     */
    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Método para retornar un nuevo Fragment de tipo FavoritesFragment.
     * @return fragment devuelto.
     */
    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    /**
     * Método llamado al iniciar la creación del Fragment.
     * @param savedInstanceState estado de la instancia.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        viewLayoutType = Constants.GRID_ITEM;

        rvFragmentFavoritesList = view.findViewById(R.id.rv_fragment_favorites_list);
        rvFragmentFavoritesList.setHasFixedSize(true);

        initRecyclerViewAndScrolling();

        srlFragmentFavoritesList.setOnRefreshListener(() -> {
            savedMovieList.clear();
            initRecyclerViewAndScrolling();
            srlFragmentFavoritesList.setRefreshing(false);
            srlFragmentFavoritesList.setColorSchemeColors(
                    requireActivity().getResources().getColor(R.color.colorAccent),
                    requireActivity().getResources().getColor(R.color.text_light_blue)
            );
        });

        return view;
    }

    /**
     * Método para inicializar el RecyclerView
     */
    private void initRecyclerViewAndScrolling() {
        savedMovieList = new ArrayList<>();

        int mNoOfColumns = CommonUtils.calculateNoOfColumns(requireContext(), Constants.GRID_COLUMN_SIZE);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), mNoOfColumns);

        adapter = new MoviesAdapter(savedMovieList, requireContext(), callback, viewLayoutType);
        rvFragmentFavoritesList.setLayoutManager(manager);
        rvFragmentFavoritesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

       // moviesDbHelper = new MoviesDbHelper(requireContext());

        FirebaseUtils.fetchFavoriteMoviesFromUser(savedMovieList, adapter);
        pbFavoritesFragment.setVisibility(View.GONE);
        //getFavoriteMovies();
    }

    /**
     * Método que se llama cuando la Activity es visible para el usuario y está activa.
     */
    @Override
    public void onResume() {
        super.onResume();
        savedMovieList.clear();
        initRecyclerViewAndScrolling();
    }

    /**
     * Método que se llama cuando el Fragment ya no está en uso.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        savedMovieList.clear();
    }

    /**
     * Método que se llama cuando el Fragment no va a volver a abrirse más.
     */
    @Override
    public void onStop() {
        super.onStop();
        savedMovieList.clear();
    }
    /**
     * Método para inicializar el contenido del menú de opciones estándar.
     * @param menu menú referenciado.
     * @param inflater vista donde instanciar el menú.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.switchview_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_switchview);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    /**
     * Método que salta al seleccionar algún item del menú de opciones.
     * @param item item en concreto.
     * @return respuesta.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_bar_switchview) {
            int mNoOfColumns = CommonUtils.calculateNoOfColumns(requireContext(), Constants.GRID_COLUMN_SIZE);

            if (viewLayoutType == Constants.LIST_ITEM) {
                final GridLayoutManager manager = new GridLayoutManager(getContext(), mNoOfColumns);
                adapter = new MoviesAdapter(savedMovieList, requireContext(), callback, Constants.GRID_ITEM);
                viewLayoutType = Constants.GRID_ITEM;
                rvFragmentFavoritesList.setLayoutManager(manager);
                item.setIcon(R.drawable.ic_list_layout_format);
            } else {
                final LinearLayoutManager manager;
                adapter = new MoviesAdapter(savedMovieList, requireContext(), callback, Constants.LIST_ITEM);
                manager = new LinearLayoutManager(getContext());
                viewLayoutType = Constants.LIST_ITEM;
                rvFragmentFavoritesList.setLayoutManager(manager);
                item.setIcon(R.drawable.ic_grid_layout_format);
            }

            rvFragmentFavoritesList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método para abrir los detalles de una película al hacer clic sobre ella.
     */
    OnMoviesClickCallback callback = (movie, moviePosterImageView) -> {
        //boolean isFavoriteChecked = false;

        // Enviar información entre activities y fragments para manejarla y mostrarla
        Intent intent = new Intent(FavoritesFragment.this.getContext(), MovieDetailsActivity.class);

        /*
         * Recuperando películas guardadas para saber si ya están marcadas como favoritas
         * y mantener marcado el fab de MovieDetailsActivity
         */
        /*for(Movie savedMovie : savedMovieList) {
            if(savedMovie.getId() == movie.getId()) {

                Log.d("FavoritesFragment", movie.getTitle() + " está en favoritos. Estado isFavoriteChecked: " + isFavoriteChecked + ", savedMovie ID: " + savedMovie.getId() + ", movie ID: " + movie.getId());
                isFavoriteChecked = true;
            }
        }*/

        intent.putExtra(Constants.MOVIE_ID, movie.getId());
        /*intent.putExtra(Constants.MOVIE_TITLE, movie.getTitle());
        intent.putExtra(Constants.MOVIE_BACKDROP, movie.getBackdrop());
        intent.putExtra(Constants.MOVIE_RATING, movie.getRating());
        intent.putExtra(Constants.MOVIE_OVERVIEW, movie.getOverview());
        intent.putExtra(Constants.MOVIE_POSTERPATH, movie.getPosterPath());
        intent.putExtra(Constants.MOVIE_RELEASE_DATE, movie.getReleaseDate());
        intent.putExtra(Constants.MOVIE_GENRES_ID, movie.getGenreIdString());

        Log.d("GenreOnFragment", movie.getGenreIdString());
        intent.putExtra("movie_favorite_status", isFavoriteChecked);*/

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                FavoritesFragment.this.getActivity(),
                moviePosterImageView,
                "fromFavoritesFragmentToMovieDetails"
        );
        FavoritesFragment.this.startActivity(intent, options.toBundle());
    };

    /*private void getFavoriteMovies() {

        new Thread(() -> {
            Log.d("FavoritesFragment", "Recuperando películas guardadas");
            savedMovieList.clear();
            savedMovieList.addAll(moviesDbHelper.getSavedMovies());
            requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
    }*/
}