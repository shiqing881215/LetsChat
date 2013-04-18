package Util;

public class Protocol {
	public static int proceed(String msg) {
		if (msg.length() >= 8 && msg.substring(0, 8).equals("UserList")) {  // To client, the user list from server
			return ProtocolEnum.USERLIST.getValue();
		} else if (msg.length() >= 6 && msg.substring(0, 6).equals("Update")) {  // To server, request for update user list
			return ProtocolEnum.UPDATE.getValue();
		} else if (msg.length() >= 6 && msg.substring(0, 6).equals("Logout")) {  // To server, request for logout
			return ProtocolEnum.LOGOUT.getValue();
		} else if (msg.length() >= 19 && msg.substring(0, 19).equals("PrivateChatToServer")) {  // To server, request for private chat
			return ProtocolEnum.PRIVATE_CHAT_TO_SERVER.getValue();
		} else if (msg.length() >= 19 && msg.substring(0, 19).equals("PrivateChatToClient")) {  // To client, resend the private chat message to target client
			return ProtocolEnum.PRIVATE_CHAT_TO_CLIENT.getValue();
		} else if (msg.length() >= 5 && msg.substring(0, 5).equals("Group")) {
			return ProtocolEnum.GROUP.getValue();
		}
		return 101; // Group chat
	}
}
