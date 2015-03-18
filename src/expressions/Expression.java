package expressions;

import java.util.Arrays;

import org.apache.log4j.Logger;

import series.NumberSeries;
import expressions.exceptions.ExpressionCalculationException;
import expressions.exceptions.NumberSeriesGenerationException;
import expressions.exceptions.TooFewPrecursorsException;
import expressions.types.ExpressionType;

public abstract class Expression {
	Logger logger = Logger.getLogger(Expression.class);
	
	private ExpressionType type;
	
	public Expression(ExpressionType type) {
		super();
		if(type == null)
			throw new IllegalArgumentException("Argument 'type' is null");
		
		this.type = type;
	}


	// How many previous numbers must be present to calculate this expression
	abstract public int requiredPrecursors();
	
	
	/*                        *
	 *                        *
	 * Number series creation *
	 *                        *
	 *                        */
	// Calculate this expression with the given precursors if you are at a certain index
	public int get(NumberSeries previous, int index) throws ExpressionCalculationException {
		int required = requiredPrecursors();
		if(previous == null && required>0)
			throw new TooFewPrecursorsException(new NullPointerException());
		if(previous != null && required>previous.size())
			throw new TooFewPrecursorsException();
		
		//System.out.println(String.format("Calculating pos %d; prefix %s", index, previous.toString()));
		int result = calculate(previous, index);
		return result;
	}
	
	// Generate a number series of a certain total length with given initial numbers
	public NumberSeries generateSeries(NumberSeries prefix, int length) throws NumberSeriesGenerationException {
		if(length < 0)
			throw new NumberSeriesGenerationException("Number series with negative length requested.");
		
		if(prefix == null)
			prefix=new NumberSeries();
		
		int[] numbers = new int[length];
		for(int i=0;i<Math.min(length,prefix.size());i++)
				numbers[i] = prefix.get(i);
			
		
		try {
			final int requiredPrecursors = requiredPrecursors();
			for(int pos = prefix.size(); pos<length;pos++) {
				numbers[pos] = get(new NumberSeries(
						Arrays.copyOfRange(numbers, pos-requiredPrecursors, pos)),pos);
			}
		} catch (ExpressionCalculationException e) {
			throw new NumberSeriesGenerationException(e);
		}
		
		return new NumberSeries(numbers);
	}
	
	// internal calculation
	abstract protected int calculate(NumberSeries previous, int index) 
			throws ExpressionCalculationException;



	public String toString() {
		return formatExpression("x",1);
	}
	
	public abstract String formatExpression(String seriesName, int totalExpressions);


	public ExpressionType getType() {
		return type;
	}


	public abstract int getDepth();
	
	abstract public boolean equals(Object obj);
	abstract public int hashCode();
}
