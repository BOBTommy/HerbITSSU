package Integrated;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import aurelienribon.slidinglayout.SLAnimator;

public class FirstPanel extends JPanel{

	private JLabel label = new JLabel();
	private Border blackline;
	
	public FirstPanel(String name){
		
		Color FG_COLOR = new Color(0x3B5998);
		label.setForeground(FG_COLOR);
		label.setFont(new Font("Sans", Font.BOLD, 90));
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText(name);
		
		blackline = BorderFactory.createLineBorder(Color.black,5);
		this.setBorder(blackline);
		this.add(label);
	}
	
	/*
	 * Tween Accessor
	 */
	
	public static class Accessor extends SLAnimator.ComponentAccessor{
		
	}
	
}
