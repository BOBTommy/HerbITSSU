package Integrated;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import AnimationComponent.AniButton;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.slidinglayout.SLSide;



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
	
	private AniButton payButton = new AniButton("결제하기");
	private AniButton cancel = new AniButton("결제취소");
	private final SLPanel basePanel = new SLPanel();
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
	private JPanel categoryPanel, menuPanel;					//PIXEL 설정
	private JPanel cardCash;					//버튼두개의 패널
	private JPanel pane;
	
	
	public OrderPanel( OrderSystem os ) {
		this.os = os;
		
		categoryPanel = new JPanel();
		menuPanel = new JPanel();
		cardCash = new JPanel();
		pane = new JPanel();
		
		this.setLayout(new BorderLayout());
		pane.setLayout(new BorderLayout());
		//pane.add(basePanel, BorderLayout.CENTER);
		//pane.add(cardCash, BorderLayout.SOUTH);
		
		JSplitPane splitPane = new JSplitPane(currentNumber, categoryPanel, basePanel);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerSize(1);
		splitPane.setDividerLocation(100);
		splitPane.setLeftComponent(categoryPanel);
		splitPane.setRightComponent(basePanel);
		
		this.add(splitPane, BorderLayout.CENTER);
		
		
		/* 테이블 레이아웃 세팅 시작 */
		orderListTable = new JTable();
		orderListTableModel = new DefaultTableModel(data, columnNames);
		orderListTable.setModel(orderListTableModel);
		
		orderListTableScroll = new JScrollPane(orderListTable);
		orderListTableScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		orderListTableScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		/* 픽셀작업 필요한부분 시작 PIXEL*/
		menuPanel.setLayout( new ModifiedFlowLayout() );
		cardCash.setLayout(new FlowLayout());
		/* 픽셀작업 필요한부분 끝 PIXEL*/
		
		menuListScroll = new JScrollPane(menuPanel);
		menuListScroll
			.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		menuListScroll
			.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		for( int tmp=0; tmp<menuList.length; tmp++)
		{
			categoryPanel.add(new JButton("Cate-" + menuList[tmp]));
		}
		
		for( int tmp=0; tmp<menuList.length; tmp++)
		{
			menuBtn[tmp] = new JButton(menuList[tmp]);
			menuBtn[tmp].addActionListener(new menuListener());
			/* 픽셀작업 필요한 부분 2 */
			menuPanel.add(menuBtn[tmp]);
			/* 픽셀작업 필요한부분 2끝 */
		}
		
		payButton.setAction(payAction);
		//cardCash.add(payButton);
		menuPanel.add(payButton);
		
		cancel.setAction(payBackAction);
		//cardCash.add(cancel);
		payPane.add(cancel);
		
		orderListTableModel.setDataVector(data, columnNames);
		
		payCfg = new SLConfig(basePanel)
		.gap(10, 10)
		.row(1f).col(2f).col(1f)
		.place(0, 0, orderListTableScroll)
		.place(0, 1, payPane);

		payBackCfg = new SLConfig(basePanel)
		.gap(10, 10)
		.row(1f).col(1f).col(2f)
		.place(0, 0, menuListScroll)
		.place(0, 1, orderListTableScroll);
		
		basePanel.setTweenManager(SLAnimator.createTweenManager());
		basePanel.initialize(payBackCfg);
		
	}
	public void dataUpdate() {			//수정시 변경되어야할 부분
		
		
	}
	
	private final Runnable payAction = new Runnable() {
		@Override
		public void run() {
			basePanel.createTransition()
				.push(new SLKeyframe(payCfg, 0.6f)
				.setEndSide(SLSide.LEFT, menuListScroll)
				.setStartSide(SLSide.RIGHT, payPane)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
				}}))
				.play();
		}
	};
	
	private final Runnable payBackAction = new Runnable() {
		@Override
		public void run() {
			basePanel.createTransition()
				.push(new SLKeyframe(payBackCfg, 0.6f)
				.setStartSide(SLSide.LEFT, menuListScroll)
				.setEndSide(SLSide.RIGHT, payPane)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
				}}))
				.play();
		}
	};
	
	public void payActionPush(){
		this.payButton.getAniAction().run();
	}
	
	public void cancelPayAction(){
		this.cancel.getAniAction().run();
	}
	
	public static class Accessor extends SLAnimator.ComponentAccessor{
		
	}
}
