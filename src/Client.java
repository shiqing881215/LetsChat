import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Client implements ActionListener{
	private JTextField textField;
	private JTextArea textArea;
	private JButton button;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
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
	
	public void send() {
		String msg = textField.getText();
		textField.setText("");
		out.println(msg);
		out.flush();
	}
	
	public void receive() {
		while (true) {
			try {
				String msg = in.readLine();
				textArea.append(msg);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public void actionPerformed(ActionEvent evt) {
		send();
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		client.receive();
	}
}