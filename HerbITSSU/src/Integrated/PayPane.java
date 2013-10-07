package Integrated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import AnimationComponent.AniButton;

public class PayPane extends JPanel{
	/*
	 * 실제로는 OrderPanel에서 받아온 cancel 버튼이 뒤로가기 역할.
	 * 뒤로가기 Flow : cancel Button Event
	 * 결제 취소 Flow : resetTotal in orderPanel -> cancel Button Event
	 */
	
	public static boolean payWorking = false;
	
	private OrderPanel parent;
	private JLabel label = new JLabel();
	private Border blackline;
	
	private JPanel centerPanel;
	private JPanel bottomPanel;
	
	private JButton cardBtn;
	private JButton cashBtn;
	private JButton backBtn;
	private AniButton cancelBtn;
	
	public PayPane(String text, OrderPanel orderPanel){
		
		this.parent = orderPanel;
		Color FG_COLOR = new Color(0x3B5998);
		label.setForeground(FG_COLOR);
		label.setFont(new Font("Sans", Font.BOLD, 30));
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		blackline = BorderFactory.createLineBorder(Color.black,5);
		this.setBorder(blackline);
		
		this.centerPanel = new JPanel();
		this.bottomPanel = new JPanel(new GridLayout(1,4,10,10));
		
		this.cardBtn = new JButton("카드결제");
		this.cashBtn = new JButton("현금결제");
		this.backBtn = new JButton("결제취소");
		
		updatePanel();
		updateBtnEvent();
	}
	
	private void updatePanel(){
		this.setLayout(new BorderLayout());
		
		this.add(this.centerPanel , BorderLayout.CENTER);
		
		this.bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(this.bottomPanel , BorderLayout.SOUTH);
	}
	
	private void updateBtnEvent(){
		
		/*
		 * 결제 취소 Flow -> resetTotal -> cancelBtn Push
		 */
		
		this.backBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.resetTotal();
				cancelBtn.getAniAction().run();
			}
		});
	}
	
	public void addCancelButton(AniButton cancelBtn){
		this.cancelBtn = cancelBtn;
		addBottomBtns();
		this.bottomPanel.add(cancelBtn);
	}
	
	private void addBottomBtns(){
		this.bottomPanel.add(cardBtn);
		this.bottomPanel.add(cashBtn);
		this.bottomPanel.add(backBtn);
	}

}
