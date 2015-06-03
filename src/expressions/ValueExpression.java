package expressions;

import expressions.types.ValueExpressionType;

public abstract class ValueExpression extends Expression {

	public ValueExpression(ValueExpressionType type) {
		super(type);
	}

	@Override
	public int getDepth() {
		return 1;
	}
	
	
}
