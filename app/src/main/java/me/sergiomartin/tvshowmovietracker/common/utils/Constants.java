package me.sergiomartin.tvshowmovietracker.common.utils;

public class Constants {

    // APIs
    public static final String BASE_URL_TMDB = "https://api.themoviedb.org/3/";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";

    // SQLite
    public static final String MOVIE_ID = "movie_id";
    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_THUMBNAIL = "movie_thumbnail";
    public static final String MOVIE_RATING = "movie_rating";
    public static final String MOVIE_SUMMARY = "movie_summary";
    public static final String MOVIE_POSTERPATH = "movie_posterpath";

    // Constante para comprobar si una película está marcada como favorita
    public static final String MOVIE_FAVORITE_STATUS = "movie_favorite_status";

    // Búsquedas de películas
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";
    public static final String SEARCH = "search_movies";

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
    public static final int LIST_ITEM = 0;
    public static final int GRID_ITEM = 1;
    public static int view = LIST_ITEM;
}
