//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.PrintWriter;
//import java.net.Socket;

/**
 * 
 * @author Qing Shi
 * Class used for update the userlist for each client UI on the fly.
 */
public class ClientUpdateUserlistThread extends Thread{
//	private Socket socket;
//	private BufferedReader in;
	private PrintWriter out;  // This thread only need to send the update request to server. No need to receive anything
	
	public ClientUpdateUserlistThread(PrintWriter out) {
		this.out = out;
//		this.socket = s;
//		try {
////			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			out = new PrintWriter(socket.getOutputStream());
//		} catch (IOException e) {
//			System.out.println("ClientUpdateUserlistThread create error!");
//		}
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);    // Each 5 seconds, send update request
				out.println("Update");
				out.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
