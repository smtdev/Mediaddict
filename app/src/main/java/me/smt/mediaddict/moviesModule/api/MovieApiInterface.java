package me.smt.mediaddict.moviesModule.api;

import java.util.List;

import me.smt.mediaddict.moviesModule.model.Language;
import me.smt.mediaddict.moviesModule.model.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiInterface {

    @GET("search/movie")
    Call<MoviesListResponse> getSearchMovies (
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page,
            @Query("query") String query
        );

    @GET("movie/popular")
    Call<MoviesListResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page,
            @Query("region") String region
    );

    @GET("movie/top_rated")
    Call<MoviesListResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page,
            @Query("region") String region
    );

    @GET("movie/upcoming")
    Call<MoviesListResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page,
            @Query("region") String region
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Call<TrailerListResponse> getTrailers(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey
    );

    @GET("genre/movie/list")
    Call<GenresListResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("configuration/languages")
    Call<List<Language>> getLanguages (
            @Query("api_key") String apiKey
    );
}