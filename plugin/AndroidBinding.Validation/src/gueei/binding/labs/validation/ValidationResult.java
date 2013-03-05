package gueei.binding.labs.validation;

import java.util.ArrayList;
import java.util.Hashtable;

public class ValidationResult {
	private boolean valid = true;
	private Hashtable<String, ArrayList<String>> errorMessages = new Hashtable<String, ArrayList<String>>(1);
	
	public boolean isValid(){
		return valid;
	}
	
	public void putValidationError(String observableName, String message){
		if (!errorMessages.containsKey(observableName)){
			errorMessages.put(observableName, new ArrayList<String>(1));
		}
		
		valid = false;
		errorMessages.get(observableName).add(message);
	}
	
	public String[] getValidationErrors(){
		ArrayList<String> output = new ArrayList<String>();
		for(ArrayList<String> messages: errorMessages.values()){
			output.addAll(messages);
		}
		return output.toArray(new String[output.size()]);
	}
	
	public String[] getValidationErrors(String observableName){
		if (!errorMessages.containsKey(observableName)) return new String[0];
		return errorMessages.get(observableName).toArray(new String[0]);
	}
}
