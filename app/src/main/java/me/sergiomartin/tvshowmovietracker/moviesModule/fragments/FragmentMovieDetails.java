package me.sergiomartin.tvshowmovietracker.moviesModule.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.common.model.dataAccess.TMDbRepositoryAPI;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Genre;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetGenresCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetMovieCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.module.GlideApp;

public class FragmentMovieDetails extends Fragment {

    public static String MOVIE_ID = "movie_id";

    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    @BindView(R.id.movieDetailsBackdrop)
    ImageView movieDetailsBackdrop;
    @BindView(R.id.movieDetailsTitle)
    TextView movieDetailsTitle;
    @BindView(R.id.movieDetailsGenres)
    TextView movieDetailsGenres;
    @BindView(R.id.movieDetailsOverview)
    TextView movieDetailsOverview;
    @BindView(R.id.summaryLabel)
    TextView moveDetailsLabel;
    @BindView(R.id.movieDetailsReleaseDate)
    TextView movieDetailsReleaseDate;
    @BindView(R.id.movieDetailsRating)
    RatingBar movieDetailsRating;
    @BindView(R.id.movieTrailers)
    LinearLayout movieTrailers;
    @BindView(R.id.movieReviews)
    LinearLayout movieReviews;
    @BindView(R.id.trailersLabel)
    TextView trailersLabel;

    private TMDbRepositoryAPI TMDbRepositoryAPI;
    private int movieId;

    public FragmentMovieDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onSupportNavigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }*/

        getMovie();
    }

    private void getMovie() {
        TMDbRepositoryAPI.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                movieDetailsTitle.setText(movie.getTitle());
                moveDetailsLabel.setVisibility(View.VISIBLE);
                movieDetailsOverview.setText(movie.getOverview());
                movieDetailsRating.setVisibility(View.VISIBLE);
                movieDetailsRating.setRating(movie.getRating() / 2);
                getGenres(movie);
                movieDetailsReleaseDate.setText(movie.getReleaseDate());
                if (!requireActivity().isFinishing()) {
                    GlideApp.with(FragmentMovieDetails.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(movieDetailsBackdrop);
                }
            }

            @Override
            public void onError() {
                requireActivity().finish();
                Log.e("FragmentMovieDetails", "Ha habido un error al obtener la información de la película " + movieDetailsTitle.getText());
            }

        });
    }


    private void getGenres(final Movie movie) {
        TMDbRepositoryAPI.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieDetailsGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
                Log.e("FragmentMovieDetails", "Ha habido un error al obtener la información del género de la película " + movieDetailsTitle.getText());

            }
        });
    }

    public boolean onSupportNavigateUp() {
        return true;
    }

    private void showError() {
        /**
         * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
         */
        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.error_message_loading_movies, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }

}