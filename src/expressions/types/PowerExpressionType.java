package expressions.types;

import java.math.BigInteger;

import expressions.exceptions.ExpressionCalculationException;

public class PowerExpressionType extends BinaryExpressionType {
	static private BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
	static private BigInteger minInt = BigInteger.valueOf(Integer.MIN_VALUE);
	
	public PowerExpressionType() {
		super("", "^", "", false);
	}

	@Override
	public int applyBinary(int first, int second)
			throws ExpressionCalculationException {
		if(second < 0)
			throw new ExpressionCalculationException(new ArithmeticException("Negative Exponent"));
	
		/*
		 * Might lead to very (long) calculation.
		 * e.g. 2^83886216
		 *
		
		BigInteger left = BigInteger.valueOf(first);
		BigInteger result = left.pow(second);
		
		if(result.compareTo(maxInt)>0 || result.compareTo(minInt)<0)
			throw new ExpressionCalculationException(new ArithmeticException(
					String.format("Integer overflow (%s^%d)", left.toString(), second)));
		
		return result.intValue();
		*/
		
		if(first==0&&second==0)
			throw new ExpressionCalculationException(new ArithmeticException(
					"Undefined calculation (0^0)"));
		if(second==0)
			return 1;
		if(first==1)
			return 1;
		
		MultiplyExpressionType mT = new MultiplyExpressionType();
		int result = first;
		try {
			for(int times = 1; times < second; times++)
				result = mT.applyBinary(result, first);
		} catch (ExpressionCalculationException e) {
			throw new ExpressionCalculationException(new ArithmeticException(
					String.format("Integer overflow (%d^%d)", first, second)));
		}
		return result;
	}

	@Override
	public int reverseOperator(int first, int result)
			throws ExpressionCalculationException {
		if(result<0)
			throw new ExpressionCalculationException(new ArithmeticException(
					String.format("Not reversible")));
		if(result==0) {
			if(first==0)
				return 0;
			throw new ExpressionCalculationException(new ArithmeticException(
					String.format("Not reversible")));
		} else {
			if(first==0) 
				throw new ExpressionCalculationException(new ArithmeticException(
						String.format("Log of zero not defined.")));
		}
		
		double log_f = Math.log(first);
		double log_r = Math.log(result);
		
		double sec = log_r/log_f;
		
		if(Double.isNaN(sec)||Double.isInfinite(sec))
			throw new ExpressionCalculationException(new ArithmeticException(
					String.format("Log calculation resulted in NaN or Infinity.")));
		if(Math.rint(sec)!=sec)
			throw new ExpressionCalculationException(new ArithmeticException(
					String.format("Log calculation resulted in fractal number.")));
		
		logger.trace(String.format("%d^s=%d  --> %f", first, result, sec ));
		
		int intermediateResult =  (int) Math.round(sec);
		if(intermediateResult<0)
			throw new ExpressionCalculationException(new ArithmeticException(
					String.format("Negative Exponent.")));
		
		return intermediateResult;
	}

	@Override
	public int reverseOperatorOther(int second, int result)
			throws ExpressionCalculationException {
		if(second == 0)
			throw new ExpressionCalculationException(new ArithmeticException(
					String.format("Division by zero.")));
		if(second < 0)
			throw new ExpressionCalculationException(new ArithmeticException(
					String.format("Negative Exponent.")));
		double fst = Math.pow(result, 1/(double)second);
		
		if(Math.rint(fst)!=fst)
			throw new ExpressionCalculationException(new ArithmeticException(
					String.format("Root calculation resulted in fractal number.")));
		
		return (int) Math.round(fst);
	}

	

}
