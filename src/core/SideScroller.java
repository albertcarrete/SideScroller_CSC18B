package core;

import javax.swing.JFrame;

import core.LayeredPanel;

public class SideScroller {

	public static void main(String[] args) {
		JFrame window = new JFrame("SideScroller CSC18B");
		window.setContentPane(new LayeredPanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);	
	}
}
