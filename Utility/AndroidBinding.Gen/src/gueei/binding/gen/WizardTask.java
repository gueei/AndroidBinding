package gueei.binding.gen;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.MultipleChoiceInputRequest;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.taskdefs.CallTarget;
import org.apache.tools.ant.taskdefs.Echo;

public class WizardTask extends Task {
	Vector<Choice> Choices = new Vector<Choice>();
	
	protected Hashtable<String, String> templateData = new Hashtable<String, String>();
	
	public Choice createChoice(){
		Choice entry = new Choice();
		Choices.add(entry);
		return entry;
	}
	
	private String message;
    public void setMessage(String message) {
    	this.message = message;
    }
    
    private String mDefault;
	public void setDefault(String default1) {
    	mDefault = default1;
    }

	public void execute() {
		Vector<String> answers = new Vector<String>();
		for(Choice c: Choices){
			answers.add(c.name);
		}
    	MultipleChoiceInputRequest request = new 
    			MultipleChoiceInputRequest(message, answers);
    	request.setDefaultValue(mDefault);
    	
    	InputHandler handler = getProject().getInputHandler();
    	handler.handleInput(request);
    	
    	String value = request.getInput();
    	String task = "";
    	for(Choice c: Choices){
    		if (c.name.equals(value)){
    			task = c.task;
    			break;
    		}
    	}
    	
    	if (task.isEmpty()){
    		System.err.println("Invalid choice, aborting");
    		return;
    	}

    	System.out.println(task);
    	Ant callee = new Ant(this);
    	callee.init();
    	
    	callee.setAntfile(getProject().getProperty("ant.file"));
    	callee.setTarget(task);
    	callee.execute();
	}
    
    public class Choice{
    	public Choice(){}
    	String name, task;
    	public void setName(String name){this.name = name;}
    	public void setTask(String task){this.task = task;}
    }
}
