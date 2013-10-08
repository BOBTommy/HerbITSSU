package Integrated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private int total;						//주문 총액
	
	private JPanel centerPanel;
	private JPanel bottomPanel;
	
	private JLabel dateLabel = new JLabel();	//결제 일시
	private JLabel totalLabel = new JLabel();	//총 결제 금액
	private JLabel recommandLabel = new JLabel();	//추천 메뉴
	private JLabel ruleLabel = new JLabel();	//판매 절차
	
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
		
		this.centerPanel = new JPanel(new GridLayout(4,1,10,10));
		this.bottomPanel = new JPanel(new GridLayout(1,4,10,10));
		
		this.cardBtn = new JButton("카드결제");
		this.cashBtn = new JButton("현금결제");
		this.backBtn = new JButton("결제취소");
		this.total =  0;
		
		updatePanel();
		updateBtnEvent();
	}
	
	private void updatePanel(){
		this.setLayout(new BorderLayout());
		
		this.centerPanel.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));
		updateCenterPanel();
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
	
	public void updateCenterPanel(){
		
		//결제 시간 업데이트
		Date dt = new Date();
		SimpleDateFormat dtForm = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
		String curTime = dtForm.format(dt.getTime());
		dateLabel.setText("결제 일시 : \t\t" + curTime);
		
		//총 결제액 업데이트
		String total = Integer.toString(this.total);
		totalLabel.setText("총 결제액 : \t" + total);
		
		//판매 절차 업데이트
		String rule = "<html>판매 절차<br>"
				+ "1. 주문 내역을 확인합니다.<br>"
				+ "2. 주문 내역에 대한 추천 메뉴의 주문 여부를 여쭤봅니다.<br>"
				+ "\tEx. 아메리카노에는 와플을 같이 드시는 분이 많은데 어떠신가요?<br>"
				+ "3. 총 금액을 말쓰드리고 결제 수단(카드, 현금)을 여쭤 봅니다.<br>"
				+ "4. 결제를 진행하고 대기표를 발급해 드리고, 기다리는 손님이 많은 경우,"
				+ " 미리 양해를 구합니다.<br>"
				+ "5. 주문과 실제 내역을 확인합니다."
				+ "</html>";
		ruleLabel.setText(rule);
		
		this.centerPanel.add(dateLabel);
		this.centerPanel.add(totalLabel);
		//추천 메뉴
		this.centerPanel.add(ruleLabel);
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
	
	public void setTotal(int total){
		this.total = total;
		this.totalLabel.setText("총 결제액 : " + Integer.toString(this.total));
	}

}
