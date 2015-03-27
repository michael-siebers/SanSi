package apps;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import expressions.exceptions.ExpressionCalculationException;
import series.NumberSeries;
import series.SimpleNumberSeriesDefinition;
import series.inducers.SimpleNumberSeriesInducer;


public class ConsoleApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
	    BasicConfigurator.configure();
	    Logger.getRootLogger().setLevel(Level.ERROR);
	    
	    
	    // parse parameters
	    if(args.length<=0 || args.length>3) {
	    	System.out.println(getUsage());
	    	System.exit(-1);
	    }
	    
	    NumberSeries series = null;
	    try{
	    	series = NumberSeries.fromString(args[0]);
	    } catch (NumberFormatException e) {
			System.out.println(String.format("Error parsing number series '%s': %s", 
					args[0], e.getMessage()));
		}
	    int depth = 4;
	    if (args.length>=2)
			try {
				depth = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println(String.format("Error parsing depth '%s': %s", 
						args[1], e.getMessage()));
			}
	    
		if(args.length == 3) {
			if(args[2].equalsIgnoreCase("DEBUG")) {
				Logger.getRootLogger().setLevel(Level.DEBUG);
			} else if (args[2].equalsIgnoreCase("TRACE")) {
				Logger.getRootLogger().setLevel(Level.TRACE);
			} 
		}
		SimpleNumberSeriesInducer inducer = new SimpleNumberSeriesInducer();
		SimpleNumberSeriesDefinition def = inducer.induce(series, depth);

	    if(def==null)
	    	System.out.println(String.format("No Definition found for %s", series.toString()));
	    else {
	    	System.out.println(def);
	    	try {
				int next = def.getExpression().get(series, series.size());
				System.out.println(String.format("Next: %d", next));
			} catch (ExpressionCalculationException e) {
				System.out.println(String.format("Error calculating next number: %s.",
						e.getMessage()));
			}
	    }
	}

	private static String getUsage() {
		return "Usage:\n" +
				"ConsoleApp <number series> [<search depth>]\n" +
				"\n" +
				"<number series> is a character separated (default: ,) list of numbers.";
	}

}
