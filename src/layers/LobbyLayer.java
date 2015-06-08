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

import core.LayeredPanel;
import core.Passport;
import net.miginfocom.swing.MigLayout;
import models.LobbyModel;

public class LobbyLayer extends JPanel{
	
	private LayeredPanel parent;
	public Passport _p;
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

	
	public LobbyLayer(LayeredPanel l, Passport p){
		
		super();
		this.parent = l;
		this._p = p;
		main = new JPanel();
		
		int calculatedWidth 	= (int)(parent.getWidth() * .80);
		int calculatedHeight 	= (int)(parent.getHeight() * .80);
		
		int calculatedWMargin = (int)((parent.getWidth() - calculatedWidth)/2);
		int calculatedHMargin = (int)((parent.getHeight() - calculatedHeight)/2);
		
		setPreferredSize(new Dimension(calculatedWidth,calculatedHeight));
		setOpaque(true);
		setBounds(calculatedWMargin,calculatedHMargin,calculatedWidth,calculatedHeight);
		
		JPanel card1 = new JPanel();
	    card1.setLayout(new BorderLayout());
	    LobbyViewState LobbyViewState = new LobbyViewState(this,this._p);
	    card1.add(LobbyViewState);
	    
		JPanel card2 = new JPanel();
	    card2.setLayout(new BorderLayout());
	    LobbyCreateState LobbyCreateState = new LobbyCreateState(this,this._p);
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

	
	/* Manages the visibility of the overlay */
	public void toggleLobbyOverlay(){
		parent.removeLobbyLayer();
	}
	public void setCurrentGameId(String id){
		parent.currentGame = id;
	}
	/* Fetches the logged in players ID from the root class
	 * LobbyLayer -> LayeredPanel.playerID
	 * */
	public String getGlobalUsername(){
		return parent.playerID;
	}

}
