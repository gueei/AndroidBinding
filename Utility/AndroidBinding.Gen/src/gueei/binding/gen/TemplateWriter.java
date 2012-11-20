package gueei.binding.gen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Map.Entry;

public class TemplateWriter {
	public static String make(InputStream template, Hashtable<String, String> data) 
			throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(template));
		StringBuilder builder = new StringBuilder();
		String line;
		while((line = reader.readLine())!=null){
			builder.append(line + "\n");
		}
		String content = builder.toString();
		
		for(Entry<String, String> entry: data.entrySet()){
			String replace = "${" + entry.getKey() + "}";
//			System.out.println("entry: " + replace + ": " + entry.getValue());
			content = content.replace(replace, entry.getValue());
		}
		
		return content;
	}
	
	public static void write(OutputStream target, String data) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(target));
		writer.write(data);
	}
	
	public static void writeToFile(File dir, String filename, String data) throws IOException{
		File file = new File(dir, filename);
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(data);
		writer.close();
	}
}