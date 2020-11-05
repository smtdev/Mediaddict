package me.sergiomartin.tvshowmovietracker.moviesModule.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sergiomartin.tvshowmovietracker.MovieDetailsActivity;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.common.model.dataAccess.TMDbRepositoryAPI;
import me.sergiomartin.tvshowmovietracker.moviesModule.adapter.MoviesAdapter;
import me.sergiomartin.tvshowmovietracker.moviesModule.adapter.SlidePagerAdapter;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Slide;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetMoviesCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnMoviesClickCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHomeList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeList extends Fragment {

    @BindView(R.id.home_slide_viewpager)
    ViewPager homeSlideViewpager;
    @BindView(R.id.home_indicator_tabLayout)
    TabLayout homeIndicatorTabLayout;
    @BindView(R.id.home_popular_movies_recyclerview)
    RecyclerView homePopularMoviesRecyclerview;

    private MoviesAdapter adapter;
    private TMDbRepositoryAPI mTMDbRepositoryAPI;

    private List<Slide> slideList;
    private List<Movie> movieList;

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

        // Prepare a list of slides
        slideList = new ArrayList<>();
        slideList.add(new Slide(R.drawable.strangerthings, "Stranger Things"));
        slideList.add(new Slide(R.drawable.twd, "The Walking Dead"));
        slideList.add(new Slide(R.drawable.serie1, "title"));

        SlidePagerAdapter slideAdapter = new SlidePagerAdapter(getContext(), slideList);

        homeSlideViewpager = view.findViewById(R.id.home_slide_viewpager);
        homeIndicatorTabLayout = view.findViewById(R.id.home_indicator_tabLayout);

        homeSlideViewpager.setAdapter(slideAdapter);

        // Configuración del timer del slide
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        homeIndicatorTabLayout.setupWithViewPager(homeSlideViewpager, true);

        homePopularMoviesRecyclerview = view.findViewById(R.id.home_popular_movies_recyclerview);

        initRecyclerView();

        getMovies(currentPage);

        return view;
    }

    private void initRecyclerView() {
            // Configuración del RecyclerView
            homePopularMoviesRecyclerview.setHasFixedSize(true);
            homePopularMoviesRecyclerview.setAdapter(null);

            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

            homePopularMoviesRecyclerview.setLayoutManager(manager);

            int totalItemCount = manager.getItemCount();
            int visibleItemCount = manager.getChildCount();
            int firstVisibleItem = manager.findFirstVisibleItemPosition();

            Log.d("initRecyclerView", "TotalItemCount: " + totalItemCount + " VisibleItemCount: " + visibleItemCount + "firstVisibleItem: " + firstVisibleItem);
            /*if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                if (!isFetchingMovies) {
                    getMovies(currentPage + 1);
                }
            }*/

    }

    private void getMovies(int page) {
        isFetchingMovies = true;
        mTMDbRepositoryAPI.getMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                Log.d("FragmentHome-getMovies", "Current Page = " + page);
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, callback);
                    homePopularMoviesRecyclerview.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearMovies();
                    }
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;

                //setTitle();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback callback = movie -> {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.getId());
        startActivity(intent);
    };

    class SliderTimer extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (homeSlideViewpager.getCurrentItem() < slideList.size() - 1) {
                        homeSlideViewpager.setCurrentItem(homeSlideViewpager.getCurrentItem() + 1);
                    } else {
                        homeSlideViewpager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    public void showError() {
        /**
         * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
         */
        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.error_message_loading_movies, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }
}