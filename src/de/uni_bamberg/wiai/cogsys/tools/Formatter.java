package de.uni_bamberg.wiai.cogsys.tools;

import java.util.List;


public class Formatter {
	
	private static final String NULL_STRING = "null";
	private static final String EMPTY_ARRAY_STRING = "empty %s-array";

	static public void output(Object o) {
		System.out.println(format(o));
	}
	
	static public String format(Object o) {
		if (o==null)
			return NULL_STRING;
		
		String simpleClassName = o.getClass().getSimpleName();
		if(simpleClassName.endsWith("[]")) 
			return String.format("%s: %s", simpleClassName.substring(0, simpleClassName.length()-2),
					implode((Object[]) o, ",", "[", "]" ));
		return String.format("%s: %s", simpleClassName, o.toString());
		
	}
	
	static public String implode(List<?> array, String separator, String opening, String closing) {
		return implode(array.toArray(), separator, opening, closing);
	}
	static public String implode(Object[] array, String separator, String opening, String closing) {
		if(array==null) 
			return NULL_STRING;
		if(array.length==0) {
			if (opening.isEmpty() && closing.isEmpty()) {
				String simpleClassName = array.getClass().getSimpleName();
				simpleClassName = simpleClassName.substring(0, simpleClassName.length()-2);
				return String.format(Formatter.EMPTY_ARRAY_STRING, simpleClassName);
			}
			else
				return String.format("%s%s", opening,closing);
		}
			
		
		StringBuilder sb = new StringBuilder(opening);
		sb.append(array[0]);
		for(int i = 1; i < array.length; i++)
			sb.append(separator).append(array[i]);
		sb.append(closing);
		
		return sb.toString();		
	}
	
	static public String implode(int[] array, String separator, String opening, String closing) {
		if(array==null) 
			return NULL_STRING;
		if(array.length==0) {
			if (opening.isEmpty() && closing.isEmpty())
				return String.format(Formatter.EMPTY_ARRAY_STRING, "int");
			else
				return String.format("%s%s", opening,closing);
		}
			
		
		StringBuilder sb = new StringBuilder(opening);
		sb.append(array[0]);
		for(int i = 1; i < array.length; i++)
			sb.append(separator).append(array[i]);
		sb.append(closing);
		
		return sb.toString();		
	}

	static public String implode(int[] array, String separator) {
		return implode(array,separator,"[", "]");
	}

	static public String implode(int[] array) {
		return implode(array,", " ,"[", "]");
	}

	
}
