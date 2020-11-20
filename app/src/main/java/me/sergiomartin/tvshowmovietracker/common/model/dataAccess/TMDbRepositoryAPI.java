package me.sergiomartin.tvshowmovietracker.common.model.dataAccess;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.sergiomartin.tvshowmovietracker.BuildConfig;
import me.sergiomartin.tvshowmovietracker.MyApplication;
import me.sergiomartin.tvshowmovietracker.common.utils.Constants;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.MovieApiInterface;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.GenresListResponse;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.MoviesListResponse;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.TrailerListResponse;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Language;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.action.OnSearchMovieCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetGenresCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetLanguagesCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetMovieCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetMoviesCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get.OnGetTrailersCallback;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDbRepositoryAPI {

    private static final String TAG = "TMDbRepositoryAPI";
    private static TMDbRepositoryAPI repository;
    private MovieApiInterface api;
    private static final long cacheSize = 5 * 1024 * 1024; // 5 MB

    private TMDbRepositoryAPI(MovieApiInterface api) {
        this.api = api;
    }

    public static TMDbRepositoryAPI getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_TMDB)
                    .client(okHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new TMDbRepositoryAPI(retrofit.create(MovieApiInterface.class));
        }

        return repository;
    }

    /*
     * Código de caching con Retrofit
     * https://github.com/mitchtabian/Retrofit-Caching-Example
     */
    private static OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .cache(cache())
                .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
                .addNetworkInterceptor(networkInterceptor()) // only used when network is on
                .addInterceptor(offlineInterceptor())
                .build();
    }

    private static Cache cache(){
        return new Cache(new File(MyApplication.getInstance().getApplicationContext().getCacheDir(),"someIdentifier"), cacheSize);
    }

    /**
     * This interceptor will be called both if the network is available and if the network is not available
     * @return
     */
    private static Interceptor offlineInterceptor() {
        return chain -> {
            Log.d(TAG, "offline interceptor: called.");
            Request request = chain.request();

            // prevent caching when network is on. For that we use the "networkInterceptor"
            if (!MyApplication.hasNetwork()) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(Constants.HEADER_PRAGMA)
                        .removeHeader(Constants.HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();
            }

            return chain.proceed(request);
        };
    }

    /**
     * This interceptor will be called ONLY if the network is available
     * @return
     */
    private static Interceptor networkInterceptor() {
        return new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                Log.d(TAG, "network interceptor: called.");

                okhttp3.Response response = chain.proceed(chain.request());

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(5, TimeUnit.SECONDS)
                        .build();

                return response.newBuilder()
                        .removeHeader(Constants.HEADER_PRAGMA)
                        .removeHeader(Constants.HEADER_CACHE_CONTROL)
                        .header(Constants.HEADER_CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    private static HttpLoggingInterceptor httpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log (String message)
                    {
                        Log.d(TAG, "log: http log: " + message);
                    }
                } );
        httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public void searchMovies(int page, String query, final OnSearchMovieCallback callback) {
        api.getSearchMovies(BuildConfig.API_KEY, Constants.LANGUAGE, page, query)
                .enqueue(new Callback<MoviesListResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<MoviesListResponse> call, @NotNull Response<MoviesListResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesListResponse moviesResponse = response.body();
                            Log.d("API-Repo-SearchMovies", "Resultados: " + moviesResponse.getTotalResults());

                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies(), query);
                            } else {
                                callback.onEmptyResult();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<MoviesListResponse> call, @NotNull Throwable t) {
                        Log.d("searchMovies API err->", t.getMessage());

                        callback.onError();
                    }

                });
    }

    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Log.d("TMDbRepositoryAPI class", "Page = " + page);

        Callback<MoviesListResponse> call = new Callback<MoviesListResponse>() {
            @Override
            public void onResponse(@NotNull Call<MoviesListResponse> call, Response<MoviesListResponse> response) {
                Log.d(TAG, "onResponse getMovies: " + response.body());
                if (response.isSuccessful()) {
                    MoviesListResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMovies() != null) {
                        callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MoviesListResponse> call, @NotNull Throwable t) {
                Log.d("getMovies API error ->", t.getMessage());

                callback.onError();
            }
        };

        switch (sortBy) {
            case Constants.TOP_RATED:
                api.getTopRatedMovies(BuildConfig.API_KEY, Constants.LANGUAGE, page, Constants.LANGUAGE)
                        .enqueue(call);
                break;
            case Constants.UPCOMING:
                api.getUpcomingMovies(BuildConfig.API_KEY, Constants.LANGUAGE, page, Constants.LANGUAGE)
                        .enqueue(call);
                break;
            case Constants.POPULAR:
                api.getPopularMovies(BuildConfig.API_KEY, Constants.LANGUAGE, page, Constants.LANGUAGE)
                        .enqueue(call);
                break;
        }
    }

    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, BuildConfig.API_KEY, Constants.LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NotNull Call<Movie> call, @NotNull Response<Movie> response) {
                        Log.e(TAG, "log: -----------------------------");
                        Log.d(TAG, "onResponse getMovie: " + response.body());

                        if(response.raw().networkResponse() != null){
                            Log.d(TAG, "onResponse: la respuesta NO es cacheada.");
                        }
                        else if(response.raw().cacheResponse() != null
                                && response.raw().networkResponse() == null){
                            Log.d(TAG, "onResponse: la respuesta es cacheada.");
                        }
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Movie> call, @NotNull Throwable t) {
                        Log.d("getMovie API error ->", t.getMessage());
                        callback.onError();
                    }
                });
    }

    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres(BuildConfig.API_KEY, Constants.LANGUAGE)
                .enqueue(new Callback<GenresListResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<GenresListResponse> call, @NotNull Response<GenresListResponse> response) {
                        if (response.isSuccessful()) {
                            GenresListResponse genresResponse = response.body();
                            if (genresResponse != null && genresResponse.getGenres() != null) {
                                callback.onSuccess(genresResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<GenresListResponse> call, @NotNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getTrailers(int movieId, final OnGetTrailersCallback callback) {
        api.getTrailers(movieId, BuildConfig.API_KEY)
                .enqueue(new Callback<TrailerListResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<TrailerListResponse> call, @NotNull Response<TrailerListResponse> response) {
                        if (response.isSuccessful()) {
                            TrailerListResponse trailerListResponse = response.body();
                            if (trailerListResponse != null && trailerListResponse.getTrailers() != null) {
                                callback.onSuccess(trailerListResponse.getTrailers());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<TrailerListResponse> call, @NotNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getLanguages(final OnGetLanguagesCallback callback) {
        api.getLanguages(BuildConfig.API_KEY)
                .enqueue(new Callback<List<Language>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Language>> call, @NotNull Response<List<Language>> response) {
                        if (response.isSuccessful()) {
                            List<Language> languages = response.body();
                            for (Language lang : languages) {
                                Log.d("Contenido de Langs", "Idiomas: "
                                        + lang.getLangIsoStandard() + ", "
                                        + lang.getName() + ", "
                                        + lang.getEnglishName()
                                        + ", ");
                            }
                            Log.i("autolog", "onResponse");
                                callback.onSuccess(languages);
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Language>> call, @NotNull Throwable t) {
                        Log.d("TMDbRepositoryAPI lang", "Dentro de onFailure: "+ t.getCause());
                        callback.onError();
                    }
                });
    }
}
