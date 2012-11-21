package gueei.binding.documentation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

public class ConverterDoclet {

	private static ArrayList<ClassDoc> converters = new ArrayList<ClassDoc>();
	
	public static boolean start(RootDoc root){
		File destDir = new File("output");
		destDir.mkdirs();
		
		
		ClassDoc[] classes = root.classes();
		for(int i=0; i<classes.length; i++){
			processClass(classes[i], destDir);
		}
		
		outputConverters(destDir);
		
		return true;
	}
	
	private static void processClass(ClassDoc classDoc, File destDir) {
		if (classDoc.qualifiedTypeName().startsWith("gueei.binding.converters.")){
			// Ignore inner class
			if (!classDoc.name().contains("."))
				converters.add(classDoc);
		}
		
    }

	private static void outputConverters(File destDir) {
		try {
			File output = new File(destDir.getAbsolutePath(), "converters.html");
	        output.createNewFile();
			PrintWriter writer = new PrintWriter(output);
			
			writer.println("---");
			writer.println("layout: converter");
			writer.println("title: Converters");
			writer.println("permalink: converters/index.html");
			writer.println("converters:");

			for(ClassDoc doc : converters){
				writeConverter(doc, writer);
			}

			writer.println("---");
			
			writer.close();
        } catch (IOException e) {
	        e.printStackTrace();
        } 
		
		
    }

	private static void writeConverter(ClassDoc doc, PrintWriter writer) {
	    String name = doc.name();
	    Hashtable<String, ArrayList<Tag>> tags = organizeTags(doc.tags());

	    writer.println("  - name: " + name);
	    writer.println("    description: " + doc.commentText().replace("\n", "<br/>"));
	    if (tags.containsKey("@return")){
		    Tag[] returnTags = tags.get("@return").toArray(new Tag[0]);
		    writer.println("    return: ");
		    for(int i=0; i<returnTags.length; i++){
		    	String[] text = returnTags[i].text().split(" ");
		    	writer.println("        type: " + text[0]);
		    	writer.println("        type_short: " + ShortTypeName(text[0]));
		    	writer.println("        comment: " + CommentString(text, 1));
		    }
	    }
	    if (tags.containsKey("@arg")){
	    	writer.println("    parameters: ");
	    	Tag[] argTags = tags.get("@arg").toArray(new Tag[0]);
	    	for(int i=0; i<argTags.length; i++){
	    		String[] text = argTags[i].text().split(" ");
	    		writer.println("      - name: " + text[0]);
	    		writer.println("        type: " + text[1]);
	    		writer.println("        type_short: " + ShortTypeName(text[1]));
	    		writer.println("        comment: " + CommentString(text, 2));
	    	}
	    }
    }
	
	private static String CommentString(String[] delimited, int start){
		String output = "";
		for(int i=start; i<delimited.length; i++){
			output += delimited[i] + " ";
		}
		return output;
	}
	
	private static String ShortTypeName(String TypeName){
		int lastDot = TypeName.lastIndexOf('.');
		if (lastDot<0) return TypeName;
		return TypeName.substring(lastDot+1, TypeName.length());
	}
	
	private static Hashtable<String, ArrayList<Tag>> organizeTags(Tag[] tags){
		Hashtable<String, ArrayList<Tag>> output = new Hashtable<String, ArrayList<Tag>>();
		for(int i=0; i<tags.length; i++){
			Tag tag = tags[i];
			String name = tag.name();
			if (!output.containsKey(name)){
				output.put(name, new ArrayList<Tag>());
			}
			output.get(name).add(tag);
		}
		return output;
	}
}
