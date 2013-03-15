import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class LoginPage implements ActionListener{
	
	public  LoginPage() {
		JFrame frame = new JFrame("Login");
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel userNameLabel = new JLabel("User Name");
		JTextField userNameTextField = new JTextField(20);
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		
		JPanel panel = new JPanel();
		panel.add(userNameLabel);
		panel.add(userNameTextField);
		panel.add(loginButton);
		
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}
	
	public static void main (String[] args) {
		LoginPage loginPage = new LoginPage();
	}
}
