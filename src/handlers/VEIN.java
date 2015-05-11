package handlers;

import core.LayeredPanel;
import layers.GeneralGraphicsLayer;

/* Handles levels of vision for the classes it holds*/
public class VEIN {
	
	LayeredPanel rootLayer;
	GeneralGraphicsLayer graphicsLayer;

	public VEIN(){
		
	}
	
	
	
	void getDebug(){
		
	}
	public void extendReach(GeneralGraphicsLayer graphicsLayer){
		this.graphicsLayer = graphicsLayer;
	}
	public void extendReach(LayeredPanel rootLayer){
		this.rootLayer = rootLayer;
	}
	public GeneralGraphicsLayer getGraphicsLayer(){
		return graphicsLayer;
	}
	public LayeredPanel getRootLayer(){
		return rootLayer;
	}
	
}
