package serializable;

import java.io.Serializable;

public class SerialSettings implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int resolution;
	private int debug;
	
	public SerialSettings(){
		this(0,0);
	}
	
	public SerialSettings(int res, int deb){
		setResolution(res);
		setDebug(deb);
	}
	public void setResolution(int res){
		resolution = res;
	}
	public int getResolution(){
		return resolution;
	}
	public void setDebug(int d){
		debug = d;
	}
	public int getDebug(){
		return debug;
	}
	
	
}
