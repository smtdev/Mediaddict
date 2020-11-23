package me.smt.mediaddict.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase que contiene diferentes métodos utilizados para
 * gestionar las SharedPreferences de un usuario de la aplicación.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class SharedPreferencesUtils {

    /**
     * Método que se utiliza para guardar el valor "insertado" al
     * añadir una película a la BBDD SQLite.
     * @param context el contexto de la aplicación.
     * @param movieId el ID de la película.
     * @param isMovieInserted estado de la inserción.
     */
    public static void setInsertState(Context context, String movieId, boolean isMovieInserted){
        SharedPreferences sharedpreferences = context.getSharedPreferences("insert_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(movieId, isMovieInserted);
        editor.apply();
    }

    /**
     * Método que se utiliza para recuperar el valor de una shared preference
     * para una película en concreto.
     * @param context el contexto de la aplicación.
     * @param movieId el ID de la aplicación.
     * @return
     */
    public static boolean getInsertState(Context context, String movieId){
        SharedPreferences sharedpreferences = context.getSharedPreferences("insert_data", Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(movieId, false);
    }

    /**
     * Método que limpia todas las shared preferences almacenadas.
     * @param context el contexto de la aplicación.
     */
    public static void clearSharedPreferences(Context context){
        context.getSharedPreferences("insert_data", Context.MODE_PRIVATE).edit().clear().apply();
    }
}