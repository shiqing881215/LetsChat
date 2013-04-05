package Util;

public class Protocol {
	public static int proceed(String msg) {
		if (msg.length() >= 8 && msg.substring(0, 8).equals("UserList")) {  // To client, the user list from server
			return ProtocolEnum.USERLIST.getValue();
		} else if (msg.length() >= 6 && msg.substring(0, 6).equals("Update")) {  // To server, request for update user list
			return ProtocolEnum.UPDATE.getValue();
		} else if (msg.length() >= 6 && msg.substring(0, 6).equals("Logout")) {  // To server, request for logout
			return ProtocolEnum.LOGOUT.getValue();
		}
		return 100;
	}
}
