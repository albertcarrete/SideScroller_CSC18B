package layers;

import handlers.VEIN;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import core.LayeredPanel;
import socket.SMSocket;
import appstate.AppStateManager;


/* 
 * The general graphics layer acts as a state
 * for which items we want to display on the 
 * screen. For instance, in the menu state we
 * will display the menu bg and any ui elements.
 * If we are in the game state then we will display
 * the ui for the game and the game elements. 
 * 
 * */
public class GeneralGraphicsLayer extends JPanel implements Runnable {
	
	VEIN _v;
	public static final int WIDTH = 600;  // 320
	public static final int HEIGHT = 330; // 240
	public static final int SCALE = 1; // Master Scale for Graphics
	
	public int screenWidth;
	public int screenHeight;
	public int screenWidthConstraint;
	
	private Color titleColor;
	private Font titleFont;
	private Graphics2D g;
	private BufferedImage drawing;
	private BufferedImage image;
	private BufferedImage image2;
	private Thread thread;
	private AppStateManager asm;
	private boolean running;
	
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	private SMSocket socket;

	public String username;
	LayeredPanel root;
	
	public GeneralGraphicsLayer(LayeredPanel root, VEIN v){	
		super();
		_v = v;
		_v.extendReach(this);
        this.root = _v.getRootLayer();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth 	= (int)(screenSize.getWidth());
		screenHeight 	= (int)(screenSize.getHeight() - 50);
		
		System.out.println("Screen width: " + screenWidth + " and screen height: " + screenHeight);

		float scaled = (float)screenHeight / (float)HEIGHT;
		float scaledWidth = (float)WIDTH * scaled;
		screenWidthConstraint = (int)scaledWidth;
		
		float getMargins = (screenWidth - screenWidthConstraint) / 2;
		int marginsNormalized = (int)getMargins;
		
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setFocusable(false);
        setBorder(BorderFactory.createLineBorder(Color.BLUE));

		setBounds(0, 0, screenWidth, screenHeight);
		asm = new AppStateManager(this);
        drawing = GraphicsEnvironment.getLocalGraphicsEnvironment()
        		.getDefaultScreenDevice().getDefaultConfiguration()
                .createCompatibleImage(WIDTH, HEIGHT); // it will stretch if you don't multiply this by scale.
                
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private void init(){		
		running = true;

//		g = (Graphics2D) this.getGraphics();
//		image = new BufferedImage(WIDTH*SCALE,HEIGHT*SCALE,BufferedImage.TYPE_INT_RGB);
		
//		titleColor = new Color(44,44,44);
//		titleFont = new Font("Century Gothic",Font.PLAIN,40);
//		g.setColor(titleColor);
//		g.setFont(titleFont);
//		g.drawString("SIDESCROLLER", 150, 50);
	
//		image = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
//		g = (Graphics2D) image.getGraphics();
//		g.drawString("General Graphics Layer", 10, 20);		
		
	}
	public void run(){
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		/* Game loop */
		while(running){
			start = System.nanoTime();
			
			update();			
		
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0){
				wait = 5;
			}
			try{
				Thread.sleep(wait);
			}catch(Exception e){
				e.printStackTrace();
			}
			repaint();
		}
//		draw();
//		drawToScreen();
//		g.drawString("Hello World", 10, 40);
//		Graphics g2 = this.getGraphics();
//		g2.drawImage(image, 0, 0, WIDTH, HEIGHT, this);
//		g2.drawString("General Graphics Layer Graphics Layer", 10, 40);
//	
//		Graphics g = this.getGraphics();
//		g.drawImage(image, 0, 0, this);
	}
	/* All drawing is done in this component */
	@Override public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D drawingBoard = drawing.createGraphics();
		asm.drawToScreen(drawingBoard);
		drawingBoard.dispose();
		
        Graphics tempGraphics = g.create();
        tempGraphics.drawImage(drawing, 0, 0, screenWidthConstraint, screenHeight, null);
        tempGraphics.dispose();
	}
	
	public void update(){
		asm.update();
	}
	public void draw(){
//		asm.draw(g);
	}
	public void drawToScreen(){
//		Graphics g2 = getGraphics();
//		g2.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
//		Graphics g2 = getGraphics();
//		g2.drawImage(image,0,0,WIDTH, HEIGHT, null);
//		Graphics g2 = getGraphics();
//		g2.drawImage(image,0,0,WIDTH * SCALE, HEIGHT * SCALE, null);
//		g2.dispose();
	}
	
	// TODO Consolidate these functions into something simpler
	public void triggerLoginOverlay(){
		root.buildLoginLayer();
	}
	public void triggerRegisterOverlay(){
		root.buildRegisterLayer();
	}
	public void triggerSettingsOverlay(){
		root.buildSettingsLayer();
	}
	public void grantAccess(){
        asm.setState(1);
	}
	public void keyTyped(KeyEvent key){
		
	}
	public void keyPressed(int k){		
		asm.keyPressed(k);
	}
	public void keyReleased(int k){
		asm.keyReleased(k);
	}
	public void updateMessages(String message){
		System.out.println(message);
	}
	
}
