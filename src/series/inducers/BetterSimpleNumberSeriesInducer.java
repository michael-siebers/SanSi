package series.inducers;

import org.apache.log4j.Logger;

import series.NumberSeries;
import series.SimpleNumberSeriesDefinition;
import expressions.Expression;
import expressions.ExpressionTree;
import expressions.search.DumbIteratorFactory;
import expressions.search.ExpressionTreeIterator;

/*
 * I don't remember why or even if this induction method is better. All I can say is 
 * that it is newer and the one used for the KI2012 paper.
 */
public class BetterSimpleNumberSeriesInducer {
	static private final Logger logger = Logger.getLogger(BetterSimpleNumberSeriesInducer.class);
	
	public SimpleNumberSeriesDefinition induce (NumberSeries series, boolean allowSubseries) {
		logger.debug("entering BetterSimpleNumberSeriesInducer.induce(NumberSeries, boolean)");
		if (series == null) 
			throw new IllegalAccessError("Argument 'series' is null.");
		
		/* the maximal initial size depends on the available
		 * numbers. To induce and check a series with n predecessors
		 * there must be n initial numbers, n numbers to induce the 
		 * regularity and 1 number to validate the definition  
		 * 
		 * Limited to 5. Bigger numbers seem psychological implausible
		 */
		int maxInitialSize = //Math.min((series.size()-1)/2, 5);
			series.size()/3;

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
					if (instanced != null) {
						logger.debug("leaving BetterSimpleNumberSeriesInducer.induce(NumberSeries, boolean)");
						return new SimpleNumberSeriesDefinition(series
								.getSubsequence(0, initialSize), instanced);
					}
					count++;
				}
				logger.info(String.format("Tried %d alternatives.", count));
			}
		}
		logger.debug("leaving BetterSimpleNumberSeriesInducer.induce(NumberSeries, boolean)");
		return null;		
	}
}
