package series;

import org.apache.log4j.Logger;

import expressions.Expression;
import expressions.ExpressionTree;
import expressions.exceptions.NumberSeriesGenerationException;
import expressions.search.DumbIteratorFactory;
import expressions.search.ExpressionTreeIterator;

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
	
	static public SimpleNumberSeriesDefinition induce (NumberSeries series, int maxDepth) {
		if (series == null) 
			throw new IllegalAccessError("Argument 'series' is null.");
		
		/* the maximal initial size depends on the available
		 * numbers. To induce and check a series with n predecessors
		 * there must be n initial numbers, n numbers to induce the 
		 * regularity and 1 number to validate the definition  
		 * 
		 * Limited to 5. Bigger numbers seem psychological unplausible
		 */
		int maxInitialSize = Math.min((series.size()-1)/2, 5);

		
		int minDepth = 2;
		int searchSize = (maxDepth - Math.min(minDepth, maxDepth)+1) * (maxInitialSize+1);
		int[] depths = new int[searchSize];
		ExpressionTreeIterator[] trees = new ExpressionTreeIterator[searchSize];
		int[] initials = new int[searchSize];
		
		
		int pos = 0;
		
		
		// Search with increasing initial size and increasing depth
		for(int initialSize = 0; initialSize <= maxInitialSize; initialSize++) {
			depths[pos] = Math.min(minDepth, maxDepth);
			trees[pos] = DumbIteratorFactory.getSearchIterator(depths[pos]);
			initials[pos] = initialSize;
			pos++;
		}	
		for (int depth = Math.min(minDepth, maxDepth)+1; depth <= maxDepth; depth++) {
			for(int initialSize = 0; initialSize <= maxInitialSize; initialSize++) {
				depths[pos] = depth;
				trees[pos] = DumbIteratorFactory.getRecursionOperatorIterator(depth);
				initials[pos] = initialSize;
				pos++;
			}
		}
		
		
		for (int i = 0; i<searchSize; i++) {
			int initialSize = initials[i];
			NumberSeries toPredict = series.getSubsequence(initialSize,
					series.size());
			ExpressionTreeIterator treeIt = trees[i];
			logger.info(String.format(
						"Trying with %d initials (depth %d)",
						initialSize, depths[i]));
			int count = 0;
			while (treeIt.hasNext()) {
				ExpressionTree nextTree = treeIt.next();
				logger.debug(String.format(
						"Trying with %d initials (depth %d): %s",
						initialSize, depths[i], nextTree.toString()));
				Expression instanced = nextTree.fitTo(series, toPredict);
				if (instanced != null)
					return new SimpleNumberSeriesDefinition(series
							.getSubsequence(0, initialSize), instanced);
				count++;
			}
			logger.info(String.format("Tried %d alternatives.", count));
		}
		
		
		return null;		
	}

	
	static public SimpleNumberSeriesDefinition induce2 (NumberSeries series, boolean allowSubseries) {
		if (series == null) 
			throw new IllegalAccessError("Argument 'series' is null.");
		
		/* the maximal initial size depends on the available
		 * numbers. To induce and check a series with n predecessors
		 * there must be n initial numbers, n numbers to induce the 
		 * regularity and 1 number to validate the definition  
		 * 
		 * Limited to 5. Bigger numbers seem psychological unplausible
		 */
		int maxInitialSize = //Math.min((series.size()-1)/2, 5);
			series.size()/3;

		/*
		int minDepth = 2;
		int searchSize = (maxDepth - Math.min(minDepth, maxDepth)+1) * (maxInitialSize+1);
		int[] depths = new int[searchSize];
		ExpressionTreeIterator[] trees = new ExpressionTreeIterator[searchSize];
		int[] initials = new int[searchSize];
		
		
		int pos = 0;
		
		
		// Search with increasing initial size and increasing depth
		for(int initialSize = 0; initialSize <= maxInitialSize; initialSize++) {
			depths[pos] = Math.min(minDepth, maxDepth);
			trees[pos] = DumbIteratorFactory.getSearchIterator(depths[pos]);
			initials[pos] = initialSize;
			pos++;
		}	
		for (int depth = Math.min(minDepth, maxDepth)+1; depth <= maxDepth; depth++) {
			for(int initialSize = 0; initialSize <= maxInitialSize; initialSize++) {
				depths[pos] = depth;
				trees[pos] = DumbIteratorFactory.getRecursionOperatorIterator(depth);
				initials[pos] = initialSize;
				pos++;
			}
		}
		
		
		for (int i = 0; i<searchSize; i++) {
			int initialSize = initials[i];
			NumberSeries toPredict = series.getSubsequence(initialSize,
					series.size());
			ExpressionTreeIterator treeIt = trees[i];
			logger.info(String.format(
						"Trying with %d initials (depth %d)",
						initialSize, depths[i]));
			int count = 0;
			while (treeIt.hasNext()) {
				ExpressionTree nextTree = treeIt.next();
				logger.debug(String.format(
						"Trying with %d initials (depth %d): %s",
						initialSize, depths[i], nextTree.toString()));
				Expression instanced = nextTree.fitTo(series, toPredict);
				if (instanced != null)
					return new NumberSeriesDefinition(series
							.getSubsequence(0, initialSize), instanced);
				count++;
			}
			logger.info(String.format("Tried %d alternatives.", count));
		}
		*/
		for(int with = 0; with <= (allowSubseries?1:0); with++) {
			for (int initialSize = 0; initialSize<=maxInitialSize;initialSize++) {
				NumberSeries toPredict = series.getSubsequence(initialSize,
						series.size());
				ExpressionTreeIterator treeIt = 
					(with==1)?DumbIteratorFactory.nsIterator():DumbIteratorFactory.naiveIterator();
				logger.info(String.format(
							"Trying with %d initials",
							initialSize));
				int count = 0;
				while (treeIt.hasNext()) {
					ExpressionTree nextTree = treeIt.next();
					logger.debug(String.format(
							"Trying with %d initials: %s",
							initialSize, nextTree.toString()));
					Expression instanced = nextTree.fitTo(series, toPredict);
					if (instanced != null)
						return new SimpleNumberSeriesDefinition(series
								.getSubsequence(0, initialSize), instanced);
					count++;
				}
				logger.info(String.format("Tried %d alternatives.", count));
			}
		}
		return null;		
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
