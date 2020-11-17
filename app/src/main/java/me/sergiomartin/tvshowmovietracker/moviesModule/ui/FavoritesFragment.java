package me.sergiomartin.tvshowmovietracker.moviesModule.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.common.utils.CommonUtils;
import me.sergiomartin.tvshowmovietracker.common.utils.Constants;
import me.sergiomartin.tvshowmovietracker.moviesModule.adapter.MoviesAdapter;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.data.MoviesDbHelper;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.action.OnMoviesClickCallback;

public class FavoritesFragment extends Fragment {

    @BindView(R.id.rv_fragment_favorites_list)
    RecyclerView rvFragmentFavoritesList;
    @BindView(R.id.srl_fragment_favorites_list)
    SwipeRefreshLayout srlFragmentFavoritesList;
    @BindView(R.id.mcl_fragment_favorites_list)
    CoordinatorLayout mclFragmentFavoritesList;

    private MoviesDbHelper moviesDbHelper;
    private MoviesAdapter adapter;
    private List<Movie> savedMovieList;

    private boolean isFavoriteChecked = false;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        ButterKnife.bind(this, view);

        rvFragmentFavoritesList = view.findViewById(R.id.rv_fragment_favorites_list);
        rvFragmentFavoritesList.setHasFixedSize(true);

        initRecyclerViewAndScrolling();

        srlFragmentFavoritesList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRecyclerViewAndScrolling();
                srlFragmentFavoritesList.setRefreshing(false);
                srlFragmentFavoritesList.setColorSchemeColors(
                        getActivity().getResources().getColor(R.color.colorAccent),
                        getActivity().getResources().getColor(R.color.text_light_blue)
                );
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void initRecyclerViewAndScrolling() {
        int mNoOfColumns = CommonUtils.calculateNoOfColumns(requireContext(), 140);

        savedMovieList = new ArrayList<>();
        adapter = new MoviesAdapter(savedMovieList, requireContext(), callback);

        final LinearLayoutManager manager = new GridLayoutManager(getContext(), mNoOfColumns);
        rvFragmentFavoritesList.setLayoutManager(manager);
        rvFragmentFavoritesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        moviesDbHelper = new MoviesDbHelper(requireContext());
        getFavoriteMovies();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.switchview_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_switchview);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int mNoOfColumns = CommonUtils.calculateNoOfColumns(requireContext(), 140);

        switch (item.getItemId()) {
            case R.id.app_bar_switchview:
                requireActivity().invalidateOptionsMenu();

                //boolean isSwitched = adapter.toggleItemViewType();

                if (Constants.view == Constants.LIST_ITEM) {
                    rvFragmentFavoritesList.setLayoutManager(new LinearLayoutManager(requireContext()));
                } else {
                    rvFragmentFavoritesList.setLayoutManager(new GridLayoutManager(requireContext(), mNoOfColumns));
                }

                initRecyclerViewAndScrolling();

                /*if (isSwitched) {
                    rvFragmentFavoritesList.setLayoutManager(new LinearLayoutManager(requireContext()));
                } else {
                    rvFragmentFavoritesList.setLayoutManager(new GridLayoutManager(requireContext(), mNoOfColumns));
                }
                adapter.notifyDataSetChanged();*/
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
     * Manejar el clic a la película para que muestre detalles
     */
    OnMoviesClickCallback callback = (movie, moviePosterImageView) -> {
        isFavoriteChecked = false;

        // Enviar información entre activities y fragments para manejarla y mostrarla
        Intent intent = new Intent(FavoritesFragment.this.getContext(), MovieDetailsActivity.class);

        /*
         * Recuperando películas guardadas para saber si ya están marcadas como favoritas
         * y mantener marcado el fab de MovieDetailsActivity
         */
        Log.d("FavoritesFragment", "Favoritos antes de comprobar pelis guardadas: " + isFavoriteChecked);

        for(Movie savedMovie : savedMovieList) {
            if(savedMovie.getId() == movie.getId()) {

                Log.d("FavoritesFragment", movie.getTitle() + " está en favoritos. Estado isFavoriteChecked: " + isFavoriteChecked + ", savedMovie ID: " + savedMovie.getId() + ", movie ID: " + movie.getId());
                isFavoriteChecked = true;
            }
        }

        Log.d("FavoritesFragment", "Favoritos después de comprobar pelis guardadas: " + isFavoriteChecked);
        intent.putExtra(Constants.MOVIE_ID, movie.getId());
        intent.putExtra(Constants.MOVIE_TITLE, movie.getTitle());
        intent.putExtra(Constants.MOVIE_THUMBNAIL, movie.getBackdrop());
        intent.putExtra(Constants.MOVIE_RATING, movie.getRating());
        intent.putExtra(Constants.MOVIE_SUMMARY, movie.getOverview());
        intent.putExtra(Constants.MOVIE_POSTERPATH, movie.getPosterPath());
        intent.putExtra("movie_favorite_status", isFavoriteChecked);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                FavoritesFragment.this.getActivity(),
                moviePosterImageView,
                "fromFavoritesFragmentToMovieDetails"
        );
        FavoritesFragment.this.startActivity(intent, options.toBundle());
    };

    private void getFavoriteMovies() {

        new Thread(() -> {
            Log.d("FavoritesFragment", "Recuperando películas guardadas");
            savedMovieList.clear();
            savedMovieList.addAll(moviesDbHelper.getSavedMovies());
            requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
    }
}