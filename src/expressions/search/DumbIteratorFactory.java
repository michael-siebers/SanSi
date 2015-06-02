package expressions.search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import expressions.ExpressionTree;
import expressions.types.BinaryExpressionType;
import expressions.types.ExpressionType;
import expressions.types.ExpressionTypeBuilder;
import expressions.types.UnaryExpressionType;
import expressions.types.ValueExpressionType;

public class DumbIteratorFactory {
	private static final BinaryExpressionType POWER = ExpressionTypeBuilder.getPowType();

	private static final BinaryExpressionType MULTIPLY = ExpressionTypeBuilder.getMulType();

	private static final BinaryExpressionType ADD = ExpressionTypeBuilder.getAddType();

	static final Logger logger = Logger.getLogger(DumbIteratorFactory.class);
	
	static final private ValueExpressionType CONST = ExpressionTypeBuilder.getConType();
	static final private ValueExpressionType PRE = ExpressionTypeBuilder.getPreType();
	static final private ValueExpressionType POS = ExpressionTypeBuilder.getPosType();
	static final private ValueExpressionType NS = ExpressionTypeBuilder.getNumType();
	
	static final List<ValueExpressionType>  valueList = new ArrayList<ValueExpressionType>();
	static final List<UnaryExpressionType>  unaryList = new ArrayList<UnaryExpressionType>();
	static final List<BinaryExpressionType> binaryList = new ArrayList<BinaryExpressionType>();
	
	static final  List<ExpressionType> leftCommTypes = new ArrayList<ExpressionType>(4);	
	static final  List<ExpressionType> rightCommTypes = new ArrayList<ExpressionType>(4);
	static final  List<ExpressionType> leftTypes = new ArrayList<ExpressionType>(9);	
	static final  List<ExpressionType> rightTypes = new ArrayList<ExpressionType>(9);
	
	
	class PreAndAppendingIterator extends ExpressionTreeIterator {
		
		private ExpressionTreeIterator main;
		private boolean prepending = false;
		private boolean finished = false;
		
		
		
		public PreAndAppendingIterator(ExpressionTreeIterator main) {
			super();
			this.main = main;
			reset();
		}

		@Override
		public void reset() {
			prepending = true;
			finished = false;
			main.reset();			
		}
		
		@Override
		public boolean hasNext() {
			if(prepending)
				return true;
			return !finished;
		}
		
		@Override
		public ExpressionTree next() {
			if(prepending) {
				prepending = false;
				return new ExpressionTree(CONST);
			}
			
			if(main.hasNext())
				return main.next();
			
			finished=true;			
			return new ExpressionTree(NS);
		}
		
		
	}
	
	
	
	static {
		valueList.add(PRE);
		valueList.add(POS);
		valueList.add(NS);
		
		unaryList.add(ExpressionTypeBuilder.getNegType());
		
		binaryList.add(ADD);
		binaryList.add(MULTIPLY);
	//	binaryList.add(new DivideExpressionType());
		binaryList.add(POWER);
		
		leftCommTypes.add(PRE); rightCommTypes.add(CONST);
		leftCommTypes.add(PRE); rightCommTypes.add(PRE);
		leftCommTypes.add(POS); rightCommTypes.add(CONST);
		leftCommTypes.add(POS); rightCommTypes.add(PRE);
		
		leftTypes.add(PRE); rightTypes.add(CONST);
		leftTypes.add(PRE); rightTypes.add(PRE);
		leftTypes.add(PRE); rightTypes.add(POS);
		
		leftTypes.add(POS); rightTypes.add(CONST);
		leftTypes.add(POS); rightTypes.add(PRE);
		leftTypes.add(POS); rightTypes.add(POS);
		
		leftTypes.add(CONST); rightTypes.add(PRE);
		leftTypes.add(CONST); rightTypes.add(POS);
	}
	
	
	static public ExpressionTreeIterator asIterator(ValueExpressionType... valueTypes) {
		List<ExpressionTree> trees = new ArrayList<ExpressionTree>(valueTypes.length);
		
		for(ValueExpressionType vtype:valueTypes)
			trees.add(new ExpressionTree(vtype));
		return new SimpleTreeIterator(trees);
	}
	
	static public ExpressionTreeIterator asIterator(List<ValueExpressionType> valueTypes) {
		return asIterator(valueTypes.toArray((ValueExpressionType[])null));
	}
	
	
	
	static private ExpressionTreeIterator getDumbIterator2() {
		List<ExpressionTree> trees = new LinkedList<ExpressionTree>();
		
			
		trees.addAll(getAddTrees2());
		trees.addAll(getMultiplyTrees2());
		trees.addAll(getPowerTrees2());
		
		return new SimpleTreeIterator(trees);
	}

	private static List<ExpressionTree> getAddTrees2() {
		List<ExpressionTree> trees = new LinkedList<ExpressionTree>();
		for(int i = 0; i < leftCommTypes.size(); i++) {
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(leftCommTypes.get(i)),
					new ExpressionTree(rightCommTypes.get(i))));
		}
		
		return trees;
	}
	
	private static List<ExpressionTree> getMultiplyTrees2() {
		List<ExpressionTree> trees = new LinkedList<ExpressionTree>();
		for(int i = 0; i < leftCommTypes.size(); i++) {
			trees.add(new ExpressionTree(MULTIPLY,
					new ExpressionTree(leftCommTypes.get(i)),
					new ExpressionTree(rightCommTypes.get(i))));
		}
		
		return trees;
	}
	
	private static List<ExpressionTree> getPowerTrees2() {
		List<ExpressionTree> trees = new LinkedList<ExpressionTree>();

		for(int i = 0; i < leftTypes.size(); i++) {
			trees.add(new ExpressionTree(POWER,
					new ExpressionTree(leftTypes.get(i)),
					new ExpressionTree(rightTypes.get(i))));
		}
		
		return trees;
	}
	
	static private ExpressionTreeIterator getNSIterator2() {
		List<ExpressionTree> trees = new LinkedList<ExpressionTree>();
		
		for(BinaryExpressionType binType:new BinaryExpressionType[]{ADD,MULTIPLY})
			for(ExpressionType t : new ExpressionType[]{CONST, PRE, POS})
				trees.add(new ExpressionTree(binType, new ExpressionTree(t), new ExpressionTree(NS)));
		for(ExpressionType t : new ExpressionType[]{CONST, PRE, POS})
			trees.add(new ExpressionTree(POWER, new ExpressionTree(t), new ExpressionTree(NS)));
		for(ExpressionType t : new ExpressionType[]{CONST, PRE, POS})
			trees.add(new ExpressionTree(POWER, new ExpressionTree(NS), new ExpressionTree(t)));
		
		return new SimpleTreeIterator(trees);
	}
	
	static private ExpressionTreeIterator getDumbIterator3() {
		List<ExpressionTree> trees = new LinkedList<ExpressionTree>();
		trees.addAll(getAddTrees3());
		// TODO Multiply + Power
		return new SimpleTreeIterator(trees);
	}
	
	static private List<ExpressionTree> getAddTrees3() {
		List<ExpressionTree> trees = new LinkedList<ExpressionTree>();
		trees.addAll(getAddTrees3Add());
		trees.addAll(getAddTrees3Multiply());
		trees.addAll(getAddTrees3Power());
		
		return trees;		
	}
	
	static private List<ExpressionTree> getAddTrees3Add() {
		List<ExpressionTree> trees = new LinkedList<ExpressionTree>();
		
		// (+ + 1) 
		for(ExpressionType t : new ExpressionType[]{CONST, PRE})
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(ADD,
							new ExpressionTree(PRE),
							new ExpressionTree(POS)),
					new ExpressionTree(t)));
		for(ExpressionType t : new ExpressionType[]{CONST, PRE})
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(ADD,
							new ExpressionTree(PRE),
							new ExpressionTree(PRE)),
					new ExpressionTree(t)));
		for(ExpressionType t : new ExpressionType[]{CONST, PRE})
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(ADD,
							new ExpressionTree(PRE),
							new ExpressionTree(POS)),
					new ExpressionTree(t)));
		// (+ + +)
		for(ExpressionType t : new ExpressionType[]{CONST, PRE})
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(ADD,
							new ExpressionTree(PRE),
							new ExpressionTree(POS)),
					new ExpressionTree(ADD,
							new ExpressionTree(t),
							new ExpressionTree(CONST))
			));
		for(ExpressionType t : new ExpressionType[]{PRE, POS})
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(ADD,
							new ExpressionTree(PRE),
							new ExpressionTree(POS)),
					new ExpressionTree(ADD,
							new ExpressionTree(t),
							new ExpressionTree(PRE))
			));
		
		// (+ + *)
		for(ExpressionTree t : getMultiplyTrees2())
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(ADD,
							new ExpressionTree(PRE),
							new ExpressionTree(CONST)),
					t));
		for(ExpressionTree t : getMultiplyTrees2())
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(ADD,
							new ExpressionTree(PRE),
							new ExpressionTree(PRE)),
					t));
		for(ExpressionTree t : new ExpressionTree[]{
				new ExpressionTree(ADD, new ExpressionTree(POS), new ExpressionTree(PRE)),
				new ExpressionTree(ADD, new ExpressionTree(POS), new ExpressionTree(CONST))}) {
			trees.add(new ExpressionTree(ADD,
					t,
					new ExpressionTree(MULTIPLY,
							new ExpressionTree(PRE),
							new ExpressionTree(CONST))
			));
			trees.add(new ExpressionTree(ADD,
					t,
					new ExpressionTree(MULTIPLY,
							new ExpressionTree(PRE),
							new ExpressionTree(PRE))
			));
			trees.add(new ExpressionTree(ADD,
					t,
					new ExpressionTree(MULTIPLY,
							new ExpressionTree(POS),
						new ExpressionTree(PRE))
		));
		}
		
		for(ExpressionTree tLeft : getAddTrees2())
			for(ExpressionTree tRight : getPowerTrees2())
				trees.add(new ExpressionTree(ADD, tLeft, tRight));
		
		return trees;
	}
	
	static private List<ExpressionTree> getAddTrees3Multiply() {
		List<ExpressionTree> trees = new LinkedList<ExpressionTree>();
		
		// (* + 1)
		for(ExpressionType t : new ExpressionType[]{CONST, PRE, POS})
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(MULTIPLY,
							new ExpressionTree(PRE),
							new ExpressionTree(CONST)),
					new ExpressionTree(t)));
		for(ExpressionType t : new ExpressionType[]{CONST, PRE, POS})
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(MULTIPLY,
							new ExpressionTree(PRE),
							new ExpressionTree(PRE)),
					new ExpressionTree(t)));
		for(ExpressionType t : new ExpressionType[]{CONST, PRE})
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(MULTIPLY,
							new ExpressionTree(PRE),
							new ExpressionTree(POS)),
					new ExpressionTree(t)));
		for(ExpressionType t : new ExpressionType[]{CONST, PRE})
			trees.add(new ExpressionTree(ADD,
					new ExpressionTree(MULTIPLY,
							new ExpressionTree(POS),
							new ExpressionTree(CONST)),
					new ExpressionTree(t)));
		
		// (* + +)
		for(ExpressionTree tLeft : new ExpressionTree[]{
				new ExpressionTree(MULTIPLY, new ExpressionTree(PRE), new ExpressionTree(CONST)),
				new ExpressionTree(MULTIPLY, new ExpressionTree(PRE), new ExpressionTree(PRE)),
				new ExpressionTree(MULTIPLY, new ExpressionTree(POS), new ExpressionTree(PRE))})
			for(ExpressionTree tRight : getAddTrees2())
				trees.add(new ExpressionTree(ADD, tLeft, tRight));
		trees.add(new ExpressionTree(ADD,
				new ExpressionTree(MULTIPLY, new ExpressionTree(POS), new ExpressionTree(CONST)),
				new ExpressionTree(ADD, new ExpressionTree(PRE), new ExpressionTree(CONST))));
		trees.add(new ExpressionTree(ADD,
				new ExpressionTree(MULTIPLY, new ExpressionTree(POS), new ExpressionTree(CONST)),
				new ExpressionTree(ADD, new ExpressionTree(PRE), new ExpressionTree(PRE))));
		
		// (* + *)
		for(ExpressionTree tLeft : new ExpressionTree[]{
				new ExpressionTree(MULTIPLY, new ExpressionTree(PRE), new ExpressionTree(CONST)),
				new ExpressionTree(MULTIPLY, new ExpressionTree(PRE), new ExpressionTree(PRE))})
			for(ExpressionTree tRight : getMultiplyTrees2())
				trees.add(new ExpressionTree(ADD, tLeft, tRight));
		for(ExpressionTree tRight : new ExpressionTree[]{
				new ExpressionTree(MULTIPLY, new ExpressionTree(PRE), new ExpressionTree(CONST)),
				new ExpressionTree(MULTIPLY, new ExpressionTree(PRE), new ExpressionTree(PRE)),
				new ExpressionTree(MULTIPLY, new ExpressionTree(POS), new ExpressionTree(PRE))})
			trees.add(new ExpressionTree(ADD, 
					new ExpressionTree(MULTIPLY, new ExpressionTree(POS),new ExpressionTree(PRE)),
					tRight));
		for(ExpressionTree tRight : new ExpressionTree[]{
				new ExpressionTree(MULTIPLY, new ExpressionTree(PRE), new ExpressionTree(CONST)),
				new ExpressionTree(MULTIPLY, new ExpressionTree(PRE), new ExpressionTree(PRE))})
			trees.add(new ExpressionTree(ADD, 
					new ExpressionTree(MULTIPLY, new ExpressionTree(POS),new ExpressionTree(CONST)),
					tRight));
		
		// (* + ^)
		for(ExpressionTree tLeft : getMultiplyTrees2())
			for(ExpressionTree tRight : getPowerTrees2())
				trees.add(new ExpressionTree(ADD, tLeft, tRight));
		
		return trees;
	}
	
	static private List<ExpressionTree> getAddTrees3Power() {
		List<ExpressionTree> trees = new LinkedList<ExpressionTree>();
		
		for(ExpressionTree tLeft : getPowerTrees2())
			for(ExpressionType t : new ExpressionType[] {CONST, PRE, POS})
				trees.add(new ExpressionTree(ADD, tLeft, new ExpressionTree(t)));
		
		for(ExpressionTree tLeft : getPowerTrees2())
			for(ExpressionTree tRight : getAddTrees2())
				trees.add(new ExpressionTree(ADD, tLeft, tRight));
		
		for(ExpressionTree tLeft : getPowerTrees2())
			for(ExpressionTree tRight : getMultiplyTrees2())
				trees.add(new ExpressionTree(ADD, tLeft, tRight));
		
		for(ExpressionTree tLeft : getPowerTrees2())
			for(ExpressionTree tRight : getPowerTrees2())
				trees.add(new ExpressionTree(ADD, tLeft, tRight));
		
		return trees;
	}
	
	static public ExpressionTreeIterator naiveIterator() {
		return new CombiningExpressionTreeIterator(
				asIterator(CONST, PRE, POS),
				getDumbIterator2(),
				getDumbIterator3()
				);
	}
	static public ExpressionTreeIterator nsIterator() {
		return getNSIterator2();
	}
	
	static public ExpressionTreeIterator getSearchIterator(int maxDepth) {
		if(maxDepth <= 0)
			throw new IllegalArgumentException("Argument 'maxDepth' must be greater than zero.");
		
		switch(maxDepth) {
		case 1: return asIterator(CONST, PRE, POS);
		
		default:
			return new CombiningExpressionTreeIterator(
					getSearchIterator(maxDepth-1),
					getRecursionOperatorIterator(maxDepth));
		}
	}
	
	static public ExpressionTreeIterator getRecursionOperatorIterator(int depth) {
		if(depth <= 0)
			throw new IllegalArgumentException("Argument 'maxDepth' must be greater than zero.");
		
		switch(depth) {
		//case 1: return asIterator(CONST, PRE, POS);
		case 1: return asIterator(CONST,PRE, POS);
		/*case 2: return new BinaryIterator(binaryList, 
				getRecursionOperatorIterator(1), getRecursionOperatorIterator(1));*/
		case 2: return getDumbIterator2();
		case 3: return getDumbIterator3();
		default:
			List<ExpressionTreeIterator> iterators = new LinkedList<ExpressionTreeIterator>();
			iterators.add(new BinaryIterator(binaryList, 
				getRecursionOperatorIterator(depth-1),
				getSearchIterator(depth-2)));
			iterators.add(new BinaryIterator(binaryList, 
				getSearchIterator(depth-2), 
				getRecursionOperatorIterator(depth-1)));
			iterators.add(new BinaryIterator(binaryList, 
				getRecursionOperatorIterator(depth-1), 
				getRecursionOperatorIterator(depth-1)));
			return new CombiningExpressionTreeIterator(iterators);
				
		}
	}

	
}
