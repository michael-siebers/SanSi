package expressions;

import series.NumberSeries;
import expressions.types.ExpressionTypeBuilder;

public class PrecursorValue extends ValueExpression {

	private int offset;
	
	public PrecursorValue(int offset) {
		super(ExpressionTypeBuilder.getPreType());
		
		if(offset<1)
			throw new IllegalArgumentException("Must at least go one back.");
		this.offset = offset;
	}
	
	
	
	public int getOffset() {
		return offset;
	}



	@Override
	public int requiredPrecursors() {
		return offset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + offset;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PrecursorValue))
			return false;
		PrecursorValue other = (PrecursorValue) obj;
		if (offset != other.offset)
			return false;
		return true;
	}

	@Override
	public int calculate(NumberSeries previous, int index) {
		return previous.getFromBack(offset);
	}
	

	@Override
	public String formatExpression(String seriesName, int totalExpressions) {
		return String.format("%s(%sn-%d)", seriesName, 
				(totalExpressions==1)?"":String.format("%d", totalExpressions), offset);
	}
	

}
