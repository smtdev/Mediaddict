package me.sergiomartin.tvshowmovietracker.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.uwetrottmann.androidutils.AndroidUtils;
import me.sergiomartin.tvshowmovietracker.R;

public class CommonUtils {
    /**
     * Convert minutes to HH:MM
     */
    public static String parseMinutesToHour(int totalTime) {
        int hours = totalTime / 60;
        int minutes = totalTime % 60;
        @SuppressLint("DefaultLocale") String finalTime = String.format("%dh %dmin", hours, minutes);
        return finalTime;
    }

    /**
     * Checks for an available network connection.
     */
    public static boolean isNotConnected(Context context) {
        boolean isConnected = AndroidUtils.isNetworkConnected(context);

        // display offline toast
        if (!isConnected) {
            Toast.makeText(context, R.string.offline, Toast.LENGTH_LONG).show();
        }

        return !isConnected;
    }

    /*
     * Utilidad para ajustar las columnas de un RecyclerView que cargue un GridLayoutManager
     * SO: https://stackoverflow.com/a/38472370/1552146
     */
    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }
}
