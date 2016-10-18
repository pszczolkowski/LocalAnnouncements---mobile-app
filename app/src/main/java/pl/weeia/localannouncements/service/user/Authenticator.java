package pl.weeia.localannouncements.service.user;

import android.content.res.Resources;
import android.util.Base64;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.weeia.localannouncements.R;
import pl.weeia.localannouncements.api.Failure;
import pl.weeia.localannouncements.api.user.AuthenticationFailure;
import pl.weeia.localannouncements.api.user.UserApi;

/**
 * Represents application authenticator.
 * This class is used to authenticateWithCredentials authenticator using credentials.
 */
@Singleton
public class Authenticator {

	private static final String grantType = "password";
	private static final String scope = "read write";

	private final String clientId;
	private final String clientSecret;
	private final UserApi userApi;
	private final TokenStorage tokenStorage;

	@Inject
	Authenticator(UserApi userApi, Resources resources, TokenStorage tokenStorage) {
		clientId = resources.getString(R.string.clientId);
		clientSecret = resources.getString(R.string.clientSecret);

		this.userApi = userApi;
		this.tokenStorage = tokenStorage;
	}

	/**
	 * Authenticate authenticator using given credentials. It performs HTTP request in separate thread
	 */
	public Promise<ApplicationUser, Failure<AuthenticationFailure>, Void> authenticateWithCredentials(String username, String password) {
		final DeferredObject<ApplicationUser, Failure<AuthenticationFailure>, Void> deferred = new DeferredObject<>();

		String authorizationHeader = "Basic " + Base64.encodeToString((clientId + ":" + clientSecret).getBytes(), Base64.NO_WRAP);

		userApi.authenticate(username, password, grantType, scope, clientId, clientSecret, authorizationHeader)
			.then(new DoneCallback<OAuthToken>() {
				@Override
				public void onDone(OAuthToken oAuthToken) {
					tokenStorage.put(oAuthToken);

					userApi.getIdentity()
						.then(new DoneCallback<ApplicationUser>() {
							@Override
							public void onDone(ApplicationUser applicationUser) {
								deferred.resolve(applicationUser);
							}
						}).fail(new FailCallback<Failure<?>>() {
							@Override
							public void onFail(Failure<?> failure) {
								Failure<AuthenticationFailure> authorizationFailure = Failure.causedBy(failure.getThrowable());
								deferred.reject(authorizationFailure);
							}
						});
				}
			}).fail(new FailCallback<Failure<AuthenticationFailure>>() {
				@Override
				public void onFail(Failure<AuthenticationFailure> failure) {
					deferred.reject(failure);
				}
			});

		return deferred.promise();
	}

}
