package expressions.types;

import java.util.Iterator;

import series.NumberSeries;
import expressions.ValueExpression;

abstract public class ValueExpressionType extends ExpressionType {

	public ValueExpressionType() {
		super();
	}

	@Override
	public int getRequiredNumberOfSubexpressions() {
		// TODO Auto-generated constructor stub
		return 0;
	}
	
	abstract public Iterator<ValueExpression> guesses(NumberSeries original, NumberSeries predicted);
	abstract public ValueExpression fitTo(NumberSeries original, NumberSeries predicted);
	abstract public String getDummyRepresentation();
}