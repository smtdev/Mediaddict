package me.sergiomartin.tvshowmovietracker.common.model.dataAccess;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;

import me.sergiomartin.tvshowmovietracker.BuildConfig;
import me.sergiomartin.tvshowmovietracker.common.utils.Constants;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.LanguagesResponse;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.MovieApiInterface;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.GenresListResponse;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.MoviesListResponse;
import me.sergiomartin.tvshowmovietracker.moviesModule.api.TrailerListResponse;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetGenresCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetLanguagesCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetMovieCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetMoviesCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnGetTrailersCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDbRepositoryAPI {


    private static TMDbRepositoryAPI repository;

    private MovieApiInterface api;

    private TMDbRepositoryAPI(MovieApiInterface api) {
        this.api = api;
    }

    public static TMDbRepositoryAPI getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_TMDB)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new TMDbRepositoryAPI(retrofit.create(MovieApiInterface.class));
        }

        return repository;
    }

    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Log.d("TMDbRepositoryAPI class", "Page = " + page);

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
                Log.d("getMovies API error ->", t.getMessage());

                callback.onError();
            }
        };

        switch (sortBy) {
            case Constants.TOP_RATED:
                api.getTopRatedMovies(BuildConfig.API_KEY, Constants.LANGUAGE, page)
                        .enqueue(call);
                break;
            case Constants.UPCOMING:
                api.getUpcomingMovies(BuildConfig.API_KEY, Constants.LANGUAGE, page)
                        .enqueue(call);
                break;
            case Constants.POPULAR:
            default:
                api.getPopularMovies(BuildConfig.API_KEY, Constants.LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }

    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, BuildConfig.API_KEY, Constants.LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NotNull Call<Movie> call, @NotNull Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                try {
                                    callback.onSuccess(movie);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
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
        api.getTrailers(movieId, BuildConfig.API_KEY, Constants.LANGUAGE)
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


    /*public void getLanguages(final OnGetLanguagesCallback callback) {
        api.getLanguages(BuildConfig.API_KEY)
                .enqueue(new Callback<LanguagesResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<LanguagesResponse> call, @NotNull Response<LanguagesResponse> response) {
                        if (response.isSuccessful()) {
                            LanguagesResponse languagesResponse = response.body();
                            if (languagesResponse != null && languagesResponse.get() != null) {
                                callback.onSuccess(languagesResponse.getLan());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<LanguagesResponse> call, @NotNull Throwable t) {
                        callback.onError();
                    }
                });
    }*/

}
