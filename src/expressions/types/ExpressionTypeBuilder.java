package expressions.types;

import de.uni_bamberg.wiai.cogsys.math.IntMath;
import expressions.exceptions.ExpressionCalculationException;

public class ExpressionTypeBuilder {
	/*
	 * 
	 * Calculators
	 * 
	 */
	private static final UnaryCalculator Negate = new UnaryCalculator() {
		
		@Override
		public int apply(int arg) throws ExpressionCalculationException {
			
			try {
				return IntMath.negate(arg);
			} catch (ArithmeticException e) {
				throw new ExpressionCalculationException(e);
			}
		}
	};
	
	private static final BinaryCalculator FirstPlusSecond = new BinaryCalculator() {
		@Override
		public int apply(int first, int second) throws ExpressionCalculationException {
			try {
				return IntMath.add(first,second);
			} catch (ArithmeticException e) {
				throw new ExpressionCalculationException(e);
			}
		}
	};
	
	private static final BinaryCalculator FirstMinusSecond = new BinaryCalculator() {
		
		@Override
		public int apply(int first, int second)
				throws ExpressionCalculationException {
			try {
				return IntMath.subtract(first, second);
			} catch (ArithmeticException e) {
				throw new ExpressionCalculationException(e);
			}
		}
	};
	
	private static final BinaryCalculator SecondMinusFirst = new BinaryCalculator() {
		
		@Override
		public int apply(int first, int second)
				throws ExpressionCalculationException {
			try {
				return IntMath.subtract(second, first);
			} catch (ArithmeticException e) {
				throw new ExpressionCalculationException(e);
			}
		}
	};
	
	private static final BinaryCalculator FirstTimesSecond = new BinaryCalculator() {
		
		@Override
		public int apply(int first, int second)
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
	};
	
	private static final BinaryCalculator FirstDividedBySecond = new BinaryCalculator() {
		
		@Override
		public int apply(int first, int second)
				throws ExpressionCalculationException {
			try {
				return IntMath.divide(first, second);
			} catch (ArithmeticException e) {
				throw new ExpressionCalculationException(e);
			}
		}
	};
	
	private static final BinaryCalculator SecondDividedByFirst = new BinaryCalculator() {
		
		@Override
		public int apply(int first, int second)
				throws ExpressionCalculationException {
			try {
				return IntMath.divide(second, first);
			} catch (ArithmeticException e) {
				throw new ExpressionCalculationException(e);
			}
		}
	};
	
	private static final BinaryCalculator FirstToThePowerOfSecond = new BinaryCalculator() {
		
		@Override
		public int apply(int first, int second)
				throws ExpressionCalculationException {
			try {
				return IntMath.exp(first,second);
			} catch (ArithmeticException e) {
				throw new ExpressionCalculationException(e);
			}
		}
	};
	
	private static final BinaryCalculator ReverseExponentiationToExponent = new BinaryCalculator() {
		
		@Override
		/*
		 * first is base
		 * second is result
		 */
		public int apply(int first, int second)
				throws ExpressionCalculationException {
			if(first==0) {
				if(second==1)
					return 0;
				else // if second==0 return >0; else not possible
					throw new ExpressionCalculationException(new ArithmeticException(
						String.format("Not reversible"))); 
			} else if (first==1) // if second==1 return >= 0; else not possible
				throw new ExpressionCalculationException("Not reversible"); 
						
			
			int result;
			
			try {
				if (second < 0) {
					if (first > 0)
						throw new ExpressionCalculationException("Not reversible");
					
					result = IntMath.log(-first, -second);
					if(result %2 == 0) // exponent even 
						throw new ExpressionCalculationException("Not reversible"); // negative^even = negative does not work

				} else	if(first < 0) {
					result = IntMath.log(-first, second);
					if(result %2 != 0) // exponent odd 
						throw new ExpressionCalculationException("Not reversible"); // negative^odd = positive does not work
					
				} else 
					result = IntMath.log(first, second);
			} catch (ArithmeticException e) {
				throw new ExpressionCalculationException(e);
			}
			
		
			if(result<0)
				throw new ExpressionCalculationException("Negative Exponent.");
			
			return result;
		}
	};
	
	private static final BinaryCalculator ReverseExponentiationToBase =  new BinaryCalculator() {
		
		@Override

		public int apply(int second, int result)//first, int second)
				throws ExpressionCalculationException {
		
			if(second < 0) 
				throw new ExpressionCalculationException("Not reversible"); // negative exponent not defined in this context
			if(second==0) { // x^0 = 1
				if(result==1)
					throw new ExpressionCalculationException("Not reversible");  // everything is possible 
				else
					throw new ExpressionCalculationException("Not reversible");  // nothing is possible
			} 
			

			try {
				if(result==0)
					return 0;
				else if (result>0) 
					return IntMath.nthRoot(second, result);
				else { // result < 0
					if(second % 2 == 0)
						throw new ExpressionCalculationException("Not reversible");  // negative^even = negative is not possible
					return -IntMath.nthRoot(second, result);
				}
			} catch (ArithmeticException e) {
				throw new ExpressionCalculationException(e);
			}
			
			
		}
	}; 
	
	
	
	
	// value types
	private static ConstantExpressionType conType = new ConstantExpressionType();
	private static NumberSeriesExpressionType numType = new NumberSeriesExpressionType();
	private static PositionExpressionType posType = new PositionExpressionType();
	private static PrecursorExpressionType preType = new PrecursorExpressionType();
	private static ValueExpressionType[] valueTypes = { conType, numType, posType, preType}; 
	
	// unary types
	private static UnaryExpressionType negType = new UnaryExpressionType("-", "", Negate, Negate); 
	private static UnaryExpressionType[] unaryTypes = {negType};
		
	// binary types
	private static BinaryExpressionType addType = new BinaryExpressionType("", "+", "", 
			FirstPlusSecond, SecondMinusFirst, SecondMinusFirst, true, 0);
	private static BinaryExpressionType subType = new BinaryExpressionType("", "-", "", 
			FirstMinusSecond, FirstPlusSecond, FirstMinusSecond, false, 0);
	private static BinaryExpressionType mulType = new BinaryExpressionType("", "*", "", 
    		FirstTimesSecond, SecondDividedByFirst, SecondDividedByFirst, true, 1);
	private static BinaryExpressionType divType = new BinaryExpressionType("", "/", "", 
    		FirstDividedBySecond, FirstTimesSecond, FirstDividedBySecond, true, 1);
	private static BinaryExpressionType powType = new BinaryExpressionType("", "^", "", 
    		FirstToThePowerOfSecond, ReverseExponentiationToBase, ReverseExponentiationToExponent, true, 1);
	private static BinaryExpressionType[] binaryTypes = {addType, subType, mulType, divType, powType};
	
	public static ConstantExpressionType getConType() {
		return conType;
	}
	
	public static NumberSeriesExpressionType getNumType() {
		return numType;
	}
	
	public static PositionExpressionType getPosType() {
		return posType;
	}
	public static PrecursorExpressionType getPreType() {
		return preType;
	}
	public static UnaryExpressionType getNegType() {
		return negType;
	}
	public static BinaryExpressionType getAddType() {
		return addType;
	}
	public static BinaryExpressionType getSubType() {
		return subType;
	}
	public static BinaryExpressionType getMulType() {
		return mulType;
	}
	public static BinaryExpressionType getDivType() {
		return divType;
	}
	public static BinaryExpressionType getPowType() {
		return powType;
	}

	public static ValueExpressionType[] getValueTypes() {
		return valueTypes;
	}

	public static UnaryExpressionType[] getUnaryTypes() {
		return unaryTypes;
	}

	public static BinaryExpressionType[] getBinaryTypes() {
		return binaryTypes;
	}
	
	
	

}
