package me.smt.mediaddict.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.smt.mediaddict.R;
import me.smt.mediaddict.common.Constants;
import me.smt.mediaddict.model.Movie;
import me.smt.mediaddict.model.dataAccess.action.OnMoviesClickCallback;
import me.smt.mediaddict.module.GlideApp;
import me.smt.mediaddict.ui.AnimationView;

/**
 * Clase que funciona como un puente entre una vista y los datos de las películas
 * que se cargarán en ella. Orientada a la funcionalidad de la búsqueda.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    /**
     * Atributo que representa las respuestas a ciertos métodos.
     */
    private OnMoviesClickCallback callback;

    /**
     * Atributo que representa una lista de películas.
     */
    private List<Movie> movieSearchList;

    /**
     * Constructor de la clase.
     * @param movieSearchList listado de películas.
     * @param callback respuesta de la llamada.
     */
    public SearchAdapter(List<Movie> movieSearchList, OnMoviesClickCallback callback) {
        Log.d("InstanciaSearchAdapter", "nueva instancia de un SearchAdapter");
        this.movieSearchList = movieSearchList;
        this.callback = callback;
    }

    /**
     * Método que iguala el valor de la lista de la clase
     * al de la recibida por parámetro y notifica.
     * @param movieList
     */
    public void setData(List<Movie> movieList){
        this.movieSearchList = movieList;
        notifyDataSetChanged();
    }

    /**
     * Método que se encarga de inflar una u otra vista dependiendo del estado
     * de ésta.
     * @param parent container de vistas.
     * @param viewType tipo de vista.
     * @return MovieViewHolder la vista a inflar.
     */
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_grid_layout, parent, false);

        return new SearchViewHolder(view);
    }

    /**
     * Método que es llamada por un RecyclerView para mostrar
     * info en una posición específica.
     * @param holder contenedor de datos.
     * @param position posición en la que mostrar información.
     */
    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        holder.bind(movieSearchList.get(position));

        AnimationView.setScrollingAnimation(holder.itemView, position);
    }

    /**
     * Método que devuelve el tamaño del ArrayList con
     * el total de películas.
     * @return int el total de películas.
     */
    @Override
    public int getItemCount() {
        if(movieSearchList == null) {
            return 0;
        }
        return movieSearchList.size();
    }

    /**
     * Método que limpia la lista de películas y avisa al
     * RecyclerView del cambio para que se actualice.
     */
    public void clearMovies() {
        int size = getItemCount();

        movieSearchList.clear();
        notifyItemRangeRemoved(0, size);
        notifyDataSetChanged();
    }

    /**
     * Método que añade la lista de películas recibida como parámetro
     * a la lista de la clase y notifica al RecyclerView.
     * @param movies
     */
    public void appendResultMovies(List<Movie> movies) {
        movieSearchList.addAll(movies);
        notifyDataSetChanged();
    }

    /**
     * Clase interna para gestionar los datos y mostrarlos en
     * la aplicación.
     */
    class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_movie_title) TextView itemMovieTitle;
        @BindView(R.id.item_movie_poster) ImageView itemMoviePoster;

        /**
         * Atributo que representa a una película en concreto.
         */
        private Movie movie;

        /**
         * Constructor de la clase.
         * @param itemView el tipo de vista.
         */
        public SearchViewHolder(View itemView) {
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
            Log.d("SearchAdapterbind", "Dentro del bind: " + movie.getTitle());

            itemMovieTitle.setText(movie.getTitle());
            GlideApp.with(itemView.getContext())
                    .load(Constants.IMAGE_BASE_URL_W500 + movie.getPosterPath())
                    .into(itemMoviePoster);
            AnimationView.outlineImageview(itemMoviePoster);
        }
    }
}
