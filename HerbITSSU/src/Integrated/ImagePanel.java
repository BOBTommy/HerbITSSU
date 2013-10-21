package Integrated;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	ImageIcon img;
	
	public ImagePanel(String bg) {
		super();
		img = new ImageIcon(bg);
		this.setPreferredSize(new Dimension(img.getIconWidth(), img.getIconHeight()+10));
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g.drawImage(img.getImage(), 0, 0, this);
	}
	
	public int getWidth() {
		return img.getIconWidth();
	}
	
	public int getHeight() {
		return img.getIconHeight() + 10;
	}
}
