package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.action;

import android.widget.Button;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;

public interface OnViewAllClickCallback {
    // Utilizado para la transición de la pestaña "Home"
    // a su lista correspondiente
    void onClick(Movie movie, Button buttonView);
}
