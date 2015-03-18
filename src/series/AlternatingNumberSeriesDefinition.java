package series;

import java.util.Arrays;

import expressions.Expression;
import expressions.exceptions.ExpressionCalculationException;
import expressions.exceptions.NumberSeriesGenerationException;

public class AlternatingNumberSeriesDefinition implements
		NumberSeriesDefinition {
	private NumberSeries initial;
	private Expression[] expressions;
	private String name;
	//static private final Logger logger = Logger.getLogger(AlternatingNumberSeriesDefinition.class);
	
	public AlternatingNumberSeriesDefinition(NumberSeries initial, String name, Expression... expressions) {
		super();
		if(initial==null)
			initial = new NumberSeries();
		this.initial = initial;
		
		if(name == null)
			throw new IllegalArgumentException("Argument 'name' is null.");
		this.name = name;
		
		if(expressions == null)
			throw new IllegalArgumentException("Argument 'expressions' is null.");
		this.expressions = expressions;
	}
	
	public AlternatingNumberSeriesDefinition(NumberSeries initial, Expression... expressions) {
		this(initial, "x", expressions);
	}
	
	@Override
	public NumberSeries produce(int length)
			throws NumberSeriesGenerationException {
		if(length < 0)
			throw new NumberSeriesGenerationException("Number series with negative length requested.");
		
		int[] numbers = Arrays.copyOf(initial.toArray(), length);
			
		
		try {
			for(int pos = initial.size(); pos<length;pos++) {
				numbers[pos] = expressions[pos%expressions.length]. 
					get(new NumberSeries(Arrays.copyOfRange(numbers, 0, pos)),pos);
			}
		} catch (ExpressionCalculationException e) {
			throw new NumberSeriesGenerationException(e);
		}
		
		return new NumberSeries(numbers);
	}

	
	public String toString() {
		return toString(this.name);
	}
	
	public String toString(String name) {
		StringBuilder sb = new StringBuilder();
		for(int pos = 0; pos < initial.size(); pos++)
			sb.append(String.format("%s(%d) = %d\n", name, pos, initial.get(pos)));
		for(int pos = 0; pos<expressions.length;pos++) 
			sb.append(String.format("%s(%sn%s) = %s\n",
					name, (expressions.length>1)?String.format("%d", expressions.length):"",
					(pos>0)?String.format("-%d", expressions.length-pos):"",
					expressions[pos].formatExpression(name, expressions.length)));
		return sb.toString();
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
