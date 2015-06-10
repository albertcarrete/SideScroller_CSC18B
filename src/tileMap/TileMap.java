package tileMap;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

import layers.GeneralGraphicsLayer;
import core.LayeredPanel;


public class TileMap {
	
	/*position (may not be needed) */
	private double x;
	private double y;
	
	/*bounds*/
	private int ymin;
	private int ymax;
	private int xmin;
	private int xmax;
	
	private double tween;

	/*tileset*/
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	/*properties */
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	
	/*drawing*/
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize){
		this.tileSize = tileSize;
		// 600 / 30 = 20
		numRowsToDraw = GeneralGraphicsLayer.HEIGHT / tileSize; // + 2 ??
		// 450 / 30 = 15
		numColsToDraw = GeneralGraphicsLayer.WIDTH / tileSize; // + 2 ??
		
//		System.out.println("Rows: " + numRowsToDraw);
//		System.out.println("Cols: " + numColsToDraw);
		tween = 0.07;
	}
	
	public void loadTiles(String s){
		
		try{
			
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			// Our image size / amount of tiles
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			BufferedImage subimage;
			
			for(int col = 0; col < numTilesAcross; col++){
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				
				subimage = tileset.getSubimage(col*tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void loadMap(String s){
		
		try{
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map 	= new int[numRows][numCols];
			width	= numCols * tileSize;
			height	= numRows * tileSize;
			
			System.out.println("Number cols: " + numCols + " Number rows " + numRows);
			
			
			// set bounds
			xmin = GeneralGraphicsLayer.WIDTH - width;
			xmax = 0;
			ymin = GeneralGraphicsLayer.HEIGHT - height;
			ymax = 0;
//			
			String delims = "\\s+";
			
			// Loop through MAP data
			for(int row = 0; row < numRows; row++){
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++){
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public int getTileSize(){return tileSize;}
	public int getx(){return (int)x;}
	public int gety(){return (int)y;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	
	/* Return the type at a given location on the tile map*/
	public int getType( int row, int col){
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	public void setTween(double d) { tween = d; }

	public void setPosition(double x, double y) {
		
//		System.out.println(this.x);
//		System.out.println((x - this.x) * tween);
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
//		System.out.println(this.x + "\n==========");
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
		
	}
	private void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void draw(Graphics2D g){
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++){
			
			if(row >= numRows) break;
			
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize,(int)y + row * tileSize, null);
				
			}			
		}
	}
	
}
