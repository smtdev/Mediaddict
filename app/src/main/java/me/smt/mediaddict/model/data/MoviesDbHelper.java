package me.smt.mediaddict.model.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.smt.mediaddict.model.Movie;

/**
 * Clase que se utiliza para la creación y control
 * de la base de datos local
 * @author Sergio Martín Teruel
 * @version 1.0
 * @see SQLiteOpenHelper
 **/
public class MoviesDbHelper extends SQLiteOpenHelper {

    /**
     * Atributo que contiene el nombre de la base de datos.
     */
    private static final String DATABASE_NAME = "movies.db";

    /**
     * Atributo que contiene la versión de la base de datos.
     */
    private static final int DATABASE_VERSION = 5;

    /**
     * Atributo que contiene una sentencia SQL para eliminar la tabla.
     */
    private static final String SQL_DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

    /**
     * Atributo que contiene el TAG para las trazas de logging.
     */
    public static final String TAG = "MoviesDbHelper";

    /**
     * Atributo que hace de manejador de la base de datos.
     */
    SQLiteOpenHelper dbHandler;

    /**
     * Atributo que referencia a la base de datos.
     */
    SQLiteDatabase db;

    /**
     * Constructor de la clase.
     * @param context El contexto de la aplicación
     */
    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Método para permitir la escritura en la base de datos.
     */
    public void open() {
        Log.d(TAG, "BBDD abierta.");
        db = dbHandler.getWritableDatabase();
    }

    /**
     * Método para cerrar el acceso a la base de datos.
     */
    public void close() {
        Log.d(TAG, "BBDD cerrada.");
        dbHandler.close();
    }

    /**
     * Es el método llamado la primera vez. Aquí es donde se crean y ponen
     * en marcha las tablas.
     * @param db Es la base de datos
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_GENRES_ID + " TEXT NOT NULL " +
                "); ";

        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * Este método es llamada cuando la base de datos necesita un upgrade.
     * Se elimina la tabla si existe y se vuelve a crear de nuevo.
     * @param db         La base de datos
     * @param oldVersion La antigua versión de la base de datos
     * @param newVersion La nueva versión de la base de datos
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Método que almacena las películas marcadas como favoritas en la base de datos.
     * @param movie Es la película a almacenar
     */
    public void saveMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getRating());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdrop());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_GENRES_ID, movie.getGenreIdString());

        db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Método que elimina la película enviada por parámetro.
     * @param id El ID de la película
     */
    public void deleteSavedMovie(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MoviesContract.MoviesEntry.TABLE_NAME, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=" + id, null);
    }

    /**
     * Método que devuelve todas las películas almacenadas
     * @return List<Movie> la lista de películas
     */
    public List<Movie> getSavedMovies(){
        String[] columns = {
                MoviesContract.MoviesEntry._ID,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_PATH,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_BACKDROP_PATH,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_GENRES_ID
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
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW)));
                movie.setRating((float) Double.parseDouble(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE))));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE)));
                movie.setBackdrop(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_BACKDROP_PATH)));
                movie.setGenreIdString(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_GENRES_ID)));

                favoriteList.add(movie);

            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return favoriteList;
    }
}
