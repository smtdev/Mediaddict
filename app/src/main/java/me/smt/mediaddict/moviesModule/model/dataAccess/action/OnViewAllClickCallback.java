package me.smt.mediaddict.moviesModule.model.dataAccess.action;

import android.widget.Button;

import me.smt.mediaddict.moviesModule.model.Movie;

public interface OnViewAllClickCallback {
    // Utilizado para la transición de la pestaña "Home"
    // a su lista correspondiente
    void onClick(Movie movie, Button buttonView);
}
