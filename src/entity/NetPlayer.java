package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tileMap.TileMap;

import org.joda.time.DateTime;

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
	public boolean previousValueSet;
	public boolean currentValueSet;
	
	private ArrayList<NetFireBall> fireBalls;
	
	double xP;
	double yP;
	long tP;
	
	double xC;
	double yC;
	long tC;
	
	boolean freshCoordinates;
	DateTime dateTime;
	
	boolean toggleTimer1;
	boolean toggleTimer2;
	
	long timer1;
	long timer2;
	
	long deltaTimer;
	long deltaUpdateMilli;
	
	double totalTime;
	
	double deltaX;
	double deltaY;
	
	double timeToTween;
	
	double totalX; 
	double totalY;
	
	double tweenX;
	double tweenY;
	
	boolean firing;

	double xFiredBall;
	double yFiredBall;
	
	public NetPlayer(TileMap tm, String username){
		super(tm);
		this.username = username;
		previousValueSet = false;
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		xP = 0;
		yP = 0;
		tP = 0;
		
		freshCoordinates = false;
		toggleTimer1 = true;
		toggleTimer2 = false;
		deltaTimer = 0;
		
		deltaX = 0;
		deltaY = 0;
		
		tweenX = 0;
		tweenY = 0;
		
		totalTime = 0;
		
		dateTime = new DateTime();
		
		firing = false;
		fireBalls = new ArrayList<NetFireBall>();
		
		xFiredBall = 0;
		yFiredBall = 0;
		
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
	public void triggerFireBall(double x, double y){
		firing = true;
		xFiredBall = x;
		yFiredBall = y;
	}
	
	
	public void update() {
		
		// Timer Toggle
		if(toggleTimer1){
			timer1 = System.nanoTime();		
		}else{
			timer2 = System.nanoTime();		
		}

		
		if(timer1 > 0 && timer2 > 0){
			if(toggleTimer1){
				// 1. Calculate time since last update
				deltaTimer = timer1 - timer2;		
				deltaUpdateMilli = deltaTimer / 1000000;
			}else{
				// 2. Calculate time since last update
				deltaTimer = timer2 - timer1;
				deltaUpdateMilli = deltaTimer / 1000000;
			}
		}
		
		// New Set of Coordinates Received
		if(freshCoordinates){
//			System.out.println("Time to tween: " + tC + " - " + tP);
			
			// Set Amount time this coordinate took to play out
			timeToTween 	= tC - tP;
			// Difference between X of current and previous position
			deltaX 			= xC - xP;
			// Difference between Y of current and previous position
			deltaY 			= yC - yP;
			
			// Set to false in wait for new coordinates
			freshCoordinates = false;
			
			totalX = 0;
			totalY = 0;
			totalTime = 0;
		
			
		}	

		if(timeToTween > 0 && deltaUpdateMilli > 0){
//			System.out.println("Time to tween: " + timeToTween);
			
			float movementScale = (float)(deltaUpdateMilli) / (float)(timeToTween);
			totalTime += deltaUpdateMilli;
//			System.out.println(movementScale*100 + " percentage time left");			
			double movementOnThisUpdateX = deltaX * (double)movementScale;
			double movementOnThisUpdateY = deltaY * (double)movementScale;
			
//			System.out.println("Movement this update: " + movementOnThisUpdate);
			if(Math.abs(deltaX) > Math.abs(totalX)){
				
				if(movementOnThisUpdateX > 0){
//					System.out.println("POSITIVE MOVEMENT");
					totalX += movementOnThisUpdateX;	
				}
				if(movementOnThisUpdateX < 0){
//					System.out.println("NEGATIVE MOVEMENT");
					totalX += movementOnThisUpdateX;
				}
				
//				setPosition(xP,yP);

			}else{
//				System.out.println("Waiting for new coordinates!");
			}
			
			if(Math.abs(deltaY) > Math.abs(totalY)){
				if(movementOnThisUpdateY > 0){
//					System.out.println("POSITIVE MOVEMENT");
					totalY += movementOnThisUpdateY;	
				}
				if(movementOnThisUpdateY < 0){
//					System.out.println("NEGATIVE MOVEMENT");
					totalY += movementOnThisUpdateY;
				}
			}else{
				
			}

			setPosition(xP + totalX, yP + totalY);					

//			if(totalX > 0){
//				setPosition(xP + totalX,yP);					
//			}else{
//				setPosition(xP + totalX,yP);
//			}
		}
		
		// Fireballs
		if(firing){
			NetFireBall fb = new NetFireBall(tileMap,facingRight);
			fb.setPosition(xFiredBall,yFiredBall);
			fireBalls.add(fb);
			firing = false;
		}
		
		// update fireballs
		for(int i = 0; i < fireBalls.size(); i++){
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()){
				fireBalls.remove(i);
				i--;
			}
		}
		
		
		
//		setPosition(xtemp,ytemp);
//		 update animation
//		animation.update();

		
		if(toggleTimer1){
			toggleTimer1 = false;
		}else{
			toggleTimer1 = true;
		}
		
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
			for(int i = 0; i < fireBalls.size(); i++){
				fireBalls.get(i).draw(g);
			}
		
	}
	public String getUsername(){
		return username;
	}
	
	public void setPreviousPosition(double x, double y, long t){
		
//		previousValueSet = true;
		xP = tweenX = x;
		yP = tweenY = y;
		tP = t;
				
	}	
	
	public void setCurrentPosition(double x, double y, long t){
		System.out.println("Setting current position : " + t + " to " + tC);
		currentValueSet = true;
		xC = x;
		yC = y;
		tC = t;
	}
	
	public void setCurrentToPrev(){	
		
		freshCoordinates = true;
		
		setPosition(xC,yC);
		
		xP = xC;
		yP = yC;
		tP = tC;
		System.out.println("Time Delta: " + tP + " - " + tC);

	}	
	
	public void setFacingRight(boolean f){
		facingRight = f;
	}
}
