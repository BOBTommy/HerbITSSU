package Integrated;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

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
	
	private String modyName;
	private int flag=0;
	private int orderTotal; //주문 총 합계
	
	public String returnName()
	{
		return modyName;
	}
	class menuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton tmp1 = (JButton) e.getSource();
			String name = tmp1.getText();
			modyName=name;
			String price = "0";
			int numberOfItem = 0, sumOfEachItemPrice = 0; // 단가, 메뉴별 합계

			try {
				java.sql.ResultSet rs = os.db
						.exec("select * from herb_menu where menu_name = '"
								+ name + "'");
				int cnt = 0;
				while (rs.next()) {
					price = rs.getString("menu_price");
				}
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
			}

			/* 반복하며 수량과 메뉴별 합계를 받아온다. */
			boolean sameMenuPickFlag = false;
			int pos = currentNumber;
			for (int tmp = 0; tmp < currentNumber; tmp++) {
				if (name.compareTo(data[tmp][0]) == 0) {
					numberOfItem = Integer.valueOf(data[tmp][1]).intValue() + 1;
					sumOfEachItemPrice = Integer.valueOf(data[tmp][3])
							.intValue() + Integer.valueOf(price).intValue();
					pos = tmp;
					sameMenuPickFlag = true;
				}
			}
			if (sameMenuPickFlag == false) {
				numberOfItem = 1;
				sumOfEachItemPrice = Integer.valueOf(price).intValue();
				currentNumber++;
			}
			/* 받아오기 끝 */
			/* 테이블에 배정 */
			/*
			 * data[][0]: 메뉴명, data[][1]: 주문수량, data[][2]: 단가, data[][3]:
			 * 메뉴주문수량별합계
			 */
			data[pos][0] = name;
			data[pos][1] = Integer.toString(numberOfItem);
			data[pos][2] = price;
			data[pos][3] = Integer.toString(sumOfEachItemPrice);
			if(flag==0)
				orderListTableModel.setDataVector(data, columnNames);
			
			orderTotal += Integer.parseInt(price);	//총 주문 합계에 현재 주문된 값을 더한다.

		}
	}

	class categoryListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton tmp1 = (JButton) e.getSource();
			String name = tmp1.getText();

			loadMenu(name);
		}
	}

	OrderSystem os;

	private AniButton payButton = new AniButton("결제하기");
	private AniButton cancel = new AniButton("뒤로가기");
	private final SLPanel basePanel = new SLPanel();
	private SLConfig payCfg, payBackCfg;
	private PayPane payPane = new PayPane("결제창 샘플", this);

	private JTable orderListTable;
	private DefaultTableModel orderListTableModel;
	private JScrollPane orderListTableScroll;
	private String data[][] = new String[30][4]; // 주문한 목록의 데이터셋, 30줄 4열
	int currentNumber = 0; // 주문중인 메뉴 수
	final int maximumNumber = 60; // 한 번에 총 주문가능한 메뉴 수

	private JScrollPane menuListScroll;
	private ArrayList<JButton> categoryBtn = new ArrayList<JButton>();
	private ArrayList<JButton> menuBtn = new ArrayList<JButton>();
	JButton cash, card;
	private String columnNames[] = { "메뉴명", "수량", "단가", "메뉴별 합계" };
	/* data[][0]: 메뉴명, data[][1]: 주문수량, data[][2]: 단가, data[][3]: 메뉴주문수량별합계 */
	private JPanel categoryPanel, menuPanel; // PIXEL 설정
	private JPanel cardCash; // 버튼두개의 패널
	private JPanel pane;
	
	

	public void loadMenu(String category) {
		try {
			System.out.println("Load Menu: " + category);
			JButton tmpBtn;

			java.sql.ResultSet rs = os.db
					.exec("select * from herb_menu where menu_category = '"
							+ category + "'");

			menuPanel.setLayout(new GridLayout(rs.getFetchSize(), 2));
			menuPanel.removeAll();
			menuBtn.clear();
			while (rs.next()) {
				tmpBtn = new JButton(rs.getString("menu_name"));
				tmpBtn.addActionListener(new menuListener());
				menuBtn.add(tmpBtn);
				menuPanel.add(tmpBtn);
			}
			menuPanel.add(payButton);
			menuPanel.updateUI();
		} catch (SQLException e) {

		}
	}

	public OrderPanel(OrderSystem os, int i) {//mody event용 
		this.os = os;
		flag=i;
		categoryPanel = new JPanel();
		menuPanel = new JPanel();
		cardCash = new JPanel();
		pane = new JPanel();
		this.orderTotal = 0; //주문 총 합계

		this.setLayout(new BorderLayout());
		pane.setLayout(new BorderLayout());

		JSplitPane splitPane = new JSplitPane(currentNumber, categoryPanel,
				basePanel);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerSize(1);
		splitPane.setDividerLocation(100);
		splitPane.setLeftComponent(categoryPanel);
		splitPane.setRightComponent(basePanel);

		this.add(splitPane, BorderLayout.CENTER);

		orderListTableScroll = new JScrollPane(orderListTable);
		orderListTableScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		orderListTableScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		/* 픽셀작업 필요한부분 시작 PIXEL */
		menuPanel.setLayout(new ModifiedFlowLayout());
		cardCash.setLayout(new FlowLayout());
		/* 픽셀작업 필요한부분 끝 PIXEL */
		
		menuListScroll = new JScrollPane(menuPanel);
		menuListScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		menuListScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		try {
			java.sql.ResultSet rs = os.db
					.exec("select distinct menu_category from herb_menu");

			JButton tmpBtn;
			categoryPanel.setLayout(new GridLayout(rs.getFetchSize(), 1));
			categoryBtn.clear();
			while (rs.next()) {
				tmpBtn = new JButton(rs.getString("menu_category"));
				tmpBtn.addActionListener(new categoryListener());
				categoryBtn.add(tmpBtn);
				categoryPanel.add(tmpBtn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		payCfg = new SLConfig(basePanel).gap(10, 10).row(1f).col(2f).col(1f)
				.place(0, 0, orderListTableScroll);

		payBackCfg = new SLConfig(basePanel).gap(10, 10).row(1f).col(1f).place(0, 0, menuListScroll);

		basePanel.setTweenManager(SLAnimator.createTweenManager());
		basePanel.initialize(payBackCfg);

	}

	public OrderPanel(OrderSystem os) {
		this.os = os;

		categoryPanel = new JPanel();
		menuPanel = new JPanel();
		cardCash = new JPanel();
		pane = new JPanel();

		this.setLayout(new BorderLayout());
		pane.setLayout(new BorderLayout());
		// pane.add(basePanel, BorderLayout.CENTER);
		// pane.add(cardCash, BorderLayout.SOUTH);

		JSplitPane splitPane = new JSplitPane(currentNumber, categoryPanel,
				basePanel);
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

		/* 픽셀작업 필요한부분 시작 PIXEL */
		menuPanel.setLayout(new ModifiedFlowLayout());
		cardCash.setLayout(new FlowLayout());
		/* 픽셀작업 필요한부분 끝 PIXEL */

		menuListScroll = new JScrollPane(menuPanel);
		menuListScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		menuListScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		try {
			java.sql.ResultSet rs = os.db
					.exec("select distinct menu_category from herb_menu");

			JButton tmpBtn;
			categoryPanel.setLayout(new GridLayout(rs.getFetchSize(), 1));
			categoryBtn.clear();
			while (rs.next()) {
				tmpBtn = new JButton(rs.getString("menu_category"));
				tmpBtn.addActionListener(new categoryListener());
				categoryBtn.add(tmpBtn);
				categoryPanel.add(tmpBtn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		payButton.setAction(payAction);
		// cardCash.add(payButton);

		cancel.setAction(payBackAction);
		// cardCash.add(cancel);
		payPane.addCancelButton(cancel);

		orderListTableModel.setDataVector(data, columnNames);

		payCfg = new SLConfig(basePanel).gap(10, 10).row(1f).col(2f).col(1f)
				.place(0, 0, orderListTableScroll).place(0, 1, payPane);

		payBackCfg = new SLConfig(basePanel).gap(10, 10).row(1f).col(1f)
				.col(2f).place(0, 0, menuListScroll)
				.place(0, 1, orderListTableScroll);

		basePanel.setTweenManager(SLAnimator.createTweenManager());
		basePanel.initialize(payBackCfg);

	}

	public void dataUpdate() { // 수정시 변경되어야할 부분

	}

	private final Runnable payAction = new Runnable() {
		@Override
		public void run() {
			basePanel
					.createTransition()
					.push(new SLKeyframe(payCfg, 0.6f)
							.setEndSide(SLSide.LEFT, menuListScroll)
							.setStartSide(SLSide.RIGHT, payPane)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
								}
							})).play();
		}
	};

	private final Runnable payBackAction = new Runnable() {
		@Override
		public void run() {
			basePanel
					.createTransition()
					.push(new SLKeyframe(payBackCfg, 0.6f)
							.setStartSide(SLSide.LEFT, menuListScroll)
							.setEndSide(SLSide.RIGHT, payPane)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
								}
							})).play();
		}
	};

	public void payActionPush() {
		this.payButton.getAniAction().run();
	}

	public void cancelPayAction() {
		this.cancel.getAniAction().run();
	}

	public static class Accessor extends SLAnimator.ComponentAccessor {

	}
	
	public void resetTotal(){	//주문취소(초기화시)
		this.orderTotal = 0;
	}
	
	public int getTotal(){		//주문 총합계 반환
		return this.orderTotal;
	}
}
