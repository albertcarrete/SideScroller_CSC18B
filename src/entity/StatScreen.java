package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import layers.GeneralGraphicsLayer;

public class StatScreen {
	public static final int NONE = 0;
	public static final int SCOREBOARD = 1;
	public static final int DEATH = 2;
	public static final int VICTORY = 3;

	GeneralGraphicsLayer graphicsLayer;
	
	int width = GeneralGraphicsLayer.WIDTH;
	int height = GeneralGraphicsLayer.HEIGHT;
	
	int MENU;
	Font death;
	Font standard;
	
	public StatScreen(int menu, GeneralGraphicsLayer graphicsLayer){
		
	this.graphicsLayer = graphicsLayer;	
		
		
		int style = Font.BOLD | Font.ITALIC;
		death = new Font ("Arial", style , 40);
		
		int standardStyle = Font.PLAIN;
		standard = new Font ("Arial", style , 12);
		
		MENU = menu;

	}
	
	public void draw(Graphics2D g){
		
		if(MENU == DEATH){
			g.setFont(death);
			g.setColor(Color.RED);
			g.drawString("YOU DIED", (int)((width/2)- 120),(int)(height/2));	
		}
		if(MENU == SCOREBOARD){
			g.drawString("SCOREDBOARD", 150, 150);
		}
		if(MENU == VICTORY){
			g.drawString("VICTORY", 150, 150);
		}
		
		g.setFont(standard);
		g.setColor(Color.WHITE);

	}
	
	public void setState(int menu){
		MENU = menu;
	}
	public int getState(){
		return MENU;
	}
	
	
}
