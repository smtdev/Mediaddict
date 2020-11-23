package me.smt.mediaddict.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.smt.mediaddict.R;
import me.smt.mediaddict.common.Constants;

/**
 * Clase que se encarga de gestionar el login del usuario
 * conectado con Firebase. Carga una UI predefinida por el SDK
 * de Firebase UI.
 * @author Sergio Martín Teruel
 * @version 1.0
 * @see AppCompatActivity
 **/
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tv_preloading_login_message)
    TextView tvPreloadingLoginMessage;
    @BindView(R.id.pb_preloading_login)
    ProgressBar pbPreloadingLogin;

    /**
     * Atributo que crea una instancia de FirebaseAuth.
     */
    private FirebaseAuth mFirebaseAuth;

    /**
     * Atributo que crea un listener de FirebaseAuth.
     */
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    /**
     * Método llamado al iniciar la creación de la Activity.
     * @param savedInstanceState estado de la instancia.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preloading_login);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if(user != null) {
                pbPreloadingLogin.setVisibility(View.VISIBLE);
                tvPreloadingLoginMessage.setText(R.string.login_message_loading);
                openMainActivity();
            } else {
                openUILogin();
            }

        };
    }

    /**
     * Método que se llama cuando la Activity es visible para el usuario y está activa.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    /**
     * Método que se llama como parte del ciclo de vida de la Activity, cuando el usuario
     * ya no interactúa activamente con ésta, pero aún está visible en pantalla.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    /**
     * Método que abre la MainActivity una vez se ha hecho Login correcto.
     */
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP); // Limpia registro de LoginActivity una vez se ha iniciado sesión

        startActivity(intent);
        finish();
    }

    /**
     * Método para lanzar la UI predefinida de Firebase para el Login.
     */
    public void openUILogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setTosAndPrivacyPolicyUrls(getString(R.string.login_activity_tos_url),
                        getString(R.string.login_activity_privacypolicy_url))
                .setAvailableProviders(providers)
                .setTheme(R.style.AppTheme)
                .setLogo(R.mipmap.ic_launcher_highres)
                .build(), Constants.RC_SIGN_IN);
    }

    /**
     * Método que se llama cuando una Activity ha "salido", devolviendo el requestCode, resultCode
     * y los datos adicionales a ellos.
     * @param requestCode el código de solicitud suministrado al "startActivityForResult"
     * @param resultCode el código de resultado devuelto por la Activity secundaria
     * @param data un Intent que puede devolver datos con resultados.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.message_welcome_post_login, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.error_message_bad_login, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
