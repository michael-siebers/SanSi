package expressions.exceptions;

public class NumberSeriesGenerationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6019444470220438929L;

	public NumberSeriesGenerationException(String string) {
		super(string);
	}

	public NumberSeriesGenerationException(Exception e) {
		super(e);
	}

}
