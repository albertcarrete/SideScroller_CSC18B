package appstate;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import layers.GeneralGraphicsLayer;

public class MainMenuState extends AppState{
	
//	private Background bg;
	private AppStateManager asm;
	private GeneralGraphicsLayer layer;
//	private int titleY;
//	private int titleX;
//	
	
	private int currentChoice;
	private String[] options = {
		"START",
		"SETTINGS",
		"QUIT"
	};

//	private Color titleColor;
//	private Font titleFont;
//	private Color menuColor;
	private Font menuFont;
	private Color selectedColor;

	Graphics offgc;
	Image offscreen = null;
	private BufferedImage titleImage;
	private BufferedImage bgImage;

	// Constructor
	public MainMenuState(AppStateManager asm, GeneralGraphicsLayer layer){
		System.out.println("Menu state instantiated!");
		this.asm = asm;
		this.layer = layer;
		try{
			titleImage = ImageIO.read(getClass().getResourceAsStream("/title.gif"));		
			bgImage = ImageIO.read(getClass().getResourceAsStream("/titlebg.gif"));			
		}
		catch(Exception e){
			e.printStackTrace();
		}

//		titleY = 0;
//		titleX = 0;
		currentChoice = 0;
	}
	// Initializer
	public void init(){
//		titleY = 0;
//		titleX = 0;
		currentChoice = 0;
	};
	public void update(){
//		titleY++;

	};
	
	
	public void drawToScreen(Graphics2D drawingBoard){
		
		drawingBoard.setColor(Color.DARK_GRAY);

		/* Background image */
        drawingBoard.drawImage(bgImage, 0,0,GeneralGraphicsLayer.WIDTH, GeneralGraphicsLayer.HEIGHT,null);
		/* Title image */
        drawingBoard.drawImage(titleImage,90,60,342,54,null);
		
        drawingBoard.setColor(Color.WHITE);

//		titleFont = new Font("Century Gothic",Font.PLAIN, 40);
		menuFont = new Font("Arial",Font.BOLD, 15);

		drawingBoard.setFont(menuFont);
		
		selectedColor = new Color(119,193,197);
		
		for(int i = 0; i < options.length; i++){
			if(i == currentChoice){
				drawingBoard.setColor(selectedColor);
			}
			else{
				drawingBoard.setColor(Color.WHITE);
			}
			drawingBoard.drawString(options[i], 90, 190+i*25);			
		}
		
	}
	
	public void draw(java.awt.Graphics g){
		
//		offscreen = new BufferedImage(GeneralGraphicsLayer.WIDTH*2, GeneralGraphicsLayer.HEIGHT*2,BufferedImage.TYPE_INT_RGB);
//		
//		try{	
//			titleImage = ImageIO.read(getClass().getResourceAsStream("/resources/title.gif"));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		
//		
//		offgc = offscreen.getGraphics();
//		bg.draw(offgc);
//		
//		offgc.drawString("Test this", 150, 150);
//		offgc.drawImage(titleImage, 150, 150, 342, 54, null);
////		offgc = offscreen.getGraphics();
//		offgc.setColor(titleColor);
//		offgc.setFont(titleFont);
//		offgc.drawString("Test Game", 150, titleY);
		
//		try{	
//			titleImage = ImageIO.read(getClass().getResourceAsStream("/resources/title.gif"));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		bg.draw(g);
//		g.drawImage(titleImage,150,150,342,54,null);
//		
//		g.setColor(titleColor);
//		g.setFont(titleFont);
//		g.drawString("Test Game", 150, titleY);
//		
//		g.setColor(menuColor);
//		g.setFont(menuFont);
//		
//		for(int i = 0; i < options.length; i++){
//			if(i == currentChoice){
//				g.setColor(selectedColor);
//			}
//			else{
//				g.setColor(Color.WHITE);
//			}
//			g.drawString(options[i], 225, 300+i*25);			
//		}
		
		
		
//		g.drawImage(offscreen, 0, 0, null);
		
	};
	
	
	private void select(){
		if(currentChoice == 0){
			asm.setState(AppStateManager.LOBBYSTATE);
		}
		if(currentChoice == 1){
			asm.setState(AppStateManager.SETTINGSSTATE);
		}
		if(currentChoice == 2){
			System.exit(0);
		}
	}
	
	public void keyPressed(int k){
		if(k==KeyEvent.VK_ENTER){
			select();
		}
		if(k==KeyEvent.VK_O){
			System.out.println("O pressed");
			layer.triggerLoginOverlay();
		}
		if(k==KeyEvent.VK_R){
			System.out.println("R pressed");
			layer.triggerRegisterOverlay();
		}
		
		
		if(k==KeyEvent.VK_UP){
			
//			titleY--;
			currentChoice--;
			if(currentChoice == -1){
				currentChoice = options.length -1;
			}
		}
		if(k==KeyEvent.VK_DOWN){
//			titleY++;
			currentChoice++;
			if(currentChoice == options.length){
				currentChoice = 0;
			}
		}
		if(k==KeyEvent.VK_LEFT){
//			titleX--;
		}
		if(k==KeyEvent.VK_RIGHT){
//			titleX++;
		}
	
		
	};
	public void keyReleased(int k){
		
	};
}
