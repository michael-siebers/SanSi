package expressions.types;

import expressions.exceptions.ExpressionCalculationException;

public abstract class BinaryCalculator {
	abstract public int apply(int first, int second) throws ExpressionCalculationException;
}
