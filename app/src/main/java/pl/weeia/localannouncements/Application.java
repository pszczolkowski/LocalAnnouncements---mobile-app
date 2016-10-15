package pl.weeia.localannouncements;


public class Application extends android.app.Application {

	private Container container;

	@Override public void onCreate() {
		super.onCreate();

		container = DaggerContainer
				.builder()
				.androidModule(new AndroidModule(this))
				.build();
	}

	public Container getContainer() {
		return container;
	}

}
