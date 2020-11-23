package me.smt.mediaddict.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.uwetrottmann.androidutils.AndroidUtils;
import me.smt.mediaddict.R;

/**
 * Clase que contiene métodos útiles para utilizar a lo largo
 * del código de la aplicación.
 * @author Sergio Martín Teruel
 * @version 1.0
 */
public class CommonUtils {

    /**
     * Método para convertir minutos en hora con formato "XXh YYm".
     * @param totalTime el tiempo en minutos .
     * @return el tiempo esperado.
     */
    public static String parseMinutesToHour(int totalTime) {
        int hours = totalTime / 60;
        int minutes = totalTime % 60;
        @SuppressLint("DefaultLocale") String finalTime = String.format("%dh %dmin", hours, minutes);
        return finalTime;
    }

    /**
     * Método que comprueba si hay conexión disponible.
     * @param context el contexto de la aplicación.
     * @return el estado de la red.
     */
    public static boolean isNotConnected(Context context) {
        boolean isConnected = AndroidUtils.isNetworkConnected(context);

        // display offline toast
        if (!isConnected) {
            Toast.makeText(context, R.string.offline, Toast.LENGTH_LONG).show();
        }

        return !isConnected;
    }

    /**
     * Método que se utiliza para calcular el ancho de las columnas de
     * un GridLayoutManager, en base a su dpi.
     * Más info: https://stackoverflow.com/a/38472370/1552146
     * @param context el contexto de la aplicación.
     * @param columnWidthDp el tamaño de las columnas.
     * @return el número de columnas totales.
     */
    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }
}
