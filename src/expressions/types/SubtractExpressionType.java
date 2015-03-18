package expressions.types;

import expressions.exceptions.ExpressionCalculationException;

public class SubtractExpressionType extends BinaryExpressionType {

	public SubtractExpressionType() {
		super("", "-", "", false);
	}

	@Override
	public int applyBinary(int first, int second)
			throws ExpressionCalculationException {
		if (second > 0 ? first < Integer.MIN_VALUE + second
                : first > Integer.MAX_VALUE + second) 
			throw new ExpressionCalculationException(new ArithmeticException(String.format("Integer overflow (%d-%d)", first,second)));
  
		return first-second;
	}

	@Override
	public int reverseOperator(int first, int result)
			throws ExpressionCalculationException {
		return applyBinary(first, result);
	}

	@Override
	public int reverseOperatorOther(int second, int result)
			throws ExpressionCalculationException {
		 if (result > 0 ? second > Integer.MAX_VALUE - result
	                : second < Integer.MIN_VALUE - result) 
	    throw new ExpressionCalculationException(new ArithmeticException("Integer overflow"));
	    
	    return second+result;
	}

}
