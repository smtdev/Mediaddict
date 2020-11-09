package me.sergiomartin.tvshowmovietracker.moviesModule.ui;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.common.model.dataAccess.TMDbRepositoryAPI;
import me.sergiomartin.tvshowmovietracker.common.utils.Constants;
import me.sergiomartin.tvshowmovietracker.moviesModule.adapter.MoviesAdapter;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.action.OnMoviesClickCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetMoviesCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHomeList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeList extends Fragment {

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

    private MoviesAdapter adapter;
    private TMDbRepositoryAPI mTMDbRepositoryAPI;

    private List<Movie> movieList;

    private String sortBy;

    /**
     * Mediante esta variable indicamos en qué página inicializa
     * el listado extraido de la API. Cada vez que se haga scroll al 50%
     * del listado de películas, se incrementará +1
     */
    private int currentPage = 1;

    public FragmentHomeList() {
        // Required empty public constructor
    }

    public static FragmentHomeList newInstance() {
        return new FragmentHomeList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        initRecyclerView();
        //getMovies(currentPage, sortBy);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displaySortedMovieList(rvHomePopularMoviesRecyclerview);
        displaySortedMovieList(rvHomeTopratedMoviesRecyclerview);
        displaySortedMovieList(rvHomeUpcomingMoviesRecyclerview);
    }

    private void initRecyclerView() {
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
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie, ImageView moviePosterImageView) {
            Intent intent = new Intent(FragmentHomeList.this.getContext(), MovieDetailsActivity.class);
            intent.putExtra(Constants.MOVIE_ID, movie.getId());
            // se puede utilizar para mostrar el título de la película en un toolbar, por ejemplo
            //intent.putExtra("movieTitle", movie.getTitle());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    FragmentHomeList.this.getActivity(),
                    moviePosterImageView,
                    "fromHomeToMovieDetails"
            );
            FragmentHomeList.this.startActivity(intent, options.toBundle());
        }
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

        FragmentMovieList movieList = new FragmentMovieList();
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
}