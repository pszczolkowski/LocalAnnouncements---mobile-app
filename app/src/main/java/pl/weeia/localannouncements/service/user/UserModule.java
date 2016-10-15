package pl.weeia.localannouncements.service.user;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

	@Provides
	@Singleton
	TokenStorage provideTokenStorage() {
		return new TokenStorage();
	}

}
