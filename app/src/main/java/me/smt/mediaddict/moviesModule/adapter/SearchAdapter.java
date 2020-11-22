package me.smt.mediaddict.moviesModule.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.smt.mediaddict.R;
import me.smt.mediaddict.common.utils.Constants;
import me.smt.mediaddict.moviesModule.model.Movie;
import me.smt.mediaddict.moviesModule.model.dataAccess.action.OnMoviesClickCallback;
import me.smt.mediaddict.moviesModule.module.GlideApp;
import me.smt.mediaddict.moviesModule.ui.AnimationView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private OnMoviesClickCallback callback;
    private List<Movie> movieSearchList;

    public SearchAdapter(List<Movie> movieSearchList, OnMoviesClickCallback callback) {
        Log.d("InstanciaSearchAdapter", "nueva instancia de un SearchAdapter");
        this.movieSearchList = movieSearchList;
        this.callback = callback;
    }

    public void setData(ArrayList<Movie> data){
        this.movieSearchList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_grid_layout, parent, false);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        holder.bind(movieSearchList.get(position));

        AnimationView.setScrollingAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        if(movieSearchList == null) {
            return 0;
        }
        return movieSearchList.size();
    }

    public void clearMovies() {
        int size = getItemCount();

        movieSearchList.clear();
        notifyItemRangeRemoved(0, size);
        notifyDataSetChanged();
    }

    public void appendResultMovies(List<Movie> movies) {
        movieSearchList.addAll(movies);
        notifyDataSetChanged();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_movie_title) TextView itemMovieTitle;
        @BindView(R.id.item_movie_poster) ImageView itemMoviePoster;

        private Movie movie;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                callback.onClick(movie, itemMoviePoster);
            });
        }

        public void bind(@NotNull Movie movie) {
            this.movie = movie;
            Log.d("SearchAdapterbind", "Dentro del bind: " + movie.getTitle());

            itemMovieTitle.setText(movie.getTitle());
            GlideApp.with(itemView.getContext())
                    .load(Constants.IMAGE_BASE_URL_W500 + movie.getPosterPath())
                    .into(itemMoviePoster);
            AnimationView.outlineImageview(itemMoviePoster);
        }
    }
}
