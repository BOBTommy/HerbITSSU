package Integrated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Database.CustomerOrder;

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
	private JButton cancelBtn;
	
	
	public PayPane(String text, OrderPanel orderPanel){
		
		this.parent = orderPanel;
		Color FG_COLOR = new Color(0x3B5998);
		label.setForeground(FG_COLOR);
		label.setFont(new Font("Sans", Font.BOLD, 30));
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		//this.db = this.parent.os.db;
		
		//blackline = BorderFactory.createLineBorder(Color.black,5);
		//this.setBorder(blackline);
		
		this.centerPanel = new JPanel(new GridLayout(7,1,10,10));
		this.bottomPanel = new JPanel(new GridLayout(1,4,10,10));
		
		this.cardBtn = new JButton(new ImageIcon("image/order/카드결제.png"));
		this.cashBtn = new JButton(new ImageIcon("image/order/현금결제.png"));
		this.backBtn = new JButton(new ImageIcon("image/order/결제취소.png"));
		this.cancelBtn = new JButton(new ImageIcon("image/order/뒤로가기.png"));
		cardBtn.setPreferredSize(new Dimension(136, 99));
		cashBtn.setPreferredSize(new Dimension(136, 99));
		backBtn.setPreferredSize(new Dimension(136, 99));
		cancelBtn.setPreferredSize(new Dimension(136, 99));
		this.total =  0;
			
		updatePanel();
		updateBtnEvent();
	}
	
	
	private void updatePanel(){
		this.setLayout(new BorderLayout());
		
		this.dateLabel.setFont(new Font("Sans", Font.BOLD, 25));
		this.recommandLabel.setFont(new Font("Sans", Font.BOLD, 20));
		this.ruleLabel.setFont(new Font("Sans", Font.BOLD, 11));
		this.ruleLabel.setPreferredSize(new Dimension(400, 400));
		this.totalLabel.setFont(new Font("Sans", Font.BOLD, 25));
		
		this.centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
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
				cancelBtn.doClick();
			}
		});
		/*
		 * 
		 * DB QUERY MODIFICATION NEEDED
		 * DATE TIME -> NOW() Function not working
		 * Solution 1 : Query direct modification.
		 * Solution 2 : Default value modification in myadmin Page
		 */
		this.cashBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 현금 결제
				ArrayList<CustomerOrder> orderList = parent.getOrderList();
				
				if(orderList.isEmpty()) return;	//Error Check
				
				parent.latestOrderID++; //order_id 1 추가
				for(int i=0; i<orderList.size(); i++){
					//System.out.println(orderList.get(i).getMenuName()
					//		+ " : " + MenuList.herbMenuInt.get(orderList.get(i).getMenuName()));
					//System.out.println("Order Count : " + orderList.get(i).getMenuCount());
					parent.os.db.exec("INSERT INTO herb_order ("
							+ "order_id, order_menu_id, order_count,  order_cash, order_date) VALUES( "
							+ parent.latestOrderID + ", " // order_id
							+ MenuList.herbMenuInt.get(orderList.get(i).getMenuName()).intValue() + ", "
							+ orderList.get(i).getMenuCount() +", " // order_count
							+ "1, "
							+ "now());"); // order_date 
					parent.resetTotal();
					cancelBtn.doClick();
					JOptionPane.showMessageDialog(null, "현금 결제가 완료되었습니다!");
				}
			}
		}); 
		/*
		 * 
		 * DB QUERY MODIFICATION NEEDED
		 * DATE TIME -> NOW() Function not working
		 * Solution 1 : Query direct modification.
		 * Solution 2 : Default value modification in myadmin Page
		 */
		this.cardBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 카드 결제
				ArrayList<CustomerOrder> orderList = parent.getOrderList();
				
				if(orderList.isEmpty()) return;	//Error Check
				
				parent.latestOrderID++; //order_id 1 추가
				for(int i=0; i<orderList.size(); i++){
					//System.out.println(orderList.get(i).getMenuName()
					//		+ " : " + MenuList.herbMenuInt.get(orderList.get(i).getMenuName()));
					//System.out.println("Order Count : " + orderList.get(i).getMenuCount());
					parent.os.db.exec("INSERT INTO herb_order ("
							+ "order_id, order_menu_id, order_count,  order_cash, order_date) VALUES( "
							+ parent.latestOrderID + ", " // order_id
							+ MenuList.herbMenuInt.get(orderList.get(i).getMenuName()).intValue() + ", "
							+ orderList.get(i).getMenuCount() +", " //order_count
							+ "0, " //order_cash
							+ "now());"); //order_date
					parent.resetTotal();
					cancelBtn.doClick();
					JOptionPane.showMessageDialog(null, "카드 결제가 완료되었습니다!");
				}
			}
		});
	}
	
	public void updateCenterPanel(){
		
		//결제 시간 업데이트
		Date dt = new Date();
		SimpleDateFormat dtForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = dtForm.format(dt.getTime());
		dateLabel.setText("결제 일시 : \t\t" + curTime);
		
		//총 결제액 업데이트
		String total = Integer.toString(this.total);
		totalLabel.setText("총 결제액 : \t" + total);
		
		//추천 메뉴 업데이트
		String recommand = "<html>추천 메뉴는 다음과 같습니다.<br>";
		recommand += PythonSyncModule.recommandString + "</html>";
		recommandLabel.setText(recommand);
		
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
		this.centerPanel.add(label);
		this.centerPanel.add(totalLabel);
		this.centerPanel.add(label);
		this.centerPanel.add(recommandLabel);
		this.centerPanel.add(label);
		//추천 메뉴
		this.centerPanel.add(ruleLabel);
		
		
	}
	
	public void setPayBackAction(ActionListener payBackActionListener){
		this.cancelBtn.addActionListener(payBackActionListener);
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
	
	public void getRecommand(ArrayList<CustomerOrder> orderList){
		if(orderList.isEmpty()) //주문 내역이 없을때
			return;
		if(MenuList.herbRecMenuList.isEmpty())
			return;
		//List가 비어있으면 따로 할일 없음
		
		//그렇지 않다면
		String recText = "<html>";
		boolean flag = false; //추천할 아이템이 텍스트에 나타나야 하는지 체크
		for(int i=0; i<orderList.size(); i++){
			if(MenuList.herbRecMenuList.get(
					orderList.get(i).getMenuName()) != null){
				flag = true;
				recText += orderList.get(i).getMenuName() + "에 대하여 " +
						MenuList.herbRecMenuList.get(orderList.get(i).getMenuName())
						+ "을 추천합니다.<br>";
			}
		}
		recText += "</html>";
		if(flag)//추천된 메뉴가 있는 경우
			recommandLabel.setText(recText);
	}
	
	public void refreshDate(){
		Date dt = new Date();
		SimpleDateFormat dtForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = dtForm.format(dt.getTime());
		dateLabel.setText("결제 일시 : \t\t" + curTime);
	}

}
