package Integrated;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class StoreGraphPanel extends JPanel {
	boolean dataset = false;
	
	private int size;
	private int points[];
	private String term[];
	
	public StoreGraphPanel() {
		this.setBackground(Color.WHITE);
	}
	
	private void doDrawing(Graphics g) {
		if (!dataset) return ;
		
		Graphics2D g2 = (Graphics2D) g;
		
		// Initial Figures
		int margin_left = 40;
		int margin_right = 20;
		int padding = 15;
		int x_step = (this.getBounds().width - margin_left - margin_right - padding * 2) / (size - 1);
		int baseline = this.getBounds().height * 4 / 5;
		
		
		// Base Line
		g2.setColor(Color.BLUE);
		g2.drawLine(margin_left, baseline, this.getBounds().width - margin_right, baseline);
		
		// X Axis Label
		g2.setColor(Color.BLACK);
		for (int i = 0; i < size; i++) {
			g2.drawString(term[i], margin_left + x_step*i, baseline + 20);
		}
		
		
		// Y Axis
		float[] dash2 = {1f, 1f, 1f};
        BasicStroke bs2 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash2, 2f);
        g2.setStroke(bs2);
		g2.drawString("0", 10, baseline + 5);
		for (int i = 1; i <= 5; i++) {
			g2.drawString(Integer.toString(i*10), 10, baseline - i*10 * 5 + 5);
			g2.drawLine(margin_left, baseline - i*10 * 5, this.getBounds().width - margin_right, baseline - i*10 * 5);
		}
		
		
		// Graph
        g2.setStroke(new BasicStroke(2));
		g2.drawString("(" + Integer.toString(points[0]) + ")", margin_left + padding - 5, baseline - (points[0] * 5) - 10);
		for (int i = 1; i < size; i++) {
			// Line
			g2.setColor(Color.RED);
			g2.drawLine(margin_left + padding + x_step*(i-1), baseline - (points[i - 1] * 5),
						margin_left + padding + x_step*i,     baseline - (points[i] * 5));
			
			// Label
			g2.setColor(Color.BLACK);
			g2.drawString("(" + Integer.toString(points[i]) + ")", margin_left + padding + x_step*i - 5, baseline - (points[i] * 5) - 10);
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	public void setPoints(int data[], String label[]) {
		size = 0;
		for (int i = 0; i < data.length; i++)
			if (label[i] == null)
				continue;
			else {
				size++;
			}
		
		points = new int[size];
		term = new String[size];
		
		for (int i = 0; i < size; i++) {
			if (label[i] == null)
				continue;
			
			points[i] = data[size - i - 1];
			term[i] = label[size - i - 1];
		}
		
		dataset = true;
		
		this.repaint();
	}
}
