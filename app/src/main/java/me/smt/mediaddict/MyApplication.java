package me.smt.mediaddict;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Clase que crea un Singleton de la aplicación.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    /**
     * Método llamado al iniciar la creación de la Activity.
     * Se instancia la aplicación.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        if(instance == null){
            instance = this;
        }
    }

    public static MyApplication getInstance(){
        return instance;
    }

    /**
     * Método que llama a la instancia a comprobar el estado de la conexión.
     * @return Boolean el estado de la conexión.
     */
    public static boolean hasNetwork(){
        return instance.isNetworkConnected();
    }

    /**
     * Método que comprueba el estado de red y devuelve el resultado.
     * @return Boolean estado de la conexión.
     */
    private Boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }
    }
}
