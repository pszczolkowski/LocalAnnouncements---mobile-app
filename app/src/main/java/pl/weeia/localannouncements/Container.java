package pl.weeia.localannouncements;

import javax.inject.Singleton;

import dagger.Component;
import pl.weeia.localannouncements.activity.main.MainActivity;
import pl.weeia.localannouncements.service.retrofit.RetrofitModule;
import pl.weeia.localannouncements.service.user.UserModule;

@Component(modules = {
		RetrofitModule.class,
		AndroidModule.class,
		UserModule.class
})
@Singleton
public interface Container {

	void inject(MainActivity mainActivity);

}
