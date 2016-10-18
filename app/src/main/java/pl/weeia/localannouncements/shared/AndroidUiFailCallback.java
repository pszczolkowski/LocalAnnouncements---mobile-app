package pl.weeia.localannouncements.shared;


import org.jdeferred.android.AndroidExecutionScope;
import org.jdeferred.android.AndroidFailCallback;

public abstract class AndroidUiFailCallback<T> implements AndroidFailCallback<T> {

	@Override
	public final AndroidExecutionScope getExecutionScope() {
		return AndroidExecutionScope.UI;
	}
}
