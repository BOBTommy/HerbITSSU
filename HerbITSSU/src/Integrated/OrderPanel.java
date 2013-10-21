package Integrated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Database.CustomerOrder;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.slidinglayout.SLSide;

class OrderPanel extends JPanel {
	OrderSystem os;
	
	private HashMap<JButton, String> categoryList = new HashMap<JButton, String>();
	private HashMap<JButton, String> menuList = new HashMap<JButton, String>();
	
	private final JSplitPane masterPanel = new JSplitPane();
	private final SLPanel basePanel = new SLPanel();
	private SLConfig payCfg, orderListCfg;
	
	//Menu Panels
	private JPanel categoryPanel = new JPanel(), menuPanel = new JPanel();
	private JScrollPane categoryScroll = new JScrollPane(categoryPanel,
															JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
															JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JScrollPane menuScroll = new JScrollPane(menuPanel,
															JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
															JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	//OrderListPanel
	private DefaultTableModel orderListTableModel = new DefaultTableModel();
	private JTable orderListTable = new JTable(orderListTableModel);
	private JScrollPane orderListTableScroll = new JScrollPane(orderListTable);
	int currentNumber = 0; // 주문중인 메뉴 수
	private int orderTotal; //주문 총 합계
	final int maximumNumber = 60; // 한 번에 총 주문가능한 메뉴 수
	private String columnNames[] = { "메뉴명", "수량", "단가", "메뉴별 합계" };
	/* data[][0]: 메뉴명, data[][1]: 주문수량, data[][2]: 단가, data[][3]: 메뉴주문수량별합계 */
	private ArrayList<CustomerOrder> orderList = new ArrayList<CustomerOrder>();
	public int latestOrderID;
	
	//Pay Panel
	private PayPane payPane = new PayPane("결제창 샘플", this);
	private JButton payButton;
	
	//Mody Use Variable
	private String modyName;
	private boolean modyMode = false;
	

	public OrderPanel(OrderSystem os,boolean modyMode) {
		this.os = os;
		this.orderTotal = 0;
		this.latestOrderID = 0; //가장 최근 거래 번호를 초기화
		this.modyMode = modyMode;
		
		//Menu Panels
		categoryScroll.setBorder(null);
		categoryPanel.setLayout(new ModifiedFlowLayout());
		menuScroll.setBorder(null);
		menuPanel.setLayout(new ModifiedFlowLayout());
		if (os.db.isConnected()) {
			try { //Loading Categories
				java.sql.ResultSet rs = os.db
						.exec("select distinct menu_category from herb_menu order by menu_category");
				
				categoryPanel.removeAll();
				categoryList.clear();
				ImageIcon imageIcon;
				JButton categoryBtn;
				while (rs.next()) {
					imageIcon = new ImageIcon("image/order/" + rs.getString("menu_category").replace("/", "") + ".png");
					categoryBtn = new JButton(imageIcon);
					categoryBtn.setRolloverIcon(new ImageIcon("image/order/" + rs.getString("menu_category").replace("/", "") + "_over.png"));
					categoryBtn.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
					categoryBtn.addActionListener(new categoryListener());
					categoryList.put(categoryBtn, rs.getString("menu_category"));
					categoryPanel.add(categoryBtn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		ImageIcon imageIcon = new ImageIcon("image/order/ordertap_payment.png");
		payButton = new JButton(imageIcon);
		payButton.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		payButton.addActionListener(new ActionRunner(payAction));
		
		//OrderListPanel
		orderListTableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		orderListTableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		orderListTable.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		orderListTable.setRowHeight(30);

		updateOrderTable();
				
		//PayPanel
		payPane.setPayBackAction(new ActionRunner(orderListAction));
		
		//Sliding Configs
		
		if(modyMode)
		{
			payCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f)
					.place(0, 0, orderListTableScroll);
			orderListCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f)
					.place(0, 0, menuScroll);
		}
		else
		{
			payCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f).col(582)
					.place(0, 0, orderListTableScroll).place(0, 1, payPane);
			orderListCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f).col(2f)
					.place(0, 0, menuScroll).place(0, 1, orderListTableScroll);
		}
		//basePanel
		basePanel.setTweenManager(SLAnimator.createTweenManager());
		basePanel.initialize(orderListCfg);
		
		//masterPanel
		masterPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		masterPanel.setDividerSize(1);
		masterPanel.setDividerLocation(140);
		masterPanel.setLeftComponent(categoryScroll);
		masterPanel.setRightComponent(basePanel);
		
		this.setLayout(new BorderLayout());
		this.add(masterPanel, BorderLayout.CENTER);
	}

	private final Runnable payAction = new Runnable() {
		@Override
		public void run() {
			payPane.setTotal(orderTotal);
			payPane.refreshDate();
			//추천 메뉴 스트링 촏기화
			PythonSyncModule.recommandString = "추천 메뉴를 등록하기에는 누적된 주문량이 부족합니다.";
			payPane.getRecommand(orderList);
			basePanel
					.createTransition()
					.push(new SLKeyframe(payCfg, 0.6f)
							.setEndSide(SLSide.LEFT, menuScroll)
							.setStartSide(SLSide.RIGHT, payPane)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
								}
							})).play();
		}
	};

	private final Runnable orderListAction = new Runnable() {
		@Override
		public void run() {
			basePanel
					.createTransition()
					.push(new SLKeyframe(orderListCfg, 0.6f)
							.setStartSide(SLSide.LEFT, menuScroll)
							.setEndSide(SLSide.RIGHT, payPane)
							.setCallback(new SLKeyframe.Callback() {
								@Override
								public void done() {
								}
							})).play();
		}
	};
	
	private class ActionRunner implements ActionListener {
		Runnable action;
		ActionRunner(Runnable action) { this.action = action; }
		public void actionPerformed(ActionEvent e) {
			if (action != null) action.run();
		}
	}
	
	private class categoryListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton tmp1 = (JButton) e.getSource();
			String name = categoryList.get(tmp1);
			loadMenu(name);
		}
	}
	
	private class menuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton tmp1 = (JButton) e.getSource();
			String name = menuList.get(tmp1);
			if (modyMode) { modyName=name; return; } //In case of ModyMode
			
			int price = 0;
			try {
				java.sql.ResultSet rs = os.db
						.exec("select * from herb_menu where menu_name = '"
								+ name + "'");
				if (rs.next()) {
					price = Integer.parseInt(rs.getString("menu_price"));
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			
			boolean isExist = false;
			for(int i=0; i< orderList.size(); i++){
				if(orderList.get(i).getMenuName().compareTo(name) == 0)
				{
					orderList.get(i).addMenuCount();
					isExist = true;
				}
			}
			
			if( !isExist ){
				orderList.add(new CustomerOrder(name, 1, price));
			}
			
			orderTotal += price;	//총 주문 합계에 현재 주문된 값을 더한다.
			updateOrderTable();
		}
	}
	
	public void loadMenu(String category) {
		try {
			java.sql.ResultSet rs = os.db
					.exec("select * from herb_menu where menu_category = '"	+ category + "'");
			
			menuPanel.removeAll();
			menuList.clear();
			if (!modyMode) menuPanel.add(payButton); //In case of Menu Modification Mode
			//ImageIcon imageIcon;
			JButton menuBtn;
			while (rs.next()) {
				//imageIcon = new ImageIcon("image/order/" + rs.getString("menu_name") + ".png");
				menuBtn = new JButton();
				String text = rs.getString("menu_name") ;
				int idx = text.indexOf("(");
				if (idx != -1) {
					text = text.substring(0, idx) + 
							"<br>" +
							text.substring(idx);
				}
				menuBtn.setText("<html><body style='text-align:center;'>" + text + "</body></html>");				
				menuBtn.setPreferredSize(new Dimension(160, 60));
				menuBtn.setMargin(new Insets(0,0,0,0));
				menuBtn.setBackground(new Color(255,255,255));
				
				menuBtn.addActionListener(new menuListener());
				menuList.put(menuBtn, rs.getString("menu_name"));
				menuPanel.add(menuBtn);
			}
			menuPanel.updateUI();
			
			ResultSet menuListQuery = os.db.exec("SELECT * FROM herb_menu");
			try{//herb_menu table 에서 메뉴 내용을 얻어옴
				while(menuListQuery.next()){
					MenuList.herbMenuInt.put(menuListQuery.getString(2), //menu_name
							new Integer(menuListQuery.getInt(1)) // menu_id
							);
				}
				ResultSet orderQuery = os.db.exec("SELECT order_id FROM herb_order");
				while(orderQuery.next()){
					if(this.latestOrderID < orderQuery.getInt(1))
						this.latestOrderID = orderQuery.getInt(1);
					//가장 최근의 orderID를 받아온다.
				}
			}catch(SQLException ex){
				ex.printStackTrace();
			}
			//End of Menu Recommend
			
		} catch (SQLException e) {

		}
	}

	private void updateOrderTable() {
		String data[][] = new String[orderList.size()][4];
		for (int i = 0; i < orderList.size(); i++) {
			data[i][0] = orderList.get(i).getMenuName();
			data[i][1] = Integer.toString(orderList.get(i).getMenuCount());
			data[i][2] = Integer.toString(orderList.get(i).getMenuPrice());
			data[i][3] = Integer.toString(orderList.get(i).getTotalPrice());
		}
		
		orderListTableModel.setDataVector(data, columnNames);
		orderListTable.getColumnModel().getColumn(0).setPreferredWidth(300);

	}
	
	public void resetTotal(){	//주문취소(초기화시)
		this.orderTotal = 0;
		this.orderList.clear();
		updateOrderTable();
	}
	
	public ArrayList<CustomerOrder> getOrderList(){
		return this.orderList;
	}
	
	
	// Mody Use Method
	public String returnName()
	{
		return modyName;
	}
	
	public void dataUpdate() { // 수정시 변경되어야할 부분

	}
}
