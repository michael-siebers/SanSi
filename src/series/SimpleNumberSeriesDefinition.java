package series;

import org.apache.log4j.Logger;

import expressions.Expression;
import expressions.exceptions.NumberSeriesGenerationException;

public class SimpleNumberSeriesDefinition implements NumberSeriesDefinition {
	private NumberSeries initial;
	private Expression expression;
	private String name;
	static private final Logger logger = Logger.getLogger(SimpleNumberSeriesDefinition.class);
	
	public SimpleNumberSeriesDefinition(NumberSeries initial, Expression expression, String name) {
		super();
		if(initial==null)
			initial = new NumberSeries();
		this.initial = initial;
		
		if(expression == null)
			throw new IllegalArgumentException("Argument 'expression' is null.");
		if(initial.size()<expression.requiredPrecursors())
			throw new IllegalArgumentException("The recursive definition requires more initial numbers.");
		this.expression = expression;
		
		if(name == null)
			throw new IllegalArgumentException("The Argument 'name' is null.");
		this.name = name;
	}
	
	public SimpleNumberSeriesDefinition(NumberSeries initial, Expression expression) {
		this(initial, expression, "x");
	}
	
	
	

	public String toString() {
		return toString(this.name);
	}

	public String toString(String name) {
		StringBuilder sb = new StringBuilder();
		for(int pos = 0; pos < initial.size(); pos++)
			sb.append(String.format("%s(%d) = %d%n", name, pos, initial.get(pos)));
		sb.append(name).append("(n) = ").append(expression.formatExpression(name, 1)).append("\n");
		return sb.toString();
	}
	
	public NumberSeries produce(int length) throws NumberSeriesGenerationException {
		return expression.generateSeries(initial, length);
	}
	
		
	



	public NumberSeries getInitial() {
		return initial;
	}



	public Expression getExpression() {
		return expression;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + ((initial == null) ? 0 : initial.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SimpleNumberSeriesDefinition))
			return false;
		SimpleNumberSeriesDefinition other = (SimpleNumberSeriesDefinition) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (initial == null) {
			if (other.initial != null)
				return false;
		} else if (!initial.equals(other.initial))
			return false;
		return true;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}
	
	
	
	
	
	
}
