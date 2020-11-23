package me.smt.mediaddict.common;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.smt.mediaddict.R;
import me.smt.mediaddict.adapter.MoviesAdapter;
import me.smt.mediaddict.model.Movie;
import me.smt.mediaddict.model.dataAccess.listener.OnCheckIfMovieExistsListener;

/**
 * Clase que contiene diferentes métodos utilizados para
 * añadir y eliminar películas de Firebase.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class FirebaseUtils {

    /**
     * Atributo que contiene el TAG para las trazas de logging.
     */
    private static final String TAG = "FIREBASE_UTILS";

    /**
     * Atributo que define el nodo inicial de búsqueda.
     */
    private static final String NODENAME = "/users";

    /**
     * Atributo que devuelve al usuario actual de la sesión.
     */
    private static final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * Atributo que crea una nueva instancia de acceso a la BBDD de Firebase.
     */
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();

    /**
     * Atributo que define una referencia al nodo padre de la BBDD de Firebase.
     */
    private static final DatabaseReference dbRef = database.getReference(NODENAME);

    /**
     * Método que almacena todos los datos de una película al hacer clic
     * en el botón de favoritos.
     * @param movie la película a añadir.
     */
    public static void addToFavorite(Movie movie)  {
        dbRef.child(Objects.requireNonNull(user).getUid())
                .child("favs")
                .child(String.valueOf(movie.getId()))
                .setValue(movie)
                .addOnFailureListener(e -> Log.d(TAG, R.string.error_message_adding_movie_firebase  + " " + e.getMessage()));
    }

    /**
     * Método que elimina todos los datos de una película al hacer
     * clic en el botón de favoritos.
     * @param movieId el ID de la película a eliminar.
     */
    public static void deleteFromFavorite(int movieId) {
        dbRef.child(Objects.requireNonNull(user).getUid())
                .child("favs")
                .child(String.valueOf(movieId))
                .removeValue().addOnFailureListener(e -> Log.d(TAG, R.string.error_message_deleting_movie_firebase + " " + e.getMessage()));
    }

    /**
     * Método que recupera todas las películas almacenadas como favoritas
     * de un mismo usuario.
     * @param movieList listado de películas.
     * @param adapter adaptador que procesará la lista de películas.
     */
    public static void fetchFavoriteMoviesFromUser(List<Movie> movieList,  MoviesAdapter adapter) {
        dbRef.child(Objects.requireNonNull(user).getUid())
                .child("favs") // subnodo "favs" dentro de cada usuario
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Movie movie = ds.getValue(Movie.class);
                                    movieList.add(movie);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        }
                );
    }

    /**
     * Método para comprobar si una película está en Firebase
     * con el fin de marcar como favorito la película al entrar en detalles.
     * Utiliza un listener que, en base al valor recibido, actúa de una u otra forma
     * en consecuencia.
     * @param movieId el ID de la película.
     * @param listener el listener.
     */
    public static void isMovieOnFirebase(int movieId, final OnCheckIfMovieExistsListener listener) {
        dbRef.child(Objects.requireNonNull(user).getUid())
                .child("favs") // subnodo "favs" dentro de cada usuario
                .child(String.valueOf(movieId)) // subnodo de una película almacenada
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    listener.onCheckReceived(true);
                                } else {
                                    listener.onCheckReceived(false);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        }
                );
    }

    /**
     * Método que recupera todas las películas favoritas de
     * todos los usuarios de Firebase.
     */
    public static void fetchFavoritesFromAllUsers() {
        Query mQuery = dbRef.orderByKey();

        mQuery.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> snapshotIterator = snapshot.getChildren();
                        for (DataSnapshot dataSnapshot : snapshotIterator) {
                            DataSnapshot next = dataSnapshot.child("favs");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    /**
     * Método de almacenamiento de películas secundario.
     * Éste recibe un Map con los datos concretos que se quieren introducir
     * y los almacena en el subnodo "favs".
     * @param movieInfo datos a añadir.
     * @param movieId ID de la película.
     */
    public static void addToFavorite(Map<String, Object> movieInfo, int movieId) {
        /* Bloque de datos de ejemplo:
        Map<String, Object> movieInfo = new HashMap<>();
        movieInfo.put("movieTitle", movieTitle);
        movieInfo.put("movieBackdrop", movieBackdrop);
        movieInfo.put("movieRating", movieRating);
        movieInfo.put("movieSummary", movieSummary);
        movieInfo.put("moviePosterpath", moviePosterpath);
        movieInfo.put("movieReleaseDate", movieReleaseDate);
        movieInfo.put("movieGenreIds", movieGenreIds);*/

        dbRef.child(Objects.requireNonNull(user).getUid())
                .child("favs")
                .child(String.valueOf(movieId))
                .setValue(movieInfo)
                .addOnFailureListener(e -> Log.d(TAG, R.string.error_message_adding_movie_firebase  + " " + e.getMessage()));
    }
}