package expressions.types;

import series.NumberSeries;
import expressions.PrecursorValue;
import expressions.ValueExpression;


public class PrecursorExpressionType extends ValueExpressionType {
	class Iterator implements java.util.Iterator<ValueExpression> {
		int max;
		int current = 0;
		
		Iterator(int max) {
			if(max<0)
				throw new IllegalArgumentException("max must be at least 0.");
			this.max = max;
			
		}
		@Override
		public boolean hasNext() {
			return current<max;
		}

		@Override
		public ValueExpression next() {
			current++;
			return new PrecursorValue(current);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	@Override
	public ValueExpression fitTo(NumberSeries original,
			NumberSeries predicted) {
		int[] local = original.toArray();
		int initialSize = original.size()-predicted.size();
				
		for(int offset = 1; offset<=initialSize; offset++) {
			boolean ok = true;
			for(int current = 0; ok && current<predicted.size(); current++) {
				if(predicted.get(current) != local[current+initialSize-offset])
					ok = false;
			}
			
			if(ok) {
				return new PrecursorValue(offset);
			}
		}
		return null;
	}

	@Override
	public java.util.Iterator<ValueExpression> guesses(
			NumberSeries original,	NumberSeries predicted) {
		return new Iterator(original.size()-predicted.size());
	}

	@Override
	public String getDummyRepresentation() {
		return "x_{n-?}";
	}

}
