package expressions.types;

import java.util.Iterator;

import de.uni_bamberg.wiai.cogsys.tools.EmptyIterator;

import series.NumberSeries;
import series.NumberSeriesDefinition;
import series.SimpleNumberSeriesDefinition;
import series.SimpleNumberSeriesDefinition;
import expressions.NumberSeriesExpression;
import expressions.ValueExpression;

public class NumberSeriesExpressionType extends ValueExpressionType {

	@Override
	public ValueExpression fitTo(NumberSeries original, NumberSeries predicted) {
		if(original.size() == predicted.size())
			return null; // might cause an infinite loop otherwise!
		
		// TODO decide maxDepth
		int maxDepth = 2;
		NumberSeriesDefinition subDef = SimpleNumberSeriesDefinition.induce2(predicted, false);
		
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
