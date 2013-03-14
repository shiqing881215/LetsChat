import java.io.IOException;
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
	private static ArrayList<Socket> clientPool;
	
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(9000);
			clientPool = new ArrayList<Socket>();
			while (true) {
				System.out.println("Waiting for connecting....");
				Socket socket = serverSocket.accept();
				System.out.println("Creating one connection with socket : " + socket.getInetAddress() + "/" + socket.getPort());
				clientPool.add(socket);
				ServerThread serverThread = new ServerThread(socket);
				serverThread.start();
			}
		} catch (IOException e) {
			System.out.println("Server down!");
		}
	}
}
