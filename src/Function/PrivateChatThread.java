package Function;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class PrivateChatThread extends Thread{
	private PrintWriter out;
	private BufferedReader in;
	
	public PrivateChatThread(PrintWriter out, BufferedReader in) {
		this.out = out;
		this.in = in;
	}
	
	public void run() {
		PrivateChatFrame privateChatFrame = new PrivateChatFrame(out,in);
	}
}
