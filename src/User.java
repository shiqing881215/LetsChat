import java.net.Socket;

/**
 * 
 * @author Qing Shi
 * User class to hold the login client information
 */
public class User {
	private Socket socket;
	private String userName;
	
	public User(Socket s, String u) {
		this.socket = s;
		this.userName = u;
	}
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
