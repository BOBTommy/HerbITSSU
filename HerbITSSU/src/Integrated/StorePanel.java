package Integrated;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.slidinglayout.SLSide;


class StorePanel extends JPanel{
	private static final int INVEN_MODE = 0;
	private static final int GRAPH_MODE = 1;
	private static final int REGIS_MODE = 2;
	private static final int MODIF_MODE = 3;
	
	private OrderSystem os;
	
	private final JSplitPane masterPanel = new JSplitPane();
	private final SLPanel basePanel = new SLPanel();
	private SLConfig invenCfg, graphCfg, regisCfg, modifCfg;
	
	private int currentMode = INVEN_MODE; 
	private JPanel controlPanel = new JPanel();
	private JPanel inventoryPanel = new JPanel();
	private JPanel modificationPanel = new JPanel();
	private StoreGraphPanel inventoryGraph = new StoreGraphPanel();
	
	private JButton controlInven = new JButton("<html><body style='height: 20px; vertical-align:middle; font-size: 12px'>목록 관리</body></html>");
	private JButton controlRegis = new JButton("<html><body style='height: 20px; vertical-align:middle; font-size: 12px'>항목 추가</body></html>");
	private JButton controlModif = new JButton("<html><body style='height: 20px; vertical-align:middle; font-size: 12px'>항목 수정</body></html>");
	
	public StorePanel(OrderSystem os) {
		this.os = os;
		
		//controlPanel
		controlInven.addActionListener(new ControlListener());
		controlRegis.addActionListener(new ControlListener());
		controlModif.addActionListener(new ControlListener());
		controlPanel.add(controlInven);
		controlPanel.add(controlRegis);
		controlPanel.add(controlModif);
		
		//inventoryPanel
		inventoryPanel.setBackground(Color.BLUE);
		inventoryPanel.setLayout(new GridLayout(10, 10));
		loadInventory();
		
		//modificationPanel
		modificationPanel.setBackground(Color.MAGENTA);
		
		
		//Sliding Configs
		invenCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f)
				.place(0, 0, inventoryPanel);
		graphCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f).col(4f)
				.place(0, 0, inventoryPanel).place(0, 1, inventoryGraph);
		regisCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f).col(4f)
				.place(0, 0, inventoryPanel).place(0, 1, modificationPanel);
		modifCfg = regisCfg;
		
		//basePanel
		basePanel.setTweenManager(SLAnimator.createTweenManager());
		basePanel.initialize(invenCfg);
		
		masterPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		masterPanel.setDividerSize(1);
		masterPanel.setDividerLocation(50);
		masterPanel.setLeftComponent(controlPanel);
		masterPanel.setRightComponent(basePanel);
		
		this.setLayout(new BorderLayout());
		this.add(masterPanel, BorderLayout.CENTER);
	}
	
	private final Runnable invenAction = new Runnable() {
		public void run() {
			SLKeyframe SLkf = new SLKeyframe(invenCfg, 0.6f);
			
			if (currentMode == GRAPH_MODE) {
				SLkf.setEndSide(SLSide.RIGHT, inventoryGraph);
			} else if (currentMode == REGIS_MODE || currentMode == MODIF_MODE) {
				SLkf.setEndSide(SLSide.RIGHT, modificationPanel);
			}
			
			basePanel.createTransition().push(SLkf).play();
			currentMode = INVEN_MODE;
		}
	};
	
	private final Runnable graphAction = new Runnable() {
		public void run() {
			SLKeyframe SLkf = new SLKeyframe(graphCfg, 0.6f);
			
			if (currentMode == INVEN_MODE) {
				SLkf.setStartSide(SLSide.RIGHT, inventoryGraph);
			} else if (currentMode == REGIS_MODE || currentMode == MODIF_MODE) {
				SLkf.setEndSide(SLSide.RIGHT, modificationPanel)
					.setStartSide(SLSide.RIGHT, inventoryGraph);
			}
			
			basePanel.createTransition().push(SLkf).play();
			currentMode = GRAPH_MODE;
		}
	};
	
	private final Runnable regisAction = new Runnable() {
		public void run() {
			SLKeyframe SLkf = new SLKeyframe(regisCfg, 0.6f);
			
			if (currentMode == INVEN_MODE) {
				SLkf.setStartSide(SLSide.RIGHT, modificationPanel);
			} else if (currentMode == GRAPH_MODE) {
				SLkf.setEndSide(SLSide.RIGHT, inventoryGraph)
					.setStartSide(SLSide.RIGHT, modificationPanel);
			}
			
			basePanel.createTransition().push(SLkf).play();
			currentMode = REGIS_MODE;
		}
	};
	
	class ControlListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton src = (JButton) e.getSource();
			
			if (src == controlInven) {
				invenAction.run();
			} else if (src == controlRegis) {
				regisAction.run();
			} else if (src == controlModif) {
				regisAction.run();
			}
		}
	}
	
	class InventoryListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton src = (JButton) e.getSource();
			String invenName = src.getText();
			String sql = "select * from herb_inventory where "
					+ "inventory_name = '" + invenName + "'";
			
			try {
				java.sql.ResultSet rs = os.db.exec(sql);
				rs.next();
				
				sql = "select * from herb_invenlog where "
						+ "invenlog_inventory_id = '" + rs.getInt("inventory_id") + "' "
						+ "ORDER BY invenlog_regdate DESC";
				rs = os.db.exec(sql);
				
				System.out.println(invenName + " -----------------------------");
				int num = 10;
				int cnt = 0;
				int tmpPoints[] = new int[num];
				String tmpLabels[] = new String[num];
				while (rs.next() && (num-cnt > 0)) {
					System.out.println(
							rs.getInt("invenlog_id") + "|"
							+ rs.getInt("invenlog_inventory_id")	+ "|"
							+ rs.getInt("invenlog_value")+ "|"
							+ DateUtil.convTimeValueToUserTypedString(DateUtil.convUserTypedStringToTimeValue(rs.getString("invenlog_regdate"), "yyyy-MM-dd kk:mm:ss"), "MM-dd")
							);
					
					tmpLabels[cnt] = DateUtil.convTimeValueToUserTypedString(DateUtil.convUserTypedStringToTimeValue(rs.getString("invenlog_regdate"), "yyyy-MM-dd kk:mm:ss"), "MM-dd");
					tmpPoints[cnt] = rs.getInt("invenlog_value");
					cnt++;
				}
				
				inventoryGraph.setPoints(tmpPoints, tmpLabels);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			
			graphAction.run();
		}
	}
	
	private void loadInventory() {
		try {
			/*
			os.db.exec("DELETE FROM herb_invenslog;");
			
			// Test Data Injection
			String sql;
			for (int i = 1; i <= 140; i++) {
				sql = "insert herb_invenlog("
						+ "invenlog_id, invenlog_inventory_id, invenlog_value, invenlog_regdate) "
						+ "values("
						+ "'" + Integer.toString(i) + "', " //inventory_id
						+ "'" + Integer.toString(((i-1) % 20) + 1) + "', " //inventory_name
						+ "'" + Integer.toString(new Random().nextInt(40)) + "', "
						+ "date_add(now(), interval -" + Integer.toString(7-(i-1)/20) + " day))";
				os.db.exec( //inventory_regdate
						sql
				);
			}
			*/
			
			String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
			SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
			
			JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
			buttonPanel.add(new JButton("Plus"));
			buttonPanel.add(new JButton("Minus"));
			
			inventoryPanel.add(buttonPanel);
			
			//Load Inventory List and Create Buttons
			
			java.sql.ResultSet rs = os.db
					.exec("select * from herb_inventory");
			
			int cnt = 0;
			while (rs.next()) {
				inventoryBtn[cnt] = new JButton(rs.getString("inventory_name"));
				inventoryBtn[cnt].addActionListener(new InventoryListener());
				inventoryPanel.add(inventoryBtn[cnt]);
				
				System.out.println(
						rs.getInt("inventory_id") + "|"
						+ rs.getString("inventory_name")	+ "|"
						+ rs.getString("inventory_unit")+ "|"
						+ format.parse(rs.getString("inventory_regdate"),
								new ParsePosition(0))
						);
			}
			
			
			
			//Load Inventory Logs
			
			System.out.println("--------------------------------------");
			
			rs = os.db
					.exec("select * from herb_invenlog");
			
			while (rs.next()) {
				System.out.println(
						rs.getInt("invenlog_id") + "|"
						+ rs.getInt("invenlog_inventory_id")	+ "|"
						+ rs.getInt("invenlog_value")+ "|"
						+ format.parse(rs.getString("invenlog_regdate"),
								new ParsePosition(0))
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private String inventoryList[] = new String[20]; //Temporary 20 restricted
	private JButton inventoryBtn[] = new JButton[inventoryList.length];
	
	public void dataUpdate() {
		
	}
}
