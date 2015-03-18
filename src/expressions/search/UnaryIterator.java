package expressions.search;

import java.util.Iterator;
import java.util.List;

import expressions.ExpressionTree;
import expressions.types.UnaryExpressionType;

public class UnaryIterator extends ExpressionTreeIterator {
	private List<UnaryExpressionType> unaryTypes;
	private Iterator<UnaryExpressionType> unaryIterator = null;
	private ExpressionTreeIterator subIterator;
	private ExpressionTree content;
	
	public UnaryIterator(
			List<UnaryExpressionType> unaryTypes,
			ExpressionTreeIterator subIterator,
			int maxDepth) {
		this.unaryTypes = unaryTypes;
		this.subIterator = subIterator;
		
		reset();
	}

	@Override
	public boolean hasNext() {
		if(content == null)
			return false; // initial state with no subExpressions
		
		return (unaryIterator==null || unaryIterator.hasNext()) || subIterator.hasNext();
	}

	@Override
	public ExpressionTree next() {
		if(unaryIterator == null) {
			unaryIterator = unaryTypes.iterator();
			return content;
		}
		
		if(unaryIterator.hasNext())
			return new ExpressionTree(unaryIterator.next(), content);
		
		// no more unary terms
		content = subIterator.next();
		unaryIterator = unaryTypes.iterator();
		
		return content;
	}

	@Override
	public void reset() {
		unaryIterator = null;
		
		content =(subIterator.hasNext())?content = subIterator.next():null;
	}

}
