package Util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Qing Shi
 * reference : http://zhidao.baidu.com/question/344298357.html
 * Class using to get the label for UI componenct.
 * All text is stored in /Users/shiqing/Documents/workspace/LetsChat/src/labels.xml
 *
 */
public class Label {
	private static String path;
	
	/**
	 * Set path for labels.xml
	 */
	public static void setPath() {
		try {
			File dir = new File("");
			path = dir.getCanonicalPath() + "/src/labels.xml";
		} catch (IOException e) {
			System.out.println("Password file doesn't exist.");
		}
	}
	
	/**
	 * Get the text for a lebel in UI from the labels.xml file
	 * @param panel --- Which panel this label in
	 * @param label --- The key word for this label
	 * @return
	 */
	public static String getLabel(String panel, String label) {
		setPath();
		
		try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(new File(path));
            Element elmtInfo = doc.getDocumentElement();
            NodeList nodes = elmtInfo.getChildNodes(); 
            
            for (int i = 0; i < nodes.getLength(); i++) {
                Node result = nodes.item(i);
                if (result.getNodeType() == Node.ELEMENT_NODE && result.getNodeName().equals(panel)) {
                    NodeList ns = result.getChildNodes();
                    for (int j = 0; j < ns.getLength(); j++) {
                        Node record = ns.item(j);
                        if (record.getNodeName().equals(label)) {
                        	return record.getTextContent();
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
		return null;
	}
}
