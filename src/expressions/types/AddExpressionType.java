package expressions.types;

import expressions.exceptions.ExpressionCalculationException;

public class AddExpressionType extends BinaryExpressionType {

	AddExpressionType() {
		super("", "+", "", true);
	}

	@Override
	public int applyBinary(int first, int second) throws ExpressionCalculationException {
		if (second > 0 ? first > Integer.MAX_VALUE - second
	                : first < Integer.MIN_VALUE - second) 
			throw new ExpressionCalculationException(
		    		new ArithmeticException(String.format("Integer overflow (%d+%d)", first,second)));
	  
		return first+second;
	}

	@Override
	public int reverseOperator(int first, int result)
			throws ExpressionCalculationException {
		if (first > 0 ? result < Integer.MIN_VALUE + first
                : result > Integer.MAX_VALUE + first) 
			throw new ExpressionCalculationException(
		    		new ArithmeticException(String.format("Integer overflow (%d-%d)", result,first)));
	  
		return result - first;
	}

	@Override
	public int reverseOperatorOther(int second, int result)
			throws ExpressionCalculationException {
		return reverseOperator(second,result);
	}
	
	

}
