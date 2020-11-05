package me.sergiomartin.tvshowmovietracker.common.model.dataAccess;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import me.sergiomartin.tvshowmovietracker.BuildConfig;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.MovieApiInterface;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.GenresListResponse;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.MoviesListResponse;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.TrailerResponse;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetGenresCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetMovieCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetMoviesCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetTrailersCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDbRepositoryAPI {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "es";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    private static TMDbRepositoryAPI repository;

    private MovieApiInterface api;

    private TMDbRepositoryAPI(MovieApiInterface api) {
        this.api = api;
    }

    public static TMDbRepositoryAPI getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new TMDbRepositoryAPI(retrofit.create(MovieApiInterface.class));
        }

        return repository;
    }

    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Log.d("TMDbRepositoryAPI class", "Next Page = " + page);

        Callback<MoviesListResponse> call = new Callback<MoviesListResponse>() {
            @Override
            public void onResponse(@NotNull Call<MoviesListResponse> call, Response<MoviesListResponse> response) {
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
                callback.onError();
            }

        };

        /*api.getPopularMovies(BuildConfig.API_KEY, LANGUAGE, page)
                .enqueue(new Callback<MoviesListResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<MoviesListResponse> call, @NotNull Response<MoviesListResponse> response) {
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
                        callback.onError();
                    }
        });*/

        switch (sortBy) {
            case TOP_RATED:
                api.getTopRatedMovies(BuildConfig.API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                api.getUpcomingMovies(BuildConfig.API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case POPULAR:
            default:
                api.getPopularMovies(BuildConfig.API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }

    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, BuildConfig.API_KEY, LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NotNull Call<Movie> call, @NotNull Response<Movie> response) {
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
                        callback.onError();
                    }
                });
    }

    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres(BuildConfig.API_KEY, LANGUAGE)
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
        api.getTrailers(movieId, BuildConfig.API_KEY, LANGUAGE)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<TrailerResponse> call, @NotNull Response<TrailerResponse> response) {
                        if (response.isSuccessful()) {
                            TrailerResponse trailerResponse = response.body();
                            if (trailerResponse != null && trailerResponse.getTrailers() != null) {
                                callback.onSuccess(trailerResponse.getTrailers());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<TrailerResponse> call, @NotNull Throwable t) {
                        callback.onError();
                    }
                });
    }
}
