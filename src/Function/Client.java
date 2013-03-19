package Function;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import Util.Protocol;
import Util.SecurityUtil;

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
	private JTextField sendTextField, userNameTextField, pwdTextField;
	private JTextArea chatTextArea, userListTextArea;
	private JButton sendButton, loginButton;
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private String userName;
	
	/**
	 * Create UI and connect server.
	 */
	public Client() {
		frame = new JFrame(userName);
		frame.setSize(400,600);
		
		loginPanel = new JPanel();
		chatPanel = new JPanel();
		
		JLabel userNameLabel = new JLabel("User Name");
		JLabel pwdLabel = new JLabel("Password");
		sendTextField = new JTextField(10);
		userNameTextField = new JTextField(15);
		pwdTextField = new JTextField(15);
		sendButton = new JButton("send");
		loginButton = new JButton("login");
		sendButton.addActionListener(this);
		loginButton.addActionListener(this);
		chatTextArea = new JTextArea(20,30);
		chatTextArea.setEditable(false);
		JScrollPane chatScroller = new JScrollPane(chatTextArea);
		chatScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		userListTextArea = new JTextArea(10,30);
		userListTextArea.setEditable(false);
		JScrollPane usreListScroller = new JScrollPane(userListTextArea);
		usreListScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		loginPanel.add(userNameLabel);
		loginPanel.add(userNameTextField);
		loginPanel.add(loginButton);
		loginPanel.add(pwdLabel);
		loginPanel.add(pwdTextField);
		
		chatPanel.add(chatScroller);
		chatPanel.add(sendButton);
		chatPanel.add(sendTextField);
		chatPanel.add(usreListScroller);
		
//		frame.getContentPane().add(new JScrollPane(textArea)); 
//		frame.getContentPane().add(textField,"South");
//		frame.getContentPane().add(button, "West");
		
		frame.setContentPane(loginPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		// When window is closed, close all its connected input/output stream and socket
		// At the same time, send the logout info to server to clear this user info on server
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {   
				try {
					System.out.println("Logout");
					out.println("Logout " + socket.getLocalPort());  // "Logout socketport"
					out.flush();
					socket.close();
					frame.dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		// A little trick here, first connect the server
		// Seems that logic should not be like this, however, it's not better to put this in the actionPerformed method.
		// For later security check, we can do some trick
		try {
			socket = new Socket("127.0.0.1",9000);
			System.out.println("Client : " + socket.getLocalPort());
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
		if (chatTextArea.getText().length() != 0) { // not the first time send, message in another line
			chatTextArea.append("\n");
		}
		chatTextArea.append(userName + " " + formatter.format(new Date()) + ":\n" + msg);
		out.flush();
	}
	
	/**
	 * Always running to receive any information from server side.
	 */
	public void receive() {
		while (true) {
			try {
				String msg = in.readLine();
				int returnCode = Protocol.proceed(msg);
				msg = msg.replaceAll("%20", "\n");

				if (returnCode == 0) {  // Update user list
					msg = msg.substring(9);  // remove the tag "userList"
					userListTextArea.setText(msg);
				} else {  // Update chat box
					chatTextArea.append("\n" + msg);
				}
			} catch (SocketException socketException) {
				System.out.println("This client has logged out.");
				return;
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
			boolean success = SecurityUtil.validate(userNameTextField.getText(), pwdTextField.getText());
			System.out.println(success);
			if (success) {
				this.userName = userNameTextField.getText();
				frame.setTitle(userName);
				frame.setContentPane(chatPanel);
				frame.setVisible(true);
				out.println(userName);   // Send the login userName to server
				out.flush();
				// Send the update request to server after login
				ClientUpdateUserlistThread cuut = new ClientUpdateUserlistThread(out);
				cuut.start();
			} else {
				final JFrame loginErrorFrame = new JFrame();
				loginErrorFrame.setSize(100, 100);
				JLabel loginFailLabel = new JLabel("Login Fail.");
				loginErrorFrame.add(loginFailLabel);
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						loginErrorFrame.dispose();
					}
				});
				loginErrorFrame.add(okButton);
				loginErrorFrame.setVisible(true);
			}
		}
		if (evt.getSource() == sendButton) {
			// Send button send the message 
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
