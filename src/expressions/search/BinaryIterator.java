package expressions.search;

import java.util.Iterator;
import java.util.List;

import expressions.ExpressionTree;
import expressions.types.BinaryExpressionType;

public class BinaryIterator extends ExpressionTreeIterator {
	
	private List<BinaryExpressionType> binaryList;
	private Iterator<BinaryExpressionType> binaryIt;
	private ExpressionTreeIterator leftIt;
	private ExpressionTreeIterator rightIt;
	
	private BinaryExpressionType binary;
	private ExpressionTree left;
	private ExpressionTree right;

	
	
	public BinaryIterator(List<BinaryExpressionType> binaryList,
			ExpressionTreeIterator leftIt, ExpressionTreeIterator rightIt) {
		this.binaryList = binaryList;
		this.leftIt = leftIt;
		this.rightIt = rightIt;

		reset();
	}

	@Override
	public void reset() {
		binaryIt = binaryList.iterator();
		binary = null;
		
		if(binaryIt.hasNext()) {
			binary = binaryIt.next();
			restartLeft();
		}
		
		
	}

	@Override
	public boolean hasNext() {
		return (binary != null && left != null && right != null);
	}

	@Override
	public ExpressionTree next() {
		ExpressionTree result = new ExpressionTree(binary, left, right);
		
		if(rightIt.hasNext())
			right = rightIt.next();
		else {
			if(leftIt.hasNext()) {
				left = leftIt.next();
				restartRight();
			} else {
				binary = null;
				if(binaryIt.hasNext()) {
					binary = binaryIt.next();
					restartLeft();
				}
			}
		}

		return result;
	}

	private void restartLeft() {
		left = null;
		leftIt.reset();
		
		if(leftIt.hasNext()) {
			left = leftIt.next();
			restartRight();
		}
	}

	private void restartRight() {
		rightIt.reset();
		right = null; // might be unneccesary. Nevertheless if rightstays null hasNext return false
		
		if (rightIt.hasNext())
			right = rightIt.next();
	}

}
