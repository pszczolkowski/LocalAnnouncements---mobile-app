package pl.weeia.localannouncements.api.user;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

interface UserRetrofitApi {

	@POST("oauth/token")
	@FormUrlEncoded
	Call<AuthenticationResponse> authenticate(
			@Field("username") String username,
			@Field("password") String password,
			@Field("grant_type") String grantType,
			@Field("scope") String scope,
			@Field("client_id")String clientId,
			@Field("client_secret") String clientSecret,
			@Header("Authorization") String authorizationHeader);

	@GET("account")
	Call<IdentityResponse> getIdentity();

}
