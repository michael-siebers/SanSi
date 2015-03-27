package series.inducers;

import org.apache.log4j.Logger;

import series.NumberSeries;
import series.SimpleNumberSeriesDefinition;
import expressions.Expression;
import expressions.ExpressionTree;
import expressions.search.DumbIteratorFactory;
import expressions.search.ExpressionTreeIterator;

public class SimpleNumberSeriesInducer {
	static private final Logger logger = Logger.getLogger(SimpleNumberSeriesInducer.class);
	
	public SimpleNumberSeriesDefinition induce (NumberSeries series, int maxDepth) {
		logger.debug("entering SimpleNumberSeriesInducer.induce(NumberSeries,int)");
		if (series == null) 
			throw new IllegalAccessError("Argument 'series' is null.");
		
		/* the maximal initial size depends on the available
		 * numbers. To induce and check a series with n predecessors
		 * there must be n initial numbers, n numbers to induce the 
		 * regularity and 1 number to validate the definition  
		 * 
		 * Limited to 5. Bigger numbers seem psychological implausible
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
				if (instanced != null) {
					logger.debug("leaving SimpleNumberSeriesInducer.induce(NumberSeries,int)");
					return new SimpleNumberSeriesDefinition(series
							.getSubsequence(0, initialSize), instanced);
				}
				count++;
			}
			logger.info(String.format("Tried %d alternatives.", count));
		}
		
		logger.debug("leaving SimpleNumberSeriesInducer.induce(NumberSeries,int)");
		return null;		
	}
}
