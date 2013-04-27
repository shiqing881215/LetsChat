package Function;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import Util.Pair;
import Util.PrivateChatUtil;
import Util.Protocol;
import Util.ProtocolEnum;

/**
 * 
 * @author Qing Shi
 * Class ServerThread
 * Helper thread to deal with multiple users.
 */
public class ServerThread extends Thread{
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	/**
	 * Initial in and out stream based on the connection socket.
	 * @param s --- the connected socket.
	 */
	public ServerThread(Socket s) {
		this.socket = s;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Server thread create error!");
		}
	}
	
	/**
	 * Always listen the user customized message.
	 * Response based on protocol.
	 * Core method to deal with client interaction
	 */
	public void run() {
		while (true) {
			try {
				String msg = in.readLine();
				if (msg == null) {
					return;
				} else {
					int returnCode = Protocol.proceed(msg); //System.out.println("RETURN CODE : " + returnCode);
					if (returnCode == ProtocolEnum.LOGIN_VALIDATION.getValue()) {
						String userName = msg.substring(msg.indexOf(' ') + 1);
						boolean userIsLogin = Server.isALoginUser(userName);
						if (userIsLogin) {
							out.println("UserExisted"); 
							out.flush();
						} else {
							out.println("UserNotExisted");
							out.flush();
						}
					} else if (returnCode == ProtocolEnum.LOGIN.getValue()) {
						String username = msg.substring(6);
						User user = new User(socket, username);
						Server.getClientsPool().add(user);
						out.println("UserList " + Server.getLoginUserList()); 
						out.flush();
					} else if (returnCode == ProtocolEnum.UPDATE.getValue()) {   
						out.println("UserList " + Server.getLoginUserList());
						out.flush();   
					} else if (returnCode == ProtocolEnum.LOGOUT.getValue()) {  
						int port = Integer.parseInt(msg.substring(7));
						Server.removeUser(port);
					} else if (returnCode == ProtocolEnum.PRIVATE_CHAT_TO_SERVER.getValue()) {
						System.out.println("GET PRIVATE CHAT MESSAGE!");
						Pair<String, String> usernameAndMsg = PrivateChatUtil.getUserNameAndMsg(msg);
						String username = usernameAndMsg.getFirst();
						String message = usernameAndMsg.getSecond();
						PrintWriter chatTargetOut = Server.getChatTargetOut(username);
						chatTargetOut.println("PrivateChatToClient " + message);  // This message include the fromUser and message, eg: "PrivateChatToClient qs hello"
						chatTargetOut.flush();
					} else if (returnCode == ProtocolEnum.GROUP.getValue()){   // Group message : "Group sq hello", add nothing here just forward to other users.
						// Get all other users and forward the message
						String sendUsername = msg.substring(6, msg.substring(6).indexOf(' ')+6);   System.out.println("SEND USER " + sendUsername);
						ArrayList<PrintWriter> others = Server.notifyGroupMessage(sendUsername);
						for (PrintWriter other : others) {
							other.println(msg);
							other.flush();
						}
					} else if (returnCode == ProtocolEnum.START_PRIVATE_CHAT.getValue()) { // msg : "StartPrivateChatRemind target from"
						int firstSpaceIndex = msg.indexOf(' ');
						int secondSpaceIndex = msg.substring(firstSpaceIndex+1).indexOf(' ') + firstSpaceIndex + 1;
						String targetUsername = msg.substring(firstSpaceIndex+1, secondSpaceIndex);
						String fromUsername = msg.substring(secondSpaceIndex+1);
						PrintWriter chatTargetOut = Server.getChatTargetOut(targetUsername);
						chatTargetOut.println("StartPrivateChatRemind " + fromUsername);  // "StartPrivateChatRemind from"
						chatTargetOut.flush();
					} else if (returnCode == ProtocolEnum.PRIVATE_CHAT_VALIDATION.getValue()) { // msg : "PrivateChatValidation target"
						String userName = msg.substring(msg.indexOf(' ') + 1);
						boolean userIsLogin = Server.isALoginUser(userName);  System.out.println("USERNAME : " + userName);
						if (userIsLogin) {
							out.println("PrivateChatValidationSuccess"); System.out.println("YES!");
							out.flush();
						} else {
							out.println("PrivateChatValidationFailed"); System.out.println("NO!");
							out.flush();
						}
					}
				}
			} catch (SocketException socketException) {  // Deal with issue when close window before login, coz now already connect with server.
				System.out.println("Close window before login.");
				return;
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
				return;
			}
		}
	}
}
