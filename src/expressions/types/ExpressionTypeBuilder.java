package expressions.types;

public class ExpressionTypeBuilder {
	
	// value types
	private static ConstantExpressionType conType = new ConstantExpressionType();
	private static NumberSeriesExpressionType numType = new NumberSeriesExpressionType();
	private static PositionExpressionType posType = new PositionExpressionType();
	private static PrecursorExpressionType preType = new PrecursorExpressionType();
	private static ValueExpressionType[] valueTypes = { conType, numType, posType, preType}; 
	
	// unary types
	private static NegateExpressionType negType = new NegateExpressionType();
	private static UnaryExpressionType[] unaryTypes = {negType};
		
	// binary types
	private static AddExpressionType addType = new AddExpressionType();
	private static SubtractExpressionType subType = new SubtractExpressionType();
	private static MultiplyExpressionType mulType = new MultiplyExpressionType();
	private static DivideExpressionType divType = new DivideExpressionType();
	private static PowerExpressionType powType = new PowerExpressionType();
	private static BinaryExpressionType[] binaryTypes = {addType, subType, mulType, divType, powType};
	
	public static ConstantExpressionType getConType() {
		return conType;
	}
	
	public static NumberSeriesExpressionType getNumType() {
		return numType;
	}
	
	public static PositionExpressionType getPosType() {
		return posType;
	}
	public static PrecursorExpressionType getPreType() {
		return preType;
	}
	public static NegateExpressionType getNegType() {
		return negType;
	}
	public static AddExpressionType getAddType() {
		return addType;
	}
	public static SubtractExpressionType getSubType() {
		return subType;
	}
	public static MultiplyExpressionType getMulType() {
		return mulType;
	}
	public static DivideExpressionType getDivType() {
		return divType;
	}
	public static PowerExpressionType getPowType() {
		return powType;
	}

	public static ValueExpressionType[] getValueTypes() {
		return valueTypes;
	}

	public static UnaryExpressionType[] getUnaryTypes() {
		return unaryTypes;
	}

	public static BinaryExpressionType[] getBinaryTypes() {
		return binaryTypes;
	}
	
	
	

}
