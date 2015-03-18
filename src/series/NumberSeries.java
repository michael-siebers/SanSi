package series;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_bamberg.wiai.cogsys.tools.Formatter;


/* Immutable number series */
public class NumberSeries {
private int[] list;
	
	public NumberSeries (int... numbers) {
		if (numbers == null)
			list = new int[0];
		list = numbers;
	}
	
	public int size() {
		return list.length;
	}
	
	// subsequence including from, excluding to
	public NumberSeries getSubsequence(int from, int to) {
		if(from < 0) 
			throw new IllegalArgumentException("Cannot create subseqence starting from negative index.");
		if(from >= list.length)
			throw new IllegalArgumentException("Cannot create subseqence starting after the end of the sequence.");
		if(from > to) 
			throw new IllegalArgumentException("Subsequence starting after its begin.");
		if(to > list.length) 
			throw new IllegalArgumentException("Cannot create subseqence ending after the end of the sequence.");
		
		int[] newNumbers = new int[to-from];
		System.arraycopy(list, from, newNumbers, 0, to-from);
		return new NumberSeries(newNumbers);
	}
	
	// starting from 0
	public int get(int index) {
		return list[index];
	}
	
	// starting from 1
	public int getFromBack(int index) {
		return list[list.length-index];
	}
	
	public String toString() {
		return Formatter.implode(list);
	}
	
	public String toString(String prefix, String separator, String suffix) {
		return Formatter.implode(list,separator,prefix,suffix);
	}
	
	public int[] toArray() {
		return Arrays.copyOf(list, list.length);
	}
	
	static public NumberSeries fromString(String asString) 
		throws NumberFormatException {
		Matcher separatorFinder = 
			Pattern.compile("\\d+\\s*(\\D+)\\s*\\d").matcher(asString);
		String sep = ","; // default
		if(separatorFinder.matches()) 
			sep = separatorFinder.group(1);
		String separator = String.format("\\s*%s\\s*", sep);
		String[] numbersAsString = asString.split(separator);
		
		int[] numbers = new int[numbersAsString.length];
		for(int i = 0; i <numbersAsString.length;i++)
			numbers[i] = Integer.parseInt(numbersAsString[i]);

		return new NumberSeries(numbers);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(list);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NumberSeries))
			return false;
		NumberSeries other = (NumberSeries) obj;
		if (!Arrays.equals(list, other.list))
			return false;
		return true;
	}
	
	
	
}
