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
	private String data[][] = new String[15][4]; // 주문한 목록의 데이터셋, 30줄 4열
	private String columnNames[] = { "메뉴명", "기간내 판매수량", "단가", "매출액" };
	private JPanel leftPanel; /* 그래프와 테이블을 가지는 패널 */
	private JPanel rightPanel; /* 버튼을 가지는 패널 */

	private JButton pie, bar, table,add,mody;
	private JButton aprioriBtn; //Apriori 예측 모듈 실행 버튼

	public AdminPanel(final OrderSystem os) {
		this.os = os;

		/* 테이블 레이아웃 세팅 시작 */
		adminTable = new JTable();
		adminTableModel = new DefaultTableModel(data, columnNames);
		adminTable.setModel(adminTableModel);

		adminTableScroll = new JScrollPane(adminTable);
		adminTableScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		adminTableScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		adminTableModel.setDataVector(data, columnNames);
		/* 테이블 레이아웃 세팅 끝 */

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
		/* 그래프 설정 시작 */
		/*
		PieChart pieC = new PieChart();
		leftPanel.add(pieC.getPieChart_HistogramChart());
		*/
		/* 그래프 설정 끝 */

		/* 우측패널에 버튼 달기 */
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
				PiePopupFrame bpf = new PiePopupFrame("단위 선택",
						"출력할 단위를 선택하세요.", leftPanel, os.db);
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
				BarPopupFrame bpf = new BarPopupFrame("단위 선택",
						"출력할 단위를 선택하세요.", leftPanel, os.db);
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

	public void dataUpdate() { // 수정시 변경되어야할 부분

	}

}
