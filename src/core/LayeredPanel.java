package core;

import handlers.Resolution;
import handlers.ResolutionHandler;
import handlers.VEIN;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

import layers.*;
import overlays.SettingsOverlay;
import serializable.ReadSettingsFile;

/* LayeredPanel
 * This JPanel class is the root panel that all the other app panels connect 
 * to and display information. 
 */
public class LayeredPanel extends JPanel implements Runnable, KeyListener{
	
	VEIN _v;
	JFrame parent;

	public static final int WIDTH = 600; 	// 600 // 512  // 480
	public static final int HEIGHT = 350; 	// 450 // 448  // 270
	public static final int SCALE = 2; 		// Master Scale for Container

	public int settingsWIDTH;
	public int settingsHEIGHT;
	
	private JLayeredPane JLP;
	
	private Thread thread; 
	private BufferedImage image;
	private Graphics2D g;
	
	private int screenW;
	private int screenH;
	GeneralGraphicsLayer gLayer;
	GeneralLayer loginLayer;
	RegisterLayer registerLayer; 
	SettingsOverlay settingsOverlay;
	
	boolean loginOpen = false;
	boolean overlay = false;
	Point origin;
	private int suboption[];

	public LayeredPanel(JFrame parent){
		
		super();
//		setDoubleBuffered(true);
		_v = new VEIN();
		_v.extendReach(this);
		this.parent = parent;
		
		/*Determine screen size*/
		suboption = new int[4];
		ReadSettingsFile readSettings = new ReadSettingsFile();
		readSettings.openFile();
		readSettings.readSettings(suboption);
		readSettings.closeFile();
		
		ResolutionHandler resolutionHandler = new ResolutionHandler();
		Resolution settingsRes = resolutionHandler.getResolutions(suboption[0]);
		setHeight(settingsRes.getHeight());
		setWidth(settingsRes.getWidth());
			
		
        setBorder(BorderFactory.createLineBorder(Color.RED));

		/* Root Panel Setting */
//        setLayout(null);
//        setLayout(new FlowLayout());
//        setLayout(new BorderLayout());
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(screenW, screenH));
//        setBounds(0,0,screenW, screenH);
		setFocusable(true);
		requestFocus();
		
		/* Layered Panel Setting */
		JLP = new JLayeredPane();
		JLP.setPreferredSize(new Dimension(screenW, screenH));
//        JLP.setBounds(0,0,screenW, screenH);

		System.out.println("Screen width: " + screenW + " and screen height: " + screenH);
		
		add(JLP);
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void init(){
						
		/* Graphics Window Layer */
		gLayer = new GeneralGraphicsLayer(this,_v);
		JLP.add(gLayer, new Integer(0));
		
//		GeneralLayer layer2 = new GeneralLayer(origin);
//		JLP.add(layer2, new Integer(2));
		
	}
	
	public void run(){
		init();
	}
	
	public void buildLoginLayer(){
        Point origin = new Point(600, 350);
        
        if(!loginOpen){
			System.out.println("Attempting to build login layer");
			loginLayer = new GeneralLayer(origin,this);
			JLP.add(loginLayer, new Integer(1));
			repaint();
			loginOpen = true;
        }else{
        	removeLoginLayer();
        }
	}
	//minaws
	public void removeLoginLayer(){

		JLP.remove(loginLayer);
		repaint();
		loginOpen = false;
		gLayer.grantAccess();
	}
	
	public void buildRegisterLayer(){
        Point origin = new Point(150, 100);
        if(!loginOpen){
			System.out.println("Attempting to build register layer");
			registerLayer = new RegisterLayer(origin,this);
			JLP.add(registerLayer, new Integer(2));
			repaint();
			loginOpen = true;
        }else{
        	removeLoginLayer();
        }
	}
	
	public void buildSettingsLayer(){
		if(!overlay){
			System.out.println("Attempting to build settings layer");
			settingsOverlay = new SettingsOverlay(this);
			JLP.add(settingsOverlay, new Integer(2));
			repaint();
			overlay = true;
		}else{
			removeOverlay();
		}
	}
	public void removeOverlay(){
		JLP.remove(settingsOverlay);
	}
	public void keyTyped(KeyEvent key){
		
	}
	public int getHeight(){
		return screenH;
	}
	public int getWidth(){
		return screenW;
		
	}
	public void setHeight(int h){
		screenH = h;
	}
	public void setWidth(int w){
		screenW = w;
	}
	public void keyPressed(KeyEvent key){
		gLayer.keyPressed(key.getKeyCode());
//		gsm.keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key){
		gLayer.keyReleased(key.getKeyCode());
	}
	
	public void applySettings(){
		System.out.println("Refreshed from resolution: " + screenW + screenH);

		//TODO consolidate this with its first call in the constructor
		ReadSettingsFile readSettings = new ReadSettingsFile();
		readSettings.openFile();
		readSettings.readSettings(suboption);
		readSettings.closeFile();
		
		ResolutionHandler resolutionHandler = new ResolutionHandler();
		Resolution settingsRes = resolutionHandler.getResolutions(suboption[0]);
		setHeight(settingsRes.getHeight());
		setWidth(settingsRes.getWidth());
		
		System.out.println("Refreshed to resolution: " + screenW + screenH);
//		setBounds(0,0,screenW,screenH);
//		JLP.setBounds(0,0,screenW,screenH);

//		setLayout(null);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(screenW, screenH));
        JLP.setPreferredSize(new Dimension(screenW, screenH));

        parent.pack();

//        revalidate();
	}
}
