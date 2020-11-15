package me.sergiomartin.tvshowmovietracker.moviesModule.model.data;

import android.provider.BaseColumns;

public class MoviesContract {

    public static final class MoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_ID = "movieid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USERRATING = "userrating";
        public static final String COLUMN_POSTERPATH = "posterpath";
        public static final String COLUMN_SUMMARY = "overview";


    }
}
