package me.smt.mediaddict.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.smt.mediaddict.R;
import me.smt.mediaddict.model.data.TMDbRepositoryAPI;
import me.smt.mediaddict.common.CommonUtils;
import me.smt.mediaddict.common.Constants;
import me.smt.mediaddict.common.FirebaseUtils;
import me.smt.mediaddict.model.Genre;
import me.smt.mediaddict.model.Language;
import me.smt.mediaddict.model.Movie;
import me.smt.mediaddict.model.ProductionCompany;
import me.smt.mediaddict.model.ProductionCountry;
import me.smt.mediaddict.model.Trailer;
import me.smt.mediaddict.model.dataAccess.get.OnGetGenresCallback;
import me.smt.mediaddict.model.dataAccess.get.OnGetLanguagesCallback;
import me.smt.mediaddict.model.dataAccess.get.OnGetMovieCallback;
import me.smt.mediaddict.model.dataAccess.get.OnGetTrailersCallback;
import me.smt.mediaddict.model.dataAccess.listener.OnCheckIfMovieExistsListener;
import me.smt.mediaddict.module.GlideApp;

/**
 * Clase mediante la cual se puede ver todos los detalles
 * de cada película que cargue.
 * @author Sergio Martín Teruel
 * @version 1.0
 * @see AppCompatActivity
 **/
public class MovieDetailsActivity extends AppCompatActivity {
    @BindView(R.id.iv_movie_details_backdrop) AppCompatImageView ivMovieDetailsBackdrop;
    @BindView(R.id.iv_movie_details_poster) AppCompatImageView ivMovieDetailsPoster;
    @BindView(R.id.tv_movie_details_title) AppCompatTextView tvMovieDetailsTitle;
    @BindView(R.id.tv_movie_details_tagline) AppCompatTextView tvMovieDetailsTagline;
    @BindView(R.id.tv_movie_details_release_date) AppCompatTextView tvMovieDetailsReleaseDate;
    @BindView(R.id.tv_movie_details_length) AppCompatTextView tvMovieDetailsLength;
    @BindView(R.id.tv_movie_details_ratingNumber) AppCompatTextView tvMovieDetailsRatingNumber;
    @BindView(R.id.tv_movie_details_votes) AppCompatTextView tvMovieDetailsVotes;
    @BindView(R.id.tv_movie_details_rating) AppCompatRatingBar tvMovieDetailsRating;
    @BindView(R.id.tv_movie_details_summary) AppCompatTextView tvMovieDetailsSummary;
    @BindView(R.id.tv_movie_details_genres_title) AppCompatTextView tvMovieDetailsGenresTitle;
    @BindView(R.id.v_movie_details_first_divider) View vMovieDetailsFirstDivider;
    @BindView(R.id.cg_movie_details_genre) ChipGroup cgMovieDetailsGenre;
    @BindView(R.id.tv_movie_details_trailer_title) AppCompatTextView tvMovieDetailsTrailerTitle;
    @BindView(R.id.v_movie_details_second_divider) View vMovieDetailsSecondDivider;
    @BindView(R.id.ly_movie_details_trailer) LinearLayoutCompat lyMovieDetailsTrailer;
    @BindView(R.id.hsv_movie_details_trailer_container) HorizontalScrollView hsvMovieDetailsTrailerContainer;
    @BindView(R.id.v_movie_details_third_divider) View vMovieDetailsThirdDivider;
    @BindView(R.id.tv_original_title_info_mdp) TextView tvOriginalTitleInfoMdp;
    @BindView(R.id.tv_original_lang_info_mdp) TextView tvOriginalLangInfoMdp;
    @BindView(R.id.tv_budget_info_mdp) TextView tvBudgetInfoMdp;
    @BindView(R.id.tv_revenue_info_mdp) TextView tvRevenueInfoMdp;
    @BindView(R.id.tv_popularity_info_mdp) TextView tvPopularityInfoMdp;
    @BindView(R.id.tv_status_info_mdp) TextView tvStatusInfoMdp;
    @BindView(R.id.tv_languages_info_mdp) TextView tvLanguagesInfoMdp;
    @BindView(R.id.tv_prod_countries_info_mdp) TextView tvProdCountriesInfoMdp;
    @BindView(R.id.tv_prod_companies_info_mdp) TextView tvProdCompaniesInfoMdp;
    @BindView(R.id.tv_homepage_info_mdp) TextView tvHomepageInfoMdp;
    @BindView(R.id.cl_movie_details_panel) ConstraintLayout clMovieDetailsPanel;
    @BindView(R.id.cl_main_layout) CoordinatorLayout clMainLayout;
    @BindView(R.id.ctl_movie_details) CollapsingToolbarLayout ctlMovieDetails;
    @BindView(R.id.bottomAppBar) BottomAppBar bottomAppBar;
    @BindView(R.id.fab_movie_details) FloatingActionButton fabMovieDetails;
    @BindView(R.id.app_bar) AppBarLayout appBarLayout;

    //private MoviesDbHelper moviesDbHelper;
    //private String movieTitle, movieBackdrop, movieSummary, moviePosterpath, movieReleaseDate, movieGenreIds;
    //private float movieRating;

    /**
     * Atributo que crea una instancia de la clase gestora de API.
     */
    private TMDbRepositoryAPI mTMDbRepositoryAPI;

    /**
     * Atributo que hace referencia al ID de la película.
     */
    private int movieId;

    /**
     * Variable que se utiliza para controlar el rotado de la vista
     * animada.
     */
    private boolean isRotate;

    /**
     * Variable que se utiliza para saber si una película está marcada
     * como favorita o no.
     */
    private boolean isStarred;

    /**
     * Atributo que hace referencia al contenido de una película.
     */
    private Movie currentMovie;

    /**
     * Constructor de la clase.
     */
    public MovieDetailsActivity() {
        // Required empty public constructor
    }

    /**
     * Método llamado al iniciar la creación de la Activity.
     * @param savedInstanceState estado de la instancia.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        // Recibiendo info de los intents para saber si la película está
        // en favoritos
        movieId = getIntent().getIntExtra(Constants.MOVIE_ID, movieId);
        /*movieTitle = getIntent().getStringExtra(Constants.MOVIE_TITLE);
        movieBackdrop = getIntent().getStringExtra(Constants.MOVIE_BACKDROP);
        movieRating = getIntent().getFloatExtra(Constants.MOVIE_RATING, movieRating);
        movieSummary = getIntent().getStringExtra(Constants.MOVIE_OVERVIEW);
        moviePosterpath = getIntent().getStringExtra(Constants.MOVIE_POSTERPATH);
        movieReleaseDate = getIntent().getStringExtra(Constants.MOVIE_RELEASE_DATE);
        movieGenreIds = getIntent().getStringExtra(Constants.MOVIE_GENRES_ID);
        isStarred = getIntent().getBooleanExtra(Constants.MOVIE_FAVORITE_STATUS, isStarred);*/

        mTMDbRepositoryAPI = TMDbRepositoryAPI.getInstance();

        // comprueba si el usuario existe en Firebase y lo marca como "En favoritos"
        checkIfMovieExistsOnFirebase(movieId);

        setupToolbar();
        bottomAppBar.setOnClickListener(v -> onBackPressed());

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        params.setBehavior(new AppBarLayout.Behavior());

        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });

        getMovie();
    }

    /**
     * Método que comprueba si la película en cuestión está en Firebase, indicando
     * que es una película almacenada en Favoritos.
     * @param movieId el ID de la película.
     */
    private void checkIfMovieExistsOnFirebase(int movieId) {
        /*
         * Referencia: https://stackoverflow.com/a/48502715/1552146
         */
        FirebaseUtils.isMovieOnFirebase(movieId, new OnCheckIfMovieExistsListener() {
            @Override
            public void onCheckReceived(boolean movieExists) {
                if (movieExists) {
                    isStarred = true;
                    fabMovieDetails.setImageResource(R.drawable.ic_heart_favorite);
                } else {
                    isStarred = false;
                    fabMovieDetails.setImageResource(R.drawable.ic_heart_outline_favorite);
                }
            }

            @Override
            public void onError(Throwable error) {
                Snackbar.make(clMainLayout, R.string.movie_details_on_firebase_exists_error, Snackbar.LENGTH_SHORT)
                        .setAnchorView(fabMovieDetails)
                        .show();
            }
        });
    }

    /**
     * Método que configura la Toolbar de la Activity.
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_movie_details_toolbar);
        setSupportActionBar(toolbar);
        ctlMovieDetails.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        ctlMovieDetails.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    /**
     * Método utilizado para cargar los datos recibidos de la API
     * en la Activity.
     */
    private void getMovie() {
        mTMDbRepositoryAPI.getMovie(movieId, new OnGetMovieCallback() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(Movie movie) {
                currentMovie = movie;
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

                /*
                 * Campos importados en el fragment incrustado con detalles adicionales
                 */
                tvOriginalTitleInfoMdp.setText(movie.getOriginalTitle());
                tvBudgetInfoMdp.setText(movie.getBudget() > 0.0 ? String.format("%,.2f €", movie.getBudget()) : "-");
                tvHomepageInfoMdp.setText(movie.getHomepage().equals("") || movie.getHomepage().isEmpty() ? "-" : movie.getHomepage());
                tvOriginalLangInfoMdp.setText(movie.getOriginalLanguage().equals("") ? "-" : movie.getOriginalLanguage().toUpperCase());
                tvPopularityInfoMdp.setText(movie.getPopularity() > 0.0 ? String.format("%,.2f", movie.getPopularity()) : "-");
                tvRevenueInfoMdp.setText(movie.getRevenue() > 0.0 ? String.format("%,.2f €", movie.getRevenue()) : "-");
                tvStatusInfoMdp.setText(movie.getStatus().equals("") ? "-" : movie.getStatus());
                /*
                 * Campos generados desde API
                 */
                getGenres(movie);
                getTrailers(movie);
                getLanguages(movie);
                getProductionCompany(movie);
                getProductionCountries(movie);
                if (!isFinishing()) {
                    GlideApp.with(getApplicationContext())
                            .load(Constants.IMAGE_BASE_URL_w780 + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(ivMovieDetailsBackdrop);

                    ivMovieDetailsBackdrop.setAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.scale_animation));

                    GlideApp.with(getApplicationContext())
                            .load(Constants.IMAGE_BASE_URL_W500 + movie.getPosterPath())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(ivMovieDetailsPoster);
                }
            }

            @Override
            public void onError() {
                finish();
                /*
                 * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
                 */
                Snackbar.make(clMainLayout, R.string.error_message_loading_movie_info, Snackbar.LENGTH_LONG)
                        .setAnchorView(fabMovieDetails)
                        .show();
            }

        });
    }

    /**
     * Método utilizado para cargar los datos de los géneros asociados
     * a la película, recibidos por la API.
     * @param movie objeto de tipo Movie.
     */
    private void getGenres(final Movie movie) {
        mTMDbRepositoryAPI.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres().size() > 0) {
                    tvMovieDetailsGenresTitle.setVisibility(View.VISIBLE);
                    vMovieDetailsFirstDivider.setVisibility(View.VISIBLE);
                    cgMovieDetailsGenre.setVisibility(View.VISIBLE);

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
                /*
                 * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
                 */
                Snackbar.make(clMainLayout, R.string.error_message_loading_genres, Snackbar.LENGTH_LONG)
                        .setAnchorView(fabMovieDetails)
                        .show();

                tvMovieDetailsGenresTitle.setVisibility(View.GONE);
                vMovieDetailsFirstDivider.setVisibility(View.GONE);
                cgMovieDetailsGenre.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Método utilizado para cargar los datos de los tráilers asociados
     * a la película, recibidos por la API.
     * @param movie objeto de tipo Movie.
     */
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
                        GlideApp.with(getApplicationContext())
                                .load(String.format(Constants.YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                                .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                                .into(thumbnail);
                        AnimationView.outlineImageview(thumbnail);
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
                /*
                 * https://stackoverflow.com/questions/49289281/android-support-library-27-1-0-new-methods-requireactivity-requirecontext
                 */
                Snackbar.make(clMainLayout, R.string.error_message_loading_trailers, Snackbar.LENGTH_LONG)
                        .setAnchorView(fabMovieDetails)
                        .show();

                tvMovieDetailsTrailerTitle.setVisibility(View.GONE);
                vMovieDetailsSecondDivider.setVisibility(View.GONE);
                hsvMovieDetailsTrailerContainer.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Método utilizado para cargar los idiomas asociados a la película,
     * recibidos por la API.
     * @param movie objeto de tipo Movie.
     */
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
                    if (definitiveMmovieLangs.size() > 0) {
                        TextUtils.join(", ", definitiveMmovieLangs);
                        tvLanguagesInfoMdp.setText(definitiveMmovieLangs.toString()
                                .replace("[", "")
                                .replace("]", ""));
                    } else {
                        tvLanguagesInfoMdp.setText("-");
                    }
                }
            }

            @Override
            public void onError() {
                Snackbar.make(clMainLayout, R.string.error_message_loading_lang, Snackbar.LENGTH_LONG)
                        .setAnchorView(fabMovieDetails)
                        .show();
            }
        });
    }

    /**
     * Método utilizado para cargar las compañías de producción asociadas
     * a la película, recibidos por la API.
     * @param movie objeto de tipo Movie.
     */
    public void getProductionCompany(Movie movie) {
        List<String> companies = new ArrayList<>();
        if (movie.getCompanies() != null) {
            for (ProductionCompany company : movie.getCompanies()) {
                Log.d("MovieDetailsProdComp", companies.toString());
                companies.add(company.getName());
            }
            TextUtils.join(", ", companies);
        }
        if(companies.size() > 0) {
            tvProdCompaniesInfoMdp.setText(companies.toString()
                    .replace("[", "")
                    .replace("]", ""));
        } else {
            tvProdCompaniesInfoMdp.setText("-");
        }
    }

    /**
     * Método utilizado para cargar los país de las compañías de
     * producción asociadas a la película, recibidos por la API.
     * @param movie objeto de tipo Movie.
     */
    public void getProductionCountries(Movie movie) {
        List<String> countries = new ArrayList<>();
        if (movie.getCountries() != null) {
            for (ProductionCountry country : movie.getCountries()) {
                Log.d("MovieDetailsProdComp", countries.toString());
                countries.add(country.getName());
            }
            TextUtils.join(", ", countries);
        }
        if(countries.size() > 0) {
            tvProdCountriesInfoMdp.setText(countries.toString()
                    .replace("[", "")
                    .replace("]", ""));
        } else {
            tvProdCountriesInfoMdp.setText("-");
        }
    }

    /**
     * Método que abre el tráiler clickeado en YouTube.
     * @param url
     */
    private void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    /**
     * Método para controlar la tecla de volver hacia atrás.
     * @return true si se ha pulsado.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Método para manejar los clics de los elementos asociados al @OnClick.
     * @param view
     */
    @OnClick(R.id.fab_movie_details)
    public void onClick(View view) {
        //moviesDbHelper = new MoviesDbHelper(getApplicationContext());

        if (isStarred) {
            fabMovieDetails.setImageResource(R.drawable.ic_heart_outline_favorite);
            isStarred = false;

            // Eliminando de la sqlite
            //moviesDbHelper.deleteSavedMovie(movieId);
            // Eliminando de Firebase
            FirebaseUtils.deleteFromFavorite(movieId);

            Snackbar.make(clMainLayout, R.string.successfully_movie_deleted_from_favs, Snackbar.LENGTH_SHORT)
                    .setAnchorView(fabMovieDetails)
                    .show();
        } else {
            fabMovieDetails.setImageResource(R.drawable.ic_heart_favorite);
            isStarred = true;

            // Añandiendo a la sqlite
            //addToFavorite();
            // Añadiendo a Firebase
            FirebaseUtils.addToFavorite(currentMovie);

            Snackbar.make(clMainLayout, R.string.successfully_movie_added_to_favs, Snackbar.LENGTH_SHORT)
                    .setAnchorView(fabMovieDetails)
                    .show();
        }

        // Animación de sacudida
        AnimationView.shakeFab(view, 200L);
        // Animación de rotado de imagen interior
        isRotate = AnimationView.rotateFab(view, !isRotate);
    }

    /*private void addToFavorite() {
        moviesDbHelper = new MoviesDbHelper(getApplicationContext());
        Movie movie = new Movie();

        Log.d("MoviestoAdd", movieId + " " + movieTitle + " " + movieBackdrop + " " + movieRating + " " +movieSummary + " " + moviePosterpath + " " + movieReleaseDate);
        movie.setId(movieId);
        movie.setTitle(movieTitle);
        movie.setBackdrop(movieBackdrop);
        movie.setRating((float)movieRating);
        movie.setOverview(movieSummary);
        movie.setPosterPath(moviePosterpath);
        movie.setReleaseDate(movieReleaseDate);
        movie.setGenreIdString(movieGenreIds);

        Log.d("GenreIdString", "GenreIdString: " + movie.getGenreIdString());

        moviesDbHelper.saveMovie(movie);
    }*/
}