import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

import Util.Protocol;

/**
 * 
 * @author Qing Shi
 * Class ServerThread
 * Helper thread to deal with multiple users.
 */
public class ServerThread extends Thread{
	private Socket socket;
//	private ArrayList<Socket> clientsPool;
	private BufferedReader in;
	private PrintWriter out;
	private boolean isLoginCheck = true;
	
	/**
	 * Initial in and out stream based on the connection socket.
	 * @param s --- the connected socket.
	 */
	public ServerThread(Socket s) {
		this.socket = s;
//		this.clientsPool = pool;
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
					if (isLoginCheck) {  // Login, msg is the user name
						User user = new User(socket,msg);
						Server.getClientsPool().add(user);
						out.println("UserList " + Server.getLoginUserList());
						out.flush();
						isLoginCheck = false;
					} else {
//						System.out.println("msg:" + msg);
						int returnCode = Protocol.proceed(msg);
						if (returnCode == 1) {   // If it's the update request
							out.println("UserList " + Server.getLoginUserList());
							out.flush();
						} else if (returnCode == 2) {  // If it's logout request
							int port = Integer.parseInt(msg.substring(7));
							Server.removeUser(port);
						} else {   // If it's chat request
							SimpleDateFormat   formatter   =   new   SimpleDateFormat("HH:mm:ss");
							out.println("server " + formatter.format(new Date()) + "%20" + msg + socket.getInetAddress()+"/"+socket.getPort());
							out.flush();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
