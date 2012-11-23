package gueei.binding.documentation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import com.sun.javadoc.TypeVariable;

public class ViewAttributesDoclet {

	public static LanguageVersion languageVersion() {
	      return LanguageVersion.JAVA_1_5;
	   }
	
	private static ArrayList<ClassDoc> converters = new ArrayList<ClassDoc>();
	
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
		if (isAttributeClass(classDoc)){
			try{
				TagsTable tags = organizeTags(classDoc.tags());
				VADocument doc = new VADocument();
				doc.Name = classDoc.name();
				doc.Type = tags.get("@output").get(0).text();
				doc.ShortType = ShortTypeName(doc.Type);
				doc.Accepts = tagsTextToArray(tags.get("@accepts").toArray(new Tag[0]));
				doc.Comment = classDoc.commentText();
				doc.Widget = tags.get("@widget").get(0).text();
				System.out.println(serializeObjectinJSON(doc));
			}catch(Exception e){
				
			}
		}
    }

	private static String[] tagsTextToArray(Tag[] tags){
		String[] output = new String[tags.length];
		for(int i=0; i<output.length; i++){
			output[i] = tags[i].text();
		}
		return output;
	}
	
	public static String serializeObjectinJSON(Object obj){
		String output = "{";
		Field[] fields = obj.getClass().getFields();
		for (int i=0; i<fields.length; i++){
			Field f = fields[i];
			try {
				if (f.getType().isArray()){
					output += "\"" + f.getName() + "\": " + serializeArrayInJSON((Object[])f.get(obj)) + ", ";
				}else{
					output += "\"" + f.getName() + "\" : \"" + f.get(obj) + "\", ";
				}
            } catch (Exception e) {
	            e.printStackTrace();
            }
		}
		output += "}";
		return output;
	}
	
	public static String serializeArrayInJSON(Object[] arr){
		String output = "[";
		for (int i=0; i<arr.length; i++){
			output += "\"" + arr[i] + "\", ";
		}
		output += "]";
		return output;
	}
	
	public static class VADocument{
		public String Name;
		public String Type;
		public String ShortType;
		public String[] Accepts;
		public String Comment;
		public String Widget;
	}
	
	private static boolean isAttributeClass(ClassDoc doc){
		if (doc.superclass() == null) return false;
		if (doc.name().equals("ViewAttribute")){
			return true;
		}
		return isAttributeClass(doc.superclass());
	}
	
	
	private static String CommentString(String[] delimited, int start){
		String output = "";
		for(int i=start; i<delimited.length; i++){
			output += delimited[i] + " ";
		}
		return formatYamlString(output);
	}
	
	private static String formatYamlString(String original){
		return "\"" + original.replace("\"", "\\\"") + "\"";
	}
	
	private static String ShortTypeName(String TypeName){
		int lastDot = TypeName.lastIndexOf('.');
		if (lastDot<0) return TypeName;
		return TypeName.substring(lastDot+1, TypeName.length());
	}
	
	private static TagsTable organizeTags(Tag[] tags){
		TagsTable output = new TagsTable();
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
