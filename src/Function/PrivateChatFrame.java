package Function;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Util.Protocol;
import Util.ProtocolEnum;

/**
 * Frame class to support the UI for person-to-person chat
 * @author Qing Shi
 *
 */
public class PrivateChatFrame extends JFrame implements ActionListener{
	private PrintWriter out;
	private BufferedReader in;
	private String chatWithUsername;
	private String myUsername;
	
	private JButton sendButton;
	private JTextArea receiveTextArea, sendTextArea;
	
	public PrivateChatFrame(PrintWriter out, BufferedReader in, String chatWithUsername, String myUsername) {
		this.out = out;
		this.in = in;
		this.chatWithUsername = chatWithUsername;
		this.myUsername = myUsername;
		init();
		receive();
	}
	
	public void init() {
		this.setTitle("Talking with " + chatWithUsername);  // Set the title to "Talking with somebody"
		this.setSize(400, 400);
		JPanel panel = new JPanel();
		receiveTextArea = new JTextArea(10,30);
		receiveTextArea.setEditable(false);
		sendTextArea = new JTextArea(10,30);
		JScrollPane receiveScroller = new JScrollPane(receiveTextArea);
		JScrollPane sendScroller = new JScrollPane(sendTextArea);
		sendButton = new JButton("send");
		sendButton.addActionListener(this);
		panel.add(receiveScroller);
		panel.add(sendButton);
		panel.add(sendScroller);
		this.setContentPane(panel);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		// Send private chat message to the server first
		if (evt.getSource() == sendButton) {
			String msg = sendTextArea.getText();
			SimpleDateFormat   formatter   =   new   SimpleDateFormat("HH:mm:ss"); 
			if (receiveTextArea.getText().length() != 0) { // not the first time send, message in another line
				receiveTextArea.append("\n");
			}
			receiveTextArea.append("me " + formatter.format(new Date()) + ":\n" + msg);
			msg = "PrivateChatToServer " + chatWithUsername + " " +myUsername + " " + msg;
			out.println(msg);
			out.flush();
			sendTextArea.setText("");
		}
	}
	
	public void receive() {
		while(true) {
			try {
				String msg = in.readLine();
				int returnCode = Protocol.proceed(msg);
				if (isPrivateChatMessage(msg) && returnCode == ProtocolEnum.PRIVATE_CHAT_TO_CLIENT.getValue()) {  // Get message like "PrivateChatToClient sq hello"
					StringBuilder sb = new StringBuilder();
					int indexOfFirstSpace = msg.indexOf(' ');
					int indexOfSecondSpace = msg.substring(indexOfFirstSpace+1).indexOf(' ') + indexOfFirstSpace + 1;
					sb.append("\n" + msg.substring(indexOfFirstSpace+1,indexOfSecondSpace) + " ");
					SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss"); 
					sb.append(formatter.format(new Date()) + ":\n");
					sb.append(msg.substring(indexOfSecondSpace+1));
					receiveTextArea.append(sb.toString());  
				}
			} catch (IOException e) {
				return;
			}
		}
	}
	
	/**
	 * Check whether this is a private chat message.
	 * Filter the updaet list noise message.
	 * @param msg
	 * @return
	 */
	public boolean isPrivateChatMessage(String msg) {
		if (msg.length() <= 8) return true;
		String header = msg.substring(0, 8);
		if (header.equals("UserList")) {
			return false;
		} else {
			return true;
		}
	}
}
