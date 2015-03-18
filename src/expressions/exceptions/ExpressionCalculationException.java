package expressions.exceptions;


public class ExpressionCalculationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 331957870160736301L;

	
	
	public ExpressionCalculationException() {
		super();
	}

	public ExpressionCalculationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExpressionCalculationException(String message) {
		super(message);
	}

	public ExpressionCalculationException(Throwable cause) {
		super(cause);
	}

}
