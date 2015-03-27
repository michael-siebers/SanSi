package expressions.types;

import expressions.BinaryOperator;
import expressions.Expression;
import expressions.exceptions.ExpressionCalculationException;

public abstract class BinaryExpressionType extends ExpressionType {
	private String operatorPrefix;
	private String operatorInfix;
	private String operatorSuffix;
	
	private boolean commutative;
	private int neutral;
	
	public BinaryExpressionType(String operatorPrefix, String operatorInfix, 
			String operatorSuffix, boolean commutative) {
		// Todo add neutral as argument
		super();
		this.operatorPrefix = operatorPrefix;
		this.operatorInfix = operatorInfix;
		this.operatorSuffix = operatorSuffix;
		this.commutative = commutative;
		//this.neutral = neutral;
	}

	abstract public int applyBinary(int first, int second)
		throws ExpressionCalculationException;

	public String formatExpressionType(String firstArgument, String secondArgument) {
		return operatorPrefix + firstArgument + operatorInfix + secondArgument + operatorSuffix;
	}

	public boolean isCommutative() {
		return commutative;
	}
	
	public int getNeutralElement() {
		return neutral;
	}
	
	public abstract int reverseOperator(int first, int result) throws ExpressionCalculationException;
	public abstract int reverseOperatorOther(int second, int result) throws ExpressionCalculationException;
	
	public int getRequiredNumberOfSubexpressions() {
		return 2;
	}
	

	public BinaryOperator getExpression(Expression leftExpression, Expression rightExpression) {
		return new BinaryOperator(this, leftExpression, rightExpression);
	}

	
	
}
