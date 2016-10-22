package pl.weeia.localannouncements.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.weeia.localannouncements.R;
import pl.weeia.localannouncements.api.Failure;
import pl.weeia.localannouncements.api.user.AuthenticationFailure;
import pl.weeia.localannouncements.api.user.UserApi;
import pl.weeia.localannouncements.service.user.ApplicationUser;
import pl.weeia.localannouncements.service.user.Authenticator;
import pl.weeia.localannouncements.shared.AndroidUiDoneCallback;
import pl.weeia.localannouncements.shared.AndroidUiFailCallback;
import pl.weeia.localannouncements.shared.BaseActivity;

import static pl.weeia.localannouncements.api.user.AuthenticationFailure.BAD_CREDENTIALS;

public class MainActivity extends BaseActivity {

	@BindView(R.id.testView) protected TextView testView;

	@Inject
	protected Authenticator authenticator;

	@Inject
	protected UserApi userApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// These two lines are required for injection
		// They need to be copied for every Activity
		ButterKnife.bind(this);
		getContainer().inject(this);

		// here's example of authenticating authenticator using credentials
		authenticator
			.authenticateWithCredentials("tester", "123456")
			.then(new AndroidUiDoneCallback<ApplicationUser>() {
				@Override
				public void onDone(ApplicationUser result) {
					testView.setText("authenticated successfully");
				}
			})
			.fail(new AndroidUiFailCallback<Failure<AuthenticationFailure>>() {
				@Override
				public void onFail(Failure<AuthenticationFailure> failure) {
					if (failure.isBecauseOfThrowable()) {
						testView.setText("" + failure.getThrowable().getMessage());
					} else if (failure.isBecauseOf(BAD_CREDENTIALS)) {
						testView.setText("bad credentials");
					} else {
						testView.setText("" + failure.getStatusCode());
					}
				}
			});



		Button butt = (Button)findViewById(R.id.button);
		butt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

}
