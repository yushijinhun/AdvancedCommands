package yushijinhun.advancedcommands.command.expression;

public class ExpressionHandlingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExpressionHandlingException() {
	}

	public ExpressionHandlingException(String message) {
		super(message);
	}

	public ExpressionHandlingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExpressionHandlingException(Throwable cause) {
		super(cause);
	}

}
