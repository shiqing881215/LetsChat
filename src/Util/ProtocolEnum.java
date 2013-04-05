package Util;

public enum ProtocolEnum {
	USERLIST(0),
	UPDATE(1),
	LOGOUT(2);
	
	private int value;
	
	private ProtocolEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
