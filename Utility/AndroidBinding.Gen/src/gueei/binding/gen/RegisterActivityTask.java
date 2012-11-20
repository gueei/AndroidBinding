package gueei.binding.gen;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RegisterActivityTask extends Task {
	private File mDir;

	public void setDir(File dir) {
		mDir = dir;
	}

	private String mActivityClassName;

	public void setActivityClassName(String appClassName) {
		mActivityClassName = appClassName;
	}

	@Override
	public void execute() throws BuildException {
		try {
			File file = new File(mDir, "AndroidManifest.xml");

			Document doc = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder()
			        .parse(new File(mDir, "AndroidManifest.xml"));
			Node appNode = doc.getElementsByTagName("application").item(0);

			NodeList activityList = doc.getElementsByTagName("activity");
			
			for(int i=0; i<activityList.getLength(); i++){
				Element activity = (Element) activityList.item(i);
				String name = activity.getAttribute("android:name");
				if (mActivityClassName.equals(name) || ("." + mActivityClassName).equals(name)){
					log("Activity defintion exists, no need to modify the manifest");
					return;
				}
			}
			
			Element activity = doc.createElement("activity");
			appNode.appendChild(activity);
			activity.setAttribute("android:name", "." + mActivityClassName);
			activity.setAttribute("android:label", mActivityClassName);

			TransformerFactory transformerFactory = TransformerFactory
			        .newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			log("Manifest <application> modified");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
