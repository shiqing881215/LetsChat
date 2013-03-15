import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Qing Shi
 * Class ServerThread
 * Helper thread to deal with multiple users.
 */
public class ServerThread extends Thread{
	private Socket socket;
//	private ArrayList<Socket> clientsPool;
	BufferedReader in;
	PrintWriter out;
	
	/**
	 * Initial in and out stream based on the connection socket.
	 * @param s --- the connected socket.
	 */
	public ServerThread(Socket s) {
		this.socket = s;
//		this.clientsPool = pool;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(s.getOutputStream());
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
//					for (int i = 0; i < clientsPool.size(); i++) {
//						if (socket == clientsPool.get(i)) {
//							out.println()
//						}
//					}
					SimpleDateFormat   formatter   =   new   SimpleDateFormat("HH:mm:ss");
					out.println("server " + formatter.format(new Date()) + ":\n" + msg + socket.getInetAddress()+"/"+socket.getPort());
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
