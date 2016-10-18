package pl.weeia.localannouncements.shared;

import org.jdeferred.android.AndroidDoneCallback;
import org.jdeferred.android.AndroidExecutionScope;

public abstract class AndroidUiDoneCallback<T> implements AndroidDoneCallback<T> {

	@Override
	public final AndroidExecutionScope getExecutionScope() {
		return AndroidExecutionScope.UI;
	}
}
