package expressions.types;

import expressions.exceptions.ExpressionCalculationException;

public class MultiplyExpressionType extends BinaryExpressionType {

	MultiplyExpressionType() {
		super("", "*", "", true);
	}
	
	@Override
	public int applyBinary(int first, int second)
			throws ExpressionCalculationException {
		if (second > 0 ? first > Integer.MAX_VALUE/second
                || first < Integer.MIN_VALUE/second
              : (second < -1 ? first > Integer.MIN_VALUE/second
                              || first < Integer.MAX_VALUE/second
                            : second == -1
                              && first == Integer.MIN_VALUE) )
			throw new ExpressionCalculationException(new ArithmeticException(String.format("Integer overflow (%d*%d)", first,second)));
		return first*second;
	}

	@Override
	public int reverseOperator(int first, int result)
			throws ExpressionCalculationException {
		if(first == 0)
			throw new ExpressionCalculationException(new ArithmeticException("Division by zero."));
		if ((result == Integer.MIN_VALUE) && (first == -1))
			throw new ExpressionCalculationException(new ArithmeticException("Integer overflow"));
		if(result%first != 0)
			throw new ExpressionCalculationException(
				new ArithmeticException("Division results in fractal number."));
		return result/first;
	}

	@Override
	public int reverseOperatorOther(int second, int result)
			throws ExpressionCalculationException {
		return reverseOperator(second,result);
	}

}
