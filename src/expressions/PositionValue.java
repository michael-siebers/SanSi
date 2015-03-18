package expressions;

import series.NumberSeries;
import expressions.types.PositionExpressionType;

public class PositionValue extends ValueExpression {
	public PositionValue() {
		super(new PositionExpressionType());
	}
	
	@Override
	public int requiredPrecursors() {
		return 0;
	}

	@Override
	public int calculate(NumberSeries previous, int index) {
		return index;
	}

	
	
	@Override
	public String formatExpression(String seriesName, int totalExpressions) {
		return "n";
	}

	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PositionValue))
			return false;
		return true;
	}
	
	
	
}
