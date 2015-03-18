package de.uni_bamberg.wiai.cogsys.tools;

import java.util.Iterator;

public class ConvertingIterator<To, From extends To> implements Iterator<To> {
	
	Iterator<From> source;
	
	public ConvertingIterator(Iterator<From> it) {
		source = it;
	}

	@Override
	public boolean hasNext() {
		return source.hasNext();
	}

	@Override
	public To next() {
		return (To) source.next();
	}

	@Override
	public void remove() {
		source.remove();		
	}
	
	
}
