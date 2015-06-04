package layers;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.json.JSONArray;
import org.json.JSONObject;

import core.LayeredPanel;
import net.miginfocom.swing.MigLayout;
import models.LobbyModel;

public class LobbyLayer extends JPanel{
	
	private LayeredPanel layer;
	JTextArea output;
	JList list;
	JTable table;
	ListSelectionModel listSelectionModel;
	String[][] results;
	
	private final String USER_AGENT = "Mozilla/5.0";
	
	String tableData[][];
	
	LobbyModel lobbyModel;
	
	JPanel main; // will hold our cards layout
	JPanel cards; // will be our card layout

	CardLayout cl;
	
	final static String LOBBYVIEW = "LOBBYVIEW";
	final static String LOBBYCREATE = "LOBBYCREATE";

	
	public LobbyLayer(LayeredPanel l){
		
		super();
		this.layer = l;
		main = new JPanel();
		
		int calculatedWidth 	= (int)(layer.getWidth() * .80);
		int calculatedHeight 	= (int)(layer.getHeight() * .80);
		
		int calculatedWMargin = (int)((layer.getWidth() - calculatedWidth)/2);
		int calculatedHMargin = (int)((layer.getHeight() - calculatedHeight)/2);
		
		setPreferredSize(new Dimension(calculatedWidth,calculatedHeight));
		setOpaque(true);
		setBounds(calculatedWMargin,calculatedHMargin,calculatedWidth,calculatedHeight);
		
		JPanel card1 = new JPanel();
	    card1.setLayout(new BorderLayout());
	    LobbyViewState LobbyViewState = new LobbyViewState(this);
	    card1.add(LobbyViewState);
	    
		JPanel card2 = new JPanel();
	    card2.setLayout(new BorderLayout());
	    LobbyCreateState LobbyCreateState = new LobbyCreateState(this);
	    card2.add(LobbyCreateState);
		
	    cards = new JPanel(new CardLayout());
		cards.add(card1,LOBBYVIEW);
		cards.add(card2,LOBBYCREATE);

		
		add(cards, BorderLayout.CENTER);
		
		cl = (CardLayout)(cards.getLayout());
        cl.show(cards,LOBBYVIEW);

	}
	
	public void changeView(String view){
		CardLayout cl = (CardLayout)(cards.getLayout());
		System.out.println("Attempting to change view to " + view);
		cl.show(cards,view);
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
	
	/* Manages the visibility of the overlay */
	public void toggleLobbyOverlay(){
		layer.removeLobbyLayer();
	}


}
