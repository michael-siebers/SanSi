package expressions.types;

import expressions.Expression;
import expressions.UnaryOperator;
import expressions.exceptions.ExpressionCalculationException;

public class UnaryExpressionType extends ExpressionType {
	private String operatorPrefix;
	private String operatorSuffix;
	
	private final UnaryCalculator resultCalculator;
	private final UnaryCalculator reverseCalculator;
	
	public UnaryExpressionType(String operatorPrefix, String operatorSuffix, 
			UnaryCalculator result, UnaryCalculator reverse) {
		super();

		this.operatorPrefix = operatorPrefix;
		this.operatorSuffix = operatorSuffix;
		this.resultCalculator = result;
		this.reverseCalculator = reverse;
	}

	public int applyOperator(int arg) throws ExpressionCalculationException {
		return this.resultCalculator.apply(arg);
	}

	public String formatExpressionType(String argument) {
		return operatorPrefix + argument + operatorSuffix;
	}

	public int reverseOperator(int result) throws ExpressionCalculationException {
		return this.reverseCalculator.apply(result);
	}

	public int getRequiredNumberOfSubexpressions() {
		return 1;
	}
	
	public UnaryOperator getExpression(Expression subExpression) {
		return new UnaryOperator(this, subExpression);
	}
}
