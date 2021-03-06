package expressions;

import series.NumberSeries;
import expressions.types.ConstantExpressionType;

public class ConstantValue extends ValueExpression{
	private int value;
	
	public ConstantValue(int val) {
		super(new ConstantExpressionType());
		value = val;
	}

	@Override
	public int requiredPrecursors() {
		return 0;
	}

	@Override
	public int calculate(NumberSeries previous, int index) {
		return value;
	}

	@Override
	public String formatExpression(String seriesName, int totalExpressions) {
		return String.format("%d", value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ConstantValue))
			return false;
		ConstantValue other = (ConstantValue) obj;
		if (value != other.value)
			return false;
		return true;
	}

	
	
}
