package me.smt.mediaddict.api;

import java.util.List;

import me.smt.mediaddict.model.Language;
import me.smt.mediaddict.model.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface que contiene cada llamada REST utilizada en
 * la aplicación para obtener los diferentes datos que se
 * van mostrando a lo largo de ésta.
 */
public interface MovieApiInterface {

    /**
     * Llamada REST que se encarga de buscar las películas
     * que cumplan los parámetros de la query enviada.
     * @param apiKey API Key de TMDb.
     * @param language idioma de la búsqueda.
     * @param page número de página.
     * @param query consulta a enviar.
     * @return listado de las películas encontradas.
     */
    @GET("search/movie")
    Call<MoviesListResponse> getSearchMovies (
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page,
            @Query("query") String query
        );

    /**
     * Llamada REST que se encarga de devolver un listado de
     * las películas más populares según TMDb.
     * @param apiKey API Key de TMDb.
     * @param language idioma de la petición.
     * @param page número de página.
     * @param region de qué región será la lista.
     * @return listado de las películas obtenidas.
     */
    @GET("movie/popular")
    Call<MoviesListResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page,
            @Query("region") String region
    );

    /**
     * Llamada REST que se encarga de devolver un listado de
     * las películas mejor valoradas según TMDb.
     * @param apiKey API Key de TMDb.
     * @param language idioma de la petición.
     * @param page número de página.
     * @param region de qué región será la lista.
     * @return listado de las películas obtenidas.
     */
    @GET("movie/top_rated")
    Call<MoviesListResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page,
            @Query("region") String region
    );

    /**
     * Llamada REST que se encarga de devolver un listado de
     * las películas que saldrán próximamente según TMDb.
     * @param apiKey API Key de TMDb.
     * @param language idioma de la petición.
     * @param page número de página.
     * @param region de qué región será la lista.
     * @return listado de las películas obtenidas.
     */
    @GET("movie/upcoming")
    Call<MoviesListResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page,
            @Query("region") String region
    );

    /**
     * Llamada REST que se encarga de devolver todos los datos que
     * TMDb tiene sobre una película en cuestión.
     * @param id el ID de la película buscada.
     * @param apiKey API Key de TMDb.
     * @param language idioma de la petición.
     * @return datos de la película.
     */
    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    /**
     * Llamada REST que se encarga de devolver todos los tráilers
     * relacionados con la película solicitada.
     * @param id ID de la película solicitada.
     * @param apiKey API Key de TMDb.
     * @return listado de tráilers disponibles.
     */
    @GET("movie/{movie_id}/videos")
    Call<TrailerListResponse> getTrailers(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey
    );

    /**
     * Llamada REST que se encarga de devolver un listado de
     * todos los géneros disponibles de películas en TMDb.
     * @param apiKey API Key de TMDb.
     * @param language idioma de la petición.
     * @return listado de géneros obtenidos.
     */
    @GET("genre/movie/list")
    Call<GenresListResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    /**
     * Llamada REST que se encarga de devolver un listado de
     * todos los lenguajes disponibles de películas en TMDb.
     * @param apiKey API Key de TMDb.
     * @return listado de lenguajes obtenidos.
     */
    @GET("configuration/languages")
    Call<List<Language>> getLanguages (
            @Query("api_key") String apiKey
    );
}
