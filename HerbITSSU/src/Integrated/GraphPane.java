package Integrated;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphPane extends JPanel {
	private static final int MARGIN_LEFT = 40;
	private static final int MARGIN_RIGHT = 40;
	private static final int PADDING = 15;
	private static final int VALUE_STEP = 5;
	private static final Font BASIC_FONT = new Font("Dialog", Font.PLAIN, 12);
	private static final Font BIG_FONT = new Font("±¼¸²", Font.BOLD, 42);
	private static final float DASH_AXIS[] = {1f, 1f, 1f};
	private static final float DASH_LINE[] = {0.5f, 1f, 0.5f};
	private static final BasicStroke DOTTED_AXIS = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, DASH_AXIS, 2f);
	private static final BasicStroke DOTTED_LINE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, DASH_LINE, 2f);
	private static final BasicStroke BASIC_LINE = new BasicStroke(2);
	
	public class GraphData {
		private String label = "";
		private int value = 0;
		public GraphData(String label, int value) {
			this.label = label;
			this.value = value;
		}
		public String getLabel() { return new String(label); }
		public void setLabel(String label) { this.label = new String(label.trim()); }
		public int getValue() { return value; }
		public void setValue(int value) { this.value = value; }
	}
	
	private boolean isDataSet = false;
	private ArrayList<GraphData> data = new ArrayList<GraphData>();
	private int min, max, yLineCnt, yLineGap;
	
	private boolean isTitleSet = false;
	private boolean isDataShown = true;
	private int dottedCnt = 0;
	private Color lineColor = Color.red;
	private Color dottedColor = Color.blue;
	private String graphTitle = "";
	
	public GraphPane() { this.setBackground(Color.WHITE); }
	public GraphPane(int numberOfDottedValue) {
		this();
		dottedCnt = numberOfDottedValue;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		int boardWidth = this.getBounds().width, boardHeight = this.getBounds().height;
		if (!isDataSet || data.isEmpty()) {
			g2.setColor(Color.RED);
			g2.setFont(BIG_FONT);
			g2.drawString("No Data!", boardWidth/2, boardHeight/2);
			
			return ;
		}
		
		// Initial Figures
		int baseline = this.getBounds().height * 4 / 5;
		int graphWidth = boardWidth - MARGIN_LEFT - MARGIN_RIGHT - PADDING * 2;
		int graphHeight = boardHeight * 3 / 5;
		int stepX = graphWidth / (data.size() - 1);
		yLineCnt = (max - min) / VALUE_STEP + 1;
		yLineGap = graphHeight / yLineCnt;
		
		if (isTitleSet) {
			g2.setColor(Color.BLACK);
			g2.setFont(BIG_FONT);
			FontMetrics metrics = g.getFontMetrics(BIG_FONT);
			g2.drawString(graphTitle, (boardWidth - metrics.stringWidth(graphTitle)) / 2 , boardHeight / 5 - 42);
		}
		g2.setFont(BASIC_FONT);
		
		// Draw Baseline
		g2.setColor(Color.BLUE);
		g2.drawLine(MARGIN_LEFT, baseline, boardWidth - MARGIN_RIGHT, baseline);		
		
		// Y Axis
		g2.setColor(Color.BLACK);
        g2.setStroke(DOTTED_AXIS);
		if (min >= 0) g2.drawString("0", 10, baseline + 5);
		for (int i = 1; i <= yLineCnt; i++) {
			g2.drawString(Integer.toString((i - 1)*VALUE_STEP + min), 10, baseline - i*yLineGap + 5);
			g2.drawLine(MARGIN_LEFT, baseline - i*yLineGap, boardWidth - MARGIN_RIGHT, baseline - i*yLineGap);
		}
		
		// X Axis Label
		for (int i = 0; i < data.size(); i++) {
			g2.drawString(data.get(i).label, MARGIN_LEFT + stepX*i, baseline + 20);
		}
		
		// Graph
        String lbl = data.get(0).label;
        int exVal = data.get(0).value, curVal;
        int exY = makeYPoint(exVal), curY;
		if (isDataShown) g2.drawString("(" + Integer.toString(exVal) + ")", MARGIN_LEFT + PADDING - 5, baseline - exY - 10);
		for (int i = 1; i < data.size(); i++) {
			lbl = data.get(i).label;
			curVal = data.get(i).value; curY = makeYPoint(curVal);
			
			// Line
			g2.setColor(lineColor);
			if (i < data.size() - dottedCnt) {
				g2.setStroke(BASIC_LINE);
				g2.drawLine(MARGIN_LEFT + PADDING + stepX * (i - 1), baseline - exY, MARGIN_LEFT + PADDING + stepX * i, baseline - curY);
			} else {
				g2.setStroke(DOTTED_LINE);
				g2.setColor(dottedColor);
				g2.drawLine(MARGIN_LEFT + PADDING + stepX * (i - 1), baseline - exY, MARGIN_LEFT + PADDING + stepX * i, baseline - curY);
			}
			exVal = curVal; exY = curY;
			
			// Label
			g2.setColor(Color.BLACK);
			g2.drawString("(" + Integer.toString(curVal) + ")", MARGIN_LEFT + PADDING + stepX*i - 5, baseline - curY - 10);
		}
	}
	
	public void setPoints(int data[], String label[]) {
		isTitleSet = false;
		this.data.clear();	
		for (int i = 0; i < data.length; i++) {
			if (label[i] == null)
				continue;
			
			this.data.add(new GraphData(label[i], data[i]));
		}
		
		if (!this.data.isEmpty())
			isDataSet = true;
		
		analyzeData();
		this.repaint();
	}
	public void setPoints(int data[], String label[], String title) {
		setPoints(data, label);
		graphTitle = title;
		isTitleSet = true;
	}
	
	private void analyzeData() {
		min = max = 0;
		if (!isDataSet) return ;
		
		min = max = data.get(0).value;
		for (int i = 1; i < data.size(); i++) {
			if (data.get(i).value < min) min = data.get(i).value;
			if (data.get(i).value > max) max = data.get(i).value;
		}
		
		if (min >= 0)
			min = min / VALUE_STEP * VALUE_STEP;
		else
			min = (min + 1) / VALUE_STEP * VALUE_STEP - VALUE_STEP;
		max = (max / VALUE_STEP + 1) * VALUE_STEP;
		
		if (min == 0) min = VALUE_STEP;
	}
	
	private int makeYPoint(int value) {
		if (value < min)
			return value * yLineGap / min;
		else
			return (value - min) * yLineGap / VALUE_STEP + yLineGap;
	}
}
