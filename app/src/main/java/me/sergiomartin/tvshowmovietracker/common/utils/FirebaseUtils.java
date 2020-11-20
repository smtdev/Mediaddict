package me.sergiomartin.tvshowmovietracker.common.utils;

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

import me.sergiomartin.tvshowmovietracker.moviesModule.adapter.MoviesAdapter;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.listener.OnCheckIfMovieExistsListener;

public class FirebaseUtils {

    private static final String TAG = "FIREBASE_UTILS";
    private static final String NODENAME = "/users";
    private static final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();;
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference dbRef = database.getReference(NODENAME);;

    /*
     * Almacena todos los datos de una película al hacer clic
     * en el botón de favoritos
     */
    public static void addToFavorite(Movie movie)  {
        dbRef.child(Objects.requireNonNull(user).getUid())
                .child("favs")
                .child(String.valueOf(movie.getId()))
                .setValue(movie)
                .addOnFailureListener(e -> Log.d(TAG, "Error añadiendo a favoritos: " + e.getMessage()));
    }

    /*
     * Elimina todos los datos de una película al hacer clic en el botón
     * de favoritos
     */
    public static void deleteFromFavorite(int movieId) {
        dbRef.child(Objects.requireNonNull(user).getUid())
                .child("favs")
                .child(String.valueOf(movieId))
                .removeValue().addOnFailureListener(e -> Log.d(TAG, "Error eliminando de favoritos: " + e.getMessage()));
    }

    /*
     * Recupera todas las películas almacenadas como favoritas de un mismo usuario
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

    /*
     * Método para comprobar si una película está en Firebase
     * con el fin de marcar como favorito la película al entrar en detalles.
     * Utiliza un listener que, en base al valor recibido, actúa de una u otra forma
     * en consecuencia
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

    /*
     * Recuperando todas las películas favoritas de todos los usuarios
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
                            Log.d(TAG, "Leyendo favoritos...: " + "Datos = " + next.getValue());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }


    /*
     * Método de almacenamiento de películas secundario
     * Éste recibe un Map con los datos concretos que se quieren introducir
     * y los almacena en el subnodo "favs"
     */
    public static void addToFavorite(Map<String, Object> movieInfo, int movieId) {
        /* Bloque de datos:
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
                .addOnFailureListener(e -> Log.d(TAG, "Error añadiendo a favoritos: " + e.getMessage()));
    }
}