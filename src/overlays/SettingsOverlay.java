package overlays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import core.LayeredPanel;

public class SettingsOverlay extends JPanel{
	
	
	private LayeredPanel parent;
	private Color overlay_color;
	
	// Components
	JComboBox resolutionComboBox;
	
	public SettingsOverlay(LayeredPanel parent){
		
		super();	
		this.parent = parent;
		
		setPreferredSize(new Dimension(500,500));
		setOpaque(true);
	    setBorder(BorderFactory.createLineBorder(Color.RED));
	    setLayout(null);
	    setBounds(0, 0, 500, 500);
	    
		overlay_color = new Color(22,22,22,80);
		setBackground(overlay_color);
		
		resolutionComboBox = new JComboBox();
		resolutionComboBox.addItem("FullScreen");
		resolutionComboBox.addItem("Standard Size");
		resolutionComboBox.setBounds(0,0,100,200);
		add(resolutionComboBox);
		
		JButton applyButton = new JButton("Apply");
		applyButton.setBounds(20,200,100,100);
		add(applyButton);
		
		applyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				applySettings();
			}
		});
		
		
	}
	public void applySettings(){
		System.out.println("Applying new settings");
		parent.setHeight(600);
		parent.setWidth(330);
		parent.setPreferredSize(new Dimension(100, 100));
		repaint();
	}

}
