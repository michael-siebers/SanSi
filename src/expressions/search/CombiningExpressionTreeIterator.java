package expressions.search;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import expressions.ExpressionTree;

public class CombiningExpressionTreeIterator extends ExpressionTreeIterator {

	private List<ExpressionTreeIterator> iterators;
	private Iterator<ExpressionTreeIterator> source;
	private ExpressionTreeIterator current;
	private ExpressionTree next;
		
	
	
		
	public CombiningExpressionTreeIterator(List<ExpressionTreeIterator> iterators) {
		super();
		this.iterators = iterators;
		
		reset();
	}
	
	public CombiningExpressionTreeIterator(ExpressionTreeIterator... iterators) {
		this(Arrays.asList(iterators));
	}

	@Override
	public void reset() {
		for(ExpressionTreeIterator eIt:iterators)
			eIt.reset();
		
		current = null; // invalid
		next = null; // invalid
		source = iterators.iterator();
		
		while(next==null && source.hasNext()) {
			current = source.next();
			
			while(next==null && current.hasNext())
				next = current.next();
		}
	}

	@Override
	public boolean hasNext() {
		return next!=null;
	}

	@Override
	public ExpressionTree next() {
		ExpressionTree result = next;
		
		next = null;
		while(next==null && current.hasNext())
			next = current.next();
		
		while(next==null && source.hasNext()) {
			current = source.next();
			
			if(current.hasNext())
				next = current.next();
		}
		return result;
	}

}
