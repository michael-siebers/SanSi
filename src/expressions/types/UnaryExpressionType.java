package expressions.types;

import expressions.Expression;
import expressions.UnaryOperator;
import expressions.exceptions.ExpressionCalculationException;

public abstract class UnaryExpressionType extends ExpressionType {
	private String operatorPrefix;
	private String operatorSuffix;
	
	public UnaryExpressionType(String operatorPrefix,
			String operatorSuffix) {
		super();

		this.operatorPrefix = operatorPrefix;
		this.operatorSuffix = operatorSuffix;
	}

	public abstract int applyOperator(int arg) throws ExpressionCalculationException;

	public String formatExpressionType(String argument) {
		return operatorPrefix + argument + operatorSuffix;
	}

	public abstract int reverseOperator(int result) throws ExpressionCalculationException;

	public int getRequiredNumberOfSubexpressions() {
		return 1;
	}
	
	public UnaryOperator getExpression(Expression subExpression) {
		return new UnaryOperator(this, subExpression);
	}
}
