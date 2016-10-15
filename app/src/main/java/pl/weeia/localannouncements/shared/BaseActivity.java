package pl.weeia.localannouncements.shared;


import android.support.v7.app.AppCompatActivity;

import pl.weeia.localannouncements.Application;
import pl.weeia.localannouncements.Container;

public abstract class BaseActivity extends AppCompatActivity {

	protected Container getContainer() {
		return ((Application) getApplication()).getContainer();
	}

}
