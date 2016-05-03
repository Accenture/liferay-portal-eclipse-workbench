package ${packageName}.exception;

/**
 * @author kailash.chand.yadav
 */
public class NoSuchEmailAddressException extends NoSuchModelException {

	public NoSuchEmailAddressException() {
		super();
	}

	public NoSuchEmailAddressException(String msg) {
		super(msg);
	}

	public NoSuchEmailAddressException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NoSuchEmailAddressException(Throwable cause) {
		super(cause);
	}

}