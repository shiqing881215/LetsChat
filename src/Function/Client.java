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

import Util.Label;
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
	// UI components
	private JFrame frame;
	private JPanel loginPanel, chatPanel;
	private JTextField sendTextField, userNameTextField, pwdTextField, 
		registerUserNameTextField, registerPwdTextField, registerConfirmPwdTextField, privateChatNameTextField;
	private JTextArea chatTextArea, userListTextArea;
	private JButton sendButton, loginButton, registerButton, startPrivateChatButton;
	
	// Util instance variable to communicate with server
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
		JLabel userNameLabel = new JLabel(Label.getLabel("login", "username"));
		JLabel pwdLabel = new JLabel(Label.getLabel("login", "pwd"));
		JLabel registerLabel = new JLabel(Label.getLabel("register", "notification"));
		JLabel registerUserNameLabel = new JLabel(Label.getLabel("register", "username"));
		JLabel registerPwdLabel = new JLabel(Label.getLabel("login", "pwd"));
		JLabel confirmPwdLabel = new JLabel(Label.getLabel("login", "confirm"));
		userNameTextField = new JTextField(23);
		pwdTextField = new JTextField(23);
		registerUserNameTextField = new JTextField(23);
		registerPwdTextField = new JTextField(23);
		registerConfirmPwdTextField = new JTextField(20);
		loginButton = new JButton(Label.getLabel("login", "button"));
		loginButton.addActionListener(this);
		registerButton = new JButton(Label.getLabel("register", "button"));
		registerButton.addActionListener(this);

		// Chat components
		sendTextField = new JTextField(10);
		privateChatNameTextField = new JTextField(10);
		sendButton = new JButton(Label.getLabel("chat", "send"));
		sendButton.addActionListener(this);
		startPrivateChatButton = new JButton(Label.getLabel("chat", "private"));
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
		
		// Default initial page is the login page
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
		// A user must pass the security check then can really login. When this window close, everything will close --- so don't worry :)
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
	
	/**
	 * Send group message to server.
	 * eg: "Group sq hello"
	 */
	public void send() {
		String msg = sendTextField.getText();
		sendTextField.setText("");
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss"); 
		if (chatTextArea.getText().length() != 0) { // not the first time send, message in another line
			chatTextArea.append("\n");
		}
		chatTextArea.append(userName + " " + formatter.format(new Date()) + ":\n" + msg);
		msg = "Group " + userName + " " + msg;   
		out.println(msg);
		out.flush();
	}
	
	/**
	 * Always running to receive any information from server side.
	 * Based on protocol, perform different action.
	 */
	public void receive() {
		while (true) {
			try {
				String msg = in.readLine();
				int returnCode = Protocol.proceed(msg); System.out.println(msg);System.out.println("CLIENT RETURN CODE:" + returnCode);
				msg = msg.replaceAll("%20", "\n");
				
				if (returnCode == ProtocolEnum.USER_EXISTED.getValue()) { // User already login, give error panel
					String loginErrMsg = Label.getLabel("error", "logUserExisted");
					final JFrame loginErrorFrame = new JFrame();   
					loginErrorFrame.setSize(150, 100);
					JPanel loginErrorPanel = new JPanel();
					JLabel loginFailLabel = new JLabel(loginErrMsg);
					JButton loginOkButton = new JButton(Label.getLabel("error", "button"));
					loginOkButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							loginErrorFrame.dispose();
						}
					});
					loginErrorPanel.add(loginFailLabel);
					loginErrorPanel.add(loginOkButton);
					loginErrorFrame.setContentPane(loginErrorPanel);
					loginErrorFrame.setVisible(true);
				} else if (returnCode == ProtocolEnum.USER_NOT_EXISTED.getValue()) { // This means login successfully
					this.userName = userNameTextField.getText();
					frame.setTitle(userName);
					frame.setContentPane(chatPanel);
					frame.setVisible(true);
					out.println("Login " + userName);   // "Login username"
					out.flush();
					// Send the update request to server after login
					ClientUpdateUserlistThread cuut = new ClientUpdateUserlistThread(out);
					cuut.start();
				} else if (returnCode == ProtocolEnum.USERLIST.getValue()) {  // Update user list
					msg = msg.substring(9);  // remove the tag "userList"
					userListTextArea.setText(msg);
				} else if (returnCode == ProtocolEnum.GROUP.getValue()){  // Group message : "Group sq hello"
					SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
					int indexOfFirstSpace = msg.indexOf(' ');
					int indexOfSecondSpace = msg.substring(indexOfFirstSpace+1).indexOf(' ') + indexOfFirstSpace + 1;
					String user = msg.substring(indexOfFirstSpace+1, indexOfSecondSpace);
					String message = msg.substring(indexOfSecondSpace+1);
					StringBuilder sb = new StringBuilder();
					if (chatTextArea.getText().length() != 0) { // not the first time, message in another line
						chatTextArea.append("\n");
					}
					sb.append(user + " ");
					sb.append(formatter.format(new Date()) + "\n");
					sb.append(message);
					chatTextArea.append(sb.toString());
				} else if (returnCode == ProtocolEnum.START_PRIVATE_CHAT.getValue()){ // msg : "StartPrivateChatRemind target"
					String targetUserName = msg.substring(msg.indexOf(' ')+1);
					PrivateChatThread p = new PrivateChatThread(out,in, targetUserName, userName);
					p.start();
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
	 * When button is pressed, perform different action.
	 * Login
	 * Register
	 * Send group message
	 * Send/start private chat
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == loginButton) {
			int loginRetCode = SecurityUtil.checkLogin(userNameTextField.getText(), pwdTextField.getText());
			System.out.println("code:" + loginRetCode);
			if (loginRetCode == SecurityUtilEnum.LOGIN_SUCCESSFUL.getValue()) {  // Pass security check, check same user login at server side
				this.userName = userNameTextField.getText();
				out.println("LoginValidation " + userName);   
				out.flush();
			} else {
				String loginErrMsg;
				if (loginRetCode == SecurityUtilEnum.LOGIN_PASSWORD_ERROR.getValue()) {
					loginErrMsg = Label.getLabel("error", "logPwd");
				} else {
					loginErrMsg = Label.getLabel("error", "logNoUser");
				}
				// Using final otherwise error in the actionPerformed below
				// Also, use panel to add label and button at the same time coz final can be changed once
				final JFrame loginErrorFrame = new JFrame();   
				loginErrorFrame.setSize(150, 100);
				JPanel loginErrorPanel = new JPanel();
				JLabel loginFailLabel = new JLabel(loginErrMsg);
				JButton loginOkButton = new JButton(Label.getLabel("error", "button"));
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
			if (registerRetCode == SecurityUtilEnum.REGISTER_SUCCESSFUL.getValue()) {  // Register success
				this.userName = registerUserNameTextField.getText();
				frame.setTitle(userName);
				frame.setContentPane(chatPanel);
				frame.setVisible(true);
				out.println("Login " + userName);   // Send the login userName to server
				out.flush();
				// Send the update request to server after login
				ClientUpdateUserlistThread cuut = new ClientUpdateUserlistThread(out);
				cuut.start();
			} else {
				String registerErrMsg;
				if (registerRetCode == SecurityUtilEnum.REGISTER_PASSWORD_ERROR.getValue()) {
					registerErrMsg = Label.getLabel("error", "regPwd");
				} else {
					registerErrMsg = Label.getLabel("error", "regUserExisted");
				}
				final JFrame registerErrorFrame = new JFrame();   
				registerErrorFrame.setSize(160, 100);
				JPanel registerErrorPanel = new JPanel();
				JLabel registerFailLabel = new JLabel(registerErrMsg);
				JButton registerOkButton = new JButton(Label.getLabel("error", "button"));
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
			// TODO error handling (similar issue with checking same user login twice --- server.clientsPool is empty)
			String targetUserName = privateChatNameTextField.getText();
//			if (Server.isALoginUser(userName)) {
				PrivateChatThread p = new PrivateChatThread(out,in, targetUserName, userName);
				p.start();
				String startPrivateChatRemind = "StartPrivateChatRemind " + targetUserName + " " + userName;
				out.println(startPrivateChatRemind);
				out.flush();
//			} else {
//				 TODO Add corresponding error panel here
//			}
		}
		if (evt.getSource() == sendButton) {  // Group message
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
