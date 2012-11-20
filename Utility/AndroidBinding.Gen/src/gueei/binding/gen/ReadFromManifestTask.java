package gueei.binding.gen;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ReadFromManifestTask extends Task {
	private File mDir;
	private String mSetPackageName;
	private String mSetSourceFolderName;

	public void setDir(File dir) {
		mDir = dir;
	}

	public void setSetPackageName(String setPackageName){
		mSetPackageName = setPackageName;
	}
	
	public void setSetSourceFolderName(String setSourceFolderName){
		mSetSourceFolderName = setSourceFolderName;
	}
	
	@Override
	public void execute() throws BuildException {
		try {
			Document doc = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder()
			        .parse(new File(mDir, "AndroidManifest.xml"));
			
			Element manifest = (Element) doc.getElementsByTagName("manifest").item(0);
			
			String pkgname = manifest.getAttribute("package");
			getProject().setNewProperty(mSetPackageName, pkgname);
			
			String srcFolder = "src/" + pkgname.replace('.', '/');
			getProject().setNewProperty(mSetSourceFolderName, srcFolder);

			log("Interpreted packagname: " + pkgname + ", default source folder: " + srcFolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
