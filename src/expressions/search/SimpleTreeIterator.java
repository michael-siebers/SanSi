package expressions.search;

import java.util.Iterator;
import java.util.List;

import expressions.ExpressionTree;

public class SimpleTreeIterator extends ExpressionTreeIterator {
	private Iterator<ExpressionTree> source;
	private List<ExpressionTree> trees;
	
	public SimpleTreeIterator(
			List<ExpressionTree> sourceTrees) {

		trees = sourceTrees;
		reset();
	}

	@Override
	public void reset() {
		source = trees.iterator();

	}

	@Override
	public boolean hasNext() {
		return source.hasNext();
	}

	@Override
	public ExpressionTree next() {
		return source.next();
	}

}
