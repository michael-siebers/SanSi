package apps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import series.NumberSeries;
import series.SimpleNumberSeriesDefinition;
import series.inducers.BetterSimpleNumberSeriesInducer;
import de.uni_bamberg.wiai.cogsys.tools.Pair;
import expressions.exceptions.NumberSeriesGenerationException;

public class BatchApp {
	private static final Logger logger = Logger.getLogger(BatchApp.class);

	private int requiredNumbers;
	private int numbersToPredict;
	
	private List<Pair<String, NumberSeries>> seriesList;
	private BufferedWriter output;
	
	public BatchApp(List<Pair<String, NumberSeries>> seriesList,
			int requiredNumbers, int numbersToPredict, File outFile)
		throws IOException {
		super();
		this.seriesList = seriesList;
		this.requiredNumbers = requiredNumbers;
		this.numbersToPredict = numbersToPredict;
		output = new BufferedWriter(new FileWriter(outFile));
	}
	
	
	
	public void checkAll() throws IOException {
		try {
			output.append("name\tsplit point\tcorrect\tdefinition\tdepth\ttime\n");
			for (Pair<String, NumberSeries> series : seriesList) {
				checkSeries(series);
				output.flush();
			}
		} finally {
			output.close();
		}
	}
	
	public boolean checkSeries(Pair<String, NumberSeries> namedSeries) throws IOException {
		logger.info(String.format("Working on '%s'", namedSeries.getFirst()));
		NumberSeries series = namedSeries.getSecond();
		int length = series.size();
		if(length <= numbersToPredict) {
			logger.error(String.format("Cannot check Series '%s'. Too few elements (%d).",
					namedSeries.getFirst(), length));
			return false;
		}
		
		if(length < requiredNumbers+numbersToPredict) {
			logger.warn(String.format("Series '%s' has too few elements. Using only %d for " +
					"number series induction.", namedSeries.getFirst(), length-numbersToPredict));
		}
		
		
		final int splitPoint = Math.min(length-numbersToPredict, requiredNumbers);
		NumberSeries forInduction = series.getSubsequence(0,  splitPoint);
		NumberSeries forCheck = series.getSubsequence(splitPoint, splitPoint+3);
		logger.debug(String.format("Checking %s: inducing on %s, testing on %s",
				series.toString(), forInduction.toString(), forCheck.toString()));
		
		long startTime = System.currentTimeMillis();
		BetterSimpleNumberSeriesInducer inducer = new BetterSimpleNumberSeriesInducer();
		SimpleNumberSeriesDefinition inducedDef = inducer.induce(forInduction, true);
		long time = System.currentTimeMillis() - startTime;
		
		boolean correct = true;
		
		if (inducedDef != null) {
			try {
				NumberSeries inducedSeries = inducedDef.produce(splitPoint
						+ numbersToPredict);

				for (int pos = 0; pos < numbersToPredict; pos++)
					if (inducedSeries.get(splitPoint + pos) != forCheck
							.get(pos)) {
						correct = false;
						break;
					}
			} catch (NumberSeriesGenerationException e) {
				correct = false;
			}
			output.write(String.format("%s\t%d\t%s\t%s\t%d\t%d\n", namedSeries
					.getFirst(), splitPoint + 1, correct ? "correct" : "wrong",
					inducedDef.toString().replace("\n", "; "), inducedDef
							.getExpression().getDepth(), time));
		} else { // null induced
			correct = false;
			output.write(String.format("%s\t%d\twrong\tnone\t--\t%d\n", namedSeries
						.getFirst(), splitPoint, time));
		}
		
		return correct;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
	    BasicConfigurator.configure();
	    Logger.getRootLogger().setLevel(Level.WARN);
	    Logger.getLogger(BatchApp.class).setLevel(Level.INFO);
	    
	 // parse parameters
	    if(args.length<=0 || args.length>2) {
	    	System.out.println(getUsage());
	    	System.exit(-1);
	    }
	    
	    
		File outfile = new File(args[1]);
		List<Pair<String, NumberSeries>> named = new LinkedList<Pair<String,NumberSeries>>();
		try {
			File infile = new File(args[0]);
			BufferedReader in = new BufferedReader(new FileReader(infile));
			
			String line = in.readLine();
			
			while(line != null) {
				line = line.trim();
				if(line.isEmpty() || line.startsWith("#")) {
					line = in.readLine();
					continue;
				}
				String[] values = line.split(";");

				if(values.length != 2) {
					logger.error(String.format("Error parsing input file. Wrong number of columns in '%s'", line));
				} else {
				try {
					final NumberSeries fromString = NumberSeries.fromString(values[1]);
					named.add(new Pair<String, NumberSeries>(
							values[0], fromString));
				} catch (NumberFormatException e) {
					logger.warn(String.format("Warning parsing input file. Malformatted number in '%s'", line));
				}
				}
				line = in.readLine();
			}
			
			BatchApp main = new BatchApp(named, 12, 3, outfile);
			main.checkAll();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	private static String getUsage() {
		return "Usage: BatchApp <input filename> <output filename>";
	}




}
