package expressions.types;

import org.apache.log4j.Logger;

public abstract class ExpressionType {
	Logger logger = Logger.getLogger(ExpressionType.class);
	
	public ExpressionType() {
		super();
	}
	
	abstract public int getRequiredNumberOfSubexpressions();

	@Override
	public boolean equals(Object other) {
		if(other == null)
			return false;
		return getClass().equals(other.getClass());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
	
}
