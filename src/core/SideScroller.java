package core;

import javax.swing.JFrame;
import core.LayeredPanel;
/**
 * SideScroller.java
 * -----------------
 * Kicks off the main JFrame window for the game and
 * sets the options for that window.  
 */
public class SideScroller {

	public static void main(String[] args) {
		JFrame window = new JFrame("SideScroller CSC18B");
		window.setContentPane(new LayeredPanel(window));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);	
	}
	
}
