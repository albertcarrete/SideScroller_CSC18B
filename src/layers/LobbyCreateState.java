package layers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.*;

import org.json.JSONObject;

import net.miginfocom.swing.MigLayout;
import models.LobbyModel;
import core.LayeredPanel;
import core.Passport;

public class LobbyCreateState extends JPanel{
	
	/**
	 * LobbyCreateState
	 */
	private static final long serialVersionUID = 1L;
	
	/* */
	private LayeredPanel layer;
	final private LobbyLayer parent;
	public Passport _p;
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
	
	public LobbyCreateState(LobbyLayer p, Passport passport){
		
		super();
		JPanel panel = new JPanel(new MigLayout());
		this.parent = p;
		_p = passport;
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
//			sendGet();			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		/* Listens for button press on "Start Lobby" */
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
	
	private boolean sendPost(String title, String map) throws Exception{
//		try{
		
			Boolean success = false;
			URL url = new URL("http://52.24.205.124/api/games");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type","application/json");

			JSONObject json = new JSONObject();
		    json.put("title", title);
		    json.put("map", map);
		    json.put("numPlayers", 0);
		    json.put("maxPlayers", 4);
		    json.put("state", "lobby");
		    json.put("player", "");
		    
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
	  		String id = (String)responseObject.get("id");
	  		
	  		System.out.println("Successfully created room. [ID: " + id + "]");
	  		parent.setCurrentGameId(id);
	  		
	  		// Set Passport
	  		_p.setGameId(id);
	  		_p.setPosition(0);
	  		
	  		success = true;
	  		return success;
	
			
	}
	

}
