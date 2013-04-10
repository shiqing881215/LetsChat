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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import Util.Protocol;
import Util.ProtocolEnum;
import Util.SecurityUtil;
import Util.SecurityUtilEnum;

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
	private JTextField sendTextField, userNameTextField, pwdTextField, 
		registerUserNameTextField, registerPwdTextField, registerConfirmPwdTextField, privateChatNameTextField;
	private JTextArea chatTextArea, userListTextArea;
	private JButton sendButton, loginButton, registerButton, startPrivateChatButton;
	
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
		
		// Login and register compenents
		JLabel userNameLabel = new JLabel("User Name");
		JLabel pwdLabel = new JLabel("Password");
		JLabel registerLabel = new JLabel("New --- Please register first.                                     ");
		JLabel registerUserNameLabel = new JLabel("User Name");
		JLabel registerPwdLabel = new JLabel("Password");
		JLabel confirmPwdLabel = new JLabel("Confirm Password");
		userNameTextField = new JTextField(23);
		pwdTextField = new JTextField(23);
		registerUserNameTextField = new JTextField(23);
		registerPwdTextField = new JTextField(23);
		registerConfirmPwdTextField = new JTextField(20);
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		registerButton = new JButton("Register");
		registerButton.addActionListener(this);

		// Chat components
		sendTextField = new JTextField(10);
		privateChatNameTextField = new JTextField(10);
		sendButton = new JButton("send");
		sendButton.addActionListener(this);
		startPrivateChatButton = new JButton("Start private chat");
		startPrivateChatButton.addActionListener(this);
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
		loginPanel.add(pwdLabel);
		loginPanel.add(pwdTextField);
		loginPanel.add(loginButton);
		loginPanel.add(registerLabel);
		loginPanel.add(registerUserNameLabel);
		loginPanel.add(registerUserNameTextField);
		loginPanel.add(registerPwdLabel);
		loginPanel.add(registerPwdTextField);
		loginPanel.add(confirmPwdLabel);
		loginPanel.add(registerConfirmPwdTextField);
		loginPanel.add(registerButton);
		
		chatPanel.add(chatScroller);
		chatPanel.add(sendButton);
		chatPanel.add(sendTextField);
		chatPanel.add(usreListScroller);
		chatPanel.add(privateChatNameTextField);
		chatPanel.add(startPrivateChatButton);
		
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
					
//					ActiveUserPool activeUserPool = ActiveUserPool.getActiveUserPool();
//					activeUserPool.removeUser(frame.getTitle());
					
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

				if (returnCode == ProtocolEnum.USERLIST.getValue()) {  // Update user list
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
	 * Login
	 * Register
	 * Send(chat message)
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == loginButton) {
			int loginRetCode = SecurityUtil.checkLogin(userNameTextField.getText(), pwdTextField.getText());
			System.out.println("code:" + loginRetCode);
			if (loginRetCode == SecurityUtilEnum.LOGIN_SUCCESSFUL.getValue()) {
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
				String loginErrMsg;
				if (loginRetCode == SecurityUtilEnum.LOGIN_USER_ALREADY_LOGIN.getValue()) {
					loginErrMsg = "This user already login!";
				} else if (loginRetCode == SecurityUtilEnum.LOGIN_PASSWORD_ERROR.getValue()) {
					loginErrMsg = "Password error!";
				} else {
					loginErrMsg = "No such user!";
				}
				// Using final otherwise error in the actionPerformed below
				// Also, use panel to add label and button at the same time coz final can be changed once
				final JFrame loginErrorFrame = new JFrame();   
				loginErrorFrame.setSize(150, 100);
				JPanel loginErrorPanel = new JPanel();
				JLabel loginFailLabel = new JLabel(loginErrMsg);
				JButton loginOkButton = new JButton("OK");
				loginOkButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						loginErrorFrame.dispose();
					}
				});
				loginErrorPanel.add(loginFailLabel);
				loginErrorPanel.add(loginOkButton);
				loginErrorFrame.setContentPane(loginErrorPanel);
				loginErrorFrame.setVisible(true);
			}
		}
		if (evt.getSource() == registerButton) {
			int registerRetCode = SecurityUtil.checkRegister
					(registerUserNameTextField.getText(), registerPwdTextField.getText(),registerConfirmPwdTextField.getText());
			if (registerRetCode == 2) {  // Register success
				this.userName = registerUserNameTextField.getText();
				frame.setTitle(userName);
				frame.setContentPane(chatPanel);
				frame.setVisible(true);
				out.println(userName);   // Send the login userName to server
				out.flush();
				// Send the update request to server after login
				ClientUpdateUserlistThread cuut = new ClientUpdateUserlistThread(out);
				cuut.start();
			} else {
				String registerErrMsg;
				if (registerRetCode == 0) {
					registerErrMsg = "Password not match!";
				} else {
					registerErrMsg = "Username already existed!";
				}
				final JFrame registerErrorFrame = new JFrame();   
				registerErrorFrame.setSize(160, 100);
				JPanel registerErrorPanel = new JPanel();
				JLabel registerFailLabel = new JLabel(registerErrMsg);
				JButton registerOkButton = new JButton("OK");
				registerOkButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						registerErrorFrame.dispose();
					}
				});
				registerErrorPanel.add(registerFailLabel);
				registerErrorPanel.add(registerOkButton);
				registerErrorFrame.setContentPane(registerErrorPanel);
				registerErrorFrame.setVisible(true);
			}
		}
		if (evt.getSource() == startPrivateChatButton) {
			PrivateChatThread p = new PrivateChatThread(out,in, privateChatNameTextField.getText());
			p.start();
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
