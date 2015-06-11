package appstate;


import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Random;

import core.Passport;
import overlays.Debugger;
import socket.SocketController;
import entity.NetPlayer;
import entity.Player;
import entity.StatScreen;
import socket.SMSocket;
import tileMap.Background;
import tileMap.TileMap;
import layers.GeneralGraphicsLayer;

public class GameState extends AppState{
		
	/* CORE */
	private AppStateManager asm;
	private GeneralGraphicsLayer gLayer;
	private TileMap tileMap;
	private Player player;
	private Background bg;
//	private DebugOverlay debugOverlay;
	private Debugger debugger;
	/*MULTIPLAYER*/
	private SMSocket socket;
	
	private StatScreen statScreen;
	
	// Passport
	public String username;
	private String gameId;
	private ArrayList<NetPlayer> networkedPlayers;
	
	private String name;
	ListIterator<NetPlayer>it;
	Passport _p;
	Font standard;
	boolean deathScreen;
	long deathCounter;
	
	HashMap<Integer,int[]> safeSpawns;
	
	public GameState(AppStateManager asm, GeneralGraphicsLayer gLayer, Passport passport){
		this.asm = asm;
		this.gLayer = gLayer;
		this.username = gLayer.username;
		this._p = passport;
//		debugOverlay = new DebugOverlay();
		networkedPlayers = new ArrayList<NetPlayer>();
		it = networkedPlayers.listIterator();
					
		safeSpawns = new HashMap<Integer,int[]>();		
		safeSpawns.put(1, new int[]{300,230});
		safeSpawns.put(2, new int[]{50,280});
		safeSpawns.put(3, new int[]{550,280});
		safeSpawns.put(4, new int[]{550,400});
		safeSpawns.put(5, new int[]{50,400});
		safeSpawns.put(6, new int[]{50,490});
		safeSpawns.put(7, new int[]{540,490});

		
		
	}
	/* Init runs when this application state is set */
	public void init(){
		
		int style = Font.PLAIN;
		standard = new Font ("Arial", style , 12);
		
		username 	= _p.getUsername();
		gameId 		= _p.getGameId();
		
		deathScreen = false;
		deathCounter = 0;
		/* Build Map */
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/customtileset2.gif");
		tileMap.loadMap("/Maps/level1-2.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);

		bg = new Background("/Backgrounds/custombg.jpg", 0.1);

		/* Create a temporary identifier for this player */
//		Random rnd = new Random(System.currentTimeMillis());
//		int number = rnd.nextInt(900) + 100;
//		name = "anonymous" + Integer.toString(number);
		
		/* Position current player's pawn */
		debugger = new Debugger();
		statScreen = new StatScreen(StatScreen.NONE,gLayer);
		
		/*Attempt to connect to socket*/
		try{
			socket = new SMSocket(debugger,username,_p);
			socket.setGame(this);
	    	SocketController socketController = new SocketController(socket, this);
			socketController.linkUp();
			socket.userJoin(username,gameId);

		}catch(Exception e){
			e.printStackTrace();
		}


		player = new Player(tileMap,socket,username,debugger);
		player.setPosition(300, 125);
	};
	
	public void update(){
		/* HACK: If player is not setup yet, don't update */
		if(player != null){
			if(statScreen.getState() != StatScreen.DEATH){
				player.update();				
			}else{
				player.setPosition(80, 80);
				player.update();
				if(deathCounter == 500){
					
					Random random = new Random();
					int randomNumber = random.nextInt(safeSpawns.size() - 1) + 1;
					int spawn[] = new int[2];
					spawn = safeSpawns.get(randomNumber);
					
					player.setPosition(spawn[0],spawn[1]);
					deathCounter = 0;
					statScreen.setState(StatScreen.NONE);
					
				}
				deathCounter++;
			}
			debugger.sendToScreen("position", player.getx(),player.gety());
			debugger.sendToScreen("size", player.getWidth(),player.getHeight(),player.getCHeight(),player.getCHeight());
			tileMap.setPosition(
					(double)GeneralGraphicsLayer.WIDTH / 2 - player.getx(),
					(double)GeneralGraphicsLayer.HEIGHT / 2 - player.gety()
						);
			player.checkAttack(networkedPlayers);
			

		}
		for(int i = 0; i < networkedPlayers.size(); i++){
			networkedPlayers.get(i).update();
		}
		


		
		
		/* Check for networked players, then update them */
//			if(it.hasNext()){
//				it.next().update();
//			}			
		
//		if(networkedPlayers.size() > 0){
//			for(NetPlayer netPlayer:networkedPlayers){
//				netPlayer.update();
//			}
//		}
		
	};
	
	public void draw(java.awt.Graphics g){
		/*TODO: delete this function from abstract */
	};
	public void drawToScreen(java.awt.Graphics2D g){
		g.setFont(standard);
		bg.draw(g);
		tileMap.draw(g);
		if(statScreen.getState() != StatScreen.DEATH){
			player.draw(g);		
		}
		debugger.draw(g);
		statScreen.draw(g);
		
		if(networkedPlayers.size() > 0){
			
			for(int i = 0; i < networkedPlayers.size(); i++){
				networkedPlayers.get(i).draw(g);
			}
//			for(NetPlayer netPlayer:networkedPlayers){
//				netPlayer.draw(g);
//			}
		}
	};
	
	public void addNetworkedPlayer(String netName){
		
		NetPlayer netPlayer = new NetPlayer(tileMap,netName);
		netPlayer.setPosition(0,0);
		networkedPlayers.add(netPlayer);
		
	}
	public void setNetworkedPlayerPosition(int x, int y){
		networkedPlayers.get(0).setPosition(x,y);
	}
	
	/* Takes in a received player coordinate and applies it to the networkedPlayers
	 * array. If the player is not found in the existing networkedPlayers array it is
	 * added.
	 * */
	public void findAndUpdateNetPlayer(String uname, double x, double y, long t){
		
		// Locals
		String temp 		= uname;
		boolean matchFound 	= false;
		
		// Loop through networked players
		for(NetPlayer netPlayer:networkedPlayers){
			
//			System.out.println("Comparing " + netPlayer.getUsername() + " with " + this.username + ".");
			
			// If arrayList netPlayer name equals the parameter user name and isn't equal to the current users name
			if(netPlayer.getUsername().equals(temp) && !netPlayer.getUsername().equals(this.username)){
//				System.out.println("Updating " + netPlayer.getUsername() + " coordinates");
//				System.out.println("Player found and updating!");
				if(!netPlayer.currentValueSet){
					netPlayer.setCurrentPosition(x,y,t);
				}else{
					netPlayer.setCurrentToPrev();
					netPlayer.setCurrentPosition(x,y,t);

//					netPlayer.setPosition(x,y);
				}
				matchFound = true;
			}
		}
		// If networked player was not found, add him
		if(matchFound == false){
			addNetworkedPlayer(temp);
		}
		
	}
	
	public void findAndUpdateNetPlayerProjectile(String username,String projID, double x, double y, long t,boolean facingRight){
		for(NetPlayer netPlayer:networkedPlayers){
			
//			System.out.println("Comparing " + netPlayer.getUsername() + " with " + this.username + ".");
			
			// If arrayList netPlayer name equals the parameter user name and isn't equal to the current users name
			if(netPlayer.getUsername().equals(username) && !netPlayer.getUsername().equals(this.username)){
				netPlayer.setFacingRight(facingRight);
				netPlayer.triggerFireBall(x, y);
			}
		}
	}
	
	public void findAndKillPlayer(String uname){
		
		if(uname.equals(this.username)){
			statScreen.setState(StatScreen.DEATH);
		}else{
			// Locals
			String temp 		= uname;
			boolean matchFound 	= false;
			
			// Loop through networked players
			outerloop:
			for(NetPlayer netPlayer:networkedPlayers){
				
//				System.out.println("Comparing " + netPlayer.getUsername() + " with " + this.username + ".");
				
				// If arrayList netPlayer name equals the parameter user name and isn't equal to the current users name
				if(netPlayer.getUsername().equals(temp) && !netPlayer.getUsername().equals(this.username)){
//					System.out.println("Updating " + netPlayer.getUsername() + " coordinates");
//					System.out.println("Player found and updating!");
					netPlayer.setPosition(80, 80);
					matchFound = true;
				}
				
				if(matchFound){
					break outerloop;
				}
			}	

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
