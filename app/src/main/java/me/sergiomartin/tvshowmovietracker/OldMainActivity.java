package me.sergiomartin.tvshowmovietracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class OldMainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private static final int RC_FROM_GALLERY = 101;

    private static final String PROVEEDOR_DESCONOCIDO = "Proveedor desconocido";
    private static final String FIREBASE = "firebase";
    private static final String GOOGLE = "google.com";

    private static final String MY_PHOTO_AUTH = "my_photo_auth";
    private static final String PATH_PROFILE = "profile";

    @BindView(R.id.imgPhotoProfile)
    CircleImageView imgPhotoProfile;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvProvider)
    TextView tvProvider;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvProgress)
    TextView tvProgress;
    /*@BindView(R.id.tvMovieInfo)
    TextView tvMovieInfo;*/

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_main_activity);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = (FirebaseAuth.AuthStateListener) (firebaseAuth) -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                onSetDataUser(user.getDisplayName(), user.getEmail(), user.getProviderId() != null ? user.getProviderId() : PROVEEDOR_DESCONOCIDO);
                loadImage(user.getPhotoUrl());
            } else {
                onSignedOutCleanup();

                AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.GoogleBuilder().build();

                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setTosAndPrivacyPolicyUrls(
                                "https://www.websitepolicies.com/uploads/docs/terms-of-service-template.pdf",
                                "https://gdpr.eu/wp-content/uploads/2019/01/Our-Company-Privacy-Policy.pdf")
                        .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), googleIdp))
                        .setTheme(R.style.BlueTheme)
                        //.setLogo(R.drawable.com_facebook_button_login_logo)
                        .build(), RC_SIGN_IN);
            }
        };

        //new MovieTask().execute();
    }

    private void onSignedOutCleanup() {
        onSetDataUser("", "", "");
    }

    private void onSetDataUser(String userName, String email, String provider) {
        tvUserName.setText(userName);
        tvEmail.setText(email);

        Toast.makeText(this, "Proveedor ID: " + provider, Toast.LENGTH_LONG).show();

        int drawableRes;

        switch (provider) {
            case FIREBASE:
                drawableRes = R.drawable.ic_firebase;
                break;
            case GOOGLE:
                drawableRes = R.drawable.ic_google;
                break;
            default:
                drawableRes = R.drawable.ic_block_helper;
                provider = PROVEEDOR_DESCONOCIDO;
        }
        tvProvider.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableRes, 0, 0, 0);
        tvProvider.setText(provider);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Bienvenido...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Algo falló, intente de nuevo.", Toast.LENGTH_SHORT).show();
                }
                break;
            case RC_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    progressBar.setVisibility(View.VISIBLE);

                    uploadImageTask(data.getData());
                    //uploadImageFile(data.getData());
                }
                break;
            default:
        }
    }

    private void uploadImageTask(Uri selectedImageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference reference = storage.getReference().child(PATH_PROFILE).child(MY_PHOTO_AUTH);

        Bitmap bitmap;


        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            bitmap = getResizedBitmap(bitmap, 1024);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = reference.putBytes(data);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressBar.setProgress((int) progress);
                    tvProgress.setText(String.format(" %s%%", progress));
                    tvProgress.animate().alpha(1).setDuration(200);
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            tvProgress.setText("Upload complete!");
                            tvProgress.animate().alpha(0).setDuration(2000);
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    if (user != null) {
                                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                                .setPhotoUri(uri)
                                                .build();

                                        user.updateProfile(request)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            loadImage(user.getPhotoUrl());
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OldMainActivity.this, "Error en la modificación.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= maxSize && height <= maxSize) {
            return bitmap;
        }

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private void uploadImageFile(Uri selectedImageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference reference = storage.getReference().child(PATH_PROFILE).child(MY_PHOTO_AUTH);

        if (selectedImageUri != null) {
            reference.putFile(selectedImageUri)
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressBar.setProgress((int) progress);
                            tvProgress.setText(String.format(" %s%%", progress));
                            tvProgress.animate().alpha(1).setDuration(200);
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            tvProgress.setText("Upload complete!");
                            tvProgress.animate().alpha(0).setDuration(2000);
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    if (user != null) {
                                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                                .setPhotoUri(uri)
                                                .build();

                                        user.updateProfile(request)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            loadImage(user.getPhotoUrl());
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OldMainActivity.this, "Error en la modificación.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadImage(Uri photoUrl) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        Glide.with(OldMainActivity.this)
                .load(photoUrl)
                .apply(options)
                .into(imgPhotoProfile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.imgPhotoProfile)
    public void onSelectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RC_FROM_GALLERY);
    }

    /*class MovieTask extends AsyncTask<Void, Void, Void> {

        TmdbApi api;
        String movieTitle;

        @Override
        protected Void doInBackground(Void... voids) {
            api = new TmdbApi("70f04380ffc1bbc4f477e94de17a34a7");
            movieTitle = api.getMovies().getMovie(741067,"es",null).getOverview();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tvMovieInfo.setText(movieTitle);
        }
    }*/
}