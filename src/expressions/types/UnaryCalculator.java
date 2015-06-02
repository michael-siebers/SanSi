package expressions.types;

import expressions.exceptions.ExpressionCalculationException;

public interface UnaryCalculator {
	public int apply(int arg) throws ExpressionCalculationException;
}
