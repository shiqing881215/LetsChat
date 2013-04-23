package Util;

/**
 * 
 * @author Qing Shi
 *
 * @param <FirstType>
 * @param <SecondType>
 * 
 * New data structure to hold two kinds type of data.
 * Used by the private chat.
 * Using generic to make it more general and flexible.
 */
public class Pair<FirstType, SecondType> {
	public FirstType first;
	public SecondType second;
	
	public Pair(FirstType first, SecondType second) {
		this.first = first;
		this.second = second;
	}

	public FirstType getFirst() {
		return first;
	}

	public void setFirst(FirstType first) {
		this.first = first;
	}

	public SecondType getSecond() {
		return second;
	}

	public void setSecond(SecondType second) {
		this.second = second;
	}
	
}
