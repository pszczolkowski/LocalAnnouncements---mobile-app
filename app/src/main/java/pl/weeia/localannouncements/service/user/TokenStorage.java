package pl.weeia.localannouncements.service.user;

public class TokenStorage {

	private OAuthToken oAuthToken;

	public OAuthToken getToken() {
		return oAuthToken;
	}

	void put(OAuthToken oAuthToken) {
		this.oAuthToken = oAuthToken;
	}

	public boolean hasToken() {
		return oAuthToken != null;
	}
}
