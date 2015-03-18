/**
 * 
 */
package de.uni_bamberg.wiai.cogsys.tools;

import java.util.List;
import java.util.Random;

/**
 * @author michaels
 *
 */
public class RandomNumberGenerator  {

	private static transient RandomNumberGenerator instance;
	
	public static RandomNumberGenerator getInstance() {
		if (instance==null)
			instance = new RandomNumberGenerator();
		return instance;
	}
	
	private transient Random rand;
	
	
	private RandomNumberGenerator() {
		super();
		rand = new Random();
	}
	
	public int getInt() {
		return rand.nextInt();
	}
	
	public int getInt(int min, int max) {
		return rand.nextInt(max-min+1)+min;
		
	}
	
	public double getDouble() {
		return rand.nextDouble();
	}
	
	public double getDouble(double min, double max) {
		return rand.nextDouble()*(max-min)+min;
	}
	
	public boolean maybe(){
		return rand.nextBoolean();
	}
	
	public boolean maybe(double probability) {
		return rand.nextDouble()<probability;
	}
	
	public <T> T choose(T... ts) {
		if(ts == null)
			throw new IllegalArgumentException("Argument 'ts' is null!");
		
		return ts[getInt(0, ts.length-1)];
	}
	
	public int choose(int... nums) {
		return nums[getInt(0,nums.length-1)];
	}
	
	public <T> T choose(List<T> ts) {
		if(ts == null)
			throw new IllegalArgumentException("Argument 'ts' is null!");
		
		return ts.get(getInt(0, ts.size()-1));
	}
	
	
	
	
	
}
