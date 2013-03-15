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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * @author Qing Shi
 * Class Client
 * Simply UI with receive text area, send text field and send button.
 *
 */
public class Client implements ActionListener{
	private JTextField textField;
	private JTextArea textArea;
	private JButton button;
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private String userName;
	
	/**
	 * Create UI and connect server.
	 */
	public Client() {
		JFrame frame = new JFrame("Simple Client");
		frame.setSize(400,300);
		textField = new JTextField();
		textArea = new JTextArea();
		button = new JButton("send");
		textArea.setEditable(false);
		frame.getContentPane().add(new JScrollPane(textArea)); 
		frame.getContentPane().add(textField,"South");
		frame.getContentPane().add(button, "West");
		button.addActionListener(this);
		
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
	
	/**
	 * Send message to server.
	 */
	public void send() {
		String msg = textField.getText();
		textField.setText("");
		out.println(msg);
		SimpleDateFormat   formatter   =   new   SimpleDateFormat("HH:mm:ss"); 
		if (textArea.getText().length() != 0) { // not the first time send, message in another line
			textArea.append("\n");
		}
		textArea.append("me " + formatter.format(new Date()) + ":\n" + msg);
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
		send();
	}
	
	/**
	 * Create UI and then wait to receive. Also listen to send action.
	 */
	public static void main(String[] args) {
		Client client = new Client();
		client.receive();
	}
}
