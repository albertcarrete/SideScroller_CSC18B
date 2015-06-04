package appstate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import layers.GeneralGraphicsLayer;

public class CharacterSelectState extends AppState{
	
	private AppStateManager asm;
	private GeneralGraphicsLayer layer;
	
	private int currentChoice;
	
	private BufferedImage bg;
	private BufferedImage character;
	
	private BufferedImage character1;
	private BufferedImage character2;
	private BufferedImage character3;

	
	private int screenHeight;
	private int screenWidth;
	private Color bgColor;
	private Color bgCharacter;
	private int margin;
	private int halfScreenW;
	private int halfScreenH;

	private int selectBoxW;
	private int selectBoxH;	
	
	boolean readyUp;
	
	public CharacterSelectState(AppStateManager a, GeneralGraphicsLayer l){
		
		System.out.println("Character Select State instantiated");
		this.asm = a;
		this.layer = l;
		screenHeight = GeneralGraphicsLayer.HEIGHT;
		screenWidth = GeneralGraphicsLayer.WIDTH;
		halfScreenW = (int)screenWidth/2;
		halfScreenH = (int)((screenHeight/2)-20);

		margin = 15;		
		selectBoxW = (int)(halfScreenW-(margin*1.5));
		selectBoxH = (int)(halfScreenH-(margin));
		
		
		bgColor = new Color(63,60,70);
		bgCharacter = new Color(119,110,128);

		readyUp = false;
		
		try{
			bg = ImageIO.read(getClass().getResourceAsStream("/titlebg.gif"));		
			character1 = ImageIO.read(getClass().getResourceAsStream("/CharacterSelect/character1.gif"));		
//			character2 = ImageIO.read(getClass().getResourceAsStream("/titlebg.gif"));		
//			character3 = ImageIO.read(getClass().getResourceAsStream("/titlebg.gif"));		

		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void init(){
		System.out.println("Character Select State initiated");

		currentChoice = 0;
	}
	public void update(){
		
	}
	public void draw(java.awt.Graphics g){
		
	}
	public void drawToScreen(Graphics2D drawingBoard){
		
		drawingBoard.setColor(Color.DARK_GRAY);
		/* Background image */
        drawingBoard.drawImage(bg, 0,0,GeneralGraphicsLayer.WIDTH, GeneralGraphicsLayer.HEIGHT,null);
        drawingBoard.drawString("Character Select",50,50);
        
		drawingBoard.drawRect(0, 0, screenWidth, screenHeight);
		drawingBoard.setColor(bgCharacter);
		
		drawingBoard.fillRect(0, 0, (int)(screenWidth), (int)(screenHeight));
		drawingBoard.setPaint(bgColor);
		
		// xpos, ypos, width, height
		/* Top Left */
		drawingBoard.fillRect(0+margin, 0+margin, selectBoxW, selectBoxH);
		/* Top Right */
		drawingBoard.fillRect((int)((screenWidth/2)+margin-(margin*0.5)), 0+margin, selectBoxW, selectBoxH);
		/* Bottom Left */
		drawingBoard.fillRect(0+margin, (int)(halfScreenH + (margin)), selectBoxW, selectBoxH);
		/* Bottom Right */
		drawingBoard.fillRect((int)((screenWidth/2)+margin-(margin*0.5)), (int)(halfScreenH + (margin)), selectBoxW, selectBoxH);
		// string, x, y
		
		drawingBoard.setColor(Color.WHITE);
		drawingBoard.drawString("P1", (int)(halfScreenW/2), 35);
		drawingBoard.drawString("P2", (int)(halfScreenW+(halfScreenW/2)), 35);
		drawingBoard.drawString("P3", (int)(halfScreenW/2), halfScreenH + 35);
		drawingBoard.drawString("P4", (int)(halfScreenW+(halfScreenW/2)), halfScreenH + 35);
	

		drawingBoard.drawString("Player 1 Name", (int)(margin+15), 135);
		drawingBoard.drawString("Player 2 Name", (int)(halfScreenW+margin+15), 135);
		drawingBoard.drawString("Player 3 Name", (int)(margin+15), halfScreenH + 135);
		drawingBoard.drawString("Player 4 Name", (int)(halfScreenW+margin+15), halfScreenH + 135);
		
		drawingBoard.setColor(Color.WHITE);

		drawingBoard.drawImage(character1, (int)((halfScreenW/2)-(25)), 60, 60, 60, null);
		drawingBoard.drawImage(character1, (int)((halfScreenW*1.5)-(25)), 60, 60, 60, null);
		drawingBoard.drawImage(character1, (int)((halfScreenW/2)-(25)), (int)(halfScreenH + 60), 60, 60, null);
		drawingBoard.drawImage(character1, (int)((halfScreenW*1.5)-(25)), (int)(halfScreenH + 60), 60, 60, null);

		if(readyUp){
			drawingBoard.setColor(Color.GREEN);
			drawingBoard.fillRect(margin, (int)(halfScreenH/2), (int)(halfScreenW-(margin*1.5)), 20);
			drawingBoard.setColor(Color.WHITE);

			drawingBoard.drawString("READY", (int)((halfScreenW/2)-10), (halfScreenH/2)+15);

		}
		
		
		drawingBoard.drawString("Press Enter to Select", (int)(halfScreenW-50), screenHeight-20);

	}
	public void keyPressed(int k){
		if(k==KeyEvent.VK_ENTER){
			if(readyUp){
				readyUp = false;

			}
			else{
				readyUp=true;
				//TODO disable character select
			}
//			asm.setState(AppStateManager.GAMESTATE);
		}
		if(k==KeyEvent.VK_ESCAPE){
			asm.setState(AppStateManager.LOBBYSTATE);
		}
	}
	public void keyReleased(int k){
		
	}
}
