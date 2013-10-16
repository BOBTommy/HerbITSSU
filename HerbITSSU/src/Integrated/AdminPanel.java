package Integrated;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import BarChart.BarPanel;
import PieChart.PiePanel;

public class AdminPanel extends JPanel {

	final OrderSystem os;

	private JTable adminTable;
	private DefaultTableModel adminTableModel;
	private JScrollPane adminTableScroll;
	private JScrollPane leftScroll, rightScroll;
	//private String data[][] = new String[50][7];// = new String[15][4]; // �ֹ��� ����� �����ͼ�
	
	private String columnNames[] = {"�ֹ��Ͻ�", "�����ֹ���ȣ", "�����ֹ���ȣ", "�޴��̸�", "�ֹ�����", "�����", "���ݿ���(0:ī��)" };
	private JPanel leftPanel; /* �׷����� ���̺��� ������ �г� */
	private JPanel rightPanel; /* ��ư�� ������ �г� */

	private JButton pie, bar, table, add, mody;
	private JButton aprioriBtn; //Apriori ���� ��� ���� ��ư
	private Font bigFont = new Font("����", Font.BOLD, 20);
	private JButton ExcelExportBtn;
	private JLabel ExcelLabel;
	//private String excelFileName;				//���� �̸�: ���� ��¥+.csv
	private String dirPath = "c:/sales_export/";						//���丮 ���: c:\herb_export
	private int tableIndex = 0;
	private File file;
	public AdminPanel(final OrderSystem os) {
		this.os = os;
		

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
		
		
		ExcelExportBtn = new JButton("csv�� ��������");
		ExcelLabel = new JLabel("������:c:/sales_export/");
		ExcelExportBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				
				//excelFileName = sdf.format(new Date()) + ".csv";
				file = new File(dirPath);
				file.mkdir();
				writeAll(file);
				
			}
		});
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
				/*
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
						+ " \"2013-10-16 11:30:00\","
						+ " 0"
						+ ")");
				*/
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
				/*
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
				*/
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
			//	JythonDriver jdObj = new JythonDriver("Building");

				/* Jython test */

				leftPanel.removeAll();
				/* ���̺� ���� ���� */
				adminTable = new JTable();
				adminTableModel = new DefaultTableModel(null, columnNames);
				adminTable.setModel(adminTableModel);
				adminTableScroll = new JScrollPane(adminTable);
				adminTableScroll
						.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				adminTableScroll
						.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				dataUpdate();		//�����Ͽ� data�� �ʱ�ȭ
								//adminTableModel.setDataVector(data, columnNames);
				/* ���̺� ���� �� */

				
				leftPanel.add(adminTableScroll, BorderLayout.CENTER);
				leftPanel.add(ExcelLabel);
				leftPanel.add(ExcelExportBtn, BorderLayout.SOUTH);
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

	public void dataUpdate() { // �ֹ� ���� String[][] data�� �ʱ�ȭ
		ResultSet rs = os.db.exec(
				"SELECT o.order_id, m.menu_name, o.order_count, m.menu_price * o.order_count AS sales, order_date, order_cash " +
				"FROM herb_menu AS m, herb_order AS o " +
				"WHERE o.order_menu_id = m.menu_id " +
				"ORDER BY o.order_id ASC");
				//"LIMIT " + tableIndex+
				//" , 50;");
		int beforeOrder = 0, tempOrder = 0;
		String beforeDay = "", tempDay = "", tempDay_sub = "";
		try {
			Integer dayCnt = 1;
			int idx = 0;
			ParsePosition pos = new ParsePosition(0);
			while (rs.next()){
				Vector temp = new Vector();
				tempDay = rs.getString("order_date");
				tempDay_sub = tempDay.substring(0, 10);
				tempOrder = rs.getInt("order_id");
			
				if (tempDay_sub.compareTo(beforeDay) != 0) {
					//���� �ֹ���¥�� �ٸ���� �ش������� �ֹ���ȣ�� 1�� �ʱ�ȭ
					beforeDay = tempDay_sub;
					dayCnt = 1;
				}
				else if (beforeOrder != tempOrder || beforeOrder == 0) {
					//���� �ֹ���ȣ�� �ٸ���� ����
					beforeOrder = tempOrder;
					dayCnt++;
				}
				else {
					
				}
				temp.add( tempDay);
				temp.add(dayCnt);
				temp.add(tempOrder);
				temp.add(rs.getString("menu_name"));
				temp.add(rs.getInt("order_count"));
				temp.add(rs.getInt("sales"));
				temp.add(rs.getBoolean("order_cash"));
				adminTableModel.addRow(temp);
				adminTableModel.fireTableDataChanged();
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeAll(File f) {
		try {
			ExcelExport ee = new ExcelExport(file, adminTableModel);
			ee.writeAll();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
