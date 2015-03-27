package expressions.types;

import expressions.exceptions.ExpressionCalculationException;

public class NegateExpressionType extends UnaryExpressionType {

	NegateExpressionType() {
		super("-", "");
	}

	@Override
	public int applyOperator(int arg) throws ExpressionCalculationException {
		if (arg == Integer.MIN_VALUE) 
		    throw new ExpressionCalculationException(new ArithmeticException("Integer overflow"));
		
		return -arg;
	}

	@Override
	public int reverseOperator(int result)
			throws ExpressionCalculationException {
		return applyOperator(result);
	}
	
}
