package expressions;

import series.NumberSeries;
import series.NumberSeriesDefinition;
import expressions.exceptions.ExpressionCalculationException;
import expressions.exceptions.NumberSeriesGenerationException;
import expressions.types.ExpressionTypeBuilder;

public class NumberSeriesExpression extends ValueExpression {
	private NumberSeriesDefinition def;
	private NumberSeries series;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((def == null) ? 0 : def.hashCode());
		result = prime * result + offset;
		result = prime * result + ((series == null) ? 0 : series.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NumberSeriesExpression))
			return false;
		NumberSeriesExpression other = (NumberSeriesExpression) obj;
		return def.equals(other.def);
		
		
	}

	private int offset;
	
	
	
	public NumberSeriesExpression(NumberSeriesDefinition def, int offset) {
		super(ExpressionTypeBuilder.getNumType());
		this.def = def;
		this.offset = offset;
	}

	@Override
	protected int calculate(NumberSeries previous, int index)
			throws ExpressionCalculationException {
		try {
			if(series==null)
				series = def.produce(index*2+1);
			else if (series.size()<=(index-offset)) 
				series = def.produce(Math.max(series.size()*2,index+1));
		} catch (NumberSeriesGenerationException e) {
			throw new ExpressionCalculationException("Could not extend subseries", e);
		}
		
		return series.get(index-offset);
	}

	@Override
	public String formatExpression(String seriesName, int totalExpressions) {
		String original = def.toString();
		return "[" + original.substring(0, original.length()-1).replace("\n", ", ") + "]";
	}

	@Override
	public int requiredPrecursors() {
		return offset;
	}

}
