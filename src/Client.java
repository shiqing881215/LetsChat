import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * 
 * @author Qing Shi
 * Class Client
 * Simply UI with receive text area, send text field and send button.
 *
 */
public class Client implements ActionListener{
	private JFrame frame;
	private JPanel loginPanel, chatPanel;
	private JTextField sendTextField, userNameTextField;
	private JTextArea textArea;
	private JButton sendButton, loginButton;
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private String userName;
	
	/**
	 * Create UI and connect server.
	 */
	public Client() {
//		this.userName = userName;
		frame = new JFrame(userName);
		frame.setSize(400,400);
		
		loginPanel = new JPanel();
		chatPanel = new JPanel();
		
		JLabel userNameLabel = new JLabel("User Name");
		sendTextField = new JTextField(10);
		userNameTextField = new JTextField(10);
		sendButton = new JButton("send");
		loginButton = new JButton("login");
		sendButton.addActionListener(this);
		loginButton.addActionListener(this);
		textArea = new JTextArea(20,30);
		textArea.setEditable(false);
		JScrollPane scroller = new JScrollPane(textArea);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		loginPanel.add(userNameLabel);
		loginPanel.add(userNameTextField);
		loginPanel.add(loginButton);
		
		chatPanel.add(scroller);
		chatPanel.add(sendButton);
		chatPanel.add(sendTextField);
		
//		frame.getContentPane().add(new JScrollPane(textArea)); 
//		frame.getContentPane().add(textField,"South");
//		frame.getContentPane().add(button, "West");
		
		frame.setContentPane(loginPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		try {
			socket = new Socket("127.0.0.1",9000);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (UnknownHostException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
	}
	
//	public String getUserName() {
//		return this.userName;
//	}
//	
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
	
	/**
	 * Send message to server.
	 */
	public void send() {
		String msg = sendTextField.getText();
		sendTextField.setText("");
		out.println(msg);
		SimpleDateFormat   formatter   =   new   SimpleDateFormat("HH:mm:ss"); 
		if (textArea.getText().length() != 0) { // not the first time send, message in another line
			textArea.append("\n");
		}
		textArea.append(userName + " " + formatter.format(new Date()) + ":\n" + msg);
		out.flush();
	}
	
	/**
	 * Always running to receive any information from server side.
	 */
	public void receive() {
		while (true) {
			try {
//				textArea.append("Waiting for server response........");
				String msg = in.readLine();
				textArea.append("\n" + msg);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	/**
	 * When button is pressed, send the message.
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == loginButton) {
			this.userName = userNameTextField.getText();
			frame.setTitle(userName);
			frame.setContentPane(chatPanel);
			frame.setVisible(true);
		}
		if (evt.getSource() == sendButton) {
			send();
		}
	}
	
	/**
	 * Create UI and then wait to receive. Also listen to send action.
	 */
	public static void main(String[] args) {
		Client client = new Client();
		client.receive();
	}
}
