package ${packageName}.exception;

/**
 * @author kailash.chand.yadav
 */
public class DuplicateUserEmailAddressException extends ${pluginMainException} {

	public DuplicateUserEmailAddressException() {
		super();
	}

	public DuplicateUserEmailAddressException(String msg) {
		super(msg);
	}

	public DuplicateUserEmailAddressException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DuplicateUserEmailAddressException(Throwable cause) {
		super(cause);
	}

}