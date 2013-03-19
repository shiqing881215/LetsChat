package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
}
