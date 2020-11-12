package me.sergiomartin.tvshowmovietracker.moviesModule.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.common.model.dataAccess.TMDbRepositoryAPI;
import me.sergiomartin.tvshowmovietracker.common.utils.CommonUtils;
import me.sergiomartin.tvshowmovietracker.common.utils.Constants;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Genre;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Language;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.ProductionCompany;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Trailer;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetGenresCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetLanguagesCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetMovieCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetTrailersCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.module.GlideApp;

public class MovieDetailsActivity extends AppCompatActivity {
    @BindView(R.id.iv_movie_details_backdrop)
    AppCompatImageView ivMovieDetailsBackdrop;
    @BindView(R.id.iv_movie_details_poster)
    AppCompatImageView ivMovieDetailsPoster;
    @BindView(R.id.tv_movie_details_title)
    AppCompatTextView tvMovieDetailsTitle;
    @BindView(R.id.tv_movie_details_tagline)
    AppCompatTextView tvMovieDetailsTagline;
    @BindView(R.id.tv_movie_details_release_date)
    AppCompatTextView tvMovieDetailsReleaseDate;
    @BindView(R.id.tv_movie_details_length)
    AppCompatTextView tvMovieDetailsLength;
    @BindView(R.id.tv_movie_details_ratingNumber)
    AppCompatTextView tvMovieDetailsRatingNumber;
    @BindView(R.id.tv_movie_details_votes)
    AppCompatTextView tvMovieDetailsVotes;
    @BindView(R.id.tv_movie_details_rating)
    AppCompatRatingBar tvMovieDetailsRating;
    @BindView(R.id.tv_movie_details_summary)
    AppCompatTextView tvMovieDetailsSummary;
    @BindView(R.id.tv_movie_details_genres_title)
    AppCompatTextView tvMovieDetailsGenresTitle;
    @BindView(R.id.v_movie_details_first_divider)
    View vMovieDetailsFirstDivider;
    @BindView(R.id.cg_movie_details_genre)
    ChipGroup cgMovieDetailsGenre;
    @BindView(R.id.tv_movie_details_trailer_title)
    AppCompatTextView tvMovieDetailsTrailerTitle;
    @BindView(R.id.v_movie_details_second_divider)
    View vMovieDetailsSecondDivider;
    @BindView(R.id.ly_movie_details_trailer)
    LinearLayoutCompat lyMovieDetailsTrailer;
    @BindView(R.id.hsv_movie_details_trailer_container)
    HorizontalScrollView hsvMovieDetailsTrailerContainer;
    @BindView(R.id.v_movie_details_third_divider)
    View vMovieDetailsThirdDivider;
    @BindView(R.id.tv_original_title_info_mdp)
    TextView tvOriginalTitleInfoMdp;
    @BindView(R.id.tv_original_lang_info_mdp)
    TextView tvOriginalLangInfoMdp;
    @BindView(R.id.tv_budget_info_mdp)
    TextView tvBudgetInfoMdp;
    @BindView(R.id.tv_revenue_info_mdp)
    TextView tvRevenueInfoMdp;
    @BindView(R.id.tv_popularity_info_mdp)
    TextView tvPopularityInfoMdp;
    @BindView(R.id.tv_status_info_mdp)
    TextView tvStatusInfoMdp;
    @BindView(R.id.tv_languages_info_mdp)
    TextView tvLanguagesInfoMdp;
    @BindView(R.id.tv_prod_countries_info_mdp)
    TextView tvProdCountriesInfoMdp;
    @BindView(R.id.tv_prod_companies_info_mdp)
    TextView tvProdCompaniesInfoMdp;
    @BindView(R.id.tv_homepage_info_mdp)
    TextView tvHomepageInfoMdp;
    @BindView(R.id.cl_movie_details_panel)
    ConstraintLayout clMovieDetailsPanel;
    @BindView(R.id.cl_main_layout)
    CoordinatorLayout clMainLayout;
    @BindView(R.id.ctl_movie_details)
    CollapsingToolbarLayout ctlMovieDetails;
    @BindView(R.id.bottomAppBar)
    BottomAppBar bottomAppBar;
    @BindView(R.id.fab_movie_details)
    FloatingActionButton fabMovieDetails;


    private TMDbRepositoryAPI mTMDbRepositoryAPI;
    private int movieId;
    private boolean isRotate, isStarred = false;

    public MovieDetailsActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        movieId = getIntent().getIntExtra(Constants.MOVIE_ID, movieId);

        mTMDbRepositoryAPI = TMDbRepositoryAPI.getInstance();

        setupToolbar();

        bottomAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getMovie();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_movie_details_toolbar);
        setSupportActionBar(toolbar);
        ctlMovieDetails.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        ctlMovieDetails.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }*/
    }

    private void getMovie() {
        mTMDbRepositoryAPI.getMovie(movieId, new OnGetMovieCallback() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(Movie movie) {
                tvMovieDetailsTitle.setText(movie.getTitle());
                ctlMovieDetails.setTitle(movie.getTitle());
                tvMovieDetailsLength.setText(CommonUtils.parseMinutesToHour((int) movie.getRuntime()));
                //tvMovieDetailsLength.setText(String.format("%s %s", (int) movie.getRuntime(), "minutos"));
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
                tvMovieDetailsReleaseDate.setText(movie.getReleaseDate());
                tvMovieDetailsTagline.setText(movie.getTagline());
                /**
                 * Campos importados en el fragment incrustado con detalles adicionales
                 */
                tvOriginalTitleInfoMdp.setText(movie.getOriginalTitle());
                Log.d("MovieDetailsActivity", String.valueOf(movie.getBudget()));
                tvBudgetInfoMdp.setText((movie.getBudget().compareTo(BigDecimal.ZERO) > 0.0) ? String.format("%,.2f €", movie.getBudget()) : "-");
                tvHomepageInfoMdp.setText(movie.getHomepage().equals("") ? "-" : movie.getHomepage());
                tvOriginalLangInfoMdp.setText(movie.getOriginalLanguage().equals("") ? "-" : movie.getOriginalLanguage().toUpperCase());
                tvPopularityInfoMdp.setText((movie.getPopularity().compareTo(BigDecimal.ZERO) > 0.0) ? String.format("%,.2f", movie.getPopularity()) : "-");
                tvRevenueInfoMdp.setText((movie.getRevenue().compareTo(BigDecimal.ZERO) > 0.0) ? String.format("%,.2f €", movie.getRevenue()) : "-");
                tvStatusInfoMdp.setText(movie.getStatus().equals("") ? "-" : movie.getStatus());

                /**
                 * Campos generados desde API
                 */
                getGenres(movie);
                getTrailers(movie);
                getLanguages(movie);
                getProductionCompany(movie);
                if (!isFinishing()) {
                    GlideApp.with(MovieDetailsActivity.this)
                            .load(Constants.IMAGE_BASE_URL_w780 + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(ivMovieDetailsBackdrop);

                    ivMovieDetailsBackdrop.setAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.scale_animation));

                    GlideApp.with(MovieDetailsActivity.this)
                            .load(Constants.IMAGE_BASE_URL_W500 + movie.getPosterPath())
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
                Snackbar.make(clMainLayout, R.string.error_message_loading_movie_info, Snackbar.LENGTH_LONG)
                        //.setAnchorView(R.id.bottom_navigation)
                        .show();
            }

        });
    }

    private void getGenres(final Movie movie) {
        mTMDbRepositoryAPI.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                tvMovieDetailsGenresTitle.setVisibility(View.VISIBLE);
                vMovieDetailsFirstDivider.setVisibility(View.VISIBLE);
                cgMovieDetailsGenre.setVisibility(View.VISIBLE);

                if (movie.getGenres() != null) {
                    for (Genre genre : movie.getGenres()) {
                        Chip mChip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_category, null, false);
                        mChip.setText(genre.getName());
                        int paddingDp = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 10,
                                getResources().getDisplayMetrics()
                        );
                        mChip.setPadding(paddingDp, 0, paddingDp, 0);
                        mChip.setOnCheckedChangeListener((compoundButton, b) -> {
                        });
                        cgMovieDetailsGenre.addView(mChip);
                    }
                } else {
                    tvMovieDetailsGenresTitle.setVisibility(View.GONE);
                    vMovieDetailsFirstDivider.setVisibility(View.GONE);
                    cgMovieDetailsGenre.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                /**
                 * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
                 */
                Snackbar.make(clMainLayout, R.string.error_message_loading_genres, Snackbar.LENGTH_LONG)
                        //.setAnchorView(R.id.bottom_navigation)
                        .show();

                tvMovieDetailsGenresTitle.setVisibility(View.GONE);
                vMovieDetailsFirstDivider.setVisibility(View.GONE);
                cgMovieDetailsGenre.setVisibility(View.GONE);
            }
        });
    }

    private void getTrailers(Movie movie) {
        mTMDbRepositoryAPI.getTrailers(movie.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                if (trailers.size() > 0) {
                    tvMovieDetailsTrailerTitle.setVisibility(View.VISIBLE);
                    vMovieDetailsSecondDivider.setVisibility(View.VISIBLE);
                    hsvMovieDetailsTrailerContainer.setVisibility(View.VISIBLE);

                    lyMovieDetailsTrailer.removeAllViews();
                    for (final Trailer trailer : trailers) {
                        View parent = getLayoutInflater().inflate(R.layout.movie_trailer, lyMovieDetailsTrailer, false);
                        ImageView thumbnail = parent.findViewById(R.id.thumbnail);
                        thumbnail.requestLayout();
                        thumbnail.setOnClickListener(v -> showTrailer(String.format(Constants.YOUTUBE_VIDEO_URL, trailer.getKey())));
                        GlideApp.with(MovieDetailsActivity.this)
                                .load(String.format(Constants.YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                                .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                                .into(thumbnail);
                        lyMovieDetailsTrailer.addView(parent);
                    }
                } else {
                    tvMovieDetailsTrailerTitle.setVisibility(View.GONE);
                    vMovieDetailsSecondDivider.setVisibility(View.GONE);
                    hsvMovieDetailsTrailerContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                /**
                 * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
                 */
                Snackbar.make(clMainLayout, R.string.error_message_loading_trailers, Snackbar.LENGTH_LONG)
                        //.setAnchorView(R.id.bottom_navigation)
                        .show();

                tvMovieDetailsTrailerTitle.setVisibility(View.GONE);
                vMovieDetailsSecondDivider.setVisibility(View.GONE);
                hsvMovieDetailsTrailerContainer.setVisibility(View.GONE);
            }
        });
    }

    private void getLanguages(Movie movie) {
        mTMDbRepositoryAPI.getLanguages(new OnGetLanguagesCallback() {
            @Override
            public void onSuccess(List<Language> languages) {
                List<String> definitiveMmovieLangs = new ArrayList<>();

                if (movie.getLanguages() != null) {
                    for (Language lang : movie.getLanguages()) {
                        Log.d("LanguagesFromDetail", lang.getLangIsoStandard() + " - " + lang.getEnglishName() + " - " + lang.getName());
                    }
                    for (Language responseLangs : languages) {
                        for (Language movieLangs : movie.getLanguages()) {
                            if (responseLangs.getLangIsoStandard().equals(movieLangs.getLangIsoStandard())) {
                                definitiveMmovieLangs.add(movieLangs.getName());
                            }
                        }
                    }
                    TextUtils.join(", ", definitiveMmovieLangs);
                    tvLanguagesInfoMdp.setText(definitiveMmovieLangs.toString()
                            .replace("[", "")
                            .replace("]", ""));
                }
            }

            @Override
            public void onError() {
                Snackbar.make(clMainLayout, R.string.error_message_loading_lang, Snackbar.LENGTH_LONG)
                        //.setAnchorView(R.id.bottom_navigation)
                        .show();
            }
        });
    }

    public void getProductionCompany(Movie movie) {
        List<String> companies = new ArrayList<>();
        if (movie.getCompanies() != null) {
            for (ProductionCompany company : movie.getCompanies()) {
                Log.d("MovieDetailsProdComp", companies.toString());
                companies.add(company.getName());
            }
            TextUtils.join(", ", companies);
        }
        tvProdCompaniesInfoMdp.setText(companies.toString()
                .replace("[", "")
                .replace("]", ""));
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

    @OnClick(R.id.fab_movie_details)
    public void onClick(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (isStarred) {
            fabMovieDetails.setImageResource(R.drawable.ic_star_rate_removed);
            isStarred = false;
            Snackbar.make(clMainLayout, "Película eliminada de favoritos correctamente.", Snackbar.LENGTH_SHORT).show();
        } else {
            fabMovieDetails.setImageResource(R.drawable.ic_star_rate_added);
            isStarred = true;
            Snackbar.make(clMainLayout, "Película añadida a favoritos correctamente.", Snackbar.LENGTH_SHORT).show();
        }

        // Animación de sacudida
        AnimationView.shakeFab(view, 200L);
        // Animación de rotado de imagen interior
        isRotate = AnimationView.rotateFab(view, !isRotate);
    }
}