package Integrated;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import BarChart.BarPopupFrame;
import JythonObjectFactory.JythonDriver;
import PieChart.PiePopupFrame;

class AdminPanel extends JPanel {

	final OrderSystem os;

	private JTable adminTable;
	private DefaultTableModel adminTableModel;
	private JScrollPane adminTableScroll;
	private JScrollPane leftScroll, rightScroll;
	private String data[][] = new String[15][4]; // �ֹ��� ����� �����ͼ�, 30�� 4��
	private String columnNames[] = { "�޴���", "�Ⱓ�� �Ǹż���", "�ܰ�", "�����" };
	private JPanel leftPanel; /* �׷����� ���̺��� ������ �г� */
	private JPanel rightPanel; /* ��ư�� ������ �г� */

	private JButton pie, bar, table,add,mody;
	private JButton aprioriBtn; //Apriori ���� ��� ���� ��ư

	public AdminPanel(final OrderSystem os) {
		this.os = os;

		/* ���̺� ���̾ƿ� ���� ���� */
		adminTable = new JTable();
		adminTableModel = new DefaultTableModel(data, columnNames);
		adminTable.setModel(adminTableModel);

		adminTableScroll = new JScrollPane(adminTable);
		adminTableScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		adminTableScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		adminTableModel.setDataVector(data, columnNames);
		/* ���̺� ���̾ƿ� ���� �� */

		this.setLayout(new BorderLayout());

		leftPanel = new JPanel(new ModifiedFlowLayout());
		leftScroll = new JScrollPane(leftPanel);
		//leftScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		this.add(leftScroll, BorderLayout.CENTER);

		// leftPanel.setLayout( new BorderLayout() );
		rightPanel = new JPanel(new ModifiedFlowLayout());
		rightScroll = new JScrollPane(rightPanel);
		//rightScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//rightScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(rightScroll, BorderLayout.EAST);
		/* �׷��� ���� ���� */
		/*
		PieChart pieC = new PieChart();
		leftPanel.add(pieC.getPieChart_HistogramChart());
		*/
		/* �׷��� ���� �� */

		/* �����гο� ��ư �ޱ� */
		pie = new JButton("pie");
		pie.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* DB test, insert */
				
				os.db.exec("insert herb_order("
						+ " order_id," 
						+ " order_menu_id,"
						+ " order_count,"
						+ " order_date"
						+ ") "
						+ "values("
						+ " 2, "
						+ " 1, "
						+ " 1, "
						+ " now()"
						+ ")");
				
				/* DB test */
				PiePopupFrame bpf = new PiePopupFrame("���� ����",
						"����� ������ �����ϼ���.", leftPanel, os.db);
			}
		});

		bar = new JButton("bar");
		bar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/* DB test */

				try {
					java.sql.ResultSet rs = os.db
							.exec("select * from herb_menu;");
					while (rs.next()) {
						String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
						SimpleDateFormat format = new SimpleDateFormat(
								DATE_FORMAT);

						System.out.println(
								rs.getInt("menu_id")+ "|"
								+ rs.getString("menu_name")	+ "|"
								+ rs.getInt("menu_price")+ "|"
								+ format.parse(rs.getString("menu_reg_date"),
										new ParsePosition(0)) + "|"
								+ rs.getString("menu_category")
								);
					}

					rs = os.db.exec("select * from herb_order");

					System.out.println("---------");
					while (rs.next()) {
						String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
						SimpleDateFormat format = new SimpleDateFormat(
								DATE_FORMAT);

						System.out.println(rs.getInt("order_id")+ "|"
								+ rs.getString("order_menu_id")+ "|"
								+ rs.getInt("order_count")+ "|"
								+ format.parse(rs.getString("order_date"),
										new ParsePosition(0))
								);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/* DB test */
				BarPopupFrame bpf = new BarPopupFrame("���� ����",
						"����� ������ �����ϼ���.", leftPanel, os.db);
			}
		});

		table = new JButton("table");
		table.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/* Jython test */
				JythonDriver jdObj = new JythonDriver("Building");

				/* Jython test */

				leftPanel.removeAll();
				leftPanel.add(adminTableScroll, BorderLayout.CENTER);
				leftPanel.setVisible(false);
				leftPanel.setVisible(true);

			}
		});
		
		aprioriBtn = new JButton("Apriori Sync");
		aprioriBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Apriori Python Module Execution
				PythonSyncModule module = new PythonSyncModule(os);

			}
		});
		// add menu
		add = new JButton("add");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new AddEvent(os, leftPanel);
			}
		});
		mody = new JButton("mody");
		mody.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ModyEvent(os, leftPanel);
			}
		});
		rightPanel.add(add);
		rightPanel.add(mody);
		rightPanel.add(pie);
		rightPanel.add(bar);
		rightPanel.add(table);
		rightPanel.add(aprioriBtn);

	}

	public void dataUpdate() { // ������ ����Ǿ���� �κ�

	}

}
