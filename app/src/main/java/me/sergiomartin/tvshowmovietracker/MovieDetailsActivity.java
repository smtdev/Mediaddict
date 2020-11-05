package me.sergiomartin.tvshowmovietracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sergiomartin.tvshowmovietracker.common.model.dataAccess.TMDbRepositoryAPI;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Genre;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetGenresCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetMovieCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.module.GlideApp;

public class MovieDetailsActivity extends AppCompatActivity {

    public static String MOVIE_ID = "movie_id";

    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/0.jpg";

    @BindView(R.id.movieDetailsBackdrop)
    ImageView movieDetailsBackdrop;
    @BindView(R.id.movieDetailsTitle)
    TextView movieDetailsTitle;
    @BindView(R.id.movieDetailsGenres)
    TextView movieDetailsGenres;
    @BindView(R.id.movieDetailsOverview)
    TextView movieDetailsOverview;
    //@BindView(R.id.summaryLabel)
    //TextView moveDetailsLabel;
    @BindView(R.id.movieDetailsReleaseDate)
    TextView movieDetailsReleaseDate;
    @BindView(R.id.movieDetailsRating)
    RatingBar movieDetailsRating;
    //@BindView(R.id.movieTrailers)
    //LinearLayout movieTrailers;
    //@BindView(R.id.movieReviews)
    //LinearLayout movieReviews;
    @BindView(R.id.movieDetailsConstraintLayout)
    ConstraintLayout movieDetailsConstraintLayout;
    @BindView(R.id.movieDetailsPoster)
    ImageView movieDetailsPoster;
    //@BindView(R.id.trailersLabel)
    //TextView trailersLabel;

    private TMDbRepositoryAPI mTMDbRepositoryAPI;
    private int movieId;

    public MovieDetailsActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        movieId = getIntent().getIntExtra(MOVIE_ID, movieId);

        mTMDbRepositoryAPI = TMDbRepositoryAPI.getInstance();

        setupToolbar();

        getMovie();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void getMovie() {
        mTMDbRepositoryAPI.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                movieDetailsTitle.setText(movie.getTitle());
                //moveDetailsLabel.setVisibility(View.VISIBLE);
                movieDetailsOverview.setText(movie.getOverview());
                movieDetailsRating.setVisibility(View.VISIBLE);
                movieDetailsRating.setRating(movie.getRating() / 2);
                getGenres(movie);
                //getTrailers(movie);
                movieDetailsReleaseDate.setText(movie.getReleaseDate());
                if (!isFinishing()) {
                    GlideApp.with(MovieDetailsActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(movieDetailsBackdrop);

                    GlideApp.with(MovieDetailsActivity.this)
                            .load(IMAGE_BASE_URL + movie.getPosterPath())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .transform(new CenterCrop())
                            .into(movieDetailsPoster);
                }
            }

            @Override
            public void onError() {
                finish();
                /**
                 * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
                 */
                Snackbar.make(movieDetailsConstraintLayout, R.string.error_message_loading_movie_info, Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.bottom_navigation)
                        .show();
            }

        });
    }

    private void getGenres(final Movie movie) {
        mTMDbRepositoryAPI.getGenres(new OnGetGenresCallback() {
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
                /**
                 * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
                 */
                Snackbar.make(movieDetailsConstraintLayout, R.string.error_message_loading_genres, Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.bottom_navigation)
                        .show();
            }
        });
    }

    /*private void getTrailers(Movie movie) {
        mTMDbRepositoryAPI.getTrailers(movie.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                trailersLabel.setVisibility(View.VISIBLE);
                movieTrailers.removeAllViews();
                for (final Trailer trailer : trailers) {
                    View parent = getLayoutInflater().inflate(R.layout.movie_trailer, movieTrailers, false);
                    ImageView thumbnail = parent.findViewById(R.id.thumbnail);
                    thumbnail.requestLayout();
                    thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.getKey()));
                        }
                    });
                    GlideApp.with(MovieDetailsActivity.this)
                            .load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                            .into(thumbnail);
                    movieTrailers.addView(parent);
                }
            }

            @Override
            public void onError() {
                // Do nothing
                trailersLabel.setVisibility(View.GONE);
            }
        });
    }*/

    private void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showError() {
        Snackbar.make(movieDetailsConstraintLayout, R.string.error_message_loading_movies, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }

}