package Integrated;

import java.awt.Color;

import javax.swing.JTabbedPane;

import aurelienribon.slidinglayout.SLAnimator;

public class HerbPane extends JTabbedPane{


	/*
	 * Tween Accessor
	 */

	public static class Accessor extends SLAnimator.ComponentAccessor{
		
	}
	
	public HerbPane() {
		this.setBackground(Color.WHITE);
	}
}
