package serializable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CreateSettingsFile {
	
	private ObjectOutputStream output;
	
	
	public void openFile(){
		try{
			System.out.println("opening file");
			output = new ObjectOutputStream( new FileOutputStream("settings.ser"));
		}
		catch(Exception e){
			System.err.println("Error opening file");
		}
	}
	
	public void addSettings(int res, int deb){
		System.out.println("writing to file");
		try{
			
			SerialSettings setting;
			setting = new SerialSettings(res,deb);	
			output.writeObject(setting);
			
		}
		catch(IOException ioException){	
			System.err.println("Error writing to file");
			return;
		}
		
	}
	
	public void closeFile(){
		try{
			if(output != null){
				System.out.println("closing file");
				output.close();
			}
		}catch(IOException ioException){
			System.err.println("Error closing file");
			System.exit(1);
		}
	}
	
	
}
