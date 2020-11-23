package me.smt.mediaddict.common;

/**
 * Clase que contiene las constantes utilizadas a lo
 * largo de la aplicación.
 * @author Sergio Martín Teruel
 * @version 1.0
 */
public class Constants {

    /**
     * Constante que indica la URL base de la API de TMDb.
     */
    public static final String BASE_URL_TMDB = "https://api.themoviedb.org/3/";

    /**
     * Constante utilizada en el cacheado con Retrofit y okHttp.
     */
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";

    /**
     * Constante utilizada en el cacheado con Retrofit y okHttp.
     */
    public static final String HEADER_PRAGMA = "Pragma";

    /**
     * Constante utilizada para identificar una película en los Intents
     */
    public static final String MOVIE_ID = "movie_id";

    // SQLite, no utilizadas en este momento
    /*public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_BACKDROP = "movie_backdrop";
    public static final String MOVIE_RATING = "movie_rating";
    public static final String MOVIE_OVERVIEW = "movie_overview";
    public static final String MOVIE_POSTERPATH = "movie_posterpath";
    public static final String MOVIE_RELEASE_DATE = "movie_release_date";
    public static final String MOVIE_GENRES = "movie_genres";
    public static final String MOVIE_GENRES_ID = "movie_genres_id";
    // Constante para comprobar si una película está marcada como favorita
    public static final String MOVIE_FAVORITE_STATUS = "movie_favorite_status";*/

    /**
     * Constante que refiere a la categoría "Popular" de TMDb.
     */
    public static final String POPULAR = "popular";

    /**
     * Constante que refiere a la categoría "Mejor valoradas" de TMDb.
     */
    public static final String TOP_RATED = "top_rated";

    /**
     * Constante que refiere a la categoría "Próximamente" de TMDb.
     */
    public static final String UPCOMING = "upcoming";

    /**
     * Constante que indica el idioma por defecto de los datos
     * obtenidos de TMDb
     */
    public static final String LANGUAGE = "es";

    /*
     * Info de los tamaños de las imágenes:
     * https://www.themoviedb.org/talk/53c11d4ec3a3684cf4006400
     * https://www.themoviedb.org/talk/5aeaaf56c3a3682ddf0010de?language=es
     */
    /**
     * Constante que define el tamaño de las imágenes a width = 500.
     */
    public static final String IMAGE_BASE_URL_W500 = "https://image.tmdb.org/t/p/w500";

    /**
     * Constante que define el tamaño de las imágenes a width = 780.
     */
    public static final String IMAGE_BASE_URL_w780 = "https://image.tmdb.org/t/p/w780";

    /**
     * Constante que define la URL por defecto de los vídeos de YouTube,
     * utilizado para los trailers.
      */
    public static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=%s";

    /**
     * Constante que define la URL de los thumbnails de los vídeos de YouTube,
     * utilizado para los trailers.
     */
    public static final String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/0.jpg";

    /**
     * Constante que define el listado de un RecyclerView a GRID.
     */
    public static final int GRID_ITEM = 0;

    /**
     * Constante que define el listado de un RecyclerView a LIST.
     */
    public static final int LIST_ITEM = 1;

    /**
     * Constante que define el tamaño de las columnas de los listados tipo GRID.
     */
    public static final int GRID_COLUMN_SIZE = 140;

    /**
     * Constante que se utiliza para la comprobación de login correcto
     * por parte de Firebase.
     */
    public static final int RC_SIGN_IN = 123;
}
