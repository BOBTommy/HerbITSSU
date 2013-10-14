package Integrated;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	private static final int DAYS_OF_GRAPH = 7;
	private static final int DAYS_OF_EXPECTATION = 3;
	private static final String afterDateOf = "'0'";
	private static final String beforeDateOf = "'now()'";
	
	private OrderSystem os;
	
	private HashMap<JButton, String> inventoryList = new HashMap<JButton, String>();
	
	private final JSplitPane masterPanel = new JSplitPane();
	private final SLPanel basePanel = new SLPanel();
	private SLConfig invenCfg, graphCfg, regisCfg, modifCfg;
	private int currentMode = INVEN_MODE; 
	
	private JPanel controlPanel = new JPanel();
	private ImageIcon controlInvenImg = new ImageIcon("image/store/list_mangement.png");
	private ImageIcon controlRegisImg = new ImageIcon("image/store/list_add.png");
	private ImageIcon controlModifImg = new ImageIcon("image/store/list_modify.png");
	private JButton controlInven = new JButton(controlInvenImg);
	private JButton controlRegis = new JButton(controlRegisImg);
	private JButton controlModif = new JButton(controlModifImg);
	
	private JPanel inventoryPanel = new JPanel();
	private JScrollPane inventoryScroll = new JScrollPane(inventoryPanel,
															JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
															JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	private GraphPane inventoryGraph = new GraphPane(DAYS_OF_EXPECTATION);
	
	private JPanel modificationPanel = new JPanel();
	private JLabel modifIDLbl, modifNameLbl, modifUnitLbl, modifDateLbl, modifStockLbl;
	private JTextField modifID, modifName, modifUnit, modifDate, modifStock;
	private JButton modifOK, modifCancel;
	private int modifOriStock;
	
	public StorePanel(OrderSystem os) {
		this.os = os;
		
		//controlPanel
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(null);
		menuPanel.setPreferredSize(new Dimension(488, 64));
		controlInven.setBounds(0, 0,
				controlInvenImg.getIconWidth(), controlInvenImg.getIconHeight());
		controlRegis.setBounds(168, 0,
				controlRegisImg.getIconWidth(), controlRegisImg.getIconHeight());
		controlModif.setBounds(336, 0,
				controlModifImg.getIconWidth(), controlModifImg.getIconHeight());
		controlInven.addActionListener(new ControlListener());
		controlRegis.addActionListener(new ControlListener());
		controlModif.addActionListener(new ControlListener());
		menuPanel.add(controlInven);
		menuPanel.add(controlRegis);
		menuPanel.add(controlModif);
		controlPanel.add(menuPanel);
		
		//inventoryPanel
		inventoryScroll.setBorder(null);
		inventoryPanel.setLayout(new ModifiedFlowLayout());
		loadInventory();
		changeMode(INVEN_MODE, false);
		
		//modificationPanel
		modificationPanel.setLayout(new BoxLayout(modificationPanel, BoxLayout.Y_AXIS));
		modifIDLbl = new JLabel("��Ϲ�ȣ");
		modifNameLbl = new JLabel("�̸�");
		modifUnitLbl = new JLabel("����");
		modifDateLbl = new JLabel("����������");
		modifStockLbl = new JLabel("���");
		modifID = new JTextField(5);
		modifName = new JTextField(10);
		modifUnit = new JTextField(10);
		modifDate = new JTextField(16);
		modifStock = new JTextField(10);
		modifOK = new JButton("�����ϱ�");
		modifCancel = new JButton("���");
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
		modificationPanel.add(modifStockLbl);
		modificationPanel.add(modifStock);
		modificationPanel.add(modifOK);
		modificationPanel.add(modifCancel);
		
		//Sliding Configs
		invenCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(1f)
				.place(0, 0, inventoryScroll);
		graphCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(294).col(1f)
				.place(0, 0, inventoryScroll).place(0, 1, inventoryGraph);
		regisCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(294).col(1f)
				.place(0, 0, inventoryScroll).place(0, 1, modificationPanel);
		modifCfg = new SLConfig(basePanel).gap(0, 0).row(1f).col(294).col(1f)
				.place(0, 0, inventoryScroll).place(0, 1, modificationPanel);
		
		//basePanel
		basePanel.setTweenManager(SLAnimator.createTweenManager());
		basePanel.initialize(invenCfg);
		
		masterPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		masterPanel.setDividerSize(1);
		masterPanel.setDividerLocation(75);
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
			String invenName = inventoryList.get(src);
			
			if (currentMode != MODIF_MODE) {
				String sql = "select * from herb_inventory where "
						+ "inventory_name = '" + invenName + "'";
				
				try {
					java.sql.ResultSet rs = os.db.exec(sql);
					rs.next();					
					
					int inventory_id = rs.getInt("inventory_id");
					sql = "SELECT * FROM (SELECT ("
								+ "SELECT sum(invenlog_value) "
								+ "FROM herb_invenlog b "
								+ "WHERE b.invenlog_inventory_id = a.invenlog_inventory_id "
								  + "and b.invenlog_regdate <= a.invenlog_regdate "
								+ ") as invenlog_value, a.invenlog_day, a.invenlog_regdate "
							+ "FROM herb_invenlog a "
							+ "WHERE a.invenlog_inventory_id = " + inventory_id + " "
							+ "and date(a.invenlog_regdate) >= " + afterDateOf + " "
							+ "and date(a.invenlog_regdate) <= " + beforeDateOf + " "
							+ "ORDER BY a.invenlog_regdate DESC "
							+ "LIMIT 0, " + (DAYS_OF_GRAPH - DAYS_OF_EXPECTATION) + ") as alias_name ORDER BY alias_name.invenlog_regdate ASC";
					rs = os.db.exec(sql);
					
					System.out.println(invenName + " -----------------------------");
					int cnt = 0;
					long lastTimeValue = 0;
					int lastDay = 0;
					int lastValue = 0;
					int tmpPoints[] = new int[DAYS_OF_GRAPH];
					String tmpLabels[] = new String[DAYS_OF_GRAPH];
					while (rs.next() && (DAYS_OF_GRAPH - DAYS_OF_EXPECTATION - cnt > 0)) {
						lastTimeValue = DateUtil.convUserTypedStringToTimeValue(rs.getString("invenlog_regdate"), "yyyy-MM-dd kk:mm:ss");
						lastDay = Integer.parseInt(rs.getString("invenlog_day"));
						lastValue = rs.getInt("invenlog_value");
						tmpLabels[cnt] = DateUtil.convTimeValueToUserTypedString(lastTimeValue, "MM-dd");
						tmpPoints[cnt] = lastValue;
						cnt++;
					}
					
					//Average
					int avg_value[] = new int[8];
					rs = os.db.exec(sql);
					rs.next();					
					
					sql = "SELECT cast(avg(invenlog_value) as signed) as invenlog_avgvalue, invenlog_day " 
							+ "FROM herb_invenlog " 
							+ "WHERE invenlog_inventory_id = " + inventory_id + " and invenlog_value < 0 " 
							+ "GROUP BY invenlog_day "
							+ "ORDER BY invenlog_day ASC ";
					rs = os.db.exec(sql);
					while (rs.next()) {
						avg_value[rs.getInt("invenlog_day")] = rs.getInt("invenlog_avgvalue");
					}
					
					for (int i = 1; i <= DAYS_OF_EXPECTATION; i++) {
						lastTimeValue = lastTimeValue + 86400 * 1000;
						lastValue = lastValue + avg_value[(lastDay - 1 + i) % 7 + 1];
						tmpLabels[cnt + i - 1] = DateUtil.convTimeValueToUserTypedString(lastTimeValue, "MM-dd");
						tmpPoints[cnt + i - 1] = lastValue;
					}
					
					inventoryGraph.setPoints(tmpPoints, tmpLabels, invenName);
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
					
						String sql = "SELECT ("
								+ "SELECT sum(invenlog_value) "
								+ "FROM herb_invenlog b "
								+ "WHERE b.invenlog_inventory_id = a.invenlog_inventory_id "
								  + "and b.invenlog_regdate <= a.invenlog_regdate "
								+ ") as invenlog_value, a.invenlog_day, a.invenlog_regdate "
							+ "FROM herb_invenlog a "
							+ "WHERE a.invenlog_inventory_id = " + rs.getString("inventory_id") + " "
							+ "ORDER BY a.invenlog_regdate DESC "
							+ "LIMIT 0, 1";
						rs = os.db.exec(sql);
						rs.next();
						
						modifOriStock = rs.getInt("invenlog_value");
						modifStock.setText(rs.getString("invenlog_value"));
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
				if (currentMode == REGIS_MODE) {
					os.db.exec("insert herb_inventory "
							+ "(inventory_name, inventory_unit, inventory_regdate) "
							+ "VALUES ("
								+ "'" + modifName.getText() + "', "
								+ "'" + modifUnit.getText() + "', "
								+ "now());");
					try {
						java.sql.ResultSet rs = os.db.exec("select inventory_id from herb_inventory where inventory_name='" + modifName.getText() + "';");
						rs.next();
						os.db.exec("insert herb_invenlog("
								+ "invenlog_inventory_id, invenlog_value, invenlog_day, invenlog_regdate) "
								+ "values("
								+ "'" + rs.getString("inventory_id") + "', " //inventory_name
								+ "'" + modifStock.getText() + "', "
								+ "dayofweek(now()), "
								+ "now())");
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				} else if (currentMode == MODIF_MODE) {
					os.db.exec("update herb_inventory set "
							+ "inventory_name='" + modifName.getText() + "', "
							+ "inventory_unit='" + modifUnit.getText() + "', "
							+ "inventory_regdate=now() "
							+ "where inventory_id='" + modifID.getText() + "'");
					
					if (Integer.parseInt(modifStock.getText()) != modifOriStock) {
						os.db.exec("insert herb_invenlog("
								+ "invenlog_inventory_id, invenlog_value, invenlog_day, invenlog_regdate) "
								+ "values("
								+ "'" + modifID.getText() + "', " //inventory_name
								+ "'" + (Integer.parseInt(modifStock.getText()) - modifOriStock) + "', "
								+ "dayofweek(now()), "
								+ "now())");
					}
				}
				loadInventory();
				changeMode(INVEN_MODE);
				JOptionPane.showMessageDialog(null, "�Ϸ�Ǿ����ϴ�!");
			}
			
			modifID.setText("");
			modifName.setText("");
			modifUnit.setText("");
			modifDate.setText("");
			modifStock.setText("");
			modifOriStock = 0;
			
			changeMode(INVEN_MODE);
		}
	}
	
	private void loadInventory() {
		try {
			/*///////////// Fake Data
			os.db.exec("DELETE FROM herb_order;");
			System.out.println("Now Start");
			String sql;
			
			int orderCntAday, orderMenus, accumulatedCnt = 1;
			for (int i = 0; i < 1000; i++) {
				orderCntAday = new Random().nextInt(5) + 5;
				orderMenus = new Random().nextInt(5) + 1;
				for (int j = 0; j < orderCntAday; j++) {
					for (int k = 0; k < orderMenus; k++) {
						sql = "INSERT INTO herb_order"
								+ "(order_id, order_menu_id, order_count, order_date, order_cash) "
								+ "VALUES("
									+ "'" + accumulatedCnt + "', "
									+ "'" + (new Random().nextInt(290) + 1) + "', "
									+ "'" + (new Random().nextInt(5) + 1) + "', "
									+ "date_add(now(), interval -" + (180 - i) + " day), "
									+ "'" + new Random().nextInt(2) +"');";
						os.db.exec(sql);
					}
					accumulatedCnt++;
				}
				System.out.println("Day " + (i+ 1) + "is Completed");
			}
			*/
			
			
			
			String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
			SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
			
			//Load Inventory List and Create Buttons
			
			java.sql.ResultSet rs = os.db
					.exec("select * from herb_inventory");
			
			inventoryPanel.removeAll();
			inventoryList.clear();
			ImageIcon imageIcon;
			JButton inventoryBtn; //Tmp
			while (rs.next()) {
				imageIcon = new ImageIcon("image/store/" + rs.getString("inventory_name") + ".png");
				inventoryBtn = new JButton(imageIcon);
				inventoryBtn.setPreferredSize(new Dimension(130, 130));
				inventoryBtn.addActionListener(new InventoryListener());
				inventoryList.put(inventoryBtn, rs.getString("inventory_name"));
				inventoryPanel.add(inventoryBtn);
			}
			inventoryPanel.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void changeMode(int newMode) { changeMode(newMode, true); }
	private void changeMode(int newMode, boolean actionate) {
		if (actionate) {
			if (newMode == INVEN_MODE) invenAction.run();
			else if (newMode == GRAPH_MODE) graphAction.run();
			else if (newMode == REGIS_MODE) {
				modifIDLbl.setVisible(false);
				modifID.setVisible(false);
				modifDateLbl.setVisible(false);
				modifDate.setVisible(false);
				modifOK.setText("����ϱ�");
				modifID.setText("");
				modifName.setText("");
				modifUnit.setText("");
				modifDate.setText("");
				modifStock.setText("");
				regisAction.run();
			}
			else if (newMode == MODIF_MODE) {
				modifIDLbl.setVisible(true);
				modifID.setVisible(true);
				modifDateLbl.setVisible(true);
				modifDate.setVisible(true);
				modifOK.setText("�����ϱ�");
				modifAction.run();
			}
		}
		
		currentMode = newMode;
	}
	
	public void dataUpdate() {
		
	}
}
