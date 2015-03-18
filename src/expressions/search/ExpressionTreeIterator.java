package expressions.search;

import java.util.Iterator;

import expressions.ExpressionTree;

abstract public class ExpressionTreeIterator implements Iterator<ExpressionTree> {

	public ExpressionTreeIterator() {
		super();
	}


	// restart iterator
	abstract public void reset();

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
