package me.smt.mediaddict.moviesModule.model.dataAccess.action;

import android.widget.ImageView;

import me.smt.mediaddict.moviesModule.model.Movie;

public interface OnMoviesClickCallback {
    //void onClick(Movie movie);

    // Utilizado para la transición del Home a los detalles de la película
    void onClick(Movie movie, ImageView movieImageView);
}
