import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.util.ArrayList;


public class ServerThread extends Thread{
	private Socket socket;
//	private ArrayList<Socket> clientsPool;
	BufferedReader in;
	PrintWriter out;
	
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
					out.println(msg+socket.getInetAddress()+"/"+socket.getPort());
					out.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
