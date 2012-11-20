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

public class RegisterApplicationTask extends Task {
	private File mDir;

	public void setDir(File dir) {
		mDir = dir;
	}

	private String mAppClassName;

	public void setAppClassName(String appClassName) {
		mAppClassName = appClassName;
	}

	@Override
	public void execute() throws BuildException {
		try {
			File file = new File(mDir, "AndroidManifest.xml");

			Document doc = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder()
			        .parse(new File(mDir, "AndroidManifest.xml"));
			Element appNode = (Element) doc.getElementsByTagName("application").item(0);
			appNode.setAttribute("android:name", mAppClassName);

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
