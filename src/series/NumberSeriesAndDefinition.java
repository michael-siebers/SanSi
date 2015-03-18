package series;

import de.uni_bamberg.wiai.cogsys.tools.Pair;

public class NumberSeriesAndDefinition {
	private Pair<NumberSeries, NumberSeriesDefinition> content;
	public NumberSeriesAndDefinition(NumberSeries series, NumberSeriesDefinition definition) {
		if(series == null)
			throw new IllegalArgumentException("Argument 'series' is null.");
		if(definition == null)
			throw new IllegalArgumentException("Argument 'definition' is null.");
		
		content = new Pair<NumberSeries, NumberSeriesDefinition>(series, definition);
	}
	public NumberSeries getSeries() {
		return content.getFirst();
	}
	public NumberSeriesDefinition getDefinition() {
		return content.getSecond();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((content == null) ? 0 : content.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NumberSeriesAndDefinition))
			return false;
		NumberSeriesAndDefinition other = (NumberSeriesAndDefinition) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		return true;
	}
	
	
	
	
	
}