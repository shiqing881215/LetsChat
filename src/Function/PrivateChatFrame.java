package Function;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Frame class to support the UI for person-to-person chat
 * @author Qing Shi
 *
 */
public class PrivateChatFrame extends JFrame implements ActionListener{
	private PrintWriter out;
	private BufferedReader in;
	private String chatWithUsername;
	
	private JButton sendButton;
	private JTextArea receiveTextArea, sendTextAre;
	
	public PrivateChatFrame(PrintWriter out, BufferedReader in, String chatWithUsername) {
		this.chatWithUsername = chatWithUsername;
		this.out = out;
		this.in = in;
		init();
		receive();
	}
	
	public void init() {
		this.setTitle("Talking with " + chatWithUsername);  // Set the title to "Talking with somebody"
		this.setSize(400, 400);
		JPanel panel = new JPanel();
		receiveTextArea = new JTextArea(10,30);
		receiveTextArea.setEditable(false);
		sendTextAre = new JTextArea(10,30);
		JScrollPane receiveScroller = new JScrollPane(receiveTextArea);
		JScrollPane sendScroller = new JScrollPane(sendTextAre);
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
//		sendTextAre.setText("hello");
		if (evt.getSource() == sendButton) {
			String msg = receiveTextArea.getText();
			msg = "PrivateChat " + chatWithUsername + " " + msg;
			out.println(msg);
			out.flush();
		}
	}
	
	public void receive() {
		while(true) {
			try {
				String msg = in.readLine();
				receiveTextArea.append(msg);
			} catch (IOException e) {
				return;
			}
		}
	}
}
