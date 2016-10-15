package pl.weeia.localannouncements.activity.main;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.jdeferred.android.AndroidDoneCallback;
import org.jdeferred.android.AndroidExecutionScope;
import org.jdeferred.android.AndroidFailCallback;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.weeia.localannouncements.R;
import pl.weeia.localannouncements.service.user.ApplicationUser;
import pl.weeia.localannouncements.service.user.Authenticator;
import pl.weeia.localannouncements.shared.BaseActivity;
import pl.weeia.localannouncements.shared.exception.HttpException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity {

	@BindView(R.id.testView) protected TextView testView;

	@Inject
	protected Authenticator user;

	@Inject
	protected Retrofit retrofit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// These two lines are required for injection
		// They need to be copied for every Activity
		ButterKnife.bind(this);
		getContainer().inject(this);

		// here's example of authenticating user using credentials
		user
			.authenticateWithCredentials("tester", "123456")
			.then(new AndroidDoneCallback<ApplicationUser>() {
				@Override
				public void onDone(ApplicationUser result) {
					testView.setText("authenticated successfully");
				}

				@Override
				public AndroidExecutionScope getExecutionScope() {
					return AndroidExecutionScope.UI;
				}
			})
			.fail(new AndroidFailCallback<Throwable>() {
				@Override
				public void onFail(Throwable result) {
					if (result instanceof HttpException) {
						testView.setText("" + ((HttpException) result).getCode());
					} else {
						testView.setText("" + result.getMessage());
					}
				}

				@Override
				public AndroidExecutionScope getExecutionScope() {
					return AndroidExecutionScope.UI;
				}
			});

	}

}
