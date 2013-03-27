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
 * Class specified for check whether a certain user has login.
 * Have no way but to do this, cannot use the clientsPool in server to check already login user.
 * Still don't know the reason.
 */
public class LoggingUserUtil {
	private static String path;
	
	/**
	 * Method set the activeUser.txt file path.
	 */
	public static void setPath() {
		try {
			File dir = new File("");
			path = dir.getCanonicalPath() + "/src/activeUser.txt";
		} catch (IOException e) {
			System.out.println("activeUser file doesn't exist.");
		}
	}
	
	public static void addUser(String userName) {
		setPath();
		FileOutputStream stream;
	    OutputStreamWriter writer;
		try {
			stream = new FileOutputStream(path, true);
			writer = new OutputStreamWriter(stream);
			writer.write ("\r\n" + userName);
			writer.close();
			stream.close();
		} catch(Exception e) {
			System.out.println("Add new user to activeUser file error.");
			e.getStackTrace ();
		}
	}
	
	public static void removeUser(String userName) {
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
					if (line.equals(userName)) {
						
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Check Existing Username Exception!");
		}
	}
}
