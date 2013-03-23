package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 
 * @author Qing Shi
 * Class for check user login.
 */
public class SecurityUtil {
	private static String path;
	
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
	 * @return true --- validate success
	 *         false --- validate fail
	 */
	public static boolean validate (String userName, String pwd) {
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
						if (Integer.parseInt(line.substring(index+1)) == pwd.hashCode()) {  // Using hash code
							return true;
						}
					}
				}
			}
			return false;
		} catch (IOException e) {
			System.out.println("Validate exception!");
			return false;
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
			return 0;
		} else if (checkExistingUserName(userName)) {
			return 1;
		} else {
			register(userName, password);
			return 2;
		}
	}
	
	/**
	 * 
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
