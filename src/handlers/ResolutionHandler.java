package handlers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

public class ResolutionHandler {
	
	private ArrayList<Resolution> resolutions;

	public ResolutionHandler(){
		resolutions = new ArrayList<Resolution>();
		Dimension currScreenSize 	= Toolkit.getDefaultToolkit().getScreenSize();
		
		Resolution res1 = new Resolution((int)(currScreenSize.getWidth() - 50),(int)(currScreenSize.getHeight()));
		Resolution res2 = new Resolution(1920,1080);
		Resolution res3 = new Resolution(720,480);
		Resolution res4 = new Resolution(600,350);
		
		resolutions.add(res1);
		resolutions.add(res2);
		resolutions.add(res3);
		resolutions.add(res4);
		
	}
	public Resolution getResolutions(int i){
		return resolutions.get(i);
	}
}

