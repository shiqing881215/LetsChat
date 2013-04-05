package Function;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
//import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private boolean isLoginCheck = true;
	
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
	 * Always listen the user input, reply automatically.
	 */
	public void run() {
		while (true) {
			try {
				String msg = in.readLine();
				if (msg == null) {
					return;
				} else {
					// Check whether it's login process and send the login user list
					if (isLoginCheck) {  // The first msg must be the login msg, which is the user name
						User user = new User(socket,msg);
						Server.getClientsPool().add(user);   // Add this login user to pool

						// Add login user into pool
//						ActiveUserPool activeUserPool = ActiveUserPool.getActiveUserPool();
//						activeUserPool.addUser(msg);
						
						out.println("UserList " + Server.getLoginUserList());
						out.flush();
						isLoginCheck = false;
					} else {
						int returnCode = Protocol.proceed(msg);
						if (returnCode == ProtocolEnum.UPDATE.getValue()) {   // If it's the update request
							out.println("UserList " + Server.getLoginUserList());
							out.flush();
						} else if (returnCode == ProtocolEnum.LOGOUT.getValue()) {  // If it's logout request
							int port = Integer.parseInt(msg.substring(7));
							Server.removeUser(port);
						} else {   // If it's chat request
							SimpleDateFormat   formatter   =   new   SimpleDateFormat("HH:mm:ss");
							out.println("server " + formatter.format(new Date()) + "%20" + msg + socket.getInetAddress()+"/"+socket.getPort());
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
