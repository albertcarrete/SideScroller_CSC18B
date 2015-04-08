package serializable;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadSettingsFile {
	private ObjectInputStream input;
	
	public void openFile(){
		try{
			input = new ObjectInputStream(new FileInputStream("settings.ser"));
			System.out.println("File opened successfully");
		}
		catch(IOException ioExpection){
			System.err.println("Error opening file.");
		}
	}
	
	public void readSettings(int arr[]){
		SerialSettings settings;
		
		try{
			System.out.println("Attempting to read settings");

			while(true){
				settings = (SerialSettings) input.readObject();
//				System.out.println("Settings retrieved were " + settings.getResolution() + settings.getDebug());
//				System.out.printf("%-%",settings.getResolution(),settings.getDebug());
				arr[0] = settings.getResolution();
				arr[1] = settings.getDebug();
			}
			
		}catch(EOFException endOfFileException){
			return;
		}
		catch(ClassNotFoundException classNotFoundException){
			System.err.println("Unable to create object");
		}
		catch(IOException ioException){
			System.err.println("Error during read from file");
		} // end catch
	} // end method readSettings
	
	public void closeFile(){
		try{
			if(input != null){
				input.close();
			}
		} catch (IOException ioException){
			System.err.println("Error closing file.");
			System.exit(1);
		} // end catch
	} /// end method closeFile
} // end class ReadSettingsFile