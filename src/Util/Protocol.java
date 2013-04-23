package Util;

/**
 * 
 * @author Qing Shi
 * Protocol defined the rule between client and server.
 * When come to new feature, we should add some rules here.
 * Future action : can move more action in this class.
 * Now it just does classify the situation, return a simple identification mark.
 * The real logic, like string parse, is still done on client or server sides.
 * In the future, we can move those action in this class.
 *
 */
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
		} else if (msg.length() >= 22 && msg.substring(0, 22).equals("StartPrivateChatRemind")) {
			return ProtocolEnum.START_PRIVATE_CHAT.getValue();
		} else if (msg.length() >= 15 && msg.substring(0, 15).equals("LoginValidation")) {
			return ProtocolEnum.LOGIN_VALIDATION.getValue();
		} else if (msg.length() >= 5 && msg.substring(0, 5).equals("Login")) {
			return ProtocolEnum.LOGIN.getValue();
		} else if (msg.length() >= 11 && msg.equals("UserExisted")) {
			return ProtocolEnum.USER_EXISTED.getValue();
		} else if (msg.length() >= 14 && msg.equals("UserNotExisted")) {
			return ProtocolEnum.USER_NOT_EXISTED.getValue();
		}
		return 101; // Group chat
	}
}
