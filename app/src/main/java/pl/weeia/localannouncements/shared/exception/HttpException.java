package pl.weeia.localannouncements.shared.exception;


public class HttpException extends RuntimeException {

	private int code;

	public HttpException(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
