package Function;

import java.io.PrintWriter;

/**
 * 
 * @author Qing Shi
 * Class used for update the userlist for each client UI on the fly.
 */
public class ClientUpdateUserlistThread extends Thread{
	private PrintWriter out;  // This thread only need to send the update request to server. Client is responsible for receiving and proceeding the info
	
	public ClientUpdateUserlistThread(PrintWriter out) {
		this.out = out;
	}
	
	/**
	 * Send update request each 5 seconds
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);   
				out.println("Update");
				out.flush();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
