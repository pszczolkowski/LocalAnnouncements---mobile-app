package pl.weeia.localannouncements.api.user;


import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import javax.inject.Inject;

import pl.weeia.localannouncements.api.ErrorResponse;
import pl.weeia.localannouncements.api.Failure;
import pl.weeia.localannouncements.service.user.ApplicationUser;
import pl.weeia.localannouncements.service.user.OAuthToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static pl.weeia.localannouncements.api.user.AuthenticationFailure.BAD_CREDENTIALS;
import static pl.weeia.localannouncements.api.user.RegistrationFailure.EMAIL_ALREADY_TAKEN;
import static pl.weeia.localannouncements.api.user.RegistrationFailure.USERNAME_ALREADY_TAKEN;

public class UserApi {

	private UserRetrofitApi api;

	@Inject
	UserApi(Retrofit retrofit) {
		api = retrofit.create(UserRetrofitApi.class);
	}

	public Promise<OAuthToken, Failure<AuthenticationFailure>, Void> authenticate(String username, String password, String grantType, String scope,
																				  String clientId, String clientSecret, String authorizationHeader) {
		final DeferredObject<OAuthToken, Failure<AuthenticationFailure>, Void> deferred = new DeferredObject<>();

		Call<AuthenticationResponse> call = api.authenticate(username, password, grantType, scope, clientId,
				clientSecret, authorizationHeader);
		call.enqueue(new Callback<AuthenticationResponse>() {
			@Override
			public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
				if (response.code() >= 200 && response.code() < 300) {
					AuthenticationResponse authenticationResponse = response.body();
					deferred.resolve(new OAuthToken(authenticationResponse.getAccessToken(),
							authenticationResponse.getTokenExpirationSeconds(), authenticationResponse.getRefreshToken()));
				} else {
					Failure<AuthenticationFailure> failure = Failure.causedByResponseCode(response.code());

					if (isResponseBadRequest(response)) {
						ErrorResponse errorResponse = new ErrorResponse(response.errorBody());

						if (errorResponse.hasError("invalid_grant", "Bad credentials")) {
							failure.addReason(BAD_CREDENTIALS);
						}
					}

					deferred.reject(failure);
				}
			}

			@Override
			public void onFailure(Call<AuthenticationResponse> call, Throwable throwable) {
				Failure<AuthenticationFailure> failure = Failure.causedBy(throwable);
				deferred.reject(failure);
			}
		});

		return deferred.promise();
	}

	public Promise<ApplicationUser, Failure<?>, Void> getIdentity() {
		final DeferredObject<ApplicationUser, Failure<?>, Void> deferred = new DeferredObject<>();

		Call<IdentityResponse> call = api.getIdentity();
		call.enqueue(new Callback<IdentityResponse>() {
			@Override
			public void onResponse(Call<IdentityResponse> call, Response<IdentityResponse> response) {
				if (response.code() >= 200 && response.code() < 300) {
					IdentityResponse identityResponse = response.body();
					deferred.resolve(new ApplicationUser(identityResponse.getId(), identityResponse.getUsername(),
							identityResponse.getAge(), identityResponse.getGender()));
				} else {
					Failure<?> failure = Failure.causedByResponseCode(response.code());
					deferred.reject(failure);
				}
			}

			@Override
			public void onFailure(Call<IdentityResponse> call, Throwable throwable) {
				Failure<AuthenticationFailure> failure = Failure.causedBy(throwable);
				deferred.reject(failure);
			}
		});

		return deferred.promise();
	}

	public Promise<Void, Failure<RegistrationFailure>, Void> register(RegistrationForm registrationForm) {
		final DeferredObject<Void, Failure<RegistrationFailure>, Void> deferred = new DeferredObject<>();

		Call<IdentityResponse> call = api.register(registrationForm);
		call.enqueue(new Callback<IdentityResponse>() {
			@Override
			public void onResponse(Call<IdentityResponse> call, Response<IdentityResponse> response) {
				if (response.code() >= 200 && response.code() < 300) {
					deferred.resolve(null);
				} else {
					Failure<RegistrationFailure> failure = Failure.causedByResponseCode(response.code());

					if (isResponseBadRequest(response)) {
						ErrorResponse errorResponse = new ErrorResponse(response.errorBody());

						if (errorResponse.hasFieldError("login", "login is already taken")) {
							failure.addReason(USERNAME_ALREADY_TAKEN);
						}
						if (errorResponse.hasFieldError("email", "email is already taken")) {
							failure.addReason(EMAIL_ALREADY_TAKEN);
						}
					}

					deferred.reject(failure);
				}
			}

			@Override
			public void onFailure(Call<IdentityResponse> call, Throwable throwable) {
				Failure<RegistrationFailure> failure = Failure.causedBy(throwable);
				deferred.reject(failure);
			}
		});

		return deferred.promise();
	}

	private boolean isResponseBadRequest(Response<?> response) {
		return response.code() == 400;
	}

}
