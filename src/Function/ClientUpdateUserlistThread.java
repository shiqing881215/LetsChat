package Function;
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
	private PrintWriter out;  // This thread only need to send the update request to server. No need to receive anything
	
	public ClientUpdateUserlistThread(PrintWriter out) {
		this.out = out;
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);    // Each 5 seconds, send update request
				out.println("Update");
				out.flush();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
