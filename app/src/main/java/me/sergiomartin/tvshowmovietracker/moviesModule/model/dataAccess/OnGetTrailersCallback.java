package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess;

import java.util.List;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Trailer;

public interface OnGetTrailersCallback {
    void onSuccess(List<Trailer> trailers);

    void onError();
}