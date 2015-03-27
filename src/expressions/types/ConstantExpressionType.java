package expressions.types;

import java.util.Iterator;

import series.NumberSeries;
import expressions.ConstantValue;
import expressions.ValueExpression;


public class ConstantExpressionType extends ValueExpressionType{

	class ConstIterator implements Iterator<ValueExpression> {
		private final int[] numbers = new int[] {
			-1, 1, 2, 3, 4, 5, 10
		};
		private int pos = 0;
		@Override
		public boolean hasNext() {
			return pos<numbers.length;
		}
		@Override
		public ValueExpression next() {
			return new ConstantValue(numbers[pos++]);
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();			
		}
		
		
		
	}
	
	ConstantExpressionType() {
		// do nothing
	}
	
	@Override
	public ValueExpression fitTo(NumberSeries original, NumberSeries predicted) {
		int guess = predicted.get(0);
		
		for(int i = 1; i < predicted.size(); i++)
			if(predicted.get(i) != guess)
				return null;
		
		return new ConstantValue(guess);
	}

	@Override
	public java.util.Iterator<ValueExpression> guesses(NumberSeries original,
			NumberSeries predicted) {
		return new ConstIterator();
	}

	@Override
	public String getDummyRepresentation() {
		return "<Const>";
	}

	
}
