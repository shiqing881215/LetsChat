package Util;

public enum SecurityUtilEnum {
	LOGIN_NO_USER(0),
	LOGIN_PASSWORD_ERROR(1),
	LOGIN_USER_ALREADY_LOGIN(2),
	LOGIN_SUCCESSFUL(3),
	LOGIN_EXCEPTION(4),
	REGISTER_PASSWORD_ERROR(0),
	REGISTER_EXISTED_USERNAME(1),
	REGISTER_SUCCESSFUL(2);
	
	private int value;
	
	private SecurityUtilEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
