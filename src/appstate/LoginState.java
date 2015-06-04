package appstate;

import java.awt.Color;
//import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import layers.GeneralGraphicsLayer;
//import tileMap.Background;

public class LoginState extends AppState{
	// dependencies
//	private Background bg;
//	private AppStateManager asm;
	private GeneralGraphicsLayer layer;
	// images 
	private BufferedImage loginBoxImage;
	private BufferedImage bgImage;
	
	private boolean overlayTriggered;
	
	/* GeneralGraphicsLayer -> LoginState */
	public LoginState(AppStateManager asm, GeneralGraphicsLayer layer){
		System.out.println("[LoginState] instantiated!");
		
		/* Set hierarchy*/
		this.asm 	= asm;
		this.layer 	= layer;
		
		/* Options */
		overlayTriggered = false;
		
		/* Background Image */
		try{	
			bgImage = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/login_bg.jpg"));			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		init();
	}
	
	public void init(){

		
	}
	public void update(){
		
		if(layer != null && overlayTriggered != true){
			layer.triggerLoginOverlay();
			overlayTriggered = true;
		}
		
	}
	public void drawToScreen(Graphics2D drawingBoard){
		drawingBoard.setColor(Color.DARK_GRAY);

		/* Background image */
        drawingBoard.drawImage(bgImage, 0,0,GeneralGraphicsLayer.WIDTH, GeneralGraphicsLayer.HEIGHT,null);
		/* Title image */
//        drawingBoard.drawImage(loginBoxImage,160,60,201,201,null);
        drawingBoard.setColor(Color.WHITE);
        if(layer == null){
        	drawingBoard.drawString("Gathering information...",180,120);
        }

	}
	public void draw(java.awt.Graphics g){
		
	}
	public void keyPressed(int k){
		
	}
	public void keyReleased(int k){
		
	};

}
