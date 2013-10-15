package Integrated;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import BarChart.BarPanel;
import JythonObjectFactory.JythonDriver;
import PieChart.PiePanel;

public class AdminPanel extends JPanel {

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
	private Font bigFont = new Font("����", Font.BOLD, 20);
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

		leftPanel = new JPanel(new BorderLayout());
		//leftScroll = new JScrollPane(leftPanel);
		//leftScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		this.add(leftPanel, BorderLayout.CENTER);

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
		ImageIcon tmpIcon;
		tmpIcon = new ImageIcon("image/admin/my_graph.png");
		pie = new JButton(tmpIcon);
		pie.setPreferredSize(new Dimension(tmpIcon.getIconWidth(), tmpIcon.getIconHeight()));
		pie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/* DB test, insert */
				
				os.db.exec("insert herb_order("
						+ " order_id," 
						+ " order_menu_id,"
						+ " order_count,"
						+ " order_date,"
						+ " order_cash"
						+ ") "
						+ "values("
						+ " 2, "
						+ " 1, "
						+ " 1, "
						+ " \"2013-10-15 11:30:00\","
						+ " 0"
						+ ")");
				
				/* DB test */
				PiePanel bpf = new PiePanel("���� ����",
						"����� ������ �����ϼ���.", leftPanel, os.db);
			}
		});

		tmpIcon = new ImageIcon("image/admin/bar_graph_expectation.png");
		bar = new JButton(tmpIcon);
		bar.setPreferredSize(new Dimension(tmpIcon.getIconWidth(), tmpIcon.getIconHeight()));
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
				BarPanel bpf = new BarPanel("���� ����",
						"����� ������ �����ϼ���.", leftPanel, os.db);
			}
		});
		
		tmpIcon = new ImageIcon("image/admin/sales_index.png");
		table = new JButton(tmpIcon);
		table.setPreferredSize(new Dimension(tmpIcon.getIconWidth(), tmpIcon.getIconHeight()));
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
		
		tmpIcon = new ImageIcon("image/admin/apriori_sync.png");
		aprioriBtn = new JButton(tmpIcon);
		aprioriBtn.setPreferredSize(new Dimension(tmpIcon.getIconWidth(), tmpIcon.getIconHeight()));
		aprioriBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Apriori Python Module Execution
				PythonSyncModule module = new PythonSyncModule(os);

			}
		});
		// add menu
		tmpIcon = new ImageIcon("image/admin/item_info_add.png");
		add = new JButton(tmpIcon);
		add.setPreferredSize(new Dimension(tmpIcon.getIconWidth(), tmpIcon.getIconHeight()));
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new AddEvent(os, leftPanel);
			}
		});
		tmpIcon = new ImageIcon("image/admin/item_info_modify.png");
		mody = new JButton(tmpIcon);
		mody.setPreferredSize(new Dimension(tmpIcon.getIconWidth(), tmpIcon.getIconHeight()));
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
