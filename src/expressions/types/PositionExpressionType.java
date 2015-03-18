package expressions.types;

import series.NumberSeries;
import expressions.PositionValue;
import expressions.ValueExpression;

public class PositionExpressionType extends ValueExpressionType {
	class Iterator implements java.util.Iterator<ValueExpression> {
		boolean delivered = false;
		
		@Override
		public boolean hasNext() {
			return !delivered;
		}

		@Override
		public ValueExpression next() {
			delivered = true;
			return new PositionValue();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(); 
		}
		
	}
	
	@Override
	public ValueExpression fitTo(NumberSeries original,
			NumberSeries predicted) {
		int initialSize = original.size()-predicted.size();
		
		for(int pos=0; pos<predicted.size(); pos++)
			if(predicted.get(pos)!=pos+initialSize)
				return null;
		return new PositionValue();
	}

	@Override
	public java.util.Iterator<ValueExpression> guesses(
			NumberSeries original, NumberSeries predicted) {
		return new Iterator();
	}

	@Override
	public String getDummyRepresentation() {
		return "<Pos>";
	}


}
