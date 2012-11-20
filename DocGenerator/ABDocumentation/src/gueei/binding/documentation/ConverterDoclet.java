package gueei.binding.documentation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

public class ConverterDoclet {
	
	public static boolean start(RootDoc root){
		File destDir = new File("output");
		destDir.mkdirs();
		
		ClassDoc[] classes = root.classes();
		for(int i=0; i<classes.length; i++){
			processClass(classes[i], destDir);
		}
		return true;
	}
	
	private static void processClass(ClassDoc classDoc, File destDir) {
		if (classDoc.qualifiedTypeName().startsWith("gueei.binding.converters.")){
			System.out.println("converter: " + classDoc.qualifiedTypeName());
			processConverter(classDoc, destDir);
		}
		
    }

	private static void processConverter(ClassDoc classDoc, File destDir) {
		// Ignore inner class
		if (classDoc.name().contains(".")) return;
		
		// Create Converter File
		String converterName = classDoc.name();
		File output = new File(destDir.getAbsolutePath(), converterName);
		try {
	        output.createNewFile();
			PrintWriter writer = new PrintWriter(output);
			
			writer.println("---");
			writer.println("layout: converter");
			writer.println("title: " + converterName);
			writer.println("permalink: converter/" + converterName + ".html");
			writer.println("---");
			
			writer.close();
        } catch (IOException e) {
	        e.printStackTrace();
        } 
		
    }
}
