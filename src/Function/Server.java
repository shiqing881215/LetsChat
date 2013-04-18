package Function;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 
 * @author Qing Shi
 * Class Server
 * Open port for client to connect, always running, send the simple reply message to client directly.
 */
public class Server {
	private static ArrayList<User> clientsPool = new ArrayList<User>();
	private static ArrayList<ServerThread> serverThreadsPool = new ArrayList<ServerThread>();  // Holding all ServerThread used for private chat
	
	public static ArrayList<User> getClientsPool() {
		return clientsPool;
	}
	
	/**
	 * Get all login user name
	 * @return A string containing all login user name, each line each user.
	 */
	public static String getLoginUserList() {
		StringBuilder userList = new StringBuilder();
		for (int i = 0; i < clientsPool.size(); i++) {
			userList.append(clientsPool.get(i).getUserName());
			userList.append("%20");
		}
		return userList.toString();
	}
	
	/**
	 * Notify all other group message receiveer.
	 * @param username --- group message sender
	 * @return All other receivers' output stream
	 */
	public static ArrayList<PrintWriter> notifyGroupMessage(String username) {
		ArrayList<PrintWriter> others = new ArrayList<PrintWriter>();
		for (int i = 0; i < clientsPool.size(); i++) {
			String otherName = clientsPool.get(i).getUserName();
			if (!otherName.equals(username)) {
				others.add(getChatTargetOut(otherName));
			}
		}
		System.out.println("SIZE " + others.size());
		return others;
	}
	
	/**
	 * Given a username, check whether this user is login
	 * @param userName
	 * @return true --- login
	 *         false --- not
	 */
	public static boolean isALoginUser(String userName) {
		if (userName == null || userName.length() == 0) return false;
		
//		System.out.println("SIZE " + clientsPool.size());
//		for (int i = 0; i < clientsPool.size(); i++) {
//			System.out.println("NAME " + clientsPool.get(i).getUserName());
//			if (clientsPool.get(i).getUserName().equals(userName)) {
//				return true;
//			}
//		}
		
		
//		String userList = getLoginUserList();
//		System.out.println("THERERER " + userList);
//		String[] list = userList.split("%20");
//		System.out.println("SIZE " + list.length);
//		System.out.println("SIZE2 " + list[0]);
//		for (int i = 0; i < list.length; i++) {
//			if (list[i].equals(userName)) return true;
//		}
		return false;
	}
	
	/**
	 * Remove the logout user information
	 * @param port --- The port of socket which should be removed
	 */
	public static void removeUser(int port) {
		int index = 0;
		for (int i = 0; i < clientsPool.size(); i++) {
			if (clientsPool.get(i).getSocket().getPort() == port) {
				index = i;
				break;
			}
		}
		clientsPool.remove(index);
	}
	
	/**
	 * Given a username, return the user.
	 * @param userName
	 * @return
	 */
	public static User getUser(String userName) {
		User user = null;
		for (int i = 0; i < clientsPool.size(); i++) {
			if (clientsPool.get(i).getUserName().equals(userName)) {
				user = clientsPool.get(i);
				return user;
			}
		}
		return user;
	}
	
	/**
	 * Given a socket, find the bound ServerSocket with this socket
	 * @param socket
	 * @return
	 */
	public static ServerThread getServerThread(Socket socket) {
		for (int i = 0; i < serverThreadsPool.size(); i++) {
			if (serverThreadsPool.get(i).getSocket() == socket) {
				return serverThreadsPool.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Given a targer user name, get the out strem to that user, so that can establish the private chat channel
	 * @param userName --- Chat target user name
	 */
	public static PrintWriter getChatTargetOut(String userName) {
		User user = getUser(userName);
		if (user == null) return null;
		Socket socket = user.getSocket();
		ServerThread serverThread = getServerThread(socket);
		if (serverThread == null) return null;
		return serverThread.getOut();
	}

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(9000);
			while (true) {
				System.out.println("Waiting for connecting....");
				Socket socket = serverSocket.accept();
				System.out.println("Creating one connection with socket : " + socket.getInetAddress() + "/" + socket.getPort());
				ServerThread serverThread = new ServerThread(socket);
				serverThreadsPool.add(serverThread);   // Add the new ServerThread to the serverThreadsPool
				serverThread.start();
			}
		} catch (BindException bindException) {
			System.out.println("Address already in use, please make sure all previous server is closed.");
			System.out.println("Server not start.");
		} catch (IOException ioException) {
			System.out.println("IOException");
		}
	}
}
