package series;

import expressions.exceptions.NumberSeriesGenerationException;


public class InterlacedNumberSeriesDefinition  {
	private NumberSeriesDefinition[] definitions;
	
	
	
	public InterlacedNumberSeriesDefinition(NumberSeriesDefinition... definitions) {
		super();
		this.definitions = definitions;
	}



	public NumberSeries produce(int length)
			throws NumberSeriesGenerationException {
		
		int[][] series = new int[definitions.length][];
		int subSize = length / definitions.length + ((length%definitions.length>0)?1:0);
		
		for(int i=0; i<definitions.length; i++)
			series[i] = definitions[i].produce(subSize).toArray();
		
		int[] numbers = new int[length];
		for(int pos=0; pos<length;pos++)
			numbers[pos] = series[pos%definitions.length]
			                      [pos/definitions.length]; 
		return new NumberSeries(numbers);
	}

	
}
