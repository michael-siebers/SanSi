package series;

import expressions.exceptions.NumberSeriesGenerationException;

public interface NumberSeriesDefinition {

	public NumberSeries produce(int length)
			throws NumberSeriesGenerationException;
	
	public String toString(String name);
	
	public void setName(String name);
}