package layers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import appstate.AppStateManager;
import net.miginfocom.swing.MigLayout;
import models.LobbyModel;
import core.LayeredPanel;
import core.Passport;


public class LobbyViewState extends JPanel{
	
	private LayeredPanel layer;
	Passport _p;
	
	JTextArea output;
	JList list;
	JTable table;
	ListSelectionModel listSelectionModel;
	String[][] results;
	private final String USER_AGENT = "Mozilla/5.0";
	String tableData[][];
	LobbyModel lobbyModel;
	AppStateManager asm;
	String selectedLobbyID;
	private final LobbyLayer parent;
	
	final static String LOBBYCREATE = "LOBBYCREATE";

	
	/* --------------------------------------------
	 * LayeredPanel -> LobbyLayer -> LobbyViewState 
	 * ----------------------------------------- */
	public LobbyViewState(LobbyLayer p, Passport passport){
		super();
		this.parent = p;
		this._p = passport;
		JPanel panel = new JPanel(new MigLayout());
		
	    String[][] tableData = {{"un",     "uno",     "uno"     },
                {"deux",   "dos",     "due"     },
                {"trois",  "tres",    "tre"     },
                { "quatre", "cuatro",  "quattro"},
                { "cinq",   "cinco",   "cinque" },
                { "six",    "seis",    "sei"    },
                { "sept",   "siete",   "sette"  } };
		
		
        lobbyModel = new LobbyModel(tableData);
        table = new JTable(lobbyModel);
        
       
        JButton refreshButton = new JButton("Refresh");
		JButton joinButton = new JButton("Join");
		JButton createButton = new JButton("Create");
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//	        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        JScrollPane tablePane = new JScrollPane(table);
		//Build output area.
        output = new JTextArea(10,50);
	        output.setEditable(false);
	        JScrollPane outputPane = new JScrollPane(output,
	                         ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
	                         ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        
	       
		
		panel.add(refreshButton);
		panel.add(joinButton);
		panel.add(createButton,"wrap");

		panel.add(tablePane,"span");
		add(panel);
	
		try{
			sendGet();			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		joinButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.out.println("Join button triggered");
				selectedLobbyID = (String)lobbyModel.getValueAt(table.getSelectedRow(), 3);
				parent.setCurrentGameId(selectedLobbyID);
				// turns off lobby overlay, and moves to character select
				// LobbyLayer.toggleLobbyOverlay -> LayeredPanel.toggleLobbyOverlay
				
				boolean successfulPut = false;
				try{
					successfulPut = sendPut();
					if(successfulPut){
						parent.toggleLobbyOverlay();
					}
				}catch(Exception e){
					e.printStackTrace();
				}


			}
		});
		
		refreshButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				refresh();
			}
		});
		
		createButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				parent.changeView(LOBBYCREATE);
			}
		});
		
		
		
	}
	private boolean sendPut() throws Exception{
		
		boolean success = false;
		
		URL url = new URL("http://localhost:8080/api/games/id/"+ selectedLobbyID);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("PUT");
//		conn.setRequestProperty("User-Agent",USER_AGENT);
//
		conn.setRequestProperty("Content-Type","application/json");

		JSONObject json = new JSONObject();
	    json.put("username",_p.getUsername());
	    System.out.println(json.toString());
		String input = json.toString();
		
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
		
		if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
			throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String output;
		System.out.println("Output from Server ... \n");
		StringBuffer response = new StringBuffer();

		while((output = br.readLine()) != null){
			System.out.println(output);
			response.append(output);
		}
		conn.disconnect();
		
		JSONObject responseObject = new JSONObject(response.toString());
		
		
		JSONArray arr = (JSONArray)responseObject.get("players");
		int size = arr.length();
		System.out.println("Looking for our username " + _p.getUsername());
				
		for(int i = 0; i < size; i++){
			JSONObject obj = arr.getJSONObject(i);	
			String tempName = (String)obj.get("username");
			if(tempName.equals(_p.getUsername())){
				_p.setPosition((int)obj.get("position")-1);
				success = true;
			}			
		}
		return success;
	}
	
	
	
	
	/* 	GET REQUEST TO VIEW LOBBIES
	 * 	This function sends a get request to the server
	 * 	for lobbies that are currently available.  
	 * */
	private void sendGet() throws Exception{
		
		// URL to query
		String url = "http://localhost:8080/api/games/Lobby";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		// (optional) default is GET
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
		String _id = "";
//
		// Convert response from server to JSONArray (USING json.org)
//		JSONArray json = new JSONArray(response.toString());
//		
//		int length = json.length();
//		results = new String[length][3];
//		
//		boolean empty = true;
//		String _id = "";
//
//		for(int i = 0; i < json.length(); i++){
//			
//			JSONObject jObj 	= json.getJSONObject(i);
//			String title 		= jObj.getString("title");
//			String numPlayers 	= jObj.getString("numPlayers");
//			String map 			= jObj.getString("map");
//			
//			if(jObj.has("_id")){
//				_id 			= jObj.getString("_id");
//				empty = false;
//			}
			// Convert response from server to JSONArray (USING json.org.simple)
			
			boolean empty = true;
			
			JSONArray json = new JSONArray(response.toString());
			int length = json.length();
			results = new String[length][4];
			
			for(int i = 0; i < json.length(); i++){
				
				JSONObject responseObject = json.getJSONObject(i);
				String title 		= responseObject.getString("title");
				String numPlayers 	= responseObject.getString("numPlayers");
				String map 			= responseObject.getString("map");
				String id			= responseObject.getString("_id");
				
				results[i][0] = title;
				results[i][1] = numPlayers;
				results[i][2] = map;
				results[i][3] = id;
				
				System.out.println("title: " + title + " numPlayers: " + numPlayers + "map: " + map);
			}
			
	        lobbyModel = new LobbyModel(results);
	        table.setModel(lobbyModel);
	        table.repaint();

//			if(responseObject.has("_id")){
//				_id = (String)responseObject.get("_id");
//				empty=false;
//			}
			
			
			
			
			
			
//			JSONParser parser = new JSONParser();
//			Object objRes =parser.parse(response.toString());
//			JSONArray array=(JSONArray)objRes;
//			JSONObject obj2=(JSONObject)array.get(0);
//			
//			String title 		= (String)obj2.get("title");
//			String numPlayers 	= obj2.get("numPlayers").toString();
//			String map 			= (String)obj2.get("map");
//			
//			if(obj2.containsKey("_id")){
//				_id = (String)obj2.get("_id");
//
//				empty = false;
//			}
//
//			System.out.println(obj2.keySet());
//			int size = array.size();
//			int sizeObj = obj2.size();
//			System.out.println("The keyset is " + size);
//			System.out.println("The keyset size is" + sizeObj);
//			results = new String[size][3];
//
//			for(int i = 0; i < size; i++){
//				results[i][0] = title;
//				results[i][1] = numPlayers;
//				results[i][2] = map;				
//			}
//
//			if(!empty){
//				System.out.println("title: " + title + " numPlayers: " + numPlayers + "map: " + map);				
//			}else{
//				System.out.println("id " + _id + " title: " + title + " numPlayers: " + numPlayers + "map: " + map);				
//
//			}
//		}

	}	
	private void refresh(){
		try{
			sendGet();			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
