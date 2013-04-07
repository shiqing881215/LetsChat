package Function;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class PrivateChatThread extends Thread{
	private PrintWriter out;
	private BufferedReader in;
	private String chatWithUsername;
	
	public PrivateChatThread(PrintWriter out, BufferedReader in, String chatWithUsername) {
		this.out = out;
		this.in = in;
		this.chatWithUsername = chatWithUsername;
	}
	
	public void run() {
		PrivateChatFrame privateChatFrame = new PrivateChatFrame(out,in, chatWithUsername);
	}
}
