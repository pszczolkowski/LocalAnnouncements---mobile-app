package pl.weeia.localannouncements.service.user;


import java.util.Calendar;

public class OAuthToken {

	private String accessToken;
	private Calendar createdAt;
	private int expirationSeconds;
	private String refreshToken;

	public OAuthToken(String accessToken, int expirationSeconds, String refreshToken) {
		this.accessToken = accessToken;
		this.createdAt = Calendar.getInstance();
		this.expirationSeconds = expirationSeconds;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public int getExpirationSeconds() {
		return expirationSeconds;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
}
