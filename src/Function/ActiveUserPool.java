package Function;

import java.util.HashSet;
//import java.util.Hashtable;

/**
 * 
 * @author shiqing
 * Singleton pattern.
 */
public class ActiveUserPool {
	private static ActiveUserPool activeUserPool = null;
	private static HashSet<String> users = null; //new HashSet<String>();
	private int readCount = 0;
	
	private ActiveUserPool() {
		users = new HashSet<String>();
		System.out.println("ONLY ONCE!!!!!!!!!!!");
	}
	
	private static synchronized void syncInit() {
		if (activeUserPool == null) {
			activeUserPool = new ActiveUserPool();
		}
	}
	
	public static ActiveUserPool getActiveUserPool() {
		if (activeUserPool == null) {
			syncInit();
		}
		return activeUserPool;
	}
	
	public synchronized void addUser(String userName) {  
		syncUpdateIn();
		users.add(userName);
	}  
	
	private synchronized void syncUpdateIn() {  
		while (readCount > 0) {  
			try {  
				wait();  
		    } catch (Exception e) {  
		    }  
		}  
	}  
		 
	private synchronized void syncReadIn() {  
		readCount++;  
	}  
		 
	private synchronized void syncReadOut() {  
		readCount--;  
		notifyAll();  
	}  
	
	public HashSet<String> getProperties() {  
		syncReadIn();  
		//Process data  
		syncReadOut();  
		return users;  
	}  
	
	
	
	
	
//	
//	public void addUser(String userName) {
////		if (!isUserActive(userName)) {
//			users.add(userName);
////		}
//	}
//	
//	public void removeUser(String userName) {
////		if (isUserActive(userName)) {
//			users.remove(userName);
////		}
//	}
	
	/**
	 * 
	 * @param userName
	 * @return false --- user not in pool. Can login	
	 * 		   true --- user in pool. Cannot login
	 */
	public boolean isUserActive(String userName) {
		System.out.println(userName);
		System.out.println("Users in hashset:");
		for(String s : users) {
			System.out.print(s + " ");
		}
		System.out.println(users.contains(userName));
		
		if (!users.contains(userName)) {
			return false;
		} else {
			return true;
		}
	}
}
