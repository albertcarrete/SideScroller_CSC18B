package overlays;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Debugger {
	
	private HashMap<String,int[]> int_params;
	private HashMap<String,double[]> 	dbl_params;
	private HashMap<String,boolean[]> bol_params;
	
	public Debugger(){
		
		int_params = new HashMap<String,int[]>();
		dbl_params = new HashMap<String,double[]>();
		bol_params = new HashMap<String,boolean[]>();
	
	}
	
	/* Overloaded input */
	public void sendToScreen(String name, int ... params){
		int_params.put(name,params);
	}
	/* Overloaded input */
	public void sendToScreen(String name, double ... params){
		dbl_params.put(name,params);
	}
	public void sendToScreen(String name, boolean ... params){
		bol_params.put(name,params);
	}
	
	
	public void draw(Graphics g){
		
		int mainItemOffsetY = 0;
		int subItemOffsetY = 0;
		int subItemOffsetX = 80;
		
		for (int i = 0; i < 3; i++) {
			// Integer params
			if(i == 0){
				for(Map.Entry<String, int[]> e: int_params.entrySet()){
					g.drawString(e.getKey(),10,10+mainItemOffsetY);
					int size = e.getValue().length;
					for(int z = 0; z < size; z++){
						g.drawString("" + e.getValue()[z],subItemOffsetX,10+subItemOffsetY);
						subItemOffsetY +=10;
						mainItemOffsetY +=10;

					}
				}				
			}
			// Double Params
			if(i == 1){
				for(Map.Entry<String, double[]> e: dbl_params.entrySet()){
					g.drawString(e.getKey(),10,10+mainItemOffsetY);
					int size = e.getValue().length;
					for(int z = 0; z < size; z++){
						g.drawString("" + e.getValue()[z],subItemOffsetX,10+subItemOffsetY);
						subItemOffsetY +=10;
						mainItemOffsetY +=10;

					}
				}
			}
			// Boolean Params
			if(i == 2){
				for(Map.Entry<String, boolean[]> e: bol_params.entrySet()){
					g.drawString(e.getKey(),10,10+mainItemOffsetY);
					int size = e.getValue().length;
					for(int z = 0; z < size; z++){
						g.drawString("" + e.getValue()[z],subItemOffsetX,10+subItemOffsetY);
						subItemOffsetY +=10;
						mainItemOffsetY +=10;

					}
				}				
			}

		
		}
		
		
//		for(HashMap maps:hashMaps){
//			
//		}
	}
	
	
	
}
