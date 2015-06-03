package de.uni_bamberg.wiai.cogsys.math;

import expressions.exceptions.ExpressionCalculationException;

public class IntMath {
	/*
	 * 
	 * Basic calculations handling int overflows and underflows
	 * 
	 */
	
	/**
	 * Returns the sum of the arguments, throwing an exception if the result overflows an int.
	 * 
	 * 
	 * @param x the first value
	 * @param y the second value
	 * @return the result
	 * @throws ArithmeticException if the result overflows an int
	 * @author taken from java.lang.Math (Java 8)
	 */
	public static int add(int x, int y) throws ArithmeticException {
		int r = x + y;
		// HD 2-12 Overflow iff both arguments have the opposite sign of the result
		if (((x ^ r) & (y ^ r)) < 0) {
			throw new ArithmeticException("integer overflow");
		}
		return r;
	}
	/**
	 * Returns the product of the arguments, throwing an exception if the result overflows an int.
	 * 
	 * 
	 * @param x the first value
	 * @param y the second value
	 * @return the result
	 * @throws ArithmeticException if the result overflows an int
	 * @author taken from java.lang.Math (Java 8)
	 */
	public static int multiply(int x, int y) throws ArithmeticException{
		long r = (long)x * (long)y;
		if ((int)r != r) 
			throw new ArithmeticException("integer overflow");
		return (int)r;
	}
	
	/**
	 * Returns the integer devision of the arguments, throws an exception if the result overflows an int.
	 * x = div(x,y) * y + mod(x,y) 
	 * 
	 * @param x the first value
	 * @param y the second value to devide by
	 * @return the result
	 * @throws ArithmeticException if the result overflows an int or if second is 0
	 */
	public static int div(int x, int y) throws ArithmeticException {
		if ((x == Integer.MIN_VALUE) 
				&&(y == -1))  
			throw new ArithmeticException("integer overflow");
		return x / y; // throws Arithmetic Exception on division by 0
	}
	
	public static int mod(int x, int y) throws ArithmeticException {
		int div = div(x,y);
		return subtract(x,multiply(div,y));
	}
	
	/**
	 * Returns the devision of the arguments, throws an exception if the result overflows an int or if the quotient is no integer.
	 * x = divide(x,y) * y 
	 * 
	 * @param x the first value
	 * @param y the second value to devide by
	 * @return the result
	 * @throws ArithmeticException if the result overflows an int or if second is 0
	 */
	public static int divide(int x, int y) throws ArithmeticException {
		if ((x == Integer.MIN_VALUE) 
				&&(y == -1))  
			throw new ArithmeticException("integer overflow");
		int div = x / y; // throws Arithmetic Exception on division by 0
		if(multiply(div,y)!=x)
			throw new ArithmeticException("No integer solution for division");
		return div;
	}
	/**
	 * Returns the negation of the argument, throwing an exception if the result overflows an int.
	 * 
	 * 
	 * @param x the value to negate
	 * @return the result
	 * @throws ArithmeticException if the result overflows an int
	 * @author taken from java.lang.Math (Java 8)
	 */
	public static int negate(int x) throws ArithmeticException {
		if(x==Integer.MIN_VALUE)
			throw new ArithmeticException("integer overflow");
		return -x;
	}
	
	
	
	/**
	 * Returns the difference of the arguments, throwing an exception if the result overflows an int.
	 * 
	 * 
	 * @param x the first value
	 * @param y the second value to subtract from the first
	 * @return the result
	 * @throws ArithmeticException if the result overflows an int
	 * @author taken from java.lang.Math (Java 8)
	 */
	public static int subtract(int x, int y) throws ArithmeticException {
		int r = x - y;
		// HD 2-12 Overflow iff the arguments have different signs and
		// the sign of the result is different than the sign of x
		if (((x ^ y) & (x ^ r)) < 0) 
			throw new ArithmeticException("integer overflow");
		return r;
	}
	
	
	/**
	 * Calculates x^n. For non-negative exponents.
	 * 
	 * @param x 
	 * @param n
	 * @return the result
	 * @throws ArithmeticException if the result overflows an int
	 */
	public static int exp(int x, int n) throws ArithmeticException {
		if (n<0) 
			throw new ArithmeticException("Only defined for non-negative exponents!");
		return internal_exp_by_squaring(x, n);
	}
	
	/**
	 * Calculates x^n. For non-negative exponents.
	 * 
	 * @param x 
	 * @param n
	 * @return the result
	 * @throws ArithmeticException if the result overflows an int
	 */
	private static int internal_exp_by_squaring(int x, int n) throws ArithmeticException {
		if (n==0)
			return 1;
		else if (n==1)
			return x;
		else if (n %2 == 0 ) 
			return internal_exp_by_squaring( multiply(x,x), n/2);
		else 
			return multiply(x, internal_exp_by_squaring(multiply(x,x), (n-1)/2) );
	}
	
	
	
	/**
	 * Calculates the n'th root of a value
	 * @param n the index of the root
	 * @param value the value to compute the root from
	 * @return the integer root
	 * @throws ArithmeticException if the root is no integer or an overflow or an underflow happens
	 */
	public static int nthRoot(int n, int value) throws ArithmeticException {
		
		double guess = Math.pow(value, 1.0/n);
		long guessLong = Math.round(guess);
		int guessInt = (int) guessLong; // the root of an int must be an int!
		
		for(int result = guessInt-1; result < guessInt+1; result++)
			if(exp(result, n) == value)
				return result;
		
		throw new ArithmeticException("No integer solution for root");
		
		
	}
	
	public static int log(int base, int value) throws ArithmeticException {
		double guess = Math.log(value)/Math.log(base);
		long guessLong = Math.round(guess);
		int guessInt = (int) guessLong; // the root of an int must be an int!
		
		for(int result = guessInt-1; result < guessInt+1; result++)
			if(exp(base, result) == value)
				return result;
		
		throw new ArithmeticException("No integer solution for log");
	}
}
