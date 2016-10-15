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
import pl.weeia.localannouncements.api.user.UserApi;

/**
 * Represents application user.
 * This class is used to authenticateWithCredentials user using credentials.
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
	 * Authenticate user using given credentials. It performs HTTP request
	 *
	 * @return Promise <ApplicationUser, Throwable, Void> Rejected promise contains Throwable object. It can be
	 * 	an HttpException instance if request failed due to response status code. IF the request
	 * 	failed for any other reason, then Throwable is an exception with failure message
	 */
	public Promise<ApplicationUser, Throwable, Void> authenticateWithCredentials(String username, String password) {
		final DeferredObject<ApplicationUser, Throwable, Void> deferred = new DeferredObject<>();

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
						}).fail(new FailCallback<Throwable>() {
							@Override
							public void onFail(Throwable throwable) {
								deferred.reject(throwable);
							}
						});
				}
			}).fail(new FailCallback<Throwable>() {
				@Override
				public void onFail(Throwable throwable) {
					deferred.reject(throwable);
				}
			});

		return deferred.promise();
	}

}
