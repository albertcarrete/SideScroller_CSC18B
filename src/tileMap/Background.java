package tileMap;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import layers.GeneralGraphicsLayer;

public class Background {
	
	private BufferedImage image;
//	private double moveScale;
	
	public Background(String s, double ms){
		try{
			image = ImageIO.read(getClass().getResourceAsStream(s));
//			moveScale = ms;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void draw (Graphics g){
		g.drawImage(image,0,0,GeneralGraphicsLayer.WIDTH,GeneralGraphicsLayer.HEIGHT,null);
	}
}
