package me.sergiomartin.tvshowmovietracker.moviesModule.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.common.utils.Constants;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Genre;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.action.OnMoviesClickCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.module.GlideApp;
import me.sergiomartin.tvshowmovietracker.moviesModule.ui.AnimationView;

//public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> implements Filterable {
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>  {

    private Context context;
    private List<Movie> movies;
    private List<Genre> genres;
    private OnMoviesClickCallback callback;
    private boolean isSwitchView;

    public MoviesAdapter(List<Movie> movies, Context context, OnMoviesClickCallback callback) {
        this.callback = callback;
        this.context = context;
        this.movies = movies;
        isSwitchView = false;
    }

    public MoviesAdapter(List<Movie> movies, OnMoviesClickCallback callback) {
        this.callback = callback;
        this.movies = movies;
        isSwitchView = false;
    }

    public MoviesAdapter(List<Movie> movies, List<Genre> genres, OnMoviesClickCallback callback) {
        this.callback = callback;
        this.movies = movies;
        this.genres = genres;
        isSwitchView = true;
    }

    @NotNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == Constants.LIST_ITEM) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.movie_card_layout, parent, false);
        } else {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.movie_grid_layout, parent, false);
        }

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));

        AnimationView.setScrollingAnimation(holder.itemView, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isSwitchView) {
            return Constants.LIST_ITEM;
        } else {
            return Constants.GRID_ITEM;
        }
    }

    public boolean toggleItemViewType() {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    /*getFilter()

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                if (charString.isEmpty()) {
                    filteredMovies = movies;
                } else {
                    List<Movie> filterMoviesList = new ArrayList<>();

                    for (Movie movie : movies) {
                        if (movie.getTitle().toLowerCase().contains(charString)) {
                            filterMoviesList.add(movie);
                        }
                    }
                    filteredMovies = filterMoviesList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredMovies;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredMovies = (ArrayList<Movie>) results.values;
                notifyDataSetChanged();
            }
        };
    }*/

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @Nullable @BindView(R.id.item_movie_release_date) TextView itemMovieReleaseDate;
        @Nullable @BindView(R.id.item_movie_title) TextView itemMovieTitle;
        @Nullable @BindView(R.id.item_movie_genre) TextView itemMovieGenre;
        @Nullable @BindView(R.id.item_movie_rating) TextView itemMovieRating;
        @Nullable @BindView(R.id.item_movie_poster) ImageView itemMoviePoster;

        private Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                callback.onClick(movie, itemMoviePoster);
            });
        }

        public void bind(@NotNull Movie movie) {
            this.movie = movie;
            Log.d("MoviesAdapterBind", "Dentro de bind: " + movie.getTitle());
            if (isSwitchView) {
                itemMovieReleaseDate.setText(movie.getReleaseDate().split("-")[0]);
                itemMovieTitle.setText(movie.getTitle());
                itemMovieRating.setText(String.valueOf(movie.getRating()));
                itemMovieGenre.setText(getGenres(movie.getGenreIds()));
            } else {
                itemMovieTitle.setText(movie.getTitle());
            }
            GlideApp.with(itemView.getContext())
                    .load(Constants.IMAGE_BASE_URL_W500 + movie.getPosterPath())
                    .into(itemMoviePoster);

        }

        private String getGenres(@NotNull List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (Genre genre : genres) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            // concatenar géneros
            return TextUtils.join(", ", movieGenres);
        }
    }
}
