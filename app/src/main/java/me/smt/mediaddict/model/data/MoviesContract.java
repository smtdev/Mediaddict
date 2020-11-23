package me.smt.mediaddict.model.data;

import android.provider.BaseColumns;

/**
 * Clase que actúa de "contrato" con la BBDD local, estableciendo
 * los atributos que tendrá
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class MoviesContract {

    /**
     * Clase interna para definir los atributos de la BBDD local
     * @see BaseColumns
     */
    public static final class MoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_MOVIE_GENRES_ID = "genres";

        /**
         * Método que define las columnas de la BBDD local
         */
        public static final String[] MOVIE_COLUMNS = {
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_TITLE,
                COLUMN_MOVIE_POSTER_PATH,
                COLUMN_MOVIE_OVERVIEW,
                COLUMN_MOVIE_VOTE_AVERAGE,
                COLUMN_MOVIE_RELEASE_DATE,
                COLUMN_MOVIE_BACKDROP_PATH,
                COLUMN_MOVIE_GENRES_ID
        };
    }
}
