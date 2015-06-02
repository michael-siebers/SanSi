package series;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.uni_bamberg.wiai.cogsys.tools.RandomNumberGenerator;
import expressions.BinaryOperator;
import expressions.ConstantValue;
import expressions.Expression;
import expressions.NumberSeriesExpression;
import expressions.PositionValue;
import expressions.PrecursorValue;
import expressions.exceptions.NumberSeriesGenerationException;
import expressions.types.BinaryExpressionType;
import expressions.types.ExpressionTypeBuilder;

public class NumberSeriesCreator {
	
	private static final NumberSeries PRIMES = (new NumberSeries(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997));
	static private final Logger logger = Logger.getLogger(NumberSeriesCreator.class);
	static private final List<String> messages = new LinkedList<String>();
	static private int offset = 0;
	
	// default values
	private static final int DEFAULT_CONSTANT_MIN = -5;
	private static final int DEFAULT_CONSTANT_MAX = 5;
	private static final int DEFAULT_INIT_MIN = -25;
	private static final int DEFAULT_INIT_MAX = 50;
	private static final int DEFAULT_MAX_DEPTH = 5;
	
	private static final int MAX_SERIES_VALUE = 1000;
	private static final int DEFAULT_SERIES_LENGTH = 20;
	
	private static final int DEFAULT_VALUE_CONSTANT_ODD = 40;
	private static final int DEFAULT_VALUE_PRECURSOR_ODD = 50;
	private static final int DEFAULT_VALUE_POSITION_ODD = 10;
	
	
	private static final int DEFAULT_BINARY_ADD_ODD = 30;
	private static final int DEFAULT_BINARY_SUBTRACT_ODD = 15;
	private static final int DEFAULT_BINARY_MULTIPLY_ODD = 20;
	private static final int DEFAULT_BINARY_DIVIDE_ODD = 2;
	private static final int DEFAULT_BINARY_POWER_ODD = 1;

	private static final int DEFAULT_RECURSION_ODD = 20;
	private static final int DEFAULT_NORECURSION_ODD = 79;
	private static final int DEFAULT_NUMBER_SERIES_ODD = 1;
	
	// tokens
	static private final Object TOKEN_CONSTANT = new Object();
	static private final Object TOKEN_PRECURSOR = new Object();
	static private final Object TOKEN_POSITION = new Object();
	
	
	static private final Object TOKEN_ADD = new Object();
	static private final Object TOKEN_MULTIPLY = new Object();
	static private final Object TOKEN_SUBTRACT = new Object();
	static private final Object TOKEN_DIVIDE = new Object();
	static private final Object TOKEN_POWER = new Object();
	
	static private final Object TOKEN_FINISH = new Object();
	static private final Object TOKEN_RECURSE = new Object();
	static private final Object TOKEN_NUMBER_SERIES = new Object();
	
	// types
	static private final BinaryExpressionType ADD = ExpressionTypeBuilder.getAddType();
	static private final BinaryExpressionType SUBTRACT = ExpressionTypeBuilder.getSubType();
	static private final BinaryExpressionType MULTIPLY = ExpressionTypeBuilder.getMulType();
	static private final BinaryExpressionType DIVIDE = ExpressionTypeBuilder.getDivType();
	static private final BinaryExpressionType POWER = ExpressionTypeBuilder.getPowType();
	
	
	
	// creation parameters
	private int constantMin = DEFAULT_CONSTANT_MIN;
	private int constantMax = DEFAULT_CONSTANT_MAX;
	private int constantMinAdd  = 1;
	private int constantMaxAdd = DEFAULT_CONSTANT_MAX;
	private int constantMinMultiply = DEFAULT_CONSTANT_MIN;
	private int constantMaxMultiply = 15;
	private int constantMinSubtract = DEFAULT_CONSTANT_MIN;
	private int constantMaxSubtract = DEFAULT_CONSTANT_MAX;
	private int constantMinDivide = -5;
	private int constantMaxDivide = 5;
	private int constantMinPower = 2;
	private int constantMaxPower = 4;
	

	private int initialMin = DEFAULT_INIT_MIN;
	private int initialMax = DEFAULT_INIT_MAX;
	
	private Object[] valueTokens;
	private Object[] binaryTokens;
	private Object[] binaryTokensNoPower;
	private Object[] recursionTokens;

	// random generator
	private RandomNumberGenerator rand = RandomNumberGenerator.getInstance();
	
	private int currentCreatedLength;
	private int maxDepth = DEFAULT_MAX_DEPTH;
	
	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		if(maxDepth<1)
			throw new IllegalArgumentException(
					"Argument 'maxDepth' must be greater than or equal to 1.");
		this.maxDepth = maxDepth;
	}

	public NumberSeriesCreator() {
		super();
		
		setValueOdds(DEFAULT_VALUE_CONSTANT_ODD, DEFAULT_VALUE_PRECURSOR_ODD, 
				DEFAULT_VALUE_POSITION_ODD);
		setBinaryOdds(DEFAULT_BINARY_ADD_ODD, DEFAULT_BINARY_SUBTRACT_ODD, 
				DEFAULT_BINARY_MULTIPLY_ODD, DEFAULT_BINARY_DIVIDE_ODD, DEFAULT_BINARY_POWER_ODD);
		setRecursionOdds(DEFAULT_RECURSION_ODD, DEFAULT_NORECURSION_ODD, DEFAULT_NUMBER_SERIES_ODD);
	}

	/* *****************************
	 * Setting creation parameters *
	 *******************************/
	public void setConstantRange(int min, int max) {
		this.constantMin = Math.min(min, max);
		this.constantMax = Math.max(min, max);
	}

	public void setInitialRange(int min, int max) {
		this.initialMin = Math.min(min, max);
		this.initialMax = Math.max(min, max);
	}

	public void setValueOdds(int constantOdd, int precursorOdd, int positionOdd) {
		if(constantOdd<0)
			throw new IllegalArgumentException("Argument 'constantOdd' is less than zero.");
		if(precursorOdd<0)
			throw new IllegalArgumentException("Argument 'precursorOdd' is less than zero.");
		if(positionOdd<0)
			throw new IllegalArgumentException("Argument 'positionOdd' is less than zero.");
		
		
		if(Integer.MAX_VALUE-constantOdd < precursorOdd 
				|| Integer.MAX_VALUE-positionOdd < constantOdd+precursorOdd)
			throw new IllegalArgumentException("The sum of the odds is greater than Integer.MAX_VALUE");
		
		int sum = constantOdd+precursorOdd+positionOdd;
		if(sum==0)
			throw new IllegalArgumentException("The sum of the odds is zero.");
		
		valueTokens = new Object[sum];
		Arrays.fill(valueTokens, 0, constantOdd, TOKEN_CONSTANT);
		Arrays.fill(valueTokens, constantOdd, constantOdd+precursorOdd, TOKEN_PRECURSOR);
		Arrays.fill(valueTokens, constantOdd+precursorOdd, sum, TOKEN_POSITION);
	
	}

	public void setBinaryOdds(int addOdd, int subtractOdd, int multiplyOdd, int divideOdd, int powerOdd) {
		if(addOdd < 0)
			throw new IllegalArgumentException("Argument 'addOdd' is less than zero.");
		if(subtractOdd < 0)
			throw new IllegalArgumentException("Argument 'subtractOdd' is less than zero.");
		if(multiplyOdd < 0)
			throw new IllegalArgumentException("Argument 'multiplyOdd' is less than zero.");
		if(divideOdd < 0)
			throw new IllegalArgumentException("Argument 'divideOdd' is less than zero.");
		if(powerOdd < 0)
			throw new IllegalArgumentException("Argument 'powerOdd' is less than zero.");
		
		if(Integer.MAX_VALUE-addOdd < subtractOdd 
				|| Integer.MAX_VALUE-multiplyOdd < addOdd+subtractOdd
				|| Integer.MAX_VALUE-divideOdd < addOdd+subtractOdd+multiplyOdd 
				|| Integer.MAX_VALUE-powerOdd < addOdd+subtractOdd+multiplyOdd+divideOdd)
			throw new IllegalArgumentException("The sum of the odds is greater than Integer.MAX_VALUE");
		int sum = addOdd + subtractOdd + multiplyOdd + divideOdd + powerOdd;
		
		binaryTokens = new Object[sum];
		Arrays.fill(binaryTokens, 0, addOdd, TOKEN_ADD);
		Arrays.fill(binaryTokens, addOdd, addOdd+subtractOdd, TOKEN_SUBTRACT);
		Arrays.fill(binaryTokens, addOdd+subtractOdd, addOdd+subtractOdd+multiplyOdd, TOKEN_MULTIPLY);
		Arrays.fill(binaryTokens, addOdd+subtractOdd+multiplyOdd, addOdd+subtractOdd+multiplyOdd+divideOdd, TOKEN_DIVIDE);
		Arrays.fill(binaryTokens, addOdd+subtractOdd+multiplyOdd+divideOdd, sum, TOKEN_POWER);
		
		binaryTokensNoPower = Arrays.copyOf(binaryTokens, sum-powerOdd);
		
	}

	public void setRecursionOdds(int recursionOdd, int noRecursionOdd, int numberSeriesOdd) {
		if(recursionOdd < 0)
			throw new IllegalArgumentException("Argument 'recursionOdd' is less than zero.");
		if(noRecursionOdd < 0)
			throw new IllegalArgumentException("Argument 'noRecursionOdd' is less than zero.");
		if(numberSeriesOdd < 0)
			throw new IllegalArgumentException("Argument 'numberSeriesOdd' is less than zero.");
		
		if((Integer.MAX_VALUE-recursionOdd < noRecursionOdd)
				|| (Integer.MAX_VALUE-numberSeriesOdd < recursionOdd + noRecursionOdd)
			)
			throw new IllegalArgumentException("Sum of odds is greater than Integer.MAX_VALUE.");
		
		final int sum = recursionOdd+noRecursionOdd+numberSeriesOdd;
		recursionTokens = new Object[sum];
		Arrays.fill(recursionTokens, 0, recursionOdd, TOKEN_RECURSE);
		Arrays.fill(recursionTokens, recursionOdd, recursionOdd+noRecursionOdd, TOKEN_FINISH);
		Arrays.fill(recursionTokens, recursionOdd+noRecursionOdd, sum, TOKEN_NUMBER_SERIES);
	}
	
	
	/* *****************************
	 * create random random values * 
	 *******************************/
	private Expression getRandomPrecursorValue(int maxPrecursors) {
		return new PrecursorValue(rand.getInt(1,maxPrecursors));
	}
	
	private Expression getRandomPrecursorValue(int maxPrecursors, int... doNotUse) {
		if(doNotUse.length==0)
			return getRandomPrecursorValue(maxPrecursors);
		
		int[] values = new int[maxPrecursors];
		for(int i=0; i<values.length;i++)
			values[i] = i+1;
			
		return new PrecursorValue(rand.choose(removeFrom(doNotUse, values)));
	}
		
	private Expression getRandomConstantValue(boolean positiveOnly, int min, int max, int... excludeNumbers) {
		int[] toExclude = (excludeNumbers==null)?new int[0]:excludeNumbers;
		
		outter:
		while(true) {
			int value = rand.getInt(min, max);
			if(positiveOnly && value<0)
				continue;
			for(int exclude:toExclude)
				if(value==exclude)
					continue outter;
			return new ConstantValue(value);
		}
	}
	
	private Expression getRandomValue(int maxPrecursors, boolean positiveOnly, 
			int min, int max, 
			List<Expression> expressions, int... excludeNumbers) throws ImpossibleOperationException {
//		enter(String.format("max %d precursors, %sonly positive, exclude %s; %s",
//				maxPrecursors, positiveOnly?"":"not ", Formatter.implode(expressions,"|","",""),  Formatter.implode(excludeNumbers)));
		
		boolean retainPosition = true;
		boolean retainConstant = true;
		int[] usedPrecursors = new int[expressions.size()];
		int precursorCount = 0;
		int maxPrecursor = 0;
		
		for(Expression expr:expressions) {
			if(expr instanceof PositionValue)
				retainPosition = false;
			else if(expr instanceof ConstantValue)
				retainConstant = false;
			else if(expr instanceof PrecursorValue) {
				PrecursorValue pc = (PrecursorValue) expr;
				int prec = pc.getOffset();
				usedPrecursors[precursorCount++] = prec;
				maxPrecursor = Math.max(maxPrecursor, prec);
			}
		}
		
		List<Object> toRetain = new ArrayList<Object>();
		if(retainConstant) toRetain.add(TOKEN_CONSTANT);
		if(precursorCount<maxPrecursors)toRetain.add(TOKEN_PRECURSOR);
		if(retainPosition) toRetain.add(TOKEN_POSITION);
		if (toRetain.isEmpty())
			throw new ImpossibleOperationException();
		
		Object token = rand.choose(retainOnly(toRetain, valueTokens));
		
		
		
		Expression result = null;
		
		if(token==TOKEN_CONSTANT)
			result =  getRandomConstantValue(positiveOnly, min, max, excludeNumbers);
		else if(token==TOKEN_PRECURSOR)
			result = getRandomPrecursorValue(maxPrecursors, Arrays.copyOf(usedPrecursors, precursorCount));
		else if(token==TOKEN_POSITION)
			result =  new PositionValue();
		if(result==null) {
			throw new RuntimeException("Encountered unexpected token!");
		}

		return result;
	}
	
	
	private Expression getRandomNumberSeriesValue(int offset) {
		NumberSeriesDefinition def = null;
		//System.out.println("Requesting subexpression ...");
		while(def==null)
		{		
			NumberSeriesDefinition candidate = getRandomNumberSeriesDefinition();
			try {
				candidate.produce(currentCreatedLength);
			} catch (NumberSeriesGenerationException e) {
				continue;
			}
			def = candidate;
			def.setName("y");
		}
		
		//System.out.println("... finished");
		
		return new NumberSeriesExpression(def, offset);
	}

	private Expression getRandomValue(int maxPrecursors,
			BinaryExpressionType topLevelType, boolean rightSide,
			List<Expression> expressions)
			throws ImpossibleOperationException {
		expressions = new ArrayList<Expression>(expressions);
		if(topLevelType == null)
			return getRandomValue(maxPrecursors, false, 
					constantMin, constantMax, 
					expressions);
		
		try {
			if(ADD.equals(topLevelType)) {
				return getRandomValue(maxPrecursors, true, 
						constantMinAdd, constantMaxAdd, expressions, 0);
			}
			if(SUBTRACT.equals(topLevelType)) {
				if (rightSide)
					return getRandomValue(maxPrecursors, true, 
							constantMinSubtract, constantMaxSubtract, expressions, 0);
				return getRandomValue(maxPrecursors, false, 
						constantMinSubtract, constantMaxSubtract, expressions, 0);
			}
			if (MULTIPLY.equals(topLevelType)) {
				return getRandomValue(maxPrecursors, false, 
						constantMinMultiply, constantMaxMultiply, expressions, 0,1);
			}
			if (DIVIDE.equals(topLevelType)) {
				if (rightSide)
					return getRandomValue(maxPrecursors, false, 
							constantMinDivide, constantMaxDivide, expressions, 0, -1,
							1);
				return getRandomValue(maxPrecursors, false, 
						constantMinDivide, constantMaxDivide, expressions, 0, 1);
				
			}
			if (POWER.equals(topLevelType)) {
				if (rightSide)
					return getRandomValue(maxPrecursors, true, 
							constantMinPower, constantMaxPower, expressions, 0, 1);
				return getRandomValue(maxPrecursors, false, 
						constantMinPower, constantMaxPower, expressions, 0, 1);
			}
			
			
			throw new RuntimeException("Encountered unexpected toplevel binary type!");
		} finally {

		}
	}
	
	
	
	public BinaryOperator getRandomBinaryExpression(int maxPrecursors) {
		List<Expression> empty = Collections.emptyList();
		try {
			return getRandomBinaryExpression(maxPrecursors, null, false, 1, empty);
		} catch (ImpossibleOperationException e) {
			System.out.println("Error");
		}
		return null;
	}


	private Expression getRandomExpression(int maxPrecursors,
			BinaryExpressionType toplevelType, boolean rightSide,
			int depth, 
			boolean excludePower, List<Expression> toExclude) throws ImpossibleOperationException {
		if (toExclude == null)
			toExclude = new ArrayList<Expression>();
		
		if(depth>=maxDepth)
			return getRandomNumberSeriesValue(maxPrecursors);
		
		Object recursionToken = rand.choose(recursionTokens);
		
		if(recursionToken == TOKEN_NUMBER_SERIES)
			return getRandomNumberSeriesValue(maxPrecursors);
		
		if (recursionToken == TOKEN_RECURSE) {
			try {
				BinaryOperator result = getRandomBinaryExpression(maxPrecursors, toplevelType,
						excludePower, depth, toExclude);
				return result;
			} catch (ImpossibleOperationException e) {
				recursionToken = TOKEN_FINISH;
			}
		}

		if (recursionToken == TOKEN_FINISH) {
			try {
				Expression result = getRandomValue(maxPrecursors, toplevelType, rightSide,
							toExclude);
				return result;
			} catch (ImpossibleOperationException e) {
					// No simple solution possible
					throw e;
			}

		} 
		throw new RuntimeException("Encountered unexpected token!");
	}

	private BinaryOperator getRandomBinaryExpression(int maxPrecursors,
			BinaryExpressionType toplevelType, boolean excludePower,
			int depth, 
			List<Expression> toExclude) throws ImpossibleOperationException {
		BinaryOperator result = null;
		Object[] possibleTokens = excludePower?binaryTokensNoPower:binaryTokens;
		
		
		while ((result == null || toExclude.contains(result))&&possibleTokens.length>0) {
			Object token = rand.choose(possibleTokens);
			
			BinaryExpressionType binType = null;
			if(token==TOKEN_ADD) {
				binType = ADD;
			}
			if(token==TOKEN_SUBTRACT) {
				binType = SUBTRACT;
			}
			if(token==TOKEN_MULTIPLY) {
				binType = MULTIPLY;
			}
			if(token==TOKEN_DIVIDE) {
				binType = DIVIDE;
			}
			if(token==TOKEN_POWER) {
				binType = POWER;
			}
			boolean internExcludePower = excludePower
					|| binType.equals(POWER);

			// binary expression types are singletons
			List<Expression> invalid = new LinkedList<Expression>();
			if(!(binType == POWER))
				invalid.addAll(toExclude);
			
			if ((binType == ADD || binType == SUBTRACT)
					&& (toplevelType == ADD || toplevelType == SUBTRACT))
				invalid.addAll(expandAdditive(toExclude));
			else if ((binType == MULTIPLY || binType == DIVIDE)
					&& (toplevelType == MULTIPLY || toplevelType == DIVIDE))
				invalid.addAll(expandMultiplicative(toExclude));

			Expression left;
			try {
				left = getRandomExpression(maxPrecursors, binType,
						false, depth+1, internExcludePower, invalid);
			} catch (ImpossibleOperationException e) {
				possibleTokens = removeFrom(new Object[]{token}, possibleTokens);
				continue;
			}

			if (binType == ADD
					|| binType == SUBTRACT)
				invalid.addAll(expandAdditive(Collections
						.singletonList(left)));
			else if (binType == MULTIPLY
					|| binType == DIVIDE)
				invalid.addAll(expandMultiplicative(Collections
						.singletonList(left)));
			else
				invalid.add(left);
			Expression right;
			try {
				right = getRandomExpression(maxPrecursors, binType,
						true, depth+1, internExcludePower, invalid);
			} catch (ImpossibleOperationException e) {
				possibleTokens = removeFrom(new Object[]{token}, possibleTokens);
				continue;
			}

			result = new BinaryOperator(binType, left, right);
		}
		
		
		if(result == null) {
			throw new ImpossibleOperationException();
		}
		
		return result;
	}
	
	private List<Expression> expandAdditive(List<Expression> original) {
		List<Expression> result = new LinkedList<Expression>();
		
		for(Expression expr:original) {
			result.add(expr);
			
			if(! (expr instanceof BinaryOperator)) 
				continue;
			BinaryOperator binOp = (BinaryOperator) expr;
			BinaryExpressionType binType = (BinaryExpressionType) binOp.getType();
			
			if (binType == ADD || binType == SUBTRACT) {
				result.addAll(expandAdditive(Collections.singletonList(binOp.getLeftChild())));	
				result.addAll(expandAdditive(Collections.singletonList(binOp.getRightChild())));	
			}
			
		}
		
		return result;
	}
	
	private List<Expression> expandMultiplicative(List<Expression> original) {
		List<Expression> result = new LinkedList<Expression>();
		
		for(Expression expr:original) {
			result.add(expr);
			
			if(! (expr instanceof BinaryOperator)) 
				continue;
			BinaryOperator binOp = (BinaryOperator) expr;
			BinaryExpressionType binType = (BinaryExpressionType) binOp.getType();
			
			if (binType == MULTIPLY || binType == DIVIDE) {
				result.addAll(expandMultiplicative(Collections.singletonList(binOp.getLeftChild())));	
				result.addAll(expandMultiplicative(Collections.singletonList(binOp.getRightChild())));	
			}
			
		}
		
		return result;
	}
	

	@SuppressWarnings("unchecked")
	private <T> T[] retainOnly(List<T> objects, T[] array) {
		List<T> objectsCopy = new LinkedList<T>(objects);
		
		List<T> result = new ArrayList<T>(array.length);
		
		
		for(int i=0; i < array.length; i++) 
			for(T target:objectsCopy)
				if((target==null && array[i]==null)
						|| (target!=null && target.equals(array[i]))) {
					result.add(target);
					break;
				}
					
		return (T[]) result.toArray();
	}
	
	
	
	@SuppressWarnings("unchecked")
	private <T> T[] removeFrom(List<T> objects, T[] array) {
		List<T> objectsCopy = new LinkedList<T>(objects);
		
		List<T> result = new ArrayList<T>(array.length);
		
		outter:
		for(int i=0; i < array.length; i++) {
			for(T target:objectsCopy)
				if((target==null && array[i]==null)
						|| (target!=null && target.equals(array[i])))
					continue outter;
			result.add(array[i]);
		}
		
					
		return (T[]) result.toArray();
	}
	
	private int[] removeFrom(int[] objects, int[] array) {
		int valid=0;
		int[] result = new int[array.length];
		
		outter:
		for(int i=0; i < array.length; i++) { 
			for(int target: objects)
				if(target == array[i]) 
					continue outter;
			result[valid++] = array[i];
		}
					
		return Arrays.copyOfRange(result, 0, valid);
	}
	
	private <T> T[] removeFrom(T[] objects, T[] array) {
		return removeFrom(Arrays.asList(objects), array);
	}
	
	public NumberSeriesDefinition getRandomNumberSeriesDefinition() {
		int initials = 0;
		int expressions = 1;
		
		while(rand.maybe(0.7*Math.pow(0.3,initials)))
			initials++;
		
		while(rand.maybe(0.3*Math.pow(0.5,expressions))&&expressions<4)
			expressions++;
		
	/*	System.out.println(String.format("Creating %d expressions with maximal %d precursors", 
				expressions, initials))
		;
		*/
		if(expressions==1) {
			Expression expr = getRandomBinaryExpression(initials);

			int[] inits = new int[expr.requiredPrecursors()];
			for (int i = 0; i < inits.length; i++)
				inits[i] = rand.getInt(initialMin, initialMax);
			
		//	System.out.println(String.format("%1$tM:%1$tS.%1$tL created candidate", 
		//			System.currentTimeMillis()));
			return new SimpleNumberSeriesDefinition(new NumberSeries(inits),
					expr);
		}
		
		Expression[] exprs = new Expression[expressions];
		int maxPrecursors = 0;
		int pos = 0;
		
		outter:
		while(pos<expressions) {
			Expression expr = getRandomBinaryExpression(initials);
			
			for(int i = 0; i<pos;i++)
				if(expr.equals(exprs[i]))
					continue outter;
			
			exprs[pos] = expr;
			maxPrecursors = Math.max(maxPrecursors, expr.requiredPrecursors());
			pos++;
		}
		
		int[] inits = new int[maxPrecursors];
		for (int i = 0; i < inits.length; i++)
			inits[i] = rand.getInt(initialMin, initialMax);
		
		//System.out.println(String.format("%1$tM:%1$tS.%1$tL created candidate", 
		//		System.currentTimeMillis()));
		return new AlternatingNumberSeriesDefinition(new NumberSeries(inits), exprs);
	}
	
	public Map<NumberSeries,Set<NumberSeriesDefinition>> getRandomNumberSeries(int count) {
		return getRandomNumberSeries(count, DEFAULT_SERIES_LENGTH,
				-MAX_SERIES_VALUE, MAX_SERIES_VALUE, new HashMap<NumberSeries, Set<NumberSeriesDefinition>>());
	}
	
	public Map<NumberSeries,Set<NumberSeriesDefinition>> getRandomNumberSeries(int count, int length, 
			int minumumValue, int maximumValue, 
			Map<NumberSeries,Set<NumberSeriesDefinition>> seed) {
		Map<NumberSeries, Set<NumberSeriesDefinition>> result = new HashMap<NumberSeries, Set<NumberSeriesDefinition>>(seed);
		currentCreatedLength = length;
		
		outter:
		while(result.size() < count) {
			NumberSeriesDefinition next = getRandomNumberSeriesDefinition();
			//System.out.println("Successfully created: "+next.toString());
			NumberSeries series = null;
			try {
			//	System.out.println("Creating series");
			//	long start = System.currentTimeMillis();
				series = next.produce(length);
			//	System.out.println(String.format("finished in %d s", 
			//			(System.currentTimeMillis()-start)/1000));
			} catch (NumberSeriesGenerationException e) {
				//System.out.println(e.getMessage());
				continue;
			}
			
			//System.out.println("Checking for out of range");
			long start = System.currentTimeMillis();
			for(int val : series.toArray())
				if(val<minumumValue || val>maximumValue) {
					//System.out.println("Value out of range");
					continue outter;
				}
			//System.out.println(String.format("finished in %d s", 
			//		(System.currentTimeMillis()-start)/1000));
			
			
			//System.out.println("checking for dupplicate");
			start = System.currentTimeMillis();
			final boolean containsKey = result.containsKey(series);
			//System.out.println(String.format("finished in %d s", 
				//	(System.currentTimeMillis()-start)/1000));
			if(containsKey) {
				Set<NumberSeriesDefinition> defs = result.get(series);
				defs.add(next);
				//System.out.println("dupplicate");
			} else {
				Set<NumberSeriesDefinition> defs = new HashSet<NumberSeriesDefinition>();
				defs.add(next);
				//System.out.println("NEW");
				result.put(series, defs);
			}
		}
		
		return result;
		
	}
	
	public Map<NumberSeries,Set<NumberSeriesDefinition>> getDefaultNumberSeries(int length) {
		Map<NumberSeries, Set<NumberSeriesDefinition>> result = new HashMap<NumberSeries, Set<NumberSeriesDefinition>>();
		List<NumberSeriesDefinition> definitions = new LinkedList<NumberSeriesDefinition>();
		currentCreatedLength = length;
		
		// simple addition
		for(int i=1;i<=3;i++)
			definitions.add( 
				new SimpleNumberSeriesDefinition(new NumberSeries(2),
						new BinaryOperator(ADD, new PrecursorValue(1), new ConstantValue(i))));

		
		// simple multiplication
		for(int i=2;i<=3;i++) 
			definitions.add(  
				new SimpleNumberSeriesDefinition(new NumberSeries(5-i),
						new BinaryOperator(MULTIPLY, new PrecursorValue(1), new ConstantValue(i))));

		// potences
		for(int i=2;i<=3;i++) 
			definitions.add( 
				new SimpleNumberSeriesDefinition(new NumberSeries(),
						new BinaryOperator(POWER, new ConstantValue(i), new PositionValue())));

		// potences
		for(int i=2;i<=3;i++)
			definitions.add( 
				new SimpleNumberSeriesDefinition(new NumberSeries(),
						new BinaryOperator(POWER, 
								new BinaryOperator(ADD, new PositionValue(), new ConstantValue(1)),
								new ConstantValue(i))));
		
		// fibonacci
		definitions.add(new SimpleNumberSeriesDefinition(new NumberSeries(1,1),
				new BinaryOperator(ADD, new PrecursorValue(1), new PrecursorValue(2))));
		// tribonacci
		definitions.add(new SimpleNumberSeriesDefinition(new NumberSeries(1,1,1),
				new BinaryOperator(ADD, new PrecursorValue(1), 
						new BinaryOperator(ADD, new PrecursorValue(2), new PrecursorValue(3)))));
		
		// Verschränkte Serien
		definitions.add(new AlternatingNumberSeriesDefinition(new NumberSeries(10,1), 
				new BinaryOperator(SUBTRACT, new PrecursorValue(2), new ConstantValue(1)),
				new BinaryOperator(ADD, new PrecursorValue(2), new ConstantValue(1))));
		definitions.add(new AlternatingNumberSeriesDefinition(new NumberSeries(18), 
				new BinaryOperator(ADD, new PrecursorValue(1), new PositionValue()),
				new BinaryOperator(SUBTRACT, new PrecursorValue(1), new PositionValue())));
		definitions.add(new AlternatingNumberSeriesDefinition(new NumberSeries(3), 
				new BinaryOperator(ADD, new PrecursorValue(1), new ConstantValue(2)),
				new BinaryOperator(ADD, new PrecursorValue(1), new ConstantValue(3)),
				new BinaryOperator(ADD, new PrecursorValue(1), new ConstantValue(5))));
		definitions.add(new AlternatingNumberSeriesDefinition(new NumberSeries(9), 
				new BinaryOperator(SUBTRACT, new PrecursorValue(1), new ConstantValue(3)),
				new BinaryOperator(MULTIPLY, new PrecursorValue(1), new ConstantValue(3)),
				new BinaryOperator(ADD, new PrecursorValue(1), new ConstantValue(3)),
				new BinaryOperator(DIVIDE, new PrecursorValue(1), new ConstantValue(3))));
		
		
		try {
		NumberSeries series = null;

		for(NumberSeriesDefinition def:definitions) {
			series = def.produce(length);
			Set<NumberSeriesDefinition> set = new HashSet<NumberSeriesDefinition>();
			set.add(def);
			result.put(series, set);
		}
			
		} catch (NumberSeriesGenerationException e) {
			// should never happen!
			throw new RuntimeException(e);
		}
	
		result.put(PRIMES.getSubsequence(0, length), 
				new HashSet<NumberSeriesDefinition>());
		
		return result;
	}
	
	
	
	static public class ImpossibleOperationException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4217286160986936239L;
		
	}
}
