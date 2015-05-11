package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import tileMap.TileMap;

public class NetPlayer extends MapObject{
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		2, 8, 1, 2, 4, 2, 5
	};
	
	// animation actions
	private static final int IDLE = 0;
//	private static final int WALKING = 1;
//	private static final int JUMPING = 2;
//	private static final int FALLING = 3;
//	private static final int GLIDING = 4;
//	private static final int FIREBALL = 5;
//	private static final int SCRATCHING = 6;
	
	private String username;
	
	public NetPlayer(TileMap tm, String username){
		super(tm);
		this.username = username;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		// load sprites
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass()
					.getResourceAsStream("/Sprites/Player/customplayersprite.gif")
			);
			sprites = new ArrayList<BufferedImage[]>();
			
			for(int i = 0; i < 7; i++) {
				
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
					System.out.println(width);
					if(i != 6) {
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
					}
					else {
						bi[j] = spritesheet.getSubimage(
								j * width * 2,
								i * height,
								width,
								height
						);
					}
					
				}
				
				sprites.add(bi);
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
	
	}
	public void update() {
		
//		setPosition(xtemp,ytemp);
		// update animation
		animation.update();
		
	}
	public void draw(Graphics2D g) {
		
		setMapPosition();
//			String coordinates = Integer.toString(this.getx()) + Integer.toString(this.gety());
			g.drawString(username, (float)x-20, (float)y-20);
//			g.drawString(coordinates, 50, 75);	

			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
			
		
	}
	public String getUsername(){
		return username;
	}
	
}
