package me.sergiomartin.tvshowmovietracker.moviesModule.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Genre;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.OnMoviesClickCallback;
import me.sergiomartin.tvshowmovietracker.moviesModule.module.GlideApp;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> implements Filterable {

    /**
     * Image size info:
     * https://www.themoviedb.org/talk/53c11d4ec3a3684cf4006400
     * https://www.themoviedb.org/talk/5aeaaf56c3a3682ddf0010de?language=es
     */
    private String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final int LIST_ITEM = 0;
    private static final int GRID_ITEM = 1;

    private List<Movie> movies;
    private List<Movie> filteredMovies;
    private List<Genre> genres;
    private OnMoviesClickCallback callback;

    private boolean isSwitchView = true;
    private boolean isLinearLayout;

    public MoviesAdapter(List<Movie> movies, OnMoviesClickCallback callback) {
        this.callback = callback;
        this.movies = movies;
        this.filteredMovies = movies;
        isLinearLayout = false;
    }

    public MoviesAdapter(List<Movie> movies, List<Genre> genres, OnMoviesClickCallback callback) {
        this.callback = callback;
        this.movies = movies;
        this.filteredMovies = movies;
        this.genres = genres;
        isLinearLayout = true;
    }

    @NotNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
        if (isLinearLayout) {
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
        //holder.bind(filteredMovies.get(position));
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (isSwitchView) {
            return LIST_ITEM;
        } else {
            return GRID_ITEM;
        }
    }

    public boolean toggleItemViewType() {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }

    @Override
    public int getItemCount() {
        return movies.size();
        //return filteredMovies.size();
    }

    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        //filteredMovies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                if (charString.isEmpty()) {
                    filteredMovies = movies;
                } else {
                    ArrayList<Movie> filterMoviesList = new ArrayList<>();

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
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_movie_release_date)
        @Nullable
        TextView itemMovieReleaseDate;

        @Nullable
        @BindView(R.id.item_movie_title)
        TextView itemMovieTitle;

        @Nullable
        @BindView(R.id.item_movie_genre)
        TextView itemMovieGenre;

        @Nullable
        @BindView(R.id.item_movie_rating)
        TextView itemMovieRating;

        @Nullable
        @BindView(R.id.item_movie_poster)
        ImageView itemMoviePoster;

        Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie, itemMoviePoster);
                }
            });

        }

        public void bind(@NotNull Movie movie) {
            this.movie = movie;
            Log.d("MoviesAdapter", "Dentro de bind: " + movie.getTitle());
            if (isLinearLayout) {
                itemMovieReleaseDate.setText(movie.getReleaseDate().split("-")[0]);
                itemMovieTitle.setText(movie.getTitle());
                itemMovieRating.setText(String.valueOf(movie.getRating()));
                itemMovieGenre.setText(getGenres(movie.getGenreIds()));
            } else {
                itemMovieTitle.setText(movie.getTitle());

            }
            GlideApp.with(itemView.getContext())
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
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
