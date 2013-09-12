package Integrated;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;


class StorePanel extends JPanel{
	OrderSystem os;
	
	private String inventoryList[] = new String[20]; //Temporary 20 restricted
	private JButton inventoryBtn[] = new JButton[inventoryList.length];
	
	class inventoryListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String name = ((JButton)e.getSource()).getText();
				String sql = "select * from herb_invenlog where "
						+ "invenlog_inventory_id = '" + name.substring(6) + "'";
				java.sql.ResultSet rs = os.db.exec(sql);
				
				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(
						DATE_FORMAT);
				
				System.out.println(name + " -----------------------------");
				while (rs.next()) {
					System.out.println(
							rs.getInt("invenlog_id") + "|"
							+ rs.getInt("invenlog_inventory_id")	+ "|"
							+ rs.getInt("invenlog_value")+ "|"
							+ format.parse(rs.getString("invenlog_regdate"),
									new ParsePosition(0))
							);
				}
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
				sqle.printStackTrace();
			}
		}	
	}
	
	public StorePanel(OrderSystem os) {
		this.os = os;
		
		try {
			
			/*
			os.db.exec("DELETE FROM herb_invenlog;");
			
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
			SimpleDateFormat format = new SimpleDateFormat(
					DATE_FORMAT);
			
			//Load Inventory List and Create Buttons
			
			java.sql.ResultSet rs = os.db
					.exec("select * from herb_inventory");
			
			int cnt = 0;
			while (rs.next()) {
				inventoryBtn[cnt] = new JButton(rs.getString("inventory_name"));
				inventoryBtn[cnt].addActionListener(new inventoryListener());
				this.add(inventoryBtn[cnt]);
				
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void dataUpdate() {
		
	}
}
