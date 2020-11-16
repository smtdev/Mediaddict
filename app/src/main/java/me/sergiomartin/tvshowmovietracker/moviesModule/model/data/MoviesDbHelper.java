package me.sergiomartin.tvshowmovietracker.moviesModule.model.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app.db";
    private static final int DATABASE_VERSION = 2;
    private static final String SQL_DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";
    public static final String LOGTAG = "FAVORITE";

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        Log.d(LOGTAG, "Database opened.");
        db = dbHandler.getWritableDatabase();
    }

    public void close() {
        Log.d(LOGTAG, "Database closed.");
        dbHandler.close();
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_USERRATING + " REAL NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POSTERPATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_SUMMARY + " TEXT NOT NULL" +
                "); ";

        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }

    public void saveMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MoviesContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MoviesContract.MoviesEntry.COLUMN_USERRATING, movie.getRating());
        values.put(MoviesContract.MoviesEntry.COLUMN_POSTERPATH, movie.getPosterPath());
        values.put(MoviesContract.MoviesEntry.COLUMN_SUMMARY, movie.getOverview());

        db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteSavedMovie(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MoviesContract.MoviesEntry.TABLE_NAME, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=" + id, null);
    }

    public List<Movie> getSavedMovies(){
        String[] columns = {
                MoviesContract.MoviesEntry._ID,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
                MoviesContract.MoviesEntry.COLUMN_TITLE,
                MoviesContract.MoviesEntry.COLUMN_USERRATING,
                MoviesContract.MoviesEntry.COLUMN_POSTERPATH,
                MoviesContract.MoviesEntry.COLUMN_SUMMARY
        };

        String sortOrder = MoviesContract.MoviesEntry._ID + " ASC";
        List<Movie> favoriteList = new ArrayList<>();

        Log.d("RecuperandoPeliculas", "dentro de getSavedMovies");

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE)));
                movie.setRating((float) Double.parseDouble(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTERPATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_SUMMARY)));

                favoriteList.add(movie);

            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return favoriteList;
    }
}
