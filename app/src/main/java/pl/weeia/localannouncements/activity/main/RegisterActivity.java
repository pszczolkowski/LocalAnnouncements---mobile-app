package pl.weeia.localannouncements.activity.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import pl.weeia.localannouncements.R;
import pl.weeia.localannouncements.api.Failure;
import pl.weeia.localannouncements.api.user.AuthenticationFailure;
import pl.weeia.localannouncements.api.user.RegistrationFailure;
import pl.weeia.localannouncements.api.user.RegistrationForm;
import pl.weeia.localannouncements.api.user.UserApi;
import pl.weeia.localannouncements.service.user.ApplicationUser;
import pl.weeia.localannouncements.service.user.Authenticator;
import pl.weeia.localannouncements.shared.AndroidUiDoneCallback;
import pl.weeia.localannouncements.shared.AndroidUiFailCallback;
import pl.weeia.localannouncements.shared.BaseActivity;
import pl.weeia.localannouncements.shared.enums.Gender;

import static pl.weeia.localannouncements.api.user.AuthenticationFailure.BAD_CREDENTIALS;
import static pl.weeia.localannouncements.api.user.RegistrationFailure.EMAIL_ALREADY_TAKEN;
import static pl.weeia.localannouncements.api.user.RegistrationFailure.USERNAME_ALREADY_TAKEN;
import static pl.weeia.localannouncements.shared.enums.Gender.MALE;

public class RegisterActivity extends BaseActivity {


    @Inject
    protected UserApi userApi;

    @Inject
    protected Authenticator authenticator;

    private RadioGroup radioGroup;
    private RadioButton radioGender;
    private TextView register_err;
    private EditText loginText, passwordText, password_repeatText, ageText, emailText;
    private Spinner genderText;

    private String login, password, password_repeat, ageTxt, email;
    private Gender register_gender;

    //email pattern
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //orginal backgroundresource
    Drawable originalDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // These two lines are required for injection
        // They need to be copied for every Activity
        ButterKnife.bind(this);
        getContainer().inject(this);


        Button register = (Button)findViewById(R.id.buttonRegister);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        register_err = (TextView)findViewById(R.id.register_error);
        loginText = (EditText)findViewById(R.id.log_in);
        passwordText = (EditText)findViewById(R.id.password);
        password_repeatText = (EditText)findViewById(R.id.password_repeat);
        ageText = (EditText)findViewById(R.id.age);
        emailText = (EditText)findViewById(R.id.e_mail);
        //genderText = (Spinner)findViewById(R.id.gender);
        originalDrawable = loginText.getBackground();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean wrong_fill;
                int radio_index = radioGroup.getCheckedRadioButtonId();
                radioGender = (RadioButton)findViewById(radio_index);
                login = loginText.getText().toString();
                password = passwordText.getText().toString();
                password_repeat = password_repeatText.getText().toString();
                ageTxt = ageText.getText().toString();
                email = emailText.getText().toString();

                //set default view
                EditTextDefault();
                //check validation
                wrong_fill = Validation();
                if(wrong_fill == true)
                {

                }
                else
                {

                    if(radioGender.getText().toString().equals("Male"))
                    {
                        register_gender = Gender.MALE;
                    }
                    else
                    {
                        register_gender = Gender.FEMALE;
                    }

                    int age = Integer.parseInt(ageTxt);


                    RegistrationForm registrationForm = new RegistrationForm()
                            .withUsername(login)
                            .withPassword(password)
                            .withAge(age)
                            .withGender(register_gender)
                            .withEmail(email);


                    userApi
                            .register(registrationForm)
                            .then(new AndroidUiDoneCallback<Void>() {
                                @Override
                                public void onDone(Void dummy) {
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).fail(new AndroidUiFailCallback<Failure<RegistrationFailure>>() {
                        @Override
                        public void onFail(Failure<RegistrationFailure> failure) {
                            if (failure.isBecauseOfThrowable()) {
                                // TODO some error occured, maybe there;s no internet connection?
                                register_err.setText("Connect Error");
                                failure.getThrowable().printStackTrace();
                            } else if (failure.isBecauseOf(USERNAME_ALREADY_TAKEN)) {
                                // TODO that username is alredy used by some other dude
                                register_err.setText("User exist");
                            } else if (failure.isBecauseOf(EMAIL_ALREADY_TAKEN)) {
                                // TODO that email address is alredy used by some other dude
                                register_err.setText("E-mail is already use");
                            }
                        }
                    });

                }











                //end onClick
            }
        });

    }


    void EditTextDefault()
    {
        loginText.setBackground(originalDrawable);
        passwordText.setBackground(originalDrawable);
        password_repeatText.setBackground(originalDrawable);
        ageText.setBackground(originalDrawable);
        emailText.setBackground(originalDrawable);
        register_err.setText("");
    }

    boolean Validation()
    {
        boolean wrong_fill = false;

        if(login.trim().length() == 0)
        {
            register_err.setText("You have empty spaces");
            loginText.setBackgroundResource(R.drawable.edittext_bg);
            wrong_fill = true;
        }
        if(login.trim().length() < 5)
        {
            register_err.setText("Login too small");
            loginText.setBackgroundResource(R.drawable.edittext_bg);
            wrong_fill = true;
        }
        if(password.trim().length() == 0)
        {
            register_err.setText("You have empty spaces");
            passwordText.setBackgroundResource(R.drawable.edittext_bg);
            wrong_fill = true;
        }

        if(password.trim().length() < 8)
        {
            register_err.setText("Password too small");
            passwordText.setBackgroundResource(R.drawable.edittext_bg);
            wrong_fill = true;
        }

        if(password_repeat.trim().length() == 0)
        {
            register_err.setText("You have empty spaces");
            password_repeatText.setBackgroundResource(R.drawable.edittext_bg);
            wrong_fill = true;
        }
        if(email.trim().length() == 0)
        {
            register_err.setText("You have empty spaces");
            emailText.setBackgroundResource(R.drawable.edittext_bg);
            wrong_fill = true;
        }
        if(email.trim().length() == 0)
        {
            register_err.setText("You have empty spaces");
            //ageText.setBackgroundResource(R.drawable.edittext_bg);
            wrong_fill = true;
        }
        if(password.equals(password_repeat) == false)
        {
            register_err.setText("Wrong password");
            password_repeatText.setBackgroundResource(R.drawable.edittext_bg);
            wrong_fill = true;
        }
        if(email.matches(emailPattern) == false)
        {
            register_err.setText("Wrong e-mail adress");
            emailText.setBackgroundResource(R.drawable.edittext_bg);
            wrong_fill = true;
        }


        return wrong_fill;
    }

}
