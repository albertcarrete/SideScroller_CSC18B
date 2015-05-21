package appstate;


import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import overlays.Debugger;
import socket.SocketController;
import entity.NetPlayer;
import entity.Player;
import socket.SMSocket;
import tileMap.Background;
import tileMap.TileMap;
import layers.GeneralGraphicsLayer;

public class GameState extends AppState{
		
	/* CORE */
	private AppStateManager asm;
//	private GeneralGraphicsLayer layer;
	private TileMap tileMap;
	private Player player;
	private Background bg;
//	private DebugOverlay debugOverlay;
	private Debugger debugger;
	/*MULTIPLAYER*/
	private SMSocket socket;
	public String username;
	private ArrayList<NetPlayer> networkedPlayers;
	
	private String name;

	public GameState(AppStateManager asm, GeneralGraphicsLayer layer){
				
		this.asm = asm;
//		this.layer = layer;
		this.username = layer.username;
//		debugOverlay = new DebugOverlay();
		networkedPlayers = new ArrayList<NetPlayer>();

	}
	/* Init runs when this application state is set */
	public void init(){
		/* Build Map */
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/customtileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		bg = new Background("/Backgrounds/custombg.jpg", 0.1);

		/* Create a temporary identifier for this player */
		Random rnd = new Random(System.currentTimeMillis());
		int number = rnd.nextInt(900) + 100;
		name = "anonymous" + Integer.toString(number);
		/* Position current player's pawn */
		debugger = new Debugger();
		
		/*Attempt to connect to socket*/
		try{
			socket = new SMSocket(debugger,name);
			socket.setGame(this);
	    	SocketController socketController = new SocketController(socket, this);
			socketController.linkUp();
			socket.userJoin(name);

		}catch(Exception e){
			e.printStackTrace();
		}


		player = new Player(tileMap,socket,name,debugger);
		player.setPosition(100, 100);
	};
	
	public void update(){
		/* HACK: If player is not setup yet, don't update */
		if(player != null){
			player.update();
			debugger.sendToScreen("position", player.getx(),player.gety());
			debugger.sendToScreen("size", player.getWidth(),player.getHeight(),player.getCHeight(),player.getCHeight());
		}
		/* Check for networked players, then update them */
		if(networkedPlayers.size() > 0){
			for(NetPlayer netPlayer:networkedPlayers){
				netPlayer.update();
			}
		}
		
	};
	
	public void draw(java.awt.Graphics g){
		/*TODO: delete this function from abstract */
	};
	public void drawToScreen(java.awt.Graphics2D g){
		
		bg.draw(g);
		tileMap.draw(g);
		player.draw(g);
		debugger.draw(g);
		if(networkedPlayers.size() > 0){
			for(NetPlayer netPlayer:networkedPlayers){
				netPlayer.draw(g);
			}
		}
		
	};
	
	public void addNetworkedPlayer(String username){
		NetPlayer netPlayer = new NetPlayer(tileMap,username);
		netPlayer.setPosition(0,0);
		networkedPlayers.add(netPlayer);
	}
	public void setNetworkedPlayerPosition(int x, int y){
		networkedPlayers.get(0).setPosition(x,y);
	}
	
	public void findAndUpdateNetPlayer(String uName, double x, double y){
		
		String temp = uName;
		boolean matchFound = false;
		for(NetPlayer netPlayer:networkedPlayers){
			
			System.out.println("Comparing " + netPlayer.getUsername() + " with " + this.name + ".");
			
			if(netPlayer.getUsername().equals(temp) && netPlayer.getUsername() != this.name){
				System.out.println("Updating " + netPlayer.getUsername() + " coordinates");
//				System.out.println("Player found and updating!");
				netPlayer.setPosition(x,y);
				matchFound = true;
			}
		}
		if(matchFound == false){
			addNetworkedPlayer(temp);
		}
	}
	
	
	
	public void keyPressed(int k){
		if(k==KeyEvent.VK_ENTER){
			asm.setState(AppStateManager.MENUSTATE);
		}
		if(k == KeyEvent.VK_LEFT){
			player.setLeft(true);
		}
		if(k == KeyEvent.VK_RIGHT){
			player.setRight(true);
		}
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_W) player.setJumping(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setScratching();
		if(k == KeyEvent.VK_F) player.setFiring();
	};
	public void keyReleased(int k){
		if(k == KeyEvent.VK_LEFT){
			player.setLeft(false);
		}
		if(k == KeyEvent.VK_RIGHT){
			player.setRight(false);
		}
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);		
	};
}
