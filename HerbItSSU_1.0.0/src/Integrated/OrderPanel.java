package Integrated;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.slidinglayout.SLSide;

import AnimationComponent.AniButton;

import java.awt.event.*;



class OrderPanel extends JPanel {
	
	class menuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton tmp1 = (JButton)e.getSource();
			String name = tmp1.getText();
			
			String price = "0";
			int numberOfItem =0 , sumOfEachItemPrice =0 ;		//단가, 메뉴별 합계
			/* 반복하며 가격을 받아온다. */
			for (int tmp=0; tmp<menuList.length; tmp++ ) {
				if (menuList[tmp].compareTo(name)==0)
				{
					price = priceList[tmp];
				}
			}
			/* 반복하며 수량과 메뉴별 합계를 받아온다. */
			boolean sameMenuPickFlag = false;
			int pos = currentNumber;
			for (int tmp=0; tmp<currentNumber; tmp++) {
				if (name.compareTo(data[tmp][0])==0) {
					numberOfItem = Integer.valueOf(data[tmp][1]).intValue()
							+ 1;
					sumOfEachItemPrice = Integer.valueOf(data[tmp][3]).intValue()
							+ Integer.valueOf(price).intValue();
					pos = tmp;
					sameMenuPickFlag = true;
				}
			}
			if (sameMenuPickFlag == false) {
				numberOfItem = 1;
				sumOfEachItemPrice = Integer.valueOf(price).intValue();
				currentNumber ++;
			}
			/* 받아오기 끝 */
			/* 테이블에 배정 */
			/* data[][0]: 메뉴명, data[][1]: 주문수량, data[][2]: 단가, data[][3]: 메뉴주문수량별합계*/
			data[pos][0] = name;
			data[pos][1] = Integer.toString(numberOfItem);
			data[pos][2] = price;
			data[pos][3] = Integer.toString(sumOfEachItemPrice);
			
			orderListTableModel.setDataVector(data, columnNames);
			
			
		}	
	}
	
	OrderSystem os;
	
	private AniButton payButton = new AniButton("결제");
	private AniButton cancel = new AniButton("취소");
	private final SLPanel basePanel;
	private SLConfig payCfg,payBackCfg;
	private PayPane payPane = new PayPane("결제창 샘플");
	
	private JTable orderListTable;
	private DefaultTableModel orderListTableModel;
	private JScrollPane orderListTableScroll;
	private String data[][] = new String[30][4];		//주문한 목록의 데이터셋, 30줄 4열
	int currentNumber = 0;							//주문중인 메뉴 수
	final int maximumNumber = 60; 						//한 번에 총 주문가능한 메뉴 수
	
	private String menuList[] = {				//버튼에 들어갈 메뉴명
			"커피1", "커피2", "커피3", "커피4", "커피5", "커피6", "커피7", "커피8", "커피9"
			,"커피10", "커피11", "커피12", "커피13", "커피14", "커피15", "커피16", "커피17", "커피18", "커피19", "커피20"
	};
	private JScrollPane menuListScroll;
	private String priceList[] = {
			"1100", "1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900",
			"2000", "2100", "2200", "2300", "2400", "2500", "2600", "2700", "2800", "2900", "3000"
	};
	private JButton menuBtn[] = new JButton[menuList.length];
	JButton cash, card;
	private String columnNames[] = { "메뉴명", "수량", "단가", "메뉴별 합계"};
	/* data[][0]: 메뉴명, data[][1]: 주문수량, data[][2]: 단가, data[][3]: 메뉴주문수량별합계*/
	private JPanel leftPanel;					//PIXEL 설정
	private JPanel cardCash;					//버튼두개의 패널
	
	
	public OrderPanel( OrderSystem os , SLPanel basePanel, SLConfig payCfg, SLConfig payBackCfg) {
		this.os = os;
		this.basePanel = basePanel;
		this.payCfg = os.getPayCfg();
		this.payBackCfg = os.getPayBackCfg();
		
		/* 테이블 레이아웃 세팅 시작 */
		orderListTable = new JTable();
		orderListTableModel = new DefaultTableModel(data, columnNames);
		orderListTable.setModel(orderListTableModel);
		
		orderListTableScroll = new JScrollPane(orderListTable);
		orderListTableScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		orderListTableScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		/* 테이블 레이아웃 세팅 끝 */
		this.setLayout(new BorderLayout());
		this.add(orderListTableScroll, BorderLayout.EAST);
		leftPanel = new JPanel();
		cardCash = new JPanel();
		this.add(cardCash, BorderLayout.SOUTH);
		/* 픽셀작업 필요한부분 시작 PIXEL*/
		leftPanel.setLayout( new ModifiedFlowLayout() );
		cardCash.setLayout(new FlowLayout());
		/* 픽셀작업 필요한부분 끝 PIXEL*/
		
		menuListScroll = new JScrollPane(leftPanel);
		menuListScroll
			.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		menuListScroll
			.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(menuListScroll, BorderLayout.CENTER);
		for( int tmp=0; tmp<menuList.length; tmp++)
		{
			menuBtn[tmp] = new JButton(menuList[tmp]);
			menuBtn[tmp].addActionListener(new menuListener());
			/* 픽셀작업 필요한 부분 2 */
			leftPanel.add(menuBtn[tmp]);
			/* 픽셀작업 필요한부분 2끝 */
		}
		
		payButton.setAction(payAction);
		cardCash.add(payButton);
		
		cancel.setAction(payBackAction);

		/*
		cancel = new JButton("취소");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for(int x=0 ; x < data.length ; x++) {
					data[x][0] = "";
					data[x][1] = "";
					data[x][2] = "";
					data[x][3] = "";
				}
				currentNumber=0;
				orderListTableModel.setDataVector(data, columnNames);
			}
			
		});*/
		cardCash.add(cancel);
		/* 픽셀작업 필요한 부분 3 끝*/
		
		orderListTableModel.setDataVector(data, columnNames);
		
	}
	public void dataUpdate() {			//수정시 변경되어야할 부분
		
		
	}
	
	private final Runnable payAction = new Runnable() {
		@Override
		public void run() {
			PayPane.payWorking = true; // 결제창이 출력되어있는 도중 
				//타 Pane으로 접근시 flag로 활용
			basePanel.createTransition()
				.push(new SLKeyframe(payCfg, 0.6f)
				.setStartSide(SLSide.RIGHT, payPane)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
				}}))
				.play();
		}
	};
	
	private final Runnable payBackAction = new Runnable() {
		@Override
		public void run() {
			PayPane.payWorking = false;
			basePanel.createTransition()
				.push(new SLKeyframe(payBackCfg, 0.6f)
				.setEndSide(SLSide.RIGHT, payPane)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
				}}))
				.play();
		}
	};
	
	public PayPane getPayPane(){
		return this.payPane;
	}
	
	public void setCfg(){
		this.payCfg = os.getPayCfg();
		this.payBackCfg = os.getPayBackCfg();
	}
	
	public void payActionPush(){
		this.payButton.getAniAction().run();
	}
	
	public void cancelPayAction(){
		this.cancel.getAniAction().run();
	}
	
}
