package me.sergiomartin.tvshowmovietracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private static final String PROVEEDOR_DESCONOCIDO = "Proveedor desconocido";
    private static final String FIREBASE = "firebase";

    @BindView(R.id.imgPhotoProfile)
    ImageView imgPhotoProfile;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvProvider)
    TextView tvProvider;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = (FirebaseAuth.AuthStateListener) (firebaseAuth) -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                onSetDataUser(user.getDisplayName(), user.getEmail(), user.getProviderId() != null ? user.getProviderId() : PROVEEDOR_DESCONOCIDO);
            } else {
                onSignedOutCleanup();
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setTosAndPrivacyPolicyUrls(
                                "https://www.websitepolicies.com/uploads/docs/terms-of-service-template.pdf",
                                "https://gdpr.eu/wp-content/uploads/2019/01/Our-Company-Privacy-Policy.pdf")
                        .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build())).build(), RC_SIGN_IN);
            }
        };
    }

    private void onSignedOutCleanup() {
        onSetDataUser("", "", "");
    }

    private void onSetDataUser(String userName, String email, String provider) {
        tvUserName.setText(userName);
        tvEmail.setText(email);

        Toast.makeText(this, "Proveedor ID: " + provider, Toast.LENGTH_LONG).show();

        int drawableRes;

        switch(provider) {
            case FIREBASE:
                drawableRes = R.drawable.ic_firebase;
                break;
            default:
                drawableRes = R.drawable.ic_block_helper;
                provider = PROVEEDOR_DESCONOCIDO;
        }
        tvProvider.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableRes, 0,0, 0);
        tvProvider.setText(provider);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bienvenido...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Algo falló, intente de nuevo.", Toast.LENGTH_SHORT).show();
            }
        }
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
            case R.id.action_sign_out:
                    AuthUI.getInstance().signOut(this);
                    return true;
            default:
                    return super.onOptionsItemSelected(item);
        }
    }
}