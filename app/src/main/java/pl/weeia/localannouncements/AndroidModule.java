package pl.weeia.localannouncements;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AndroidModule {

	private Context context;

	AndroidModule(Context context) {
		this.context = context;
	}

	@Provides
	@Singleton
	Context provideContext() {
		return context;
	}

	@Provides
	@Singleton
	Resources provideResources() {
		return context.getResources();
	}

}
