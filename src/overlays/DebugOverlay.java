package overlays;

import java.awt.Color;
import java.awt.Graphics;
//import java.util.ArrayList;

import entity.Player;

public class DebugOverlay {
//	private ArrayList<DebugItem> debugItems;
	DebugItem position;
	DebugItem size;
	DebugItem action;

	private Player player;

	public DebugOverlay(){
//		debugItems = new ArrayList<DebugItem>();
		position = new DebugItem("position");
		position.addParamater("x");
		position.addParamater("y");
		
		size = new DebugItem("size");
		size.addParamater("cwidth");
		size.addParamater("cheight");
		size.addParamater("height");
		size.addParamater("width");
		
		action = new DebugItem("action");
		action.addParamater("currentAction");
		
	}
	
	public void getPlayer(Player player){
		this.player = player;
	}
	public void update(){
		if(player != null){
			position.setParameter("x", player.getx());
			position.setParameter("y", player.gety());
			size.setParameter("cwidth", player.getCWidth());
			size.setParameter("cheight", player.getCHeight());
			size.setParameter("width", player.getWidth());
			size.setParameter("height", player.getHeight());
			action.setParameter("currentAction",player.getCurrentAnim());
		}
	}
	public void draw(Graphics g){
		g.drawString("Position " + position.getParameterValue("x") + " " + position.getParameterValue("y") , 10,50);
		g.drawString("W&H " + size.getParameterValue("width") + " " + size.getParameterValue("height") , 10,75);
		g.drawString("cW&H " + size.getParameterValue("cwidth") + " " + size.getParameterValue("cheight") , 10,100);
		g.drawString("Action" + action.getParameterValue("currentAction"),10,125);
		g.setColor(Color.RED);
		g.drawRect(position.getParameterValue("x"),position.getParameterValue("y"),1,1);
	}
	
	
	
}
