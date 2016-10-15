package pl.weeia.localannouncements.service.retrofit;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pl.weeia.localannouncements.R;
import pl.weeia.localannouncements.service.user.TokenStorage;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class RetrofitModule {

	@Provides
	@Singleton
	static OkHttpClient provideOkHttpClient(final TokenStorage tokenStorage) {
		return new OkHttpClient.Builder()
				.addInterceptor(createInterceptor(tokenStorage))
				.build();
	}

	@NonNull
	private static Interceptor createInterceptor(final TokenStorage tokenStorage) {
		return new Interceptor() {
			@Override
			public okhttp3.Response intercept(Chain chain) throws IOException {
				Request request = chain.request();

				if (request.header("Authorization") == null) {
					request = request.newBuilder()
							.header("Authorization", "Bearer " + tokenStorage.getToken().getAccessToken())
							.method(request.method(), request.body())
							.build();
				}

				return chain.proceed(request);
			}
		};
	}

	@Provides
	@Singleton
	static Retrofit provideRetrofit(OkHttpClient okHttpClient, Resources resources) {
		String serverUrl = resources.getString(R.string.serverUrl);

		return new Retrofit.Builder()
				.baseUrl(serverUrl)
				.addConverterFactory(JacksonConverterFactory.create())
				.client(okHttpClient)
				.build();
	}

}
