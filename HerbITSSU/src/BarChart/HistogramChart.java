package BarChart;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Database.DBGenerator;
import Database.HerbMenuTable;
import Database.HerbOrderTable;

/*바차트 */
/* 비 로컬 연결시 매우 느림. 수정필 */
public class HistogramChart {
	private XYSeries series = null;
//	private XYDataset dataset = null;
	private XYSeriesCollection dataset = null;
	private JFreeChart chart = null;
	private TextTitle subTitle = null;
	String menu, chart_subTitle1, chart_subTitle2;
	DBGenerator db;

	public HistogramChart(DBGenerator db) {
		menu = "Sum of Sales";

		series = new XYSeries(menu);
		
		this.db = db;
		/* add의 첫번째 인자: 월, 두번째 인자: 판매량 */
		/*
		 * series.add(1, 10); series.add(3, 11); series.add(7, 12);
		 * series.add(12, 14);
		 */
	}

	public void setData(String option) {
		// DBGenerator testDB = new DBGenerator();
		try {
			if (option.compareTo("시간") == 0) {
				/*
				 * LinkedList<HerbOrderTable> res = testDB.selectHerbOrderTable(
				 * "select * from herb_order " +
				 * "where date(order_date) = date(now())" );
				 */

				LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
				ResultSet resultSet = db.exec("select * from herb_order "
						+ "where date(order_date) = date(now())");

				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

				while (resultSet.next()) {
					HerbOrderTable hot = new HerbOrderTable();
					hot.setOrder_id(resultSet.getInt("order_id"));
					hot.setOrder_menu_id(resultSet.getInt("order_menu_id"));
					hot.setOrder_count(resultSet.getInt("order_count"));

					hot.setOrder_date(format.parse(
							resultSet.getString("order_date"),
							new ParsePosition(0)));
					res.add(hot);
				}
				Iterator<HerbOrderTable> i = res.iterator();
				long sumOfSales[] = new long[6];
				for (int tmp = 0; tmp < sumOfSales.length; tmp++) {
					sumOfSales[tmp] = 0;
				}
				int timeIndex = 0; // 9~11 : 0, 11~13 : 1, 13~15 : 2, 15~17 : 3,
									// 17~19 : 4, 19~21 : 5
				while (i.hasNext()) {
					HerbOrderTable hot = (HerbOrderTable) i.next();
					SimpleDateFormat time = new SimpleDateFormat("kk");

					// System.out.print(hot.getOrder_date()+":");
					int hour = Integer
							.valueOf(time.format(hot.getOrder_date()))
							.intValue();
					// System.out.println(hour+",");
					if (hour < 11) {
						timeIndex = 0;
					} else if (hour < 13) {
						timeIndex = 1;
					} else if (hour < 15) {
						timeIndex = 2;
					} else if (hour < 17) {
						timeIndex = 3;
					} else if (hour < 19) {
						timeIndex = 4;
					} else {
						timeIndex = 5;
					}

					/* 비 로컬 연결시 매우 느림. 수정필 */
					/*
					 * LinkedList<HerbMenuTable> res2 =
					 * testDB.selectHerbMenuTable( "select * from herb_menu " +
					 * "where menu_id = " + hot.getOrder_menu_id()
					 * 
					 * );
					 */
					LinkedList<HerbMenuTable> res2 = new LinkedList<HerbMenuTable>();
					ResultSet resultSet2 = db.exec("select * from herb_menu "
							+ "where menu_id = " + hot.getOrder_menu_id());

					String DATE_FORMAT2 = "yyyy-MM-dd kk:mm:ss";
					SimpleDateFormat format2 = new SimpleDateFormat(DATE_FORMAT2);

					while (resultSet2.next()) {
						HerbMenuTable hmt = new HerbMenuTable();
						hmt.setMenu_id(resultSet2.getInt("menu_id"));
						hmt.setMenu_name(resultSet2.getString("menu_name"));
						hmt.setMenu_price(resultSet2.getInt("menu_price"));
						hmt.setMenu_category(resultSet2
								.getString("menu_category"));
						hmt.setMenu_reg_date(format2.parse(
								resultSet2.getString("menu_reg_date"),
								new ParsePosition(0)));

						res2.add(hmt);
					}
					
					sumOfSales[timeIndex] += res2.get(0).getMenu_price();
					if( resultSet != null )resultSet.close();
					if( resultSet2 != null )resultSet2.close();
				}

				series.add(9, sumOfSales[0]);
				series.add(11, sumOfSales[1]);
				series.add(13, sumOfSales[2]);
				series.add(15, sumOfSales[3]);
				series.add(17, sumOfSales[4]);
				series.add(19, sumOfSales[5]);

				chart_subTitle1 = "Hour";
				chart_subTitle2 = "시간별 매출현황";

			} else if (option.compareTo("일") == 0) {

				// 현재 날짜를 기준으로 7일 이내의 판매내용을 출력
				/*
				 * LinkedList<HerbOrderTable> res = testDB.selectHerbOrderTable(
				 * "select * from herb_order " +
				 * "where date(order_date) >= date(subdate(now(), INTERVAL 7 DAY)) and date(order_date) <= date(now())"
				 * );
				 */
				LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
				ResultSet resultSet = db
						.exec("select * from herb_order "
								+ "where date(order_date)  >= date(subdate(now(), INTERVAL 7 DAY)) and date(order_date) <= date(now())");

				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

				while (resultSet.next()) {
					HerbOrderTable hot = new HerbOrderTable();
					hot.setOrder_id(resultSet.getInt("order_id"));
					hot.setOrder_menu_id(resultSet.getInt("order_menu_id"));
					hot.setOrder_count(resultSet.getInt("order_count"));

					hot.setOrder_date(format.parse(
							resultSet.getString("order_date"),
							new ParsePosition(0)));
					res.add(hot);
				}
				Iterator<HerbOrderTable> i = res.iterator();
				long sumOfSales[] = new long[7];
				for (int tmp = 0; tmp < sumOfSales.length; tmp++) {
					sumOfSales[tmp] = 0;
				}
				int timeIndex = 0; // 현재가 일요일이라 가정할 경우 월 : 0, 화 : 1, 수 : 2, 목 :
									// 3, 금 : 4, 토 : 5, 일 : 6
				Date now = new Date();
				long nowDay = now.getTime() / (3600 * 24000); // 1970~ 현재 일 수
				while (i.hasNext()) {
					HerbOrderTable hot = (HerbOrderTable) i.next();
					long orderDay = hot.getOrder_date().getTime()
							/ (3600 * 24000);

					// int hour =
					// Integer.valueOf(time.format(hot.getOrder_date())).intValue();
					// System.out.println(hour+",");
					if (nowDay - orderDay > 6) { // 6일 전
						timeIndex = 0;
					} else if (nowDay - orderDay > 5) {
						timeIndex = 1;
					} else if (nowDay - orderDay > 4) {
						timeIndex = 2;
					} else if (nowDay - orderDay > 3) {
						timeIndex = 3;
					} else if (nowDay - orderDay > 2) {
						timeIndex = 4;
					} else if (nowDay - orderDay > 1) {
						timeIndex = 5;
					} else {
						timeIndex = 6;
					}
					/*
					 * LinkedList<HerbMenuTable> res2 =
					 * testDB.selectHerbMenuTable( "select * from herb_menu " +
					 * "where menu_id = " + hot.getOrder_menu_id()
					 * 
					 * );
					 */
					LinkedList<HerbMenuTable> res2 = new LinkedList<HerbMenuTable>();
					ResultSet resultSet2 = db.exec("select * from herb_menu "
							+ "where menu_id = " + hot.getOrder_menu_id());

					String DATE_FORMAT2 = "yyyy-MM-dd kk:mm:ss";
					SimpleDateFormat format2 = new SimpleDateFormat(DATE_FORMAT2);

					while (resultSet2.next()) {
						HerbMenuTable hmt = new HerbMenuTable();
						hmt.setMenu_id(resultSet2.getInt("menu_id"));
						hmt.setMenu_name(resultSet2.getString("menu_name"));
						hmt.setMenu_price(resultSet2.getInt("menu_price"));
						hmt.setMenu_category(resultSet2
								.getString("menu_category"));
						hmt.setMenu_reg_date(format2.parse(
								resultSet2.getString("menu_reg_date"),
								new ParsePosition(0)));

						res2.add(hmt);
					}
					if( resultSet != null )resultSet.close();
					if( resultSet2 != null )resultSet2.close();

					sumOfSales[timeIndex] += res2.get(0).getMenu_price();
				}

				series.add(-6, sumOfSales[0]);
				series.add(-5, sumOfSales[1]);
				series.add(-4, sumOfSales[2]);
				series.add(-3, sumOfSales[3]);
				series.add(-2, sumOfSales[4]);
				series.add(-1, sumOfSales[5]);
				series.add(0, sumOfSales[6]);
				chart_subTitle1 = "Day";
				chart_subTitle2 = "일별 매출현황";
			} else if (option.compareTo("주") == 0) {
				// 현재 날짜를 기준으로 한 달(28)이내의 판매내용을 출력
				/*
				 * LinkedList<HerbOrderTable> res = testDB.selectHerbOrderTable(
				 * "select * from herb_order " +
				 * "where date(order_date) >= date(subdate(now(), INTERVAL 28 DAY)) and date(order_date) <= date(now())"
				 * );
				 */
				LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
				ResultSet resultSet = db
						.exec("select * from herb_order "
								+ "where date(order_date) >= date(subdate(now(), INTERVAL 28 DAY)) and date(order_date) <= date(now())");

				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

				while (resultSet.next()) {
					HerbOrderTable hot = new HerbOrderTable();
					hot.setOrder_id(resultSet.getInt("order_id"));
					hot.setOrder_menu_id(resultSet.getInt("order_menu_id"));
					hot.setOrder_count(resultSet.getInt("order_count"));

					hot.setOrder_date(format.parse(
							resultSet.getString("order_date"),
							new ParsePosition(0)));
					res.add(hot);
				}
				Iterator<HerbOrderTable> i = res.iterator();
				long sumOfSales[] = new long[4];
				for (int tmp = 0; tmp < sumOfSales.length; tmp++) {
					sumOfSales[tmp] = 0;
				}
				int timeIndex = 0; //
				Date now = new Date();
				long nowDay = now.getTime() / (3600 * 24000); // 1970~ 현재 일 수
				while (i.hasNext()) {
					HerbOrderTable hot = (HerbOrderTable) i.next();
					long orderDay = hot.getOrder_date().getTime()
							/ (3600 * 24000);

					// int hour =
					// Integer.valueOf(time.format(hot.getOrder_date())).intValue();
					// System.out.println(hour+",");
					if (nowDay - orderDay > 21) { // 6일 전
						timeIndex = 0;
					} else if (nowDay - orderDay > 14) {
						timeIndex = 1;
					} else if (nowDay - orderDay > 7) {
						timeIndex = 2;
					} else {
						timeIndex = 3;
					}
					/*
					 * LinkedList<HerbMenuTable> res2 =
					 * testDB.selectHerbMenuTable( "select * from herb_menu " +
					 * "where menu_id = " + hot.getOrder_menu_id()
					 * 
					 * );
					 */
					LinkedList<HerbMenuTable> res2 = new LinkedList<HerbMenuTable>();
					ResultSet resultSet2 = db.exec("select * from herb_menu "
							+ "where menu_id = " + hot.getOrder_menu_id());

					String DATE_FORMAT2 = "yyyy-MM-dd kk:mm:ss";
					SimpleDateFormat format2 = new SimpleDateFormat(DATE_FORMAT2);

					while (resultSet2.next()) {
						HerbMenuTable hmt = new HerbMenuTable();
						hmt.setMenu_id(resultSet2.getInt("menu_id"));
						hmt.setMenu_name(resultSet2.getString("menu_name"));
						hmt.setMenu_price(resultSet2.getInt("menu_price"));
						hmt.setMenu_category(resultSet2
								.getString("menu_category"));
						hmt.setMenu_reg_date(format2.parse(
								resultSet2.getString("menu_reg_date"),
								new ParsePosition(0)));

						res2.add(hmt);
					}
					if( resultSet != null )resultSet.close();
					if( resultSet2 != null )resultSet2.close();

					sumOfSales[timeIndex] += res2.get(0).getMenu_price();
				}

				series.add(-21, sumOfSales[0]);
				series.add(-14, sumOfSales[1]);
				//series.add(-21, 7000);
			//	series.add(-14, 1000);
				series.add(-7, sumOfSales[2]);
				series.add(0, sumOfSales[3]);
				chart_subTitle1 = "Week";
				chart_subTitle2 = "주별 매출현황";
			} else if (option.compareTo("월") == 0) {
				// 현재 날짜를 기준으로 일 년 이내의 내용을 출력

				/*
				 * LinkedList<HerbOrderTable> res = testDB.selectHerbOrderTable(
				 * "select * from herb_order " +
				 * "where date(order_date) >= date(subdate(now(), INTERVAL 1 YEAR)) and date(order_date) <= date(now())"
				 * );
				 */
				LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
				ResultSet resultSet = db
						.exec("select * from herb_order "
								+ "where date(order_date) >= date(subdate(now(), INTERVAL 1 YEAR)) and date(order_date) <= date(now())");

				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

				while (resultSet.next()) {
					HerbOrderTable hot = new HerbOrderTable();
					hot.setOrder_id(resultSet.getInt("order_id"));
					hot.setOrder_menu_id(resultSet.getInt("order_menu_id"));
					hot.setOrder_count(resultSet.getInt("order_count"));

					hot.setOrder_date(format.parse(
							resultSet.getString("order_date"),
							new ParsePosition(0)));
					res.add(hot);
				}
				Iterator<HerbOrderTable> i = res.iterator();
				long sumOfSales[] = new long[12];
				for (int tmp = 0; tmp < sumOfSales.length; tmp++) {
					sumOfSales[tmp] = 0;
				}
				int timeIndex = 0; //
				Date now = new Date();
				long nowDay = now.getTime() / (3600 * 24000); // 1970~ 현재 일 수
				while (i.hasNext()) {
					HerbOrderTable hot = (HerbOrderTable) i.next();
					long orderDay = hot.getOrder_date().getTime()
							/ (3600 * 24000);

					// int hour =
					// Integer.valueOf(time.format(hot.getOrder_date())).intValue();
					// System.out.println(hour+",");
					if (nowDay - orderDay > 30 * 11) { // 6일 전
						timeIndex = 0;
					} else if (nowDay - orderDay > 30 * 10) {
						timeIndex = 1;
					} else if (nowDay - orderDay > 30 * 9) {
						timeIndex = 2;
					} else if (nowDay - orderDay > 30 * 8) {
						timeIndex = 3;
					} else if (nowDay - orderDay > 30 * 7) {
						timeIndex = 4;
					} else if (nowDay - orderDay > 30 * 6) {
						timeIndex = 5;
					} else if (nowDay - orderDay > 30 * 5) {
						timeIndex = 6;
					} else if (nowDay - orderDay > 30 * 4) {
						timeIndex = 7;
					} else if (nowDay - orderDay > 30 * 3) {
						timeIndex = 8;
					} else if (nowDay - orderDay > 30 * 2) {
						timeIndex = 9;
					} else if (nowDay - orderDay > 30 * 1) {
						timeIndex = 10;
					} else {
						timeIndex = 11;
					}
					/*
					 * LinkedList<HerbMenuTable> res2 =
					 * testDB.selectHerbMenuTable( "select * from herb_menu " +
					 * "where menu_id = " + hot.getOrder_menu_id()
					 * 
					 * );
					 */
					LinkedList<HerbMenuTable> res2 = new LinkedList<HerbMenuTable>();
					ResultSet resultSet2 = db.exec("select * from herb_menu "
							+ "where menu_id = " + hot.getOrder_menu_id());

					String DATE_FORMAT2 = "yyyy-MM-dd kk:mm:ss";
					SimpleDateFormat format2 = new SimpleDateFormat(DATE_FORMAT2);

					while (resultSet2.next()) {
						HerbMenuTable hmt = new HerbMenuTable();
						hmt.setMenu_id(resultSet2.getInt("menu_id"));
						hmt.setMenu_name(resultSet2.getString("menu_name"));
						hmt.setMenu_price(resultSet2.getInt("menu_price"));
						hmt.setMenu_category(resultSet2
								.getString("menu_category"));
						hmt.setMenu_reg_date(format2.parse(
								resultSet2.getString("menu_reg_date"),
								new ParsePosition(0)));

						res2.add(hmt);
					}
					if( resultSet != null )resultSet.close();
					if( resultSet2 != null )resultSet2.close();

					sumOfSales[timeIndex] += res2.get(0).getMenu_price();
				}

				series.add(-11, sumOfSales[0]);
				series.add(-10, sumOfSales[1]);
				series.add(-9, sumOfSales[2]);
				series.add(-8, sumOfSales[3]);
				series.add(-7, sumOfSales[4]);
				series.add(-6, sumOfSales[5]);
				series.add(-5, sumOfSales[6]);
				series.add(-4, sumOfSales[7]);
				series.add(-3, sumOfSales[8]);
				series.add(-2, sumOfSales[9]);
				series.add(-1, sumOfSales[10]);
				series.add(0, sumOfSales[11]);
				chart_subTitle1 = "Month";
				chart_subTitle2 = "월별 매출현황";
			} else if (option.compareTo("일년") == 0) {
				// 현재 날짜를 기준으로 5 년 이내의 내용을 출력
				/*
				 * LinkedList<HerbOrderTable> res = testDB.selectHerbOrderTable(
				 * "select * from herb_order " +
				 * "where date(order_date) >= date(subdate(now(), INTERVAL 5 YEAR)) and date(order_date) <= date(now())"
				 * );
				 */
				LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
				ResultSet resultSet = db
						.exec("select * from herb_order "
								+ "where date(order_date) >= date(subdate(now(), INTERVAL 5 YEAR)) and date(order_date) <= date(now())");

				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

				while (resultSet.next()) {
					HerbOrderTable hot = new HerbOrderTable();
					hot.setOrder_id(resultSet.getInt("order_id"));
					hot.setOrder_menu_id(resultSet.getInt("order_menu_id"));
					hot.setOrder_count(resultSet.getInt("order_count"));

					hot.setOrder_date(format.parse(
							resultSet.getString("order_date"),
							new ParsePosition(0)));
					res.add(hot);
				}
				Iterator<HerbOrderTable> i = res.iterator();
				long sumOfSales[] = new long[5];
				for (int tmp = 0; tmp < sumOfSales.length; tmp++) {
					sumOfSales[tmp] = 0;
				}
				int timeIndex = 0; //
				Date now = new Date();
				long nowDay = now.getTime() / (3600 * 24000); // 1970~ 현재 일 수
				while (i.hasNext()) {
					HerbOrderTable hot = (HerbOrderTable) i.next();
					long orderDay = hot.getOrder_date().getTime()
							/ (3600 * 24000);

					// int hour =
					// Integer.valueOf(time.format(hot.getOrder_date())).intValue();
					// System.out.println(hour+",");
					if (nowDay - orderDay > 365 * 4) { // 6일 전
						timeIndex = 0;
					} else if (nowDay - orderDay > 365 * 3) {
						timeIndex = 1;
					} else if (nowDay - orderDay > 365 * 2) {
						timeIndex = 2;
					} else if (nowDay - orderDay > 365 * 1) {
						timeIndex = 3;
					} else {
						timeIndex = 4;
					}
					/*
					 * LinkedList<HerbMenuTable> res2 =
					 * testDB.selectHerbMenuTable( "select * from herb_menu " +
					 * "where menu_id = " + hot.getOrder_menu_id()
					 * 
					 * );
					 */
					LinkedList<HerbMenuTable> res2 = new LinkedList<HerbMenuTable>();
					ResultSet resultSet2 = db.exec("select * from herb_menu "
							+ "where menu_id = " + hot.getOrder_menu_id());

					String DATE_FORMAT2 = "yyyy-MM-dd kk:mm:ss";
					SimpleDateFormat format2 = new SimpleDateFormat(DATE_FORMAT2);

					while (resultSet2.next()) {
						HerbMenuTable hmt = new HerbMenuTable();
						hmt.setMenu_id(resultSet2.getInt("menu_id"));
						hmt.setMenu_name(resultSet2.getString("menu_name"));
						hmt.setMenu_price(resultSet2.getInt("menu_price"));
						hmt.setMenu_category(resultSet2
								.getString("menu_category"));
						hmt.setMenu_reg_date(format2.parse(
								resultSet2.getString("menu_reg_date"),
								new ParsePosition(0)));

						res2.add(hmt);
					}
					if( resultSet != null )resultSet.close();
					if( resultSet2 != null )resultSet2.close();

					sumOfSales[timeIndex] += res2.get(0).getMenu_price();
				}
				/*
				series.add(-8, sumOfSales[0]);
				series.add(-7, sumOfSales[0]);
				series.add(-6, sumOfSales[0]);
				series.add(-5, sumOfSales[0]);
				
				series.add(-4, sumOfSales[0]);
				series.add(-3, sumOfSales[1]);
				*/
				//series.add(-4, 5000);
				//series.add(-3, 7000);
				series.add(-2, sumOfSales[2]);
				series.add(-1, sumOfSales[3]);
				series.add(0, sumOfSales[4]);
				chart_subTitle1 = "Year";
				chart_subTitle2 = "년도별 매출현황";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ChartPanel getHistogramChart_HistogramChart() {
		ChartPanel chartPanel_HistogramChart = null;
		dataset = new XYSeriesCollection(series);
		
		//final XYSeriesCollection datasetC = new XYSeriesCollection(series);
		 dataset.setAutoWidth(true);
	     dataset.setIntervalWidth(0.2);//set width here
	    
	    //return dataset;
	     
	    // chartFactory.createBar
		chart = ChartFactory.createXYBarChart("Sales Monitoring",
				chart_subTitle2, false, "Sale", (IntervalXYDataset) dataset,
				org.jfree.chart.plot.PlotOrientation.VERTICAL, true, true,
				false);
		chart.setBorderVisible(false);
		
		subTitle = new TextTitle(chart_subTitle2);

		chart.setBackgroundPaint(Color.WHITE);
		chart.addSubtitle(subTitle);
		chartPanel_HistogramChart = new ChartPanel(chart);
		
		//chartPanel_HistogramChart.setSize(1024, 500);
		return chartPanel_HistogramChart;
	}
}
