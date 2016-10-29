package pl.weeia.localannouncements.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing API request failure. The failure can be caused either by error status code
 * returned by server or by exception that occurred while sending a request.
 * Generic parameter should be an enum representing particular error that was returned from server
 * (e.g. bad credentials on authorization).
 */
public class Failure<T> {

	private Integer statusCode;
	private Throwable throwable;
	private List<T> reasons = new ArrayList<>();

	private Failure() {}

	/**
	 * Creates new Failure instance that represents error based on status code returned from server
	 */
	public static <T> Failure<T> causedByResponseCode(int statusCode) {
		Failure<T> failure = new Failure<>();
		failure.statusCode = statusCode;
		return failure;
	}

	/**
	 * Creates new Failure instance that represents error that occurred while sending a request
	 */
	public static <T> Failure<T> causedBy(Throwable throwable) {
		Failure<T> failure = new Failure<>();
		failure.throwable = throwable;
		return failure;
	}

	public Integer getStatusCode() {
		if (statusCode == null) {
			throw new IllegalStateException("failure caused by throwable has no status code");
		}

		return statusCode;
	}

	public Throwable getThrowable() {
		if (throwable == null) {
			throw new IllegalStateException("failure caused by response status code has no throwable");
		}

		return throwable;
	}

	/**
	 * Adds error reason for status code failure (e.g. bad credentials)
	 */
	public Failure<T> addReason(T reason) {
		if (throwable != null) {
			throw new IllegalStateException("failure caused by throwable cannot have reason");
		}

		reasons.add(reason);
		return this;
	}

	public boolean isCausedByThrowable() {
		return throwable != null;
	}

	public boolean isCausedBy(T reason) {
		return reasons.contains(reason);
	}

}
