package appstate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import serializable.CreateSettingsFile;
import serializable.ReadSettingsFile;
import layers.GeneralGraphicsLayer;

public class SettingsState extends AppState{
	
	// Dependencies
	private AppStateManager asm;
	private GeneralGraphicsLayer parent;
	
	private int screenW;
	private int screenH;
	
	private BufferedImage bgImage;
	private Color titleColor;
	private Font titleFont;

	private int currentChoice;
	private int subChoice;
	
	private int optionX;
	private int optionY;
	
	/* Here we use a LinkedHashMap instead of a 
	 * HashMap because a LHM preserves the order
	 * of the added values. */
	public LinkedHashMap<String,String[]> hm;
	public int suboption[];
	public int currSubOption;
	private String options[] ={
			"resolution",
			"debug",
			"exit"
	};
	
	
	public SettingsState(AppStateManager asm, GeneralGraphicsLayer parent){
		System.out.println("Constructor: Settings State");
		
		// Retrieve dependencies
		this.asm = asm;
		this.parent = parent;
		
		// Setup screen specifications
		screenW = parent.screenHeight;
		screenH = parent.screenWidth;
		
		currentChoice = 0;
		

		
		// Menu Map
		hm = new LinkedHashMap<String,String[]>();
		hm.put( "RESOLUTION", new String[]{ "FULLSCREEN", "NATIVE SCREEN" } );
		hm.put( "DEBUG", new String[]{ "ON", "OFF" } );
		hm.put("EXIT",new String[]{"SAVE AND EXIT", "DISCARD AND EXIT"});
		
		suboption = new int[hm.size()];
		for(int i = 0; i < suboption.length; i++){
			suboption[i] = 0;
		}
		
		try{
			titleColor = new Color(128,0,0);
			titleFont = new Font("Century Gothic",Font.PLAIN, 40);
			bgImage = ImageIO.read(getClass().getResourceAsStream("/titlebg.gif"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		optionX = 50;
		optionY = 50;

	}
	public void init(){
//		parent.triggerSettingsOverlay();
		System.out.println("We should be reading in the settings file here.");
		ReadSettingsFile rSet = new ReadSettingsFile();
		rSet.openFile();
		rSet.readSettings(suboption);
		rSet.closeFile();
	};
	public void update(){
		currSubOption  = suboption[currentChoice];

	};
	public void draw(java.awt.Graphics g){
//		g.setColor(titleColor);
//		g.setFont(titleFont);
//		g.drawString("Settings", 150, titleY);
	};
	
	/* Draw to Screen ===
	 * All drawing calls for this AppState occur in this function  */
	public void drawToScreen(Graphics2D drawingBoard){
		
		optionX = 50;
		optionY = 50;
		drawingBoard.setColor(Color.DARK_GRAY);
		drawingBoard.drawImage(bgImage, 0,0,GeneralGraphicsLayer.WIDTH,GeneralGraphicsLayer.HEIGHT,null);
		drawingBoard.drawRect(0,0,GeneralGraphicsLayer.WIDTH, GeneralGraphicsLayer.HEIGHT);
		drawingBoard.setColor(Color.WHITE);
		
		Color selectedColor = new Color(119,193,197);
		
		int iter = 0;
		for(String key: hm.keySet()){
			if(iter == currentChoice){
				drawingBoard.setColor(Color.RED);
			}else{
				drawingBoard.setColor(Color.WHITE);
			}
			drawingBoard.drawString(key,optionX,optionY);
			String arr[] = hm.get(key);
			drawingBoard.drawString(arr[suboption[iter]],optionX+100,optionY);
			optionY+=25;
			iter++;
		}

	}; // end method drawToScreen
	
	public void keyPressed(int k){
		if(k==KeyEvent.VK_ENTER){
//			subChoice--;
//			if(subChoice == -1){
//				subChoice = suboption.length -1;
//			}
			if(currentChoice == 2){
				if(suboption[currentChoice] == 1){
					asm.setState(AppStateManager.MENUSTATE);
				}else{
					System.out.println("Applying new settings!");
					CreateSettingsFile process = new CreateSettingsFile();
					process.openFile();
					process.addSettings(suboption[0], suboption[1]);
					process.closeFile();
					asm.setState(AppStateManager.MENUSTATE);
				}

			}else{
				currSubOption++;
				if(currSubOption == 2){
					currSubOption = 0;
				}
				suboption[currentChoice] = currSubOption;				
			}

			
//			System.out.println("PRESSED THE ENTER KEY!!!");
//			asm.setState(AppStateManager.MENUSTATE);
		}
		if(k==KeyEvent.VK_RIGHT){
			currSubOption++;
			if(currSubOption == 2){
				currSubOption = 0;
			}
			suboption[currentChoice] = currSubOption;						
		}
		if(k== KeyEvent.VK_LEFT){
			currSubOption--;
			if(currSubOption == -1){
				currSubOption = 1;
			}
			suboption[currentChoice] = currSubOption;
		}
		if(k==KeyEvent.VK_UP){
			currentChoice--;
			if(currentChoice == -1){
				currentChoice = hm.size()-1;
			}
		}
		if(k==KeyEvent.VK_DOWN){
			currentChoice++;
			if(currentChoice == options.length){
				currentChoice = 0;
			}
		}
		
		
		
	};
	public void keyReleased(int k){
		
	};
}
