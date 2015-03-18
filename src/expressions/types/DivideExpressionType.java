package expressions.types;

import expressions.exceptions.ExpressionCalculationException;

public class DivideExpressionType extends BinaryExpressionType {

	public DivideExpressionType() {
		super("", "/", "", false);
	}

	@Override
	public int applyBinary(int first, int second)
			throws ExpressionCalculationException {
		if (second == 0)
			throw new ExpressionCalculationException(
		    		new ArithmeticException("Division by zero"));
		if ((first == Integer.MIN_VALUE) && (second == -1))
			throw new ExpressionCalculationException(
		    		new ArithmeticException(String.format("Integer overflow (%d/%d)", first,second)));
		if(first%second != 0)
			throw new ExpressionCalculationException(
		    		new ArithmeticException(String.format("Division resulted in fractal number (%d/%d).", first,second)));
		
		return first/second;
	}

	@Override
	public int reverseOperator(int first, int result)
			throws ExpressionCalculationException {
		int intermediateResult = applyBinary(first, result);
		
		if(intermediateResult==0)
			throw new ExpressionCalculationException(
		    		new ArithmeticException("Division by zero"));
		return intermediateResult;
	}

	@Override
	public int reverseOperatorOther(int second, int result)
			throws ExpressionCalculationException {
		if (second == 0)
			throw new ExpressionCalculationException(
		    		new ArithmeticException("Division by zero"));
		if (second > 0 ? result > Integer.MAX_VALUE/second
                || result < Integer.MIN_VALUE/second
              : (second < -1 ? result > Integer.MIN_VALUE/second
                              || result < Integer.MAX_VALUE/second
                            : second == -1
                              && result == Integer.MIN_VALUE) )
			throw new ExpressionCalculationException(new ArithmeticException("Integer overflow"));
		return result*second;
	}

}
