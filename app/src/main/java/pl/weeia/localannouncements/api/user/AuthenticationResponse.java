package pl.weeia.localannouncements.api.user;


import com.fasterxml.jackson.annotation.JsonProperty;

class AuthenticationResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("expires_in")
	private int tokenExpirationSeconds;

	@JsonProperty("scope")
	private String scope;

	String getAccessToken() {
		return accessToken;
	}

	String getRefreshToken() {
		return refreshToken;
	}

	int getTokenExpirationSeconds() {
		return tokenExpirationSeconds;
	}
}
