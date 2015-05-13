package layers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;

import org.json.JSONObject;

import com.sun.net.ssl.HttpsURLConnection;

import core.LayeredPanel;

public class GeneralLayer extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 620;
	public static final int HEIGHT = 400;
	public static final int SCALE = 2;
	private Color bg_overlay;
//	private Color loginFontColor;
	private Font loginFont;
	private Font h1;
	private Font h2;
	private Font inputText;
//	private Color h1_color;
	private Color h2_color;

	
	// Text Fields
	JTextField userText;
	JTextArea textArea;
	JPasswordField passwordText;
	
	private LayeredPanel layer;
	public int menu = 0;
	private final String USER_AGENT = "Mozilla/5.0";

	public GeneralLayer(Point origin,LayeredPanel layer){
		
		super();	
		this.layer = layer;
		
		int calculatedWidth 	= (int)(layer.getWidth() / 2);
		int calculatedHeight 	= (int)(layer.getHeight() / 2);
		
		/* Scale everything to new size */
		
		double scale = (layer.getWidth()/1920.0);
		System.out.println("scale is" + scale);
			
		loginFont = new Font("Arial",Font.BOLD, (int)(20*scale));
		setFont(loginFont);
		bg_overlay = new Color(22,22,22,80);
		
		int h1_px 	= (int)(25 * scale);
		System.out.println("Scaled font is " + h1_px);
		h1 			= new Font ("Arial",Font.BOLD,h1_px);
		h2 			= new Font ("Arial",Font.BOLD, (int)(16*scale));

//		h1_color	= new Color(208,177,131);
		h2_color 	= new Color(255,255,255);
		inputText	= new Font("Arial", Font.PLAIN,(int)(35*scale));
		
		
		/* Center the component to the parent component */
		int sideMargin = calculatedWidth / 2;
		int topMargin = calculatedHeight / 2;
		setPreferredSize(new Dimension(calculatedWidth,calculatedHeight));
		setOpaque(true);
		setBackground(bg_overlay);
//        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setLayout(null);
        setBounds(sideMargin, topMargin, calculatedWidth, calculatedHeight);

//        System.out.println(menu);
        
        int li = (int)(80*scale);
        int leftMargin = (int)(108*scale);
//        int inputBoxHeight = (int)(50*scale);
        
        if(menu == 0){
        	
        	// pos x y & size width height
        	JLabel titleLabel = new JLabel("Login");
        	titleLabel.setFont(h1);
        	titleLabel.setForeground(h2_color);
        	titleLabel.setBounds((int)(calculatedWidth * .10),0,(int)(calculatedWidth * .90),(int)(calculatedHeight*.10));
        	
    		JLabel userLabel = new JLabel("User");
    		userLabel.setBounds((int)(calculatedWidth * .10),(int)(calculatedHeight * .10),(int)(calculatedWidth * .90),(int)(calculatedHeight*.05));
    		userLabel.setFont(h2);
    		userLabel.setForeground(h2_color);
    		
    		
    		userText = new JTextField(20);
    		userText.setBounds((int)(calculatedWidth * .10),(int)(calculatedHeight * .15),(int)(calculatedWidth * .80),(int)(calculatedHeight*.10));
    		userText.setFont(inputText);

    		JLabel passwordLabel = new JLabel("Password");
    		passwordLabel.setBounds((int)(calculatedWidth * .10),(int)(calculatedHeight * .25),(int)(calculatedWidth * .80),(int)(calculatedHeight*.05));
    		passwordLabel.setFont(h2);
    		passwordLabel.setForeground(h2_color);

    		passwordText = new JPasswordField(20);
    		passwordText.setBounds((int)(calculatedWidth * .10),(int)(calculatedHeight * .30),(int)(calculatedWidth * .80),(int)(calculatedHeight*.10));
    		userText.setFont(inputText);

    		JButton loginButton = new JButton("Login");
    		loginButton.setBounds((int)(calculatedWidth * .10),(int)(calculatedHeight * .40),(int)(calculatedWidth * .40),(int)(calculatedHeight*.10));

    		JButton registerButton = new JButton("Register a new account");
    		registerButton.setBounds((int)(calculatedWidth * .50),(int)(calculatedHeight * .40),(int)(calculatedWidth * .40),(int)(calculatedHeight*.10));
    		
    		textArea = new JTextArea(20,20);
    		textArea.setBounds(leftMargin, li+200, 560, 100);
    		textArea.setEditable(false);
    		textArea.setForeground(h2_color);
    		textArea.setBackground(Color.BLACK);

    		
    		add(titleLabel);
    		add(userLabel);   		
    		add(userText);    		
    		add(passwordLabel);
    		add(passwordText);	
    		add(loginButton);
    		add(registerButton);  
    		add(textArea);

    		
    		
    		loginButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent event){
    				checkInput();
    			}
    		});
    		/* Button is clicked -- send message */
    		registerButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent event){
//    				showRegister();
    				bypass();
    			}
    		});	
    		
        }

	}
	
	public void showRegister(){
		System.out.println("Register was pressed");
		menu = 1;
		repaint();
	}
	public void bypass(){
		layer.removeLoginLayer();
	}
	public void checkInput(){
		boolean errors = false;
		boolean success = false;
		ArrayList<String> errorCodes = new ArrayList<String>();
		
		if(!(userText.getText().matches("^[a-z0-9_-]{3,15}$"))){
			errorCodes.add("Username invalid");
			errors = true;
		}
		
		char arr[] = passwordText.getPassword();
		String b = new String(arr);
		System.out.println(b);
		if(!(b.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})"))){
			errorCodes.add("Password invalid");
			errors = true;		
		}
		
		if(errors){
			textArea.setText("");
			for(String error : errorCodes){
				textArea.setText(textArea.getText() + error + "\n" );;
			}
		}else{
			textArea.setText("");
			textArea.setText("Validation successful! Sending information...");
	//			layer.removeLoginLayer();
			try{
				success = sendPost(userText.getText(),b);				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(success){
				layer.removeLoginLayer();
			}else{
				errorCodes.add("User not found");
			}
		}
	}
	private void sendGet() throws Exception{
		
		System.out.println("Is this even called?");
		
		String url = "http://localhost:8080/api/testers/554abf64794096d429f81d30";
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
		
		JSONObject responseObject = new JSONObject(response.toString());
  		String username = (String)responseObject.get("username");  
  		String id = (String)responseObject.get("_id");  

  		System.out.println(username);
  		System.out.println(id);
		//print result
		System.out.println(response.toString());
//		layer.removeLoginLayer();

		
	}
	private boolean sendPost(String username, String password) throws Exception{
//		try{
		
			Boolean success = false;
			URL url = new URL("http://localhost:8080/api/authenticate");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type","application/json");
			
			JSONObject json = new JSONObject();
		    json.put("username", username);
		    json.put("password", password);
		      	
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
	  		success = (Boolean)responseObject.get("success");
	  		System.out.println(success);
	  		
	  		return success;
//	}catch (MalformedURLException e){
//		e.printStackTrace();
//	}catch(IOException e){
//		e.printStackTrace();
//	}
			
			
	}
}
