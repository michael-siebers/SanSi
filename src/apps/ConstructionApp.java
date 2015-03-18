package apps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import series.NumberSeries;
import series.NumberSeriesCreator;
import series.NumberSeriesDefinition;
import series.NumberSeriesCreator.ImpossibleOperationException;


public class ConstructionApp {
	/**
	 * @param args
	 * @throws ImpossibleOperationException 
	 */
	public static void main(String[] args) throws ImpossibleOperationException {
		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);

		if(args.length<2) {
			System.out.println("Usage:\nConstructionApp <number of series to create> <lenght of series>");
			System.exit(-1);
		}
		
		int seriesToCreate = Integer.parseInt(args[0]);
		int length = Integer.parseInt(args[1]);

		System.out.println(String.format("Creating %d series of length %d%s",
				seriesToCreate, length, (seriesToCreate*length>5000)?"\nThis may take a while":""));
		
		NumberSeriesCreator creator = new NumberSeriesCreator();
		final Map<NumberSeries, Set<NumberSeriesDefinition>> randomNumberSeries =
			creator.getRandomNumberSeries(seriesToCreate, length,-1000,1000,
					creator.getDefaultNumberSeries(length));
		
		List<NumberSeries> seriesList = new ArrayList<NumberSeries>(randomNumberSeries.keySet());
		
		File seriesFile = new File("resources/series.csv");
		File defFile = new File("resources/definitions.csv");
		
		try {
			BufferedWriter seriesWriter = new BufferedWriter(new FileWriter(seriesFile));
			BufferedWriter defWriter = new BufferedWriter(new FileWriter(defFile));
			
			int digits = (int) Math.floor(Math.log10(seriesList.size())) +1;
			String nameString=String.format("S%%0%dd", digits);
			
			int pos = 1;
			for(NumberSeries ns:seriesList) {
				String name = String.format(nameString, pos++);
				seriesWriter.append(name).append(";").append(ns.toString("",",","")).append("\n");
				for(NumberSeriesDefinition def:randomNumberSeries.get(ns)) {
					defWriter.append(name).append(";").
						append(def.toString().replace("\n", ", ")).append("\n");
				}
				
			}
			
			defWriter.close();
			seriesWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		
		
	}

	
}
