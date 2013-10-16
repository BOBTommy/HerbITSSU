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
	//private String data[][] = new String[50][7];// = new String[15][4]; // 주문한 목록의 데이터셋
	
	private String columnNames[] = {"주문일시", "당일주문번호", "누적주문번호", "메뉴이름", "주문갯수", "매출액", "현금여부(0:카드)" };
	private JPanel leftPanel; /* 그래프와 테이블을 가지는 패널 */
	private JPanel rightPanel; /* 버튼을 가지는 패널 */

	private JButton pie, bar, table, add, mody;
	private JButton aprioriBtn; //Apriori 예측 모듈 실행 버튼
	private Font bigFont = new Font("굴림", Font.BOLD, 20);
	private JButton ExcelExportBtn;
	private JLabel ExcelLabel;
	//private String excelFileName;				//파일 이름: 현재 날짜+.csv
	private String dirPath = "c:/sales_export/";						//디렉토리 경로: c:\herb_export
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
		
		
		ExcelExportBtn = new JButton("csv로 내보내기");
		ExcelLabel = new JLabel("저장경로:c:/sales_export/");
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
		/* 그래프 설정 시작 */
		/*
		PieChart pieC = new PieChart();
		leftPanel.add(pieC.getPieChart_HistogramChart());
		*/
		/* 그래프 설정 끝 */

		/* 우측패널에 버튼 달기 */
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
				PiePanel bpf = new PiePanel("단위 선택",
						"출력할 단위를 선택하세요.", leftPanel, os.db);
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
				BarPanel bpf = new BarPanel("단위 선택",
						"출력할 단위를 선택하세요.", leftPanel, os.db);
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
				/* 테이블 세팅 시작 */
				adminTable = new JTable();
				adminTableModel = new DefaultTableModel(null, columnNames);
				adminTable.setModel(adminTableModel);
				adminTableScroll = new JScrollPane(adminTable);
				adminTableScroll
						.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				adminTableScroll
						.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				dataUpdate();		//쿼리하여 data를 초기화
								//adminTableModel.setDataVector(data, columnNames);
				/* 테이블 세팅 끝 */

				
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

	public void dataUpdate() { // 주문 정보 String[][] data를 초기화
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
					//이전 주문날짜와 다를경우 해당일자의 주문번호는 1로 초기화
					beforeDay = tempDay_sub;
					dayCnt = 1;
				}
				else if (beforeOrder != tempOrder || beforeOrder == 0) {
					//이전 주문번호와 다른경우 증가
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
