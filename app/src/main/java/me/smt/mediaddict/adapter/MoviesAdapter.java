package me.smt.mediaddict.adapter;

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
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.smt.mediaddict.R;
import me.smt.mediaddict.common.Constants;
import me.smt.mediaddict.model.Genre;
import me.smt.mediaddict.model.Movie;
import me.smt.mediaddict.model.dataAccess.action.OnMoviesClickCallback;
import me.smt.mediaddict.module.GlideApp;
import me.smt.mediaddict.ui.AnimationView;

/**
 * Clase que funciona como un puente entre una vista y los datos de las películas
 * que se cargarán en ella.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>  {

    /**
     * Atributo que se refiere al contexto de la aplicación.
     */
    private Context context;

    /**
     * Atributo que representa un listado de películas.
     */
    private List<Movie> movies;

    /**
     * Atributo que representa un listado de géneros.
     */
    private List<Genre> genres;

    /**
     * Atributo que representa las respuestas a ciertos métodos.
     */
    private OnMoviesClickCallback callback;

    /**
     * Atributo que representa el modo de listado de películas.
     */
    private int viewLayoutType;

    /**
     * Atributo que representa si una película está marcada como favorita o no.
     */
    private boolean isFavMovie = false;

    /**
     * Constructor de la clase.
     * @param movies listado de películas.
     * @param context contexto de la aplicación.
     * @param callback respuesta de la petición.
     * @param viewLayoutType modo de listado.
     */
    public MoviesAdapter(List<Movie> movies, Context context, OnMoviesClickCallback callback, int viewLayoutType) {
        this.callback = callback;
        this.context = context;
        this.movies = movies;
        this.viewLayoutType = viewLayoutType;
        isFavMovie = true;
        //genres = new ArrayList<>();
    }

    /**
     * Constructor de la clase.
     * @param movies listado de películas.
     * @param callback respuesta de la petición.
     */
    public MoviesAdapter(List<Movie> movies, OnMoviesClickCallback callback) {
        this.callback = callback;
        this.movies = movies;
        viewLayoutType = Constants.GRID_ITEM;
    }

    /**
     * Constructor de la clase.
     * @param movies listado de películas.
     * @param genres listado de géneros.
     * @param callback respuesta de la petición.
     */
    public MoviesAdapter(List<Movie> movies, List<Genre> genres, OnMoviesClickCallback callback) {
        this.callback = callback;
        this.movies = movies;
        this.genres = genres;
        viewLayoutType = Constants.LIST_ITEM;
    }

    /**
     * Método que se encarga de inflar una u otra vista dependiendo del estado
     * de ésta.
     * @param parent container de vistas.
     * @param viewType tipo de vista.
     * @return MovieViewHolder la vista a inflar.
     */
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

    /**
     * Método que es llamada por un RecyclerView para mostrar
     * info en una posición específica.
     * @param holder contenedor de datos.
     * @param position posición en la que mostrar información.
     */
    @Override
    public void onBindViewHolder(@NotNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));

        AnimationView.setScrollingAnimation(holder.itemView, position);
    }

    /**
     * Método que devuelve el tipo de vista en una posición concreta.
     * @param position posición.
     * @return int el tipo de vista.
     */
    @Override
    public int getItemViewType(int position) {
        if(viewLayoutType == Constants.LIST_ITEM) {
            return Constants.LIST_ITEM;
        } else {
            return Constants.GRID_ITEM;
        }
    }

    /**
     * Método que devuelve el tamaño del ArrayList con
     * el total de películas.
     * @return int el total de películas.
     */
    @Override
    public int getItemCount() {
        return movies.size();
    }

    /**
     * Método que limpia la lista de películas y avisa al
     * RecyclerView del cambio para que se actualice.
     */
    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    /**
     * Método que añade las películas recibidas por parámetro
     * a la lista y notifica al RecyclerView del cambio para que se actualice.
     * @param moviesToAppend películas a añadir
     */
    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    /**
     * Clase interna para gestionar los datos y mostrarlos en
     * la aplicación.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {
        @Nullable @BindView(R.id.item_movie_release_date) TextView itemMovieReleaseDate;
        @Nullable @BindView(R.id.item_movie_title) TextView itemMovieTitle;
        @Nullable @BindView(R.id.item_movie_genre) TextView itemMovieGenre;
        @Nullable @BindView(R.id.item_movie_rating) TextView itemMovieRating;
        @Nullable @BindView(R.id.item_movie_poster) ImageView itemMoviePoster;

        /**
         * Atributo que representa a una película en concreto.
         */
        private Movie movie;

        /**
         * Constructor de la clase.
         * @param itemView el tipo de vista.
         */
        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> callback.onClick(movie, itemMoviePoster));
        }

        /**
         * Método que enlaza los datos con la vista.
         * @param movie película con los datos a enlazar.
         */
        public void bind(@NotNull Movie movie) {
            this.movie = movie;
            Log.d("MoviesAdapterBind", "Dentro de bind: " + movie.getTitle());
            if (viewLayoutType == Constants.LIST_ITEM) { // Modo ListLayoutManager
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
                    if(!isFavMovie) {
                        itemMovieGenre.setText(getGenres(movie.getGenreIds()));
                    } else {
                        itemMovieGenre.setText("");
                    }
                }
            } else { // Modo GridLayoutManager
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

        /**
         * Método que obtiene los géneros de una película en concreto
         * en base a los IDs obtenidos por la API.
         * @param genreIds IDs de los géneros.
         * @return géneros concatenados.
         */
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