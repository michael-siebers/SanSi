package expressions.types;

import expressions.exceptions.ExpressionCalculationException;

public interface BinaryCalculator {
	public int apply(int first, int second) throws ExpressionCalculationException;
}
