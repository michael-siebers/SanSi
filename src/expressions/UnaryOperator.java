package expressions;

import series.NumberSeries;
import expressions.exceptions.ExpressionCalculationException;
import expressions.types.UnaryExpressionType;

public class UnaryOperator extends Expression {

	private Expression subExpression;
	private UnaryExpressionType unaryType;
	
	public UnaryOperator(UnaryExpressionType type, Expression subExpression) {
		super(type);
		if(subExpression == null)
			throw new IllegalArgumentException("Argument 'subExpression' is null.");
		this.subExpression = subExpression;
		this.unaryType = type;
	}

	@Override
	protected int calculate(NumberSeries previous, int index)
			throws ExpressionCalculationException {
		return getUnaryType().applyOperator(subExpression.calculate(previous, index));
	}

	private UnaryExpressionType getUnaryType() {
		return unaryType;
	}

	@Override
	public String formatExpression(String seriesName, int totalExpressions) {
		return getUnaryType().formatExpressionType(
				subExpression.formatExpression(seriesName, totalExpressions));
	}

	@Override
	public int requiredPrecursors() {
		return subExpression.requiredPrecursors();
	}
	
	@Override
	public int getDepth() {
		return 1+subExpression.getDepth();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((subExpression == null) ? 0 : subExpression.hashCode());
		result = prime * result
				+ ((unaryType == null) ? 0 : unaryType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UnaryOperator))
			return false;
		UnaryOperator other = (UnaryOperator) obj;
		if (subExpression == null) {
			if (other.subExpression != null)
				return false;
		} else if (!subExpression.equals(other.subExpression))
			return false;
		if (unaryType == null) {
			if (other.unaryType != null)
				return false;
		} else if (!unaryType.equals(other.unaryType))
			return false;
		return true;
	}

	/**
	 * Get this unary operators subexpression
	 * @return
	 */
	Expression getSubExpression() {
		return subExpression;
	}
	
	@Override
	public Expression normalize() throws ExpressionCalculationException {
		Expression innerExpression = subExpression.normalize();
		
		// apply to constant
		if(innerExpression instanceof ConstantValue) {
			ConstantValue cValue = (ConstantValue) innerExpression;
			int innerValue =  cValue.getValue();
			return new ConstantValue(unaryType.applyOperator(innerValue));
		}
		
		// eliminate double application (if operation is self inverse)
		if(unaryType.isSelfinverse() && (innerExpression instanceof UnaryOperator)) {
			UnaryOperator unOp = (UnaryOperator) innerExpression;
			if(unOp.getType() == unaryType)
				return unOp.getSubExpression();
		}
		return this.unaryType.getExpression(innerExpression);
	}

	

}
