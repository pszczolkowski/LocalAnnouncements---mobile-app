package pl.weeia.localannouncements.api.user;


import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import javax.inject.Inject;

import pl.weeia.localannouncements.service.user.ApplicationUser;
import pl.weeia.localannouncements.service.user.OAuthToken;
import pl.weeia.localannouncements.shared.exception.HttpException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserApi {

	private UserRetrofitApi api;

	@Inject
	UserApi(Retrofit retrofit) {
		api = retrofit.create(UserRetrofitApi.class);
	}

	public Promise<OAuthToken, Throwable, Void> authenticate(String username, String password, String grantType, String scope,
															 String clientId, String clientSecret, String authorizationHeader) {
		final DeferredObject<OAuthToken, Throwable, Void> deferred = new DeferredObject<>();

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
					deferred.reject(new HttpException(response.code()));
				}
			}

			@Override
			public void onFailure(Call<AuthenticationResponse> call, Throwable throwable) {
				deferred.reject(throwable);
			}
		});

		return deferred.promise();
	}

	public Promise<ApplicationUser, Throwable, Void> getIdentity() {
		final DeferredObject<ApplicationUser, Throwable, Void> deferred = new DeferredObject<>();

		Call<IdentityResponse> call = api.getIdentity();
		call.enqueue(new Callback<IdentityResponse>() {
			@Override
			public void onResponse(Call<IdentityResponse> call, Response<IdentityResponse> response) {
				if (response.code() >= 200 && response.code() < 300) {
					IdentityResponse identityResponse = response.body();
					deferred.resolve(new ApplicationUser(identityResponse.getId(), identityResponse.getUsername(),
							identityResponse.getAge(), identityResponse.getGender()));

				} else {
					deferred.reject(new HttpException(response.code()));
				}
			}

			@Override
			public void onFailure(Call<IdentityResponse> call, Throwable t) {
				deferred.reject(t);
			}
		});

		return deferred.promise();
	}

}
