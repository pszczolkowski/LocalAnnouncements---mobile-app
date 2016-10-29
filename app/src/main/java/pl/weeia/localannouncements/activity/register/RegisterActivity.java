package pl.weeia.localannouncements.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.weeia.localannouncements.R;
import pl.weeia.localannouncements.activity.main.MainActivity;
import pl.weeia.localannouncements.api.Failure;
import pl.weeia.localannouncements.api.user.RegistrationFailure;
import pl.weeia.localannouncements.api.user.RegistrationForm;
import pl.weeia.localannouncements.api.user.UserApi;
import pl.weeia.localannouncements.shared.AndroidUiDoneCallback;
import pl.weeia.localannouncements.shared.AndroidUiFailCallback;
import pl.weeia.localannouncements.shared.BaseActivity;
import pl.weeia.localannouncements.shared.enums.Gender;

import static android.view.View.VISIBLE;
import static pl.weeia.localannouncements.api.user.RegistrationFailure.EMAIL_ALREADY_TAKEN;
import static pl.weeia.localannouncements.api.user.RegistrationFailure.USERNAME_ALREADY_TAKEN;
import static pl.weeia.localannouncements.shared.enums.Gender.FEMALE;
import static pl.weeia.localannouncements.shared.enums.Gender.MALE;

public class RegisterActivity extends BaseActivity {

    @Inject
    protected UserApi userApi;

    @BindView(R.id.register_edittext_username)
    protected EditText usernameEditText;
    @BindView(R.id.register_edittext_password)
    protected EditText passwordEditText;
    @BindView(R.id.register_edittext_email)
    protected EditText emailEditText;
    @BindView(R.id.register_edittext_age)
    protected EditText ageEditText;
    @BindView(R.id.register_radiogroup_gender)
    protected RadioGroup genderRadioGroup;
    @BindView(R.id.register_textview_error)
    protected TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        getContainer().inject(this);
    }

    @OnClick(R.id.register_button_sign_up)
    public void onSignUpButtonClick(Button button) {
        RegistrationForm registrationForm = generateRegistrationForm();

        usernameEditText.setError("blablabab");

        userApi
            .register(registrationForm)
            .then(new AndroidUiDoneCallback<Void>() {
                @Override
                public void onDone(Void dummy) {
                    startMainActivity();
                }
            }).fail(new AndroidUiFailCallback<Failure<RegistrationFailure>>() {
                @Override
                public void onFail(Failure<RegistrationFailure> failure) {
                    onRegistrationFailure(failure);
                }
            });
    }

    private RegistrationForm generateRegistrationForm() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        int age = Integer.parseInt(ageEditText.getText().toString());
        int checkedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        Gender gender = checkedGenderId == R.id.register_radiobutton_male ? MALE : FEMALE;

        return new RegistrationForm()
                .withUsername(username)
                .withPassword(password)
                .withAge(age)
                .withGender(gender)
                .withEmail(email);
    }

    private void startMainActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void onRegistrationFailure(Failure<RegistrationFailure> failure) {
        if (failure.isBecauseOf(USERNAME_ALREADY_TAKEN)) {
            showRegistrationError(R.string.register_message_username_taken);
        } else if (failure.isBecauseOf(EMAIL_ALREADY_TAKEN)) {
            showRegistrationError(R.string.register_message_email_taken);
        } else {
            showErrorMessage();

            if (failure.isBecauseOfThrowable()) {
                failure.getThrowable().printStackTrace();
            }
        }
    }

    private void showRegistrationError(int resourceId) {
        errorTextView.setText(getResources().getString(resourceId));
        errorTextView.setVisibility(VISIBLE);
    }

    private void showErrorMessage() {
        Toast
            .makeText(RegisterActivity.this, R.string.register_message_error, Toast.LENGTH_SHORT)
            .show();
    }

}
