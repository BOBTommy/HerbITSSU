package Integrated;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

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
	
	private HashMap<JButton, String> inventory = new HashMap<JButton, String>();
	
	private final JSplitPane masterPanel = new JSplitPane();
	private final SLPanel basePanel = new SLPanel();
	private SLConfig invenCfg, graphCfg, regisCfg, modifCfg;
	private int currentMode = INVEN_MODE; 
	
	private JPanel controlPanel = new JPanel();
	private JButton controlInven = new JButton("<html><body style='height: 20px; vertical-align:middle; font-size: 12px'>목록 관리</body></html>");
	private JButton controlRegis = new JButton("<html><body style='height: 20px; vertical-align:middle; font-size: 12px'>항목 추가</body></html>");
	private JButton controlModif = new JButton("<html><body style='height: 20px; vertical-align:middle; font-size: 12px'>항목 수정</body></html>");
	
	private JPanel inventoryPanel = new JPanel();
	
	private StoreGraphPanel inventoryGraph = new StoreGraphPanel();
	
	private JPanel modificationPanel = new JPanel();
	private JLabel modifIDLbl, modifNameLbl, modifUnitLbl, modifDateLbl;
	private JTextField modifID, modifName, modifUnit, modifDate;
	private JButton modifOK, modifCancel;
	
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
		//inventoryPanel.setBackground(Color.BLUE);
		loadInventory();
		changeMode(MODIF_MODE, false); ////////////////////////////////////////INVEN_MODE by Default
		
		//modificationPanel
		modifIDLbl = new JLabel("등록번호");
		modifNameLbl = new JLabel("이름");
		modifUnitLbl = new JLabel("단위");
		modifDateLbl = new JLabel("최종수정일");
		modifID = new JTextField(5);
		modifName = new JTextField(10);
		modifUnit = new JTextField(10);
		modifDate = new JTextField(16);
		modifOK = new JButton("수정하기");
		modifCancel = new JButton("취소");
		modifID.setEditable(false);
		modifDate.setEditable(false);
		modifOK.addActionListener(new ModificationListener());
		modifCancel.addActionListener(new ModificationListener());
		modificationPanel.add(modifIDLbl);
		modificationPanel.add(modifID);
		modificationPanel.add(modifNameLbl);
		modificationPanel.add(modifName);
		modificationPanel.add(modifUnitLbl);
		modificationPanel.add(modifUnit);
		modificationPanel.add(modifDateLbl);
		modificationPanel.add(modifDate);
		modificationPanel.add(modifOK);
		modificationPanel.add(modifCancel);
		
		//Sliding Configs
		invenCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f)
				.place(0, 0, inventoryPanel);
		graphCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f).col(4f)
				.place(0, 0, inventoryPanel).place(0, 1, inventoryGraph);
		regisCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f).col(4f)
				.place(0, 0, inventoryPanel).place(0, 1, modificationPanel);
		modifCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f).col(4f)
				.place(0, 0, inventoryPanel).place(0, 1, modificationPanel);
		
		//basePanel
		basePanel.setTweenManager(SLAnimator.createTweenManager());
		basePanel.initialize(modifCfg);
		
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
		}
	};
	
	private final Runnable modifAction = new Runnable() {
		public void run() {
			SLKeyframe SLkf = new SLKeyframe(modifCfg, 0.6f);
			
			if (currentMode == INVEN_MODE) {
				SLkf.setStartSide(SLSide.RIGHT, modificationPanel);
			} else if (currentMode == GRAPH_MODE) {
				SLkf.setEndSide(SLSide.RIGHT, inventoryGraph)
					.setStartSide(SLSide.RIGHT, modificationPanel);
			}
			
			basePanel.createTransition().push(SLkf).play();
		}
	};
	
	class ControlListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton src = (JButton) e.getSource();
			
			if (src == controlInven) {
				changeMode(INVEN_MODE);
			} else if (src == controlRegis) {
				changeMode(REGIS_MODE);
			} else if (src == controlModif) {
				changeMode(MODIF_MODE);
			}
		}
	}
	
	class InventoryListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton src = (JButton) e.getSource();
			String invenName = inventory.get(src);
			
			if (currentMode != MODIF_MODE) {
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
				
				changeMode(GRAPH_MODE);
			} else {
				try {
					java.sql.ResultSet rs = os.db
							.exec("select * from herb_inventory "
									+ "where inventory_name='" + invenName + "'");
					
					if (rs.next()) {
						modifID.setText(rs.getString("inventory_id"));
						modifName.setText(rs.getString("inventory_name"));
						modifUnit.setText(rs.getString("inventory_unit"));
						modifDate.setText(rs.getString("inventory_regdate"));
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	class ModificationListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton src = (JButton) e.getSource();
			if (src == modifOK) {
				os.db.exec("update herb_inventory set "
						+ "inventory_name='" + modifName.getText() + "', "
						+ "inventory_unit='" + modifUnit.getText() + "', "
						+ "inventory_regdate=now() "
						+ "where inventory_id='" + modifID.getText() + "'");
				loadInventory();
			} else if (src == modifCancel) {
				modifID.setText("");
				modifName.setText("");
				modifUnit.setText("");
				modifDate.setText("");
			}
			
			changeMode(INVEN_MODE);
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
			
			//Load Inventory List and Create Buttons
			
			java.sql.ResultSet rs = os.db
					.exec("select * from herb_inventory");
			
			inventoryPanel.removeAll();
			inventory.clear();
			JButton inventoryBtn; //Tmp
			while (rs.next()) {
				inventoryBtn = new JButton(
						"<html>" +
						"<body style='vertical-align:middle; font-size: 16px;'>" +
						rs.getString("inventory_name") +
						"</body>" +
						"</html>");
				inventoryBtn.addActionListener(new InventoryListener());
				inventory.put(inventoryBtn, rs.getString("inventory_name"));
				inventoryPanel.add(inventoryBtn);
				
				System.out.println(
						rs.getInt("inventory_id") + "|"
						+ rs.getString("inventory_name")	+ "|"
						+ rs.getString("inventory_unit")+ "|"
						+ format.parse(rs.getString("inventory_regdate"),
								new ParsePosition(0))
						);
			}
			inventoryPanel.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void changeMode(int newMode) { changeMode(newMode, true); }
	private void changeMode(int newMode, boolean actionate) {
		if (newMode == INVEN_MODE)
			inventoryPanel.setLayout(new GridLayout(inventory.size() / 6 + 1, 6));
		else
			inventoryPanel.setLayout(new GridLayout(inventory.size() / 2 + 1, 2));
		
		if (actionate) {
			if (newMode == INVEN_MODE) invenAction.run();
			else if (newMode == GRAPH_MODE) graphAction.run();
			else if (newMode == REGIS_MODE) regisAction.run();
			else if (newMode == MODIF_MODE) modifAction.run();
		}
		
		currentMode = newMode;
	}
	
	public void dataUpdate() {
		
	}
}
