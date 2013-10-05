package Integrated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import AnimationComponent.AniButton;

public class PayPane extends JPanel{
	
	public static boolean payWorking = false;
	private JLabel label = new JLabel();
	private Border blackline;
	
	private JPanel centerPanel;
	private JPanel bottomPanel;
	
	private JButton cardBtn;
	private JButton cashBtn;
	
	public PayPane(String text){
		
		Color FG_COLOR = new Color(0x3B5998);
		label.setForeground(FG_COLOR);
		label.setFont(new Font("Sans", Font.BOLD, 30));
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		blackline = BorderFactory.createLineBorder(Color.black,5);
		this.setBorder(blackline);
		
		this.centerPanel = new JPanel();
		this.bottomPanel = new JPanel();
		
		updatePanel();
		
	}
	
	private void updatePanel(){
		this.setLayout(new BorderLayout());
		
		this.add(this.centerPanel , BorderLayout.CENTER);
		this.add(this.bottomPanel , BorderLayout.SOUTH);
	}
	
	public void addCancelButton(AniButton cancelBtn){
		this.bottomPanel.add(cancelBtn);
	}

}
