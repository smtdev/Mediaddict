package me.sergiomartin.tvshowmovietracker.moviesModule.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.moviesModule.adapter.MoviesAdapter;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.data.MoviesDbHelper;

public class FavoritesFragment extends Fragment {

    @BindView(R.id.rv_fragment_favorites_list)
    RecyclerView rvFragmentFavoritesList;
    @BindView(R.id.srl_fragment_favorites_list)
    SwipeRefreshLayout srlFragmentFavoritesList;
    @BindView(R.id.mcl_fragment_favorites_list)
    CoordinatorLayout mclFragmentFavoritesList;

    private MoviesDbHelper moviesDbHelper;
    private MoviesAdapter adapter;
    private List<Movie> movieList;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        ButterKnife.bind(this, view);

        rvFragmentFavoritesList = view.findViewById(R.id.rv_fragment_favorites_list);
        rvFragmentFavoritesList.setHasFixedSize(true);

        rvFragmentFavoritesList.setLayoutManager(new LinearLayoutManager(getContext()));

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
        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(movieList, requireContext());

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvFragmentFavoritesList.setLayoutManager(manager);
        rvFragmentFavoritesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        moviesDbHelper = new MoviesDbHelper(requireContext());
        getFavoriteMovies();
    }


    /*
     * Recuperar en un nuevo hilo la lista de
     * películas guardadas en la SQLite para mostrarlas en el RecyclerView
     */
    private void getFavoriteMovies() {

        new Thread(() -> {
            movieList.clear();
            movieList.addAll(moviesDbHelper.getSavedMovies());
            requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
    }
}