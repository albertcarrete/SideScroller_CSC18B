package appstate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import core.Passport;
import entity.NetPlayer;
import overlays.Debugger;
import socket.SMSocket;
import socket.SocketController;
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

	private final String USER_AGENT = "Mozilla/5.0";

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
	
	private SMSocket socket;
	private Debugger debugger;
	
	// Passport data
	public String username;
	public String gameId;
	public boolean userReady;
	public boolean allReady;
	
	
	private boolean checkReady;
	private boolean allowProgressToGame;
	private String playerNameArray[];
	private boolean playerReadyArray[];
	Font big;
	Passport _p;
	public CharacterSelectState(AppStateManager a, GeneralGraphicsLayer l,Passport passport){
		
		System.out.println("Character Select State instantiated");
		this.asm = a;
		this.layer = l;
		this._p = passport;
		
		screenHeight = GeneralGraphicsLayer.HEIGHT;
		screenWidth = GeneralGraphicsLayer.WIDTH;
		halfScreenW = (int)screenWidth/2;
		halfScreenH = (int)((screenHeight/2)-20);

		margin = 15;		
		selectBoxW = (int)(halfScreenW-(margin*1.5));
		selectBoxH = (int)(halfScreenH-(margin));
		
		int style = Font.BOLD | Font.ITALIC;
		big = new Font ("Arial", style , 25);
		
		bgColor = new Color(63,60,70);
		bgCharacter = new Color(119,110,128);
		
		Random rnd = new Random(System.currentTimeMillis());
		int number = rnd.nextInt(900) + 100;

		readyUp = false;
		debugger = new Debugger();		
		
		playerNameArray = new String[4];
		playerNameArray[0] = "Player1 Name";
		playerNameArray[1] = "Player2 Name";
		playerNameArray[2] = "Player3 Name";
		playerNameArray[3] = "Player4 Name";
		
		playerReadyArray = new boolean[4];
		playerReadyArray[0] = false;
		playerReadyArray[1] = false;
		playerReadyArray[2] = false;
		playerReadyArray[3] = false;
		
		allReady = false;
		checkReady = false;
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
		username 	= layer.getCurrentPlayerID();
		gameId 		= layer.getCurrentGameID(); 
		currentChoice = 0;
		
		/* Attempt to connect to socket */
		try{
			socket = new SMSocket(debugger,username,_p);
			socket.setCharacterSelect(this);
	    	SocketController socketController = new SocketController(socket, this);
			socketController.linkUpCSS();
			
			JSONObject obj = new JSONObject();
			obj.put("username", username);
			obj.put("id",layer.getCurrentGameID());
			
			// request to join a private room
			socket.requestPrivateRoom(obj);
//			socket.joinPrivateRoom(layer.getCurrentGameID());
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void update(){
		
	}
	public void draw(java.awt.Graphics g){
		
	}
	public void setPlayerPropeties(int position){
		
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
	
		// Player Names
		drawingBoard.drawString(playerNameArray[0], (int)(margin+15), 135);
		drawingBoard.drawString(playerNameArray[1], (int)(halfScreenW+margin+15), 135);
		drawingBoard.drawString(playerNameArray[2], (int)(margin+15), halfScreenH + 135);
		drawingBoard.drawString(playerNameArray[3], (int)(halfScreenW+margin+15), halfScreenH + 135);
		
		drawingBoard.setColor(Color.WHITE);

		drawingBoard.drawImage(character1, (int)((halfScreenW/2)-(25)), 60, 60, 60, null);
		drawingBoard.drawImage(character1, (int)((halfScreenW*1.5)-(25)), 60, 60, 60, null);
		drawingBoard.drawImage(character1, (int)((halfScreenW/2)-(25)), (int)(halfScreenH + 60), 60, 60, null);
		drawingBoard.drawImage(character1, (int)((halfScreenW*1.5)-(25)), (int)(halfScreenH + 60), 60, 60, null);
		
		checkReady = true;
		
		if(playerReadyArray[0]){
			drawingBoard.setColor(Color.GREEN);
			drawingBoard.fillRect(margin, (int)(halfScreenH/2), (int)(halfScreenW-(margin*1.5)), 20);
			drawingBoard.setColor(Color.WHITE);
			drawingBoard.drawString("READY", (int)((halfScreenW/2)-10), (halfScreenH/2)+15);
		}else{
			checkReady = false;
		}
		if(playerReadyArray[1]){
			drawingBoard.setColor(Color.GREEN);
			drawingBoard.fillRect((int)(halfScreenW + (margin*0.5)), (int)(halfScreenH/2), (int)(halfScreenW-(margin*1.5)), 20);
			drawingBoard.setColor(Color.WHITE);
			drawingBoard.drawString("READY", (int)((halfScreenW + (halfScreenW/2)-10)), (halfScreenH/2)+15);
		}else{
			checkReady = false;
		}
		if(playerReadyArray[2]){	
			drawingBoard.setColor(Color.GREEN);
			drawingBoard.fillRect(margin, (int)((halfScreenH)+(halfScreenH/2 + (margin*0.5))), (int)(halfScreenW-(margin*1.5)), 20);
			drawingBoard.setColor(Color.WHITE);
			drawingBoard.drawString("READY", (int)((halfScreenW/2)-10), (int)(((halfScreenH/2)+15)+halfScreenH+margin*0.5));
		}else{
			checkReady = false;
		}
		if(playerReadyArray[3]){
			drawingBoard.setColor(Color.GREEN);
			drawingBoard.fillRect((int)(halfScreenW + (margin*0.5)), (int)((halfScreenH)+(halfScreenH/2 + (margin*0.5))), (int)(halfScreenW-(margin*1.5)), 20);
			drawingBoard.setColor(Color.WHITE);
			drawingBoard.drawString("READY", (int)((halfScreenW + (margin*0.5))+((halfScreenW/2)-20)), (int)(((halfScreenH/2)+15)+halfScreenH+margin*0.5));
		}else{
			checkReady = false;
		}
		
		if(checkReady){
			allReady = true;
		}else{
			allReady = false;
		}
		drawingBoard.drawString("Press Enter to Select", (int)(halfScreenW-50), screenHeight-20);

		if(_p.getPosition() == 0 && allReady == true){
			allowProgressToGame = true;
			drawingBoard.setFont(big);
			drawingBoard.drawString("Press S to Start Match", (int)(halfScreenW-80), halfScreenH);
			
		}

	
		
		

	}
	public void startGame(){
		asm.setState(AppStateManager.GAMESTATE);
	}
	public void keyPressed(int k){
		if(k==KeyEvent.VK_ENTER){
			if(readyUp){
				readyUp = false;
				userReady = false;
				socket.messagePrivateRoom(layer.getCurrentGameID(), "Player is unready!");
//				sendLobbySync();
			}
			else{
				readyUp=true;
				userReady = true;
				socket.messagePrivateRoom(layer.getCurrentGameID(), "Player is ready!");

				//TODO disable character select
				
			}
//			asm.setState(AppStateManager.GAMESTATE);
		}
		if(k==KeyEvent.VK_ESCAPE){
			asm.setState(AppStateManager.LOBBYSTATE);
		}
		if(k==KeyEvent.VK_S){
			if(allowProgressToGame){
				socket.allPlayersStart();
			}
		}
	}
	public void keyReleased(int k){
		
	}
	public void syncCharacterSelect(String username, int pos, boolean ready){
		
		System.out.println("[NEW SYNC] -  at " + pos + " by " + username + " status " + ready);
		playerNameArray[pos] = username;
		playerReadyArray[pos] = ready;

	}
	public void getGameById(String id) throws Exception{
		
		String url = "http://localhost:8080/api/games/id/" + id;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		// optional default is GET
		con.setRequestMethod("GET");
		
		// add request header
		con.setRequestProperty("User-Agent",USER_AGENT);
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while((inputLine = in.readLine()) != null){
			response.append(inputLine);
		}
		in.close();
		

  		// ORG.JSON
		JSONObject responseObject = new JSONObject(response.toString());
		
		System.out.println("Response Object =====");
		System.out.println(responseObject);
  		
		JSONArray players = (JSONArray)responseObject.get("players");
		
		for(int i = 0; i < players.length(); i++){
			JSONObject playerObject = players.getJSONObject(i);
			String uname = (String)playerObject.get("username");
			 System.out.println(uname);
			// if there is a player in this position
			if(uname != ""){
				// set the player to its proper position
				playerNameArray[i] = uname;
			}			
		}
		//print result
//		layer.removeLoginLayer();

		
	}
	
	
}
