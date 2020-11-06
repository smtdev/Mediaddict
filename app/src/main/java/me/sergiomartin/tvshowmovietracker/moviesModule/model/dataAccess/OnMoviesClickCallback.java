package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess;

import android.widget.ImageView;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;

public interface OnMoviesClickCallback {
    //void onClick(Movie movie);

    // Utilizado para la transición del Home a los detalles de la película
    void onClick(Movie movie, ImageView movieImageView);
}
