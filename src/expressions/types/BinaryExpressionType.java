package expressions.types;

import expressions.BinaryOperator;
import expressions.Expression;
import expressions.exceptions.ExpressionCalculationException;

public class BinaryExpressionType extends ExpressionType {
	private String operatorPrefix;
	private String operatorInfix;
	private String operatorSuffix;
	
	private boolean commutative;
	private final BinaryCalculator resultCalculator;
	private final BinaryCalculator firstCalculator;
	private final BinaryCalculator secondCalculator;
	private int neutral;
	
	/**
	 * Create a binary operator type
	 * 
	 * @param operatorPrefix String representation of the operator prefix
	 * @param operatorInfix String representation of the operator infix 
	 * @param operatorSuffix String representation of the operator suffix
	 * @param result BinaryCalculator used to calculate the result (first and second argument given)
	 * @param first BinaryCalculator used to calculate the first argument (second argument and result given)
	 * @param second BinaryCalculator used to calculate the second argument (first argument and result given)
	 * @param commutative whether the operator is commutative
	 * @param neutral the neutral element of this operator
	 * 
	 */
	public BinaryExpressionType(String operatorPrefix, String operatorInfix, String operatorSuffix,
			BinaryCalculator result, BinaryCalculator first, BinaryCalculator second, 
			boolean commutative, int neutral) {
		// Todo add neutral as argument
		super();
		this.operatorPrefix = operatorPrefix;
		this.operatorInfix = operatorInfix;
		this.operatorSuffix = operatorSuffix;
		
		resultCalculator = result;
		firstCalculator = first;
		secondCalculator = second;
		
		this.commutative = commutative;
		this.neutral = neutral;
	}

	public String formatExpressionType(String firstArgument, String secondArgument) {
		return operatorPrefix + firstArgument + operatorInfix + secondArgument + operatorSuffix;
	}

	public boolean isCommutative() {
		return commutative;
	}
	
	public int getNeutralElement() {
		return neutral;
	}
	
	/**
	 * Calculate the operation on both arguments
	 * 
	 * @param first first argument of operation
	 * @param second second argument
	 * @return result of operation
	 * @throws ExpressionCalculationException
	 */
	public int applyBinary(int first, int second) throws ExpressionCalculationException {
		return this.resultCalculator.apply(first, second);
	}
	
	/**
	 * Calculate the second argument for given first argument and result
	 * @param first first argument of operation
	 * @param result result of operation
	 * @return second argument of operation
	 * @throws ExpressionCalculationException
	 */
	public int reverseOperator(int first, int result) throws ExpressionCalculationException  {
		return this.secondCalculator.apply(first, result);
	}
	
	/**
	 * Calculate the first argument for given second argument and result
	 * 
	 * @param second second argument of operation
	 * @param result result of of operation
	 * @return first arguement of operation
	 * @throws ExpressionCalculationException
	 */
	public int reverseOperatorOther(int second, int result) throws ExpressionCalculationException {
		return this.firstCalculator.apply(second, result);
	}
	
	public int getRequiredNumberOfSubexpressions() {
		return 2;
	}
	

	public BinaryOperator getExpression(Expression leftExpression, Expression rightExpression) {
		return new BinaryOperator(this, leftExpression, rightExpression);
	}

	
	
}
