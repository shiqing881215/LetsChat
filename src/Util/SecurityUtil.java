package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import Function.Server;
import Function.User;

/**
 * 
 * @author Qing Shi
 * Security Util to do the most security check for login and register.
 */
public class SecurityUtil {
	private static String path;
	
	/**
	 * Method set the pwd.txt file path.
	 */
	public static void setPath() {
		try {
			File dir = new File("");
			path = dir.getCanonicalPath() + "/src/pwd.txt";
		} catch (IOException e) {
			System.out.println("Password file doesn't exist.");
		}
	}
	
	/**
	 * 
	 * @param userName
	 * @param pwd
	 * @return 0 --- No such user
	 *         1 --- Password error
	 *         2 --- User already login --- this one is not checked here, validate from server
	 *         3 --- All good
	 */
	public static int checkLogin (String userName, String pwd) {
		setPath();
		System.out.println(userName + "    " + pwd + "    " + pwd.hashCode());
		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			
			boolean eof = false;
			int index;
			while (!eof) {
				String line = reader.readLine();
				if (line == null) {
					eof = true;
				} else {
					index = line.indexOf(' ');
					if (line.substring(0, index).equals(userName)) {
						if (Integer.parseInt(line.substring(index+1)) == pwd.hashCode()) { 
							return SecurityUtilEnum.LOGIN_SUCCESSFUL.getValue();
						} else {
							return SecurityUtilEnum.LOGIN_PASSWORD_ERROR.getValue();  // Pwd error
						}
					}
				}
			}
			return SecurityUtilEnum.LOGIN_NO_USER.getValue(); // No such user
		} catch (IOException e) {
			System.out.println("Validate exception!");
			return SecurityUtilEnum.LOGIN_EXCEPTION.getValue();
		}
	}
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @param confirmPassword
	 * @return 0 --- Password not match
	 *         1 --- Existed user name
	 *         2 --- All good
	 */
	public static int checkRegister(String userName, String password, String confirmPassword) {
		setPath();
		if (!password.equals(confirmPassword)) {
			return SecurityUtilEnum.REGISTER_PASSWORD_ERROR.getValue();
		} else if (checkExistingUserName(userName)) {
			return SecurityUtilEnum.REGISTER_EXISTED_USERNAME.getValue();
		} else {
			register(userName, password);
			return SecurityUtilEnum.REGISTER_SUCCESSFUL.getValue();
		}
	}
	
	/**
	 * Check whether this user has logged in.
	 * @param userName
	 * @return false --- no logging user, this user can log in
	 *         true --- already login user, this user cannot duplicatly login
	 */
	public static boolean checkLoggingUser(String userName) {
		for (User user : Server.getClientsPool()) {
			if (user.getUserName().equals(userName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check whether this user name has been used.
	 * @param userName --- Register Username
	 * @return false --- no duplicate name, can be registered
	 *         true --- duplicate name, cannot be registered
	 */
	public static boolean checkExistingUserName(String userName) { 
		setPath();
		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			
			boolean eof = false;
			int index;
			while (!eof) {
				String line = reader.readLine();
				if (line == null) {
					eof = true;
				} else {
					index = line.indexOf(' ');
					if (line.substring(0, index).equals(userName)) {
						return true;
					}
				}
			}
			return false;
		} catch (IOException e) {
			System.out.println("Check Existing Username Exception!");
			return true;
		}
	}
	
	/**
	 * Write new user to pwd file
	 * @param userName
	 * @param pwd
	 */
	public static void register(String userName, String pwd) {
		setPath();
		FileOutputStream stream;
	    OutputStreamWriter writer;
		try {
			stream = new FileOutputStream(path, true);
			writer = new OutputStreamWriter(stream);
			writer.write ("\r\n" + userName + " " + pwd.hashCode());
			writer.close();
			stream.close();
		} catch(Exception e) {
			System.out.println("Add new user to pwd file error.");
			e.getStackTrace ();
		}
	}
}
