package me.sergiomartin.tvshowmovietracker.moviesModule.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.common.utils.Constants;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Genre;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.action.OnMoviesClickCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.module.GlideApp;
import me.sergiomartin.tvshowmovietracker.moviesModule.ui.AnimationView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>  {

    private Context context;
    private List<Movie> movies;
    private List<Genre> genres;
    private OnMoviesClickCallback callback;
    private int viewLayoutType;
    private boolean isSwitchView;
    private boolean isFavMovie = false;

    public MoviesAdapter(List<Movie> movies, Context context, OnMoviesClickCallback callback, int viewLayoutType) {
        this.callback = callback;
        this.context = context;
        this.movies = movies;
        isSwitchView = false;
        this.viewLayoutType = viewLayoutType;
        isFavMovie = true;
        genres = new ArrayList<>();
    }

    public MoviesAdapter(List<Movie> movies, OnMoviesClickCallback callback) {
        this.callback = callback;
        this.movies = movies;
        isSwitchView = false;
        viewLayoutType = Constants.GRID_ITEM;
    }

    public MoviesAdapter(List<Movie> movies, List<Genre> genres, OnMoviesClickCallback callback) {
        this.callback = callback;
        this.movies = movies;
        this.genres = genres;
        isSwitchView = true;
        viewLayoutType = Constants.LIST_ITEM;
    }

    @NotNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == Constants.LIST_ITEM) {
        //if (isSwitchView) {
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
        //if (isSwitchView) {
        if(viewLayoutType == Constants.LIST_ITEM) {
            return Constants.LIST_ITEM;
        } else {
            return Constants.GRID_ITEM;
        }
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
            //if (isSwitchView) {
            if (viewLayoutType == Constants.LIST_ITEM) {
                if (itemMovieReleaseDate != null) {
                    itemMovieReleaseDate.setText(movie.getReleaseDate().split(String.valueOf(R.string.release_date_movie_regex_separator))[0]);
                }
                if (itemMovieTitle != null) {
                    itemMovieTitle.setText(movie.getTitle());
                }
                if (itemMovieRating != null) {
                    itemMovieRating.setText(String.valueOf(movie.getRating()));
                }
                if (itemMovieGenre != null) {
                    if(isFavMovie) {
                        String genres = movie.getGenreIdString().replace("[", "").replace("]", "");
                        List<String> tempList = new ArrayList<>(Arrays.asList(genres.split(",")));
                        List<Integer> movieGenreIdList = new ArrayList<>();

                        for (String s : tempList) {
                            movieGenreIdList.add(Integer.parseInt(s.trim()));
                        }

                        itemMovieGenre.setText(getGenres(movieGenreIdList));
                    } else {
                        itemMovieGenre.setText(getGenres(movie.getGenreIds()));
                    }

                }
            } else {
                if (itemMovieTitle != null) {
                    itemMovieTitle.setText(movie.getTitle());
                }
            }
            if (itemMoviePoster != null) {
                GlideApp.with(itemView.getContext())
                        .load(Constants.IMAGE_BASE_URL_W500 + movie.getPosterPath())
                        .into(itemMoviePoster);
                AnimationView.outlineImageview(itemMoviePoster);
            }
        }

        private String getGenres(@NotNull List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (Genre genre : genres) {
                    if (Objects.equals(genre.getId(), genreId)) {
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
