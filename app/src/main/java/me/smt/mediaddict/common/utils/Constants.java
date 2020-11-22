package me.smt.mediaddict.common.utils;

public class Constants {

    // APIs
    public static final String BASE_URL_TMDB = "https://api.themoviedb.org/3/";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";

    // SQLite, no utilizadas en este momento
    public static final String MOVIE_ID = "movie_id";
    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_BACKDROP = "movie_backdrop";
    public static final String MOVIE_RATING = "movie_rating";
    public static final String MOVIE_OVERVIEW = "movie_overview";
    public static final String MOVIE_POSTERPATH = "movie_posterpath";
    public static final String MOVIE_RELEASE_DATE = "movie_release_date";
    public static final String MOVIE_GENRES = "movie_genres";
    public static final String MOVIE_GENRES_ID = "movie_genres_id";

    // Constante para comprobar si una película está marcada como favorita
    public static final String MOVIE_FAVORITE_STATUS = "movie_favorite_status";

    // Búsquedas de películas
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    // Idioma por defecto
    public static final String LANGUAGE = "es";

    /*
     * Info de los tamaños de las imágenes:
     * https://www.themoviedb.org/talk/53c11d4ec3a3684cf4006400
     * https://www.themoviedb.org/talk/5aeaaf56c3a3682ddf0010de?language=es
     */
    public static final String IMAGE_BASE_URL_W500 = "https://image.tmdb.org/t/p/w500";
    public static final String IMAGE_BASE_URL_w780 = "https://image.tmdb.org/t/p/w780";

    // YouTube
    public static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=%s";
    public static final String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/0.jpg";

    // Layout
    public static final int GRID_ITEM = 0;
    public static final int LIST_ITEM = 1;
    public static final int GRID_COLUMN_SIZE = 140;

    // Firebase
    public static final int RC_SIGN_IN = 123;
}
