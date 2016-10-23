package pl.weeia.localannouncements.activity.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import pl.weeia.localannouncements.R;
import pl.weeia.localannouncements.api.Failure;
import pl.weeia.localannouncements.api.user.AuthenticationFailure;
import pl.weeia.localannouncements.service.user.ApplicationUser;
import pl.weeia.localannouncements.service.user.Authenticator;
import pl.weeia.localannouncements.shared.AndroidUiDoneCallback;
import pl.weeia.localannouncements.shared.AndroidUiFailCallback;
import pl.weeia.localannouncements.shared.BaseActivity;

import static pl.weeia.localannouncements.api.user.AuthenticationFailure.BAD_CREDENTIALS;

public class LoginActivity extends BaseActivity {
    TextView tv1;
    EditText ed1,ed2,ed3;
    Button b1;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Login = "loginKey";
    public static final String Password = "passKey";
    SharedPreferences sharedpreferences;

    @Inject
    protected Authenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed1=(EditText)findViewById(R.id.editText);
        ed2=(EditText)findViewById(R.id.editText2);

        b1=(Button)findViewById(R.id.button);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String l  = ed1.getText().toString();
                String pass  = ed2.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Login, l);
                editor.putString(Password, pass);
                editor.commit();
                Toast.makeText(LoginActivity.this,"Thanks",Toast.LENGTH_LONG).show();

                authenticator
                        .authenticateWithCredentials(l, pass)
                        .then(new AndroidUiDoneCallback<ApplicationUser>() {
                            @Override
                            public void onDone(ApplicationUser result) {
                                tv1.setText("authenticated successfully");
                            }
                        })
                        .fail(new AndroidUiFailCallback<Failure<AuthenticationFailure>>() {
                            @Override
                            public void onFail(Failure<AuthenticationFailure> failure) {
                                if (failure.isBecauseOfThrowable()) {
                                    tv1.setText("" + failure.getThrowable().getMessage());
                                } else if (failure.isBecauseOf(BAD_CREDENTIALS)) {
                                    tv1.setText("bad credentials");
                                } else {
                                    tv1.setText("" + failure.getStatusCode());
                                }
                            }
                        });
            }
        });
    }

}
