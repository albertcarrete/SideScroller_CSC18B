package overlays;

import java.util.ArrayList;
import java.util.HashMap;

public class DebugItem {
	
	String name;
	private HashMap<String,Integer> parameters;
	
	public DebugItem(String name){
		setName(name);
		parameters = new HashMap<String,Integer>();
	}
	
	
	public void addParamater(String name){
		parameters.put(name, 0);
	}
	
	// TODO do something is the value is not found
	public void setParameter(String name, int value){
		if(parameters.containsKey(name)){
			parameters.put(name,value);
		}
	}
	public int getParameterValue(String name){
		if(parameters.containsKey(name)){
			return parameters.get(name);
		}else{
			return -1;
		}
	}
	
	
	private void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
}
