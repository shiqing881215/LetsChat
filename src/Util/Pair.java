package Util;

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
