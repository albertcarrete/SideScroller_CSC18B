package layers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import core.LayeredPanel;

public class GeneralLayer extends JPanel {
	
	public static final int WIDTH = 620;
	public static final int HEIGHT = 400;
	public static final int SCALE = 2;
	private Color bg_overlay;
	private Color loginFontColor;
	private Font loginFont;
	private Font h1;
	private Font h2;
	private Font inputText;
	private Color h1_color;
	private Color h2_color;

	
	// Text Fields
	JTextField userText;
	JTextArea textArea;
	JPasswordField passwordText;
	
	private LayeredPanel layer;
	public int menu = 0;
	
	public GeneralLayer(Point origin,LayeredPanel layer){
		
		super();	
		this.layer = layer;
		loginFont = new Font("Arial",Font.BOLD, 20);
		setFont(loginFont);
		bg_overlay = new Color(22,22,22,80);
		
		
		h1 			= new Font ("Arial",Font.BOLD, 25);
		h2 			= new Font ("Arial",Font.BOLD, 16);

		h1_color	= new Color(208,177,131);
		h2_color 	= new Color(255,255,255);
		inputText	= new Font("Arial", Font.PLAIN,35);
		

		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setOpaque(true);
		setBackground(bg_overlay);
//        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setLayout(null);
        setBounds(origin.x, origin.y, WIDTH, HEIGHT);

//        System.out.println(menu);
        
        int li = 80;
        int leftMargin = 30;
        int inputBoxHeight = 50;
        if(menu == 0){
        	
        	JLabel titleLabel = new JLabel("Login");
        	titleLabel.setBounds(leftMargin,10,100,40);
        	titleLabel.setFont(h1);
        	titleLabel.setForeground(h2_color);
        	
    		JLabel userLabel = new JLabel("User");
    		userLabel.setBounds(leftMargin,li,100,25);
    		userLabel.setFont(h2);
    		userLabel.setForeground(h2_color);
    	
    		userText = new JTextField(20);
    		userText.setBounds(leftMargin,li+25,560,inputBoxHeight);
    		userText.setFont(inputText);

    		JLabel passwordLabel = new JLabel("Password");
    		passwordLabel.setBounds(leftMargin,li+75,150,25);
    		passwordLabel.setFont(h2);
    		passwordLabel.setForeground(h2_color);

    		passwordText = new JPasswordField(20);
    		passwordText.setBounds(leftMargin,li+100,560,inputBoxHeight);
    		userText.setFont(inputText);

    		JButton loginButton = new JButton("Login");
    		loginButton.setBounds(leftMargin,li+150,90,35);

    		JButton registerButton = new JButton("Register a new account");
    		registerButton.setBounds(390,li+150,200,35);
    		
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
		ArrayList<String> errorCodes = new ArrayList();
		
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
			layer.removeLoginLayer();
		}
	}
}
