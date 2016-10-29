package pl.weeia.localannouncements.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.weeia.localannouncements.R;
import pl.weeia.localannouncements.activity.main.MainActivity;
import pl.weeia.localannouncements.activity.register.RegisterActivity;
import pl.weeia.localannouncements.api.Failure;
import pl.weeia.localannouncements.api.user.AuthenticationFailure;
import pl.weeia.localannouncements.service.user.ApplicationUser;
import pl.weeia.localannouncements.service.user.Authenticator;
import pl.weeia.localannouncements.shared.AndroidUiDoneCallback;
import pl.weeia.localannouncements.shared.AndroidUiFailCallback;
import pl.weeia.localannouncements.shared.BaseActivity;

import static pl.weeia.localannouncements.api.user.AuthenticationFailure.BAD_CREDENTIALS;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_edittext_username)
    protected EditText usernameEditText;

    @BindView(R.id.login_edittext_password)
    protected EditText passwordEditText;

    @Inject
    protected Authenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        getContainer().inject(this);
    }

    @OnClick(R.id.login_button_sign_in)
    public void onSignInButtonClick(Button button) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        authenticator
            .authenticateWithCredentials(username, password)
            .then(new AndroidUiDoneCallback<ApplicationUser>() {
                @Override
                public void onDone(ApplicationUser result) {
                    startMainActivity();
                }
            })
            .fail(new AndroidUiFailCallback<Failure<AuthenticationFailure>>() {
                @Override
                public void onFail(Failure<AuthenticationFailure> failure) {
                    if (failure.isBecauseOf(BAD_CREDENTIALS)) {
                       informAboutBadCredentials();
                    } else {
                        showErrorMessage();

                        if (failure.isBecauseOfThrowable()) {
                            failure.getThrowable().printStackTrace();
                        }
                    }
                }
            });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void informAboutBadCredentials() {
        Toast
            .makeText(LoginActivity.this, R.string.login_message_bad_credentials, Toast.LENGTH_LONG)
            .show();
    }

    private void showErrorMessage() {
        Toast
            .makeText(LoginActivity.this, R.string.login_message_error, Toast.LENGTH_SHORT)
            .show();
    }

    @OnClick(R.id.login_textview_sign_up)
    public void onSignUpTextViewClick() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
