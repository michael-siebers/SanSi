package expressions;

import series.NumberSeries;
import expressions.exceptions.ExpressionCalculationException;
import expressions.types.BinaryExpressionType;

public class BinaryOperator extends	Expression {
	private Expression firstChild;
	private Expression secondChild;
	private BinaryExpressionType binaryType;
	
	public BinaryOperator(BinaryExpressionType type, 
			Expression firstArgument, Expression secondArgument) {
		super(type);
		binaryType = type;
		
		if(firstArgument==null || secondArgument==null)
			throw new NullPointerException("Expressions must be initialized!");
		
		this.firstChild = firstArgument;
		this.secondChild = secondArgument;
	}

	
	
	public BinaryExpressionType getBinaryType() {
		return binaryType;
	}



	@Override
	protected int calculate(NumberSeries previous, int index) 
		throws ExpressionCalculationException  {
		return getBinaryType().applyBinary(firstChild.calculate(previous, index),
				secondChild.calculate(previous, index));
	}
	

	@Override
	public String formatExpression(String seriesName, int totalExpressions) {
		String firstString = firstChild.getType().getRequiredNumberOfSubexpressions()>1?
				"(" + firstChild.formatExpression(seriesName, totalExpressions) + ")" : 
					firstChild.formatExpression(seriesName, totalExpressions);
		String secondString = secondChild.getType().getRequiredNumberOfSubexpressions()>1?
				"(" + secondChild.formatExpression(seriesName, totalExpressions) + ")" : 
						secondChild.formatExpression(seriesName, totalExpressions);		
		return getBinaryType().formatExpressionType(firstString, secondString);
	}

	
	@Override
	public int requiredPrecursors() {
		return Math.max(firstChild.requiredPrecursors(), secondChild.requiredPrecursors());
	}
	
	@Override
	public int getDepth() {
		return 1+Math.max(firstChild.getDepth(), secondChild.getDepth());
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((binaryType == null) ? 0 : binaryType.hashCode());
		result = prime * result
				+ ((firstChild == null) ? 0 : firstChild.hashCode());
		result = prime * result
				+ ((secondChild == null) ? 0 : secondChild.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BinaryOperator))
			return false;
		BinaryOperator other = (BinaryOperator) obj;
		if (binaryType == null) {
			if (other.binaryType != null)
				return false;
		} else if (!binaryType.equals(other.binaryType))
			return false;
		return (firstChild.equals(other.firstChild) && secondChild.equals(other.secondChild))
			|| ( binaryType.isCommutative() && (firstChild.equals(other.secondChild) && secondChild.equals(other.firstChild)));
	}

	
	public Expression getLeftChild() {
		return firstChild;
	}
	
	public Expression getRightChild() {
		return secondChild;
	}



	/*
	 * TODO:
	 * - combine multiple operator applications (if possible)
	 * @see expressions.Expression#normalize()
	 */
	@Override
	public Expression normalize() throws ExpressionCalculationException {
		Expression innerFirst = firstChild.normalize();
		Expression innerSecond = secondChild.normalize();
		
		// apply on constants
		if((innerFirst instanceof ConstantValue)
				&& (innerSecond instanceof ConstantValue) ) {
			int firstVal =  ((ConstantValue) innerFirst).getValue();
			int secondVal =  ((ConstantValue) innerSecond).getValue();
			return new ConstantValue(binaryType.applyBinary(firstVal, secondVal));
		}
		
		// remove neutral element
		if(innerSecond instanceof ConstantValue) {
			int secondVal =  ((ConstantValue) innerSecond).getValue();
			if(secondVal == binaryType.getNeutralElement())
				return innerFirst;
		} else if (binaryType.isCommutative() && (innerFirst instanceof ConstantValue)) {
			int firstVal =  ((ConstantValue) innerFirst).getValue();
			if(firstVal == binaryType.getNeutralElement())
				return innerSecond;
		}
		
		// reorder subexpressions (if operator is commutative)
		if(binaryType.isCommutative()) {
			if(innerFirst instanceof ConstantValue) { 
				// if both were constant the operator would have been applied
				// so do nothing unchanged
			} else if (innerSecond instanceof ConstantValue) {
				// only the second is a constant bring it to front
				final Expression innerTmp = innerFirst;
				innerFirst = innerSecond;
				innerSecond = innerTmp;
			} else {
				// no constants
				// order subexpressions according to depth
				if(innerSecond.getDepth()<innerFirst.getDepth()) {
					final Expression innerTmp = innerFirst;
					innerFirst = innerSecond;
					innerSecond = innerTmp;
				}
			}
		}
		
		return this.binaryType.getExpression(innerFirst, innerSecond);
	}

	
}
