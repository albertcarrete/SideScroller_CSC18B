package appstate;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.simple.*;
//import org.json.simple.parser.JSONParser;


import layers.GeneralGraphicsLayer;

public class LobbyState extends AppState{
	
	private Color titleColor;
	private Font titleFont;
	private AppStateManager asm;
	private GeneralGraphicsLayer layer;
	private int titleY;
	
	private BufferedImage bg;
	private final String USER_AGENT = "Mozilla/5.0";
	private boolean overlayTriggered;
	
	public LobbyState(AppStateManager asm, GeneralGraphicsLayer layer){
		
		System.out.println("Lobby state instantiated!");
		this.asm = asm;
		this.layer = layer;
		overlayTriggered = false;

		try{
			titleColor = new Color(128,0,0);
			titleFont = new Font("Century Gothic",Font.PLAIN, 40);
//			bg = ImageIO.read(getClass().getResourceAsStream("/msbg.gif"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		titleY = 1;

	}
	public void init(){
		titleY = 0;
		layer.username = "";

		if(layer != null && overlayTriggered != true){
			layer.triggerLobbyOverlay();
			overlayTriggered = true;
		}
		
		
	};
	public void update(){
		titleY++;

	};
	public void draw(java.awt.Graphics g){
		
	};
	public void drawToScreen(java.awt.Graphics2D g){
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,GeneralGraphicsLayer.WIDTH, GeneralGraphicsLayer.HEIGHT);
        g.drawImage(bg,0,0,GeneralGraphicsLayer.WIDTH, GeneralGraphicsLayer.HEIGHT,null);
        

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
	
	
	/* TODO: This was moved to LobbyLayer, remove from this class*/
	private void sendGet() throws Exception{
		
		String url = "http://localhost:8080/api/lobbies";
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
		
		System.out.println(response.toString());

		JSONArray json = new JSONArray(response.toString());
		
		for(int i = 0; i < json.length(); i++){
			JSONObject jObj = json.getJSONObject(i);
			String title = jObj.getString("title");
			String numPlayers = jObj.getString("numPlayers");
			String map = jObj.getString("map");
			
			System.out.println("title: " + title + " | numPlayers: " + numPlayers + " | map: " + map);
		}
		
		
//		JSONObject completeObject = new JSONObject(response); 
//		System.out.println(completeObject.length());
		
		
//		JSONParser parser = new JSONParser();
//		JSONObject responseObject = (JSONObject)(parser.parse(response.toString()));
////		JSONObject obj = (JSONObject)(parser.parse((String) args[0]));      				
//		System.out.println(responseObject.toString());
		
//		completeObject.key
////		if(responseObject.containsKey("success")){
////			
////		}
////		JSONObject object = (JSONObject)(parser.parse(response));   
//		for(Iterator<?> iterator = completeObject.keys().iterator(); iterator.hasNext();) {
//			
//			String key = (String) iterator.next();
//			if(key == ""){
//				System.out.println("There is no key!");
//			}else{
//				System.out.println("key is " + key);
//
//			}
////			System.out.println(key);
////			System.out.println(obj.get(key));
//			JSONObject subObj = (JSONObject)responseObject.get(key);
////  		String username = (String)obj.get("n");
//			String title = ((String)subObj.get("title"));
//			int numPlayers = ((Number)subObj.get("numPlayers")).intValue();
//      		System.out.println("{ " + key + " ,x: " + title + ", y: " + numPlayers + " }");
//////      		System.out.println("{ " + counter + " }");
//				    
//				    
//		}
		
		
//  		String title = (String)responseObject.get("title");  
//  		String numPlayers = (String)responseObject.get("numPlayers");  
//  		String id = (String)responseObject.get("_id");  
//
//  		System.out.println(title);
//  		System.out.println(numPlayers);
//  		System.out.println(id);

		//print result
//		layer.removeLoginLayer();

		
	}
	
	
}
