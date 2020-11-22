package me.smt.mediaddict.moviesModule.model.dataAccess.get;

import java.util.List;

import me.smt.mediaddict.moviesModule.model.Trailer;

public interface OnGetTrailersCallback {
    void onSuccess(List<Trailer> trailers);

    void onError();
}