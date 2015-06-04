package layers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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

import net.miginfocom.swing.MigLayout;
import models.LobbyModel;
import core.LayeredPanel;


public class LobbyViewState extends JPanel{
	
	private LayeredPanel layer;
	JTextArea output;
	JList list;
	JTable table;
	ListSelectionModel listSelectionModel;
	String[][] results;
	private final String USER_AGENT = "Mozilla/5.0";
	String tableData[][];
	LobbyModel lobbyModel;
	private final LobbyLayer parent;
	
	final static String LOBBYCREATE = "LOBBYCREATE";

	
	/* --------------------------------------------
	 * LayeredPanel -> LobbyLayer -> LobbyViewState 
	 * ----------------------------------------- */
	public LobbyViewState(LobbyLayer p){
		super();
		this.parent = p;
		
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
	private void sendGet() throws Exception{
		
		String url = "http://localhost:8080/api/games/Lobby";
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
			
			JSONObject jObj 	= json.getJSONObject(i);
			String title 		= jObj.getString("title");
			String numPlayers 	= jObj.getString("numPlayers");
			String map 			= jObj.getString("map");
			
			results[i][0] = title;
			results[i][1] = numPlayers;
			results[i][2] = map;
			
			System.out.println("title: " + title + " numPlayers: " + numPlayers + "map: " + map);
		}
        lobbyModel = new LobbyModel(results);
        table.setModel(lobbyModel);
        table.repaint();
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
