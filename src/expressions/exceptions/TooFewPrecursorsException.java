package expressions.exceptions;

public class TooFewPrecursorsException extends ExpressionCalculationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9130276750625717446L;

	
	public TooFewPrecursorsException(Exception e) {
		super(e);
	}


	public TooFewPrecursorsException() {
	}

}
