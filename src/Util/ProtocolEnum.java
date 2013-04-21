package Util;

public enum ProtocolEnum {
	USERLIST(0),
	UPDATE(1),
	LOGOUT(2),
	PRIVATE_CHAT_TO_SERVER(3),
	PRIVATE_CHAT_TO_CLIENT(4),
	START_PRIVATE_CHAT(5),
	GROUP(100);
	
	private int value;
	
	private ProtocolEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
