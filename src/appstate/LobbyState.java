package appstate;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import layers.GeneralGraphicsLayer;

public class LobbyState extends AppState{
	
	private Color titleColor;
	private Font titleFont;
	private AppStateManager asm;
	private GeneralGraphicsLayer layer;
	private int titleY;
	
	private BufferedImage bg;
	
	public LobbyState(AppStateManager asm, GeneralGraphicsLayer layer){
		
		System.out.println("Lobby state instantiated!");
		this.asm = asm;
		this.layer = layer;
		
		try{
			titleColor = new Color(128,0,0);
			titleFont = new Font("Century Gothic",Font.PLAIN, 40);
			bg = ImageIO.read(getClass().getResourceAsStream("/msbg.gif"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		titleY = 1;

	}
	public void init(){
		titleY = 0;
		layer.username = "";
	};
	public void update(){
//		System.out.println("Updating...");
		titleY++;

	};
	public void draw(java.awt.Graphics g){
		
	};
	public void drawToScreen(java.awt.Graphics2D g){
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, GeneralGraphicsLayer.WIDTH, GeneralGraphicsLayer.HEIGHT);
        g.drawImage(bg, 0,0,GeneralGraphicsLayer.WIDTH, GeneralGraphicsLayer.HEIGHT,null);

		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString(layer.username, 150, titleY);	
		
	}
	public void keyPressed(int k){
		if(k==KeyEvent.VK_ENTER){
			System.out.println("PRESSED THE ENTER KEY!!!");
			asm.setState(AppStateManager.GAMESTATE);
		}
		if(k==KeyEvent.VK_M){
			layer.username += "M";
		}
		if(k==KeyEvent.VK_L){
			layer.username += "L";
		}
	};
	public void keyReleased(int k){
		
	};
}
