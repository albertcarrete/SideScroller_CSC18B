package layers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import core.LayeredPanel;

public class RegisterLayer extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	public int menu = 0;

	JLabel emailLabel;
	JTextField emailText;
	JLabel userLabel;
	JTextField userText;
	JLabel passwordLabel;
	JPasswordField passwordText;
	JButton registerButton;
	JLabel errorLabel;
	JTextArea textArea;
	
	private LayeredPanel layer;
	
	public RegisterLayer(Point origin,LayeredPanel layer){
		
		super();	
		this.layer = layer;
		
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setLayout(null);
        setBounds(origin.x, origin.y, WIDTH, HEIGHT);


			emailLabel = new JLabel("Email");
			emailLabel.setBounds(10,10,80,25); // x , y, width, height
			add(emailLabel);
			
			emailText = new JTextField(20);
			emailText.setBounds(100,10,210,25);
			add(emailText);
	
    		userLabel = new JLabel("User");
    		userLabel.setBounds(10,40,80,25);
    		add(userLabel);
    		userText = new JTextField(20);
    		userText.setBounds(100,40,210,25);
    		add(userText);
    		
    		passwordLabel = new JLabel("Password");
    		passwordLabel.setBounds(10,70,80,25);
    		add(passwordLabel);
    		passwordText = new JPasswordField(20);
    		passwordText.setBounds(100,70,210,25);
    		add(passwordText);	
    		
    		registerButton = new JButton("Register");
    		registerButton.setBounds(100,100,90,25);
    		add(registerButton);
    		
    		textArea = new JTextArea(20,20);
    		textArea.setBounds(10, 130, 300, 100);
//    		JScrollPane errorPane = new JScrollPane(textArea); 
    		textArea.setEditable(false);
    		
    		add(textArea);
//    		errorLabel = new JLabel("");
//    		Color errorColor = Color.RED;
//    		errorLabel.setBackground(errorColor);
//    		errorLabel.setBounds(10,130,200,25);

    		
    		
    		/* Button is clicked -- send message */
    		registerButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent event){
    				checkValues();
    			}
    		});	
        
	}
	public void showRegister(){
		System.out.println("Register was pressed");
		menu = 1;
		repaint();
	}
	public void checkValues(){
		
		boolean errors = false;
		ArrayList<String> errorCodes = new ArrayList<String>();
		
		if(!(emailText.getText().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))){
			errorCodes.add("Email invalid");
			errors = true;
		}
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

