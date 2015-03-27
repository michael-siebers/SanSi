package expressions.types;

import java.util.Iterator;

import series.NumberSeries;
import series.NumberSeriesDefinition;
import series.inducers.BetterSimpleNumberSeriesInducer;
import de.uni_bamberg.wiai.cogsys.tools.EmptyIterator;
import expressions.NumberSeriesExpression;
import expressions.ValueExpression;

public class NumberSeriesExpressionType extends ValueExpressionType {

	NumberSeriesExpressionType() {
	}
	
	@Override
	public ValueExpression fitTo(NumberSeries original, NumberSeries predicted) {
		if(original.size() == predicted.size())
			return null; // might cause an infinite loop otherwise!
		
		NumberSeriesDefinition subDef = (new BetterSimpleNumberSeriesInducer()).induce(predicted, false);
		
		if (subDef != null)
			return new NumberSeriesExpression(subDef, original.size()-predicted.size());
		return null;
	}

	@Override
	public String getDummyRepresentation() {
		return "[x_0, x_1, ...]";
	}

	@Override
	public Iterator<ValueExpression> guesses(NumberSeries original,
			NumberSeries predicted) {
		return new EmptyIterator<ValueExpression>();
	}

}
