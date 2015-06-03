package expressions;

import java.util.Iterator;

import org.apache.log4j.Logger;

import series.NumberSeries;
import de.uni_bamberg.wiai.cogsys.tools.ConvertingIterator;
import expressions.exceptions.ExpressionCalculationException;
import expressions.types.BinaryExpressionType;
import expressions.types.ExpressionType;
import expressions.types.UnaryExpressionType;
import expressions.types.ValueExpressionType;

/**
 * A tree of expression types
 * 
 * @author Michael Siebers
 *
 */
public class ExpressionTree {
	static private Logger logger = Logger.getLogger(ExpressionTree.class);
	
	private ExpressionType type;
	private ExpressionTree firstChild;
	private ExpressionTree secondChild;
	
	public ExpressionTree(ExpressionType type,
			ExpressionTree firstChild, ExpressionTree secondChild) {
		super();
		this.type = type;
		this.firstChild = firstChild;
		this.secondChild = secondChild;
		
		int reqChildren = type.getRequiredNumberOfSubexpressions();
		if ((reqChildren >= 1 && firstChild==null) ||
				(reqChildren>=2 && secondChild==null))
			throw new IllegalArgumentException(
					String.format("The type '%s' needs %d subexpressions.",
							type.getClass().getSimpleName(), reqChildren));
	}
	
	public ExpressionTree(ExpressionType type,
			ExpressionTree child) {
		this(type, child, null);
	}
	
	public ExpressionTree(ExpressionType type) {
		this(type, null, null);
	}
	
	
	public ExpressionType getType() {
		return type;
	}

	public ExpressionTree getFirstChild() {
		return firstChild;
	}

	public ExpressionTree getSecondChild() {
		return secondChild;
	}

	class UnaryIterator implements Iterator<Expression> {
		UnaryExpressionType type;
		Iterator<Expression> subIterator;
		
		UnaryIterator (UnaryExpressionType type, Iterator<Expression> subExpressions) {
			this.type =type;
			this.subIterator = subExpressions;
		}

		@Override
		public boolean hasNext() {
			return subIterator.hasNext();
		}

		@Override
		public UnaryOperator next() {
			return type.getExpression(subIterator.next());
		}

		@Override
		public void remove() {
			subIterator.remove();
		}
		
		
	}
	
	class BinaryIterator implements Iterator<Expression> {
		BinaryExpressionType type;
		Iterator<Expression> firstIterator;
		Iterator<Expression> secondIterator;
		ExpressionTree second;
		NumberSeries initials;
		NumberSeries toPredict;
		
		Expression currentFirst;

		public BinaryIterator(BinaryExpressionType type, 
				ExpressionTree first, ExpressionTree second,
				NumberSeries initials, NumberSeries toPredict) {
			super();
			this.type = type;
			this.second = second;
			this.initials = initials;
			this.toPredict = toPredict;
			
			firstIterator = first.guesses(initials, toPredict);
			secondIterator = second.guesses(initials, toPredict);
		}

		@Override
		public boolean hasNext() {
			if(currentFirst==null) { // initial
				return firstIterator.hasNext() && secondIterator.hasNext();
			}
			
			// at least next called once
			return firstIterator.hasNext() || secondIterator.hasNext();
		}

		@Override
		public Expression next() {
			if(currentFirst == null) { // initial
				currentFirst = firstIterator.next();
			}
			
			if(!secondIterator.hasNext()) {
				currentFirst = firstIterator.next();
				secondIterator = second.guesses(initials, toPredict);
			}
			return type.getExpression(currentFirst, secondIterator.next());
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();			
		}
		
		
		
	}
	
	public Iterator<Expression> guesses(NumberSeries original,
			NumberSeries predicted) {
		if(type instanceof ValueExpressionType) {
			ValueExpressionType valType = (ValueExpressionType) type;
			return  new ConvertingIterator<Expression, ValueExpression>(
					valType.guesses(original, predicted));
		} else if(type instanceof UnaryExpressionType) {
			UnaryExpressionType unaType = (UnaryExpressionType) type;
			return new UnaryIterator(unaType, firstChild.guesses(original, predicted));
		} else if(type instanceof BinaryExpressionType) {
			BinaryExpressionType binType = (BinaryExpressionType) type;
			return new BinaryIterator(binType, firstChild, secondChild, original, predicted);
		}

		return null;
	}
	
	// TODO move this into expression types (if possible)!
	public Expression fitTo(NumberSeries original,
			NumberSeries predicted) {
		if(type instanceof ValueExpressionType) {
			ValueExpressionType valType = (ValueExpressionType) type;
			logger.trace(String.format("%s for %s -> %s", valType.getDummyRepresentation(),
					original, predicted));
			return valType.fitTo(original, predicted);
		} else if(type instanceof UnaryExpressionType) {
			UnaryExpressionType unaType = (UnaryExpressionType) type;
			logger.trace(String.format("%s for %s -> %s", 
					unaType.formatExpressionType(firstChild.toString()), original, predicted));
			
			return fitToUnary(unaType, original, predicted);
		} else if(type instanceof BinaryExpressionType) {
			BinaryExpressionType binType = (BinaryExpressionType) type;
			
			return fitToBinary(binType, original, predicted);
		}
		return null;
	}

	private Expression fitToBinary(BinaryExpressionType binType,
			NumberSeries original, NumberSeries predicted) {
		int initialSize = original.size()-predicted.size();
		// guess left
		Iterator<Expression> leftIt = firstChild.guesses(original, predicted);
		while(leftIt.hasNext()) {
			Expression left = leftIt.next();
			logger.trace(String.format("%s for %s -> %s", 
					binType.formatExpressionType(left.toString(), 
							(secondChild.getType() instanceof ValueExpressionType)?secondChild.toString():
								String.format("(%s)", secondChild.toString())), 
					original, predicted));
			try {
				int[] abducedSequence = new int[predicted.size()];
				
				for(int pos = 0; pos<abducedSequence.length;pos++) {
					abducedSequence[pos] = binType.reverseOperator(
							left.calculate(original.getSubsequence(0, pos+initialSize), pos+initialSize),
							predicted.get(pos));
				}
				NumberSeries abducedRight = 
					new NumberSeries(abducedSequence);
				logger.trace(abducedRight.toString());
				
				Expression fittedRight = secondChild.fitTo(original, abducedRight);
				if(fittedRight != null)
					return binType.getExpression(left, fittedRight);
			} catch (ExpressionCalculationException e) {
				continue;
			}
			
		}
		
		if(!binType.isCommutative()) {
			// guess right
			Iterator<Expression> rightIt = secondChild.guesses(original, predicted);
			while(rightIt.hasNext()) {
				Expression right = rightIt.next();
				logger.trace(String.format("%s for %s -> %s", 
						binType.formatExpressionType((firstChild.getType() instanceof ValueExpressionType)?
								firstChild.toString():
									String.format("(%s)", firstChild.toString()),
									right.toString()), 
						original, predicted));
				try {
					int[] abducedSequence = new int[predicted.size()];
					
					for(int pos = 0; pos<abducedSequence.length;pos++) {
						abducedSequence[pos] = binType.reverseOperatorOther(
								right.calculate(original.getSubsequence(0, pos+initialSize), pos+initialSize),
								predicted.get(pos));
					}
					NumberSeries abducedLeft = 
						new NumberSeries(abducedSequence);
					logger.trace(abducedLeft.toString());
					
					Expression fittedLeft = firstChild.fitTo(original, abducedLeft);
					if(fittedLeft != null)
						return binType.getExpression(fittedLeft, right);
				} catch (ExpressionCalculationException e) {
					continue;
				}
				
			}
		}
		return null;
	}

	private Expression fitToUnary(UnaryExpressionType unaType, NumberSeries original,
			NumberSeries predicted) {
		// abduce Examples
		int[] numbers = new int[predicted.size()];
		NumberSeries abducedSeries;
		
		try {
			for(int pos=0; pos<predicted.size();pos++)
				numbers[pos] = unaType.reverseOperator(predicted.get(pos));
			abducedSeries = new NumberSeries(numbers);
			logger.trace(abducedSeries.toString());
		} catch (ExpressionCalculationException e) {
			// Reversing calculation not possible
			// so this expression type can not have been used!
			return null;
		}
		
		Expression abducedResult = firstChild.fitTo(original, abducedSeries);
		if(abducedResult != null)
			return unaType.getExpression(abducedResult);
		return null;
	}
	
	public int getDepth() {
		if (firstChild==null)
			return 1;
		if (secondChild==null)
			return firstChild.getDepth();
		return Math.max(firstChild.getDepth(), secondChild.getDepth());
	}
	
	public String toString() {
		if(type instanceof ValueExpressionType) {
			ValueExpressionType valType = (ValueExpressionType) type;
			return  valType.getDummyRepresentation();
		} else if(type instanceof UnaryExpressionType) {
			UnaryExpressionType unaType = (UnaryExpressionType) type;
			
			return unaType.formatExpressionType(
					(firstChild.getType() instanceof ValueExpressionType)?firstChild.toString():
						String.format("(%s)", firstChild.toString()));
		} else if(type instanceof BinaryExpressionType) {
			BinaryExpressionType binType = (BinaryExpressionType) type;
			return binType.formatExpressionType(
					(firstChild.getType() instanceof ValueExpressionType)?firstChild.toString():
						String.format("(%s)", firstChild.toString()),
					(secondChild.getType() instanceof ValueExpressionType)?secondChild.toString():
							String.format("(%s)", secondChild.toString()));
		}
		return null;
	}
	
}
