package Util;

/**
 * 
 * @author Qing Shi
 * Protocol Enum for protocol return value
 * When add new protocol, also add new enum here.
 */
public enum ProtocolEnum {
	USERLIST(0),
	UPDATE(1),
	LOGOUT(2),
	PRIVATE_CHAT_TO_SERVER(3),
	PRIVATE_CHAT_TO_CLIENT(4),
	START_PRIVATE_CHAT(5),
	LOGIN(6),
	LOGIN_VALIDATION(7),
	USER_EXISTED(8),
	USER_NOT_EXISTED(9),
	GROUP(100);
	
	private int value;
	
	private ProtocolEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
