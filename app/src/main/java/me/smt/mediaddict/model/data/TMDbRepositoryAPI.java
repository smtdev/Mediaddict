package me.smt.mediaddict.model.data;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.smt.mediaddict.BuildConfig;
import me.smt.mediaddict.MyApplication;
import me.smt.mediaddict.common.utils.Constants;
import me.smt.mediaddict.api.GenresListResponse;
import me.smt.mediaddict.api.MovieApiInterface;
import me.smt.mediaddict.api.MoviesListResponse;
import me.smt.mediaddict.api.TrailerListResponse;
import me.smt.mediaddict.model.Language;
import me.smt.mediaddict.model.Movie;
import me.smt.mediaddict.model.dataAccess.action.OnSearchMovieCallback;
import me.smt.mediaddict.model.dataAccess.get.OnGetGenresCallback;
import me.smt.mediaddict.model.dataAccess.get.OnGetLanguagesCallback;
import me.smt.mediaddict.model.dataAccess.get.OnGetMovieCallback;
import me.smt.mediaddict.model.dataAccess.get.OnGetMoviesCallback;
import me.smt.mediaddict.model.dataAccess.get.OnGetTrailersCallback;
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

/**
 * Clase que se encarga de gestionar las diferentes
 * peticiones contra la API de TMDb.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class TMDbRepositoryAPI {

    /**
     * Atributo que contiene el TAG para las trazas de logging.
     */
    private static final String TAG = "TMDbRepositoryAPI";

    /**
     * Atributo que representa al repositorio de TMDb.
     */
    private static TMDbRepositoryAPI repository;

    /**
     * Atributo que referencia a la interface que contiene las
     * llamadas REST a la API.
     */
    private MovieApiInterface api;

    /**
     * Atributo que contiene el tamaño caché máximo.
     */
    private static final long cacheSize = 5 * 1024 * 1024; // 5 MB

    /**
     * Método constructor de la clase.
     * @param api la interface que contiene las llamadas REST.
     */
    private TMDbRepositoryAPI(MovieApiInterface api) {
        this.api = api;
    }

    /**
     * Método que crea una nueva instancia para poder realizar llamadas
     * a la API.
     * @return TMDbRepositoryAPI la instancia apuntando a la interface.
     */
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

    /**
     * Método para el caching con Retrofit.
     * Referencia: https://github.com/mitchtabian/Retrofit-Caching-Example
     */
    private static OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .cache(cache())
                .addInterceptor(httpLoggingInterceptor()) // utilizado si la red está ON u OFF
                .addNetworkInterceptor(networkInterceptor()) // utilizado solo si la red está ON
                .addInterceptor(offlineInterceptor())
                .build();
    }

    /**
     * Método que crea la caché de la aplicación.
     * @return Cache la nueva caché.
     */
    private static Cache cache(){
        return new Cache(new File(MyApplication.getInstance().getApplicationContext().getCacheDir(),"someIdentifier"), cacheSize);
    }

    /**
     * Método que se utilizará para ser llamado tanto si la red está disponible
     * como si no, para utilizar la caché en ese caso.
     * @return Interceptor qué tipo de petición se ha hecho.
     */
    private static Interceptor offlineInterceptor() {
        return chain -> {
            Log.d(TAG, "offline interceptor: llamado.");
            Request request = chain.request();

            // previene cachear cuando hay red disponible. Para eso se utiliza "networkInterceptor"
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
     * Método que será llamado solo si hay red disponible.
     * @return Interceptor la respuesta esperada.
     */
    private static Interceptor networkInterceptor() {
        return chain -> {
            Log.d(TAG, "network interceptor: llamado.");

            okhttp3.Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(5, TimeUnit.SECONDS)
                    .build();

            return response.newBuilder()
                    .removeHeader(Constants.HEADER_PRAGMA)
                    .removeHeader(Constants.HEADER_CACHE_CONTROL)
                    .header(Constants.HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();
        };
    }

    /**
     * Método que loggea las peticiones http.
     * @return HttpLoggingInterceptor el mensaje recibido.
     */
    private static HttpLoggingInterceptor httpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(message -> Log.d(TAG, "log: http log: " + message));
        httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    /**
     * Método que llama a la API para realizar la búsqueda de películas en TMDb.
     * Más info: https://developers.themoviedb.org/3/search/search-movies
     * @param page número de página.
     * @param query película busada.
     * @param callback respuesta de la llamada.
     */
    public void searchMovies(int page, String query, final OnSearchMovieCallback callback) {
        api.getSearchMovies(BuildConfig.API_KEY, Constants.LANGUAGE, page, query)
                .enqueue(new Callback<MoviesListResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<MoviesListResponse> call, @NotNull Response<MoviesListResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesListResponse moviesResponse = response.body();
                            if (moviesResponse != null) {
                                Log.d("API-Repo-SearchMovies", "Resultados: " + moviesResponse.getTotalResults());
                            }

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

    /**
     * Método que llama a la API para conseguir un listado de películas. Dependiendo
     * del valor que reciba en el parámetro "sortBy", realizará una búsqueda diferente.
     * Más info: https://developers.themoviedb.org/3/getting-started/search-and-query-for-details
     * @param page número de página.
     * @param sortBy qué tipo de listado se quiere recuperar.
     * @param callback respuesta de la llamada.
     */
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

    /**
     * Método que devuelve toda la información reference a la película enviada por parámetro.
     * @param movieId ID de la película a buscar.
     * @param callback respuesta de la llamada.
     */
    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, BuildConfig.API_KEY, Constants.LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NotNull Call<Movie> call, @NotNull Response<Movie> response) {
                        Log.e(TAG, "log: -----------------------------");
                        Log.d(TAG, "onResponse getMovie: " + response.body());

                        if(response.raw().networkResponse() != null) {
                            Log.d(TAG, "onResponse: la respuesta NO es cacheada.");
                        }
                        else if(response.raw().cacheResponse() != null) {
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

    /**
     * Método que se utiliza para devolver un listado de todos los tipos
     * de género disponibles para una película.
     * @param callback respuesta de la llamada.
     */
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

    /**
     * Método que se utiliza para recuperar todos los trailers de una
     * película en concreto, enviada por parámetro.
     * @param movieId ID de la película de la que se quieren recuperar los trailers.
     * @param callback respuesta de la llamada.
     */
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

    /**
     * Método que se utiliza para devolver los diferentes lenguajes hablados en una película
     * @param callback respuesta de la llamada
     */
    public void getLanguages(final OnGetLanguagesCallback callback) {
        api.getLanguages(BuildConfig.API_KEY)
                .enqueue(new Callback<List<Language>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Language>> call, @NotNull Response<List<Language>> response) {
                        if (response.isSuccessful()) {
                            List<Language> languages = response.body();
                            if (languages != null) {
                                for (Language lang : languages) {
                                    Log.d("Contenido de Langs", "Idiomas: "
                                            + lang.getLangIsoStandard() + ", "
                                            + lang.getName() + ", "
                                            + lang.getEnglishName()
                                            + ", ");
                                }
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
