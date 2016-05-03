package ${packageName}.exception;

/**
 * @author kailash.chand.yadav
 */
public class PasswordExpiredException extends ${pluginMainException} {

	public PasswordExpiredException() {
		super();
	}

	public PasswordExpiredException(String msg) {
		super(msg);
	}

	public PasswordExpiredException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public PasswordExpiredException(Throwable cause) {
		super(cause);
	}

}