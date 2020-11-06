package me.sergiomartin.tvshowmovietracker;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sergiomartin.tvshowmovietracker.common.model.dataAccess.TMDbRepositoryAPI;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Genre;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Trailer;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetGenresCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetMovieCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetTrailersCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.module.GlideApp;

public class MovieDetailsActivity extends AppCompatActivity {

    public static String MOVIE_ID = "movie_id";
    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/0.jpg";

    @BindView(R.id.iv_movie_details_backdrop)
    ImageView ivMovieDetailsBackdrop;
    @BindView(R.id.iv_movie_details_poster)
    ImageView ivMovieDetailsPoster;
    @BindView(R.id.tv_movie_details_title)
    TextView tvMovieDetailsTitle;
    @BindView(R.id.tv_movie_details_release_date)
    TextView tvMovieDetailsReleaseDate;
    @BindView(R.id.tv_movie_details_genre)
    TextView tvMovieDetailsGenre;
    @BindView(R.id.tv_movie_details_rating)
    RatingBar tvMovieDetailsRating;
    @BindView(R.id.tv_movie_details_ratingNumber)
    TextView tvMovieDetailsRatingNumber;
    @BindView(R.id.tv_movie_details_votes)
    TextView tvMovieDetailsVotes;
    @BindView(R.id.iv_votes_person)
    ImageView ivVotesPerson;
    @BindView(R.id.tv_movie_details_summary)
    TextView tvMovieDetailsSummary;
    @BindView(R.id.tv_movie_details_trailer_title)
    TextView tvMovieDetailsTrailerTitle;
    @BindView(R.id.constly_movie_details)
    ConstraintLayout constlyMovieDetails;
    @BindView(R.id.ly_movie_details_trailer)
    LinearLayout lyMovieDetailsTrailer;
    @BindView(R.id.v_movie_details_first_divider)
    View vMovieDetailsFirstDivider;
    @BindView(R.id.v_movie_details_second_divider)
    View vMovieDetailsSecondDivider;
    @BindView(R.id.rv_movie_details_cast)
    RecyclerView rvMovieDetailsCast;

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
                tvMovieDetailsTitle.setText(movie.getTitle());
                //movieDetailsLabel.setVisibility(View.VISIBLE);
                tvMovieDetailsSummary.setText(movie.getOverview());
                tvMovieDetailsRating.setVisibility(View.VISIBLE);
                tvMovieDetailsRating.setRating(movie.getRating() / 2);
                /*
                 * Cambiar tamaño de parte del textView para resaltar la puntuación
                 */
                tvMovieDetailsRatingNumber.setText(String.format("%s/%s", movie.getRating(), 10));
                int start = tvMovieDetailsRatingNumber.getText().length() - 3;
                int end = tvMovieDetailsRatingNumber.getText().length();
                Spannable span = new SpannableString(tvMovieDetailsRatingNumber.getText());
                span.setSpan(new RelativeSizeSpan(0.7f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new StyleSpan(Typeface.NORMAL), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new StyleSpan(Typeface.BOLD), 0, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvMovieDetailsRatingNumber.setText(span);

                tvMovieDetailsVotes.setText(String.format("%s", (int) movie.getVoteCount()));
                getGenres(movie);
                getTrailers(movie);
                tvMovieDetailsReleaseDate.setText(movie.getReleaseDate());
                if (!isFinishing()) {
                    GlideApp.with(MovieDetailsActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(ivMovieDetailsBackdrop);

                    ivMovieDetailsBackdrop.setAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.scale_animation));

                    GlideApp.with(MovieDetailsActivity.this)
                            .load(IMAGE_BASE_URL + movie.getPosterPath())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(ivMovieDetailsPoster);
                }
            }

            @Override
            public void onError() {
                finish();
                /**
                 * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
                 */
                Snackbar.make(constlyMovieDetails, R.string.error_message_loading_movie_info, Snackbar.LENGTH_LONG)
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
                    tvMovieDetailsGenre.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                /**
                 * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
                 */
                Snackbar.make(constlyMovieDetails, R.string.error_message_loading_genres, Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.bottom_navigation)
                        .show();
            }
        });
    }

    private void getTrailers(Movie movie) {
        mTMDbRepositoryAPI.getTrailers(movie.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                if (trailers.size() > 0) {
                    tvMovieDetailsTrailerTitle.setVisibility(View.VISIBLE);
                    vMovieDetailsFirstDivider.setVisibility(View.VISIBLE);
                    lyMovieDetailsTrailer.removeAllViews();
                    for (final Trailer trailer : trailers) {
                        View parent = getLayoutInflater().inflate(R.layout.movie_trailer, lyMovieDetailsTrailer, false);
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
                        lyMovieDetailsTrailer.addView(parent);
                    }
                } else {
                    onError();
                }
            }

            @Override
            public void onError() {
                // Do nothing
                tvMovieDetailsTrailerTitle.setVisibility(View.GONE);
            }
        });
    }

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
        Snackbar.make(constlyMovieDetails, R.string.error_message_loading_movies, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }

}