package gueei.binding.gen;

import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.tools.ant.Task;

public class WriteTemplateTask extends Task {
	Vector<Entry> Entries = new Vector<Entry>();
	
	protected Hashtable<String, String> templateData = new Hashtable<String, String>();
	
	private File mDir;
	public void setDir(File dir){
		mDir = dir;
	}
	
	private String mInput;
	public void setInput(String input){
		if (input.startsWith("*"))
			input = input.substring(1).toLowerCase();
		mInput = "/" + input;
	}
	
	private String mOutput;
	public void setOutput(String output){
		if (output.startsWith("*"))
			mOutput = output.substring(1).toLowerCase();
		else
			mOutput = output;
	}
	
	public Entry createEntry(){
		Entry entry = new Entry();
		Entries.add(entry);
		return entry;
	}
	
    public void execute() {
    	for(Entry entry: Entries){
    		if (entry.value.startsWith("*")){
    			templateData.put(entry.key, entry.value.substring(1).toLowerCase());
    		}else{
    			templateData.put(entry.key, entry.value);
    		}
    	}
    	
        try {
        	log("Template: " + mInput + " generating output to: " + mDir + "/" + mOutput);
        	InputStream stream = this.getClass().getResourceAsStream(mInput);
        	String data = TemplateWriter.make(stream, templateData);
        	//log(data);
        	mDir.mkdirs();
        	TemplateWriter.writeToFile(mDir, mOutput, data);
        	// log("Template: " + mInput + " generated output to: " + mDir + "/" + mOutput);
        } catch (Exception e) {
	        e.printStackTrace();
        }
    }
    
    public class Entry{
    	public Entry(){}
    	String key, value;
    	public void setKey(String key){this.key = key;}
    	public void setValue(String value){this.value = value;}
    }
}
