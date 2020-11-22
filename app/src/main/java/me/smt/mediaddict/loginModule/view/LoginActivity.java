package me.smt.mediaddict.loginModule.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.smt.mediaddict.R;
import me.smt.mediaddict.loginModule.LoginPresenter;
import me.smt.mediaddict.loginModule.LoginPresenterClass;
import me.smt.mediaddict.moviesModule.ui.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginView {

    public static final int RC_SIGN_IN = 21;
    @BindView(R.id.tv_preloading_login_message)
    TextView tvPreloadingLoginMessage;
    @BindView(R.id.pb_preloading_login)
    ProgressBar pbPreloadingLogin;

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preloading_login);
        ButterKnife.bind(this);

        mPresenter = new LoginPresenterClass(this);
        mPresenter.onCreate(); // nos registramos en EventBus
        mPresenter.getStatusAuth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    /**
     * Al hacer clic en cualquier tipo de modo de login, se recibirá un OnActivityResult.
     * Con este método, enviamos al presenter la validación del intento para su procesado
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.result(requestCode, resultCode, data);
    }

    /**
     * LoginView
     */
    @Override
    public void showProgress() {
        pbPreloadingLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbPreloadingLogin.setVisibility(View.GONE);
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP); // Limpia registro de LoginActivity una vez se ha iniciado sesión

        startActivity(intent);
        finish();
    }

    @Override
    public void openUILogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setTosAndPrivacyPolicyUrls("https://mockupToS.example",
                        "https://mockupPrivacyPolicy.example")
                .setAvailableProviders(providers)
                .setTheme(R.style.AppTheme)
                .setLogo(R.mipmap.ic_launcher_highres)
                .build(), RC_SIGN_IN);
    }

    @Override
    public void showLoginSuccessfully(Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        String email = "";

        if(response != null) {
            email = response.getEmail();
        }
        Toast.makeText(this, getString(R.string.login_message_success, email),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageStarting() {
        tvPreloadingLoginMessage.setText(R.string.login_message_loading);
    }

    @Override
    public void showError(int resMsg) {
        Toast.makeText(this, resMsg, Toast.LENGTH_LONG).show();
    }
}