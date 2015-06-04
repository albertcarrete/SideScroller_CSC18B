package layers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONObject;

import net.miginfocom.swing.MigLayout;
import models.LobbyModel;
import core.LayeredPanel;

public class LobbyCreateState extends JPanel{
	
	/**
	 * LobbyCreateState
	 */
	private static final long serialVersionUID = 1L;
	
	/* */
	private LayeredPanel layer;
	final private LobbyLayer parent;

	JTextArea output;
	JList list;
	JTable table;
	ListSelectionModel listSelectionModel;
	String[][] results;
	private final String USER_AGENT = "Mozilla/5.0";
	String tableData[][];
	LobbyModel lobbyModel;
	String[] mapStrings = { "Valhalla","Space Road" };
	
	// Inputs
	JTextField titleField;
	JComboBox mapList;
	
	public LobbyCreateState(LobbyLayer p){
		
		super();
		JPanel panel = new JPanel(new MigLayout());
		this.parent = p;
	    String[][] tableData = {{"[title]","[numPlayers]","[map]"},
                {"deux",   "dos",     "due"     },
                {"trois",  "tres",    "tre"     },
                { "quatre", "cuatro",  "quattro"},
                { "cinq",   "cinco",   "cinque" },
                { "six",    "seis",    "sei"    },
                { "sept",   "siete",   "sette"  } };
		
		
        lobbyModel = new LobbyModel(tableData);
        table = new JTable(lobbyModel);
        
       
		JButton startLobbyButton = new JButton("Start Lobby");
		JButton backButton 		= new JButton("Back");

		JLabel titleLabel = new JLabel("Title");
		titleField = new JTextField(20);

		JLabel mapLabel = new JLabel("Map");
		mapList = new JComboBox(mapStrings);

		
	    panel.add(backButton);
		panel.add(startLobbyButton,"wrap");
		
		panel.add(titleLabel);
		panel.add(titleField,"wrap");
		panel.add(mapLabel);
		panel.add(mapList,"wrap");
		add(panel);
	
		try{
			sendGet();			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		startLobbyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				// hide the lobby UI overlay
				// turn on the character select
		        boolean success = false;

				try{
					success = sendPost(titleField.getText(),mapStrings[mapList.getSelectedIndex()]);				
				}
				catch(Exception e){
					e.printStackTrace();
				}
				if(success){
					parent.toggleLobbyOverlay();
				}else{
					System.out.println("Error has occured with game creation");
				}
				

				
			}
		});
		
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				/* Change card view */
				parent.changeView("LOBBYVIEW");
			}
		});
		
		
	}
	public void hideOverlay(){
		layer.removeLobbyLayer();

	}
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
		
		int length = json.length();
		results = new String[length][3];
		
		
		for(int i = 0; i < json.length(); i++){
			
			JSONObject jObj = json.getJSONObject(i);
			String title = jObj.getString("title");
			String numPlayers = jObj.getString("numPlayers");
			String map = jObj.getString("map");
			
			results[i][0] = title;
			results[i][1] = numPlayers;
			results[i][2] = map;
			
			System.out.println("title: " + title + " numPlayers: " + numPlayers + "map: " + map);
		}
        lobbyModel = new LobbyModel(results);
        table.setModel(lobbyModel);
        table.repaint();
	}	
	private boolean sendPost(String title, String map) throws Exception{
//		try{
		
			Boolean success = false;
			URL url = new URL("http://localhost:8080/api/games");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type","application/json");

			JSONObject json = new JSONObject();
		    json.put("title", title);
		    json.put("map", map);
		    json.put("numPlayers", 1);
		    json.put("state", "lobby");
		    json.put("player", "default name");
		    
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
			
//			JSONObject responseObject = new JSONObject(response.toString());
//	  		success = (Boolean)responseObject.get("success");
//	  		System.out.println(success);
	  		success = true;
	  		return success;
	
			
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
