package Function;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
//import java.util.Hashtable;

/**
 * 
 * @author Qing Shi
 * Class Server
 * Open port for client to connect, always running, send the simple reply message to client directly.
 */
public class Server {
	private static ArrayList<User> clientsPool = new ArrayList<User>();
	
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

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(9000);
			while (true) {
				System.out.println("Waiting for connecting....");
				Socket socket = serverSocket.accept();
				System.out.println("Creating one connection with socket : " + socket.getInetAddress() + "/" + socket.getPort());
				ServerThread serverThread = new ServerThread(socket);
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
