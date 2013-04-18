package Util;

/**
 * Helper class for private chat.
 * @author Qing Shi
 *
 */
public class PrivateChatUtil {
	/**
	 * Given the whole receive string, parse it into username and (fromUser + message) and store in pair structure.
	 * Example "PrivateChatToServer sq lx hello" -> Pair<"sq","lx hello">
	 * @param info
	 * @return
	 */
	public static Pair<String, String> getUserNameAndMsg(String info) {
		int blankOneIndex = info.indexOf(' ');
		int blankTwoIndex = info.substring(blankOneIndex+1).indexOf(' ') + blankOneIndex + 1;
		String userName = info.substring(blankOneIndex+1, blankTwoIndex);
		String msg = info.substring(blankTwoIndex+1);
		Pair<String, String> pair = new Pair<String, String>(userName, msg);
		return pair;
	}
}
