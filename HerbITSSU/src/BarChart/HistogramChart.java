package BarChart;

import java.awt.Color;
import java.awt.Font;
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
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import Approximator.Nth_InterpolatingPolynomial;
import Database.DBGenerator;
import Database.HerbMenuTable;
import Database.HerbOrderTable;

/*����Ʈ */
public class HistogramChart {
	private DefaultCategoryDataset dataSet = null;
	private JFreeChart chart = null;
	private TextTitle subTitle = null;
	String menu, chart_subTitle1, chart_subTitle2;
	String date;
	private int opt = -1;
	DBGenerator db;
	String category_sumOfSales = "���� �հ�", category_approximationData = "���� �Ǹŷ�";
	
	public HistogramChart(DBGenerator db, int opt) {
		menu = "Sum of Sales";

		dataSet = new DefaultCategoryDataset();
		this.db = db;
		this.opt = opt;
	}
	public void setTime(String year, String month, String day) {
		this.date = "\""+year+"-"
				+ month+"-"
				+ day
				+" 00:00:00"
				+"\"";
	}
	public void setData(String option) {
		try {
			if (option.compareTo("�ð�") == 0) {
				
				String targetDate = date.substring(1, date.length()-1);
				
				chart_subTitle1 = "Hour";
				chart_subTitle2 = "�ð��� ������Ȳ";
				ResultSet resultSet = db.exec(
						"select m.menu_price, o.order_count, o.order_date " +
						" from herb_menu as m " +
						" join herb_order as o "
						+ " on " +
						" m.menu_id = o.order_menu_id and " +
						" date(o.order_date) = date(" +
						//"now()" +
						date +
						");");
				SimpleDateFormat time = new SimpleDateFormat("kk");	//�ð�
				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
				SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				if (dayFormat.format(dayFormat.parse(targetDate, new ParsePosition(0)))
						.compareTo(dayFormat.format(new Date())) == 0 ) {
					targetDate = format.format(new Date());
				}
				
				boolean isToday = true;
				if (dayFormat.parse(targetDate, new ParsePosition(0)).before(
						dayFormat.parse(dayFormat.format(
								new Date()), new ParsePosition(0))))
					isToday = false;	//������ �ε����� ���� �ð��� �ƴ��� Ȯ��
				
				long sumOfSales[] = new long[6];
				for (int tmp = 0; tmp < sumOfSales.length; tmp++) {
					sumOfSales[tmp] = 0;
				}
				
				while (resultSet.next()) {
					int hour = Integer
							.valueOf(time.format(format.parse(resultSet.getString("order_date"), new ParsePosition(0))))
							.intValue();
					int timeIndex = 0; // 9~11 : 0, 11~13 : 1, 13~15 : 2, 15~17 : 3,
					// 17~19 : 4, 19~21 : 5		
					
				
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
					
					sumOfSales[timeIndex] += resultSet.getInt("menu_price") * resultSet.getInt("order_count");
				}
					//������ ���ؽð� ���
				String nowHour = time.format( format.parse(targetDate, new ParsePosition(0)) );
				int k = Integer.valueOf(nowHour).intValue();
				int pos = -1;
				if (k < 11) {
					pos = 0;
				} else if (k < 13) {
					pos = 1;
				} else if (k < 15) {
					pos = 2;
				} else if (k < 17) {
					pos = 3;
				} else if (k < 19) {
					pos = 4;
				} else {
					pos = 5;
				}
				
				if (isToday == false) {
					pos = 5;
				}
				
				
				
				int tmp[] = new int[pos + 1];
				int j=0, startPos=0;
				/* tmp�迭�� ��ǥ�� x�� ���� */
				for (int tp= 9 ; j < tmp.length ; tp+=2 ,j++ ) {
					tmp[j] = tp;
				}
				
				
				for( j = startPos; j<tmp.length; j++) {
					dataSet.addValue(sumOfSales[j], category_sumOfSales, tmp[j]+"��" );
				}
				
				int size = tmp.length - startPos;
				long arrayBiggerThanZero[] = new long[size];
				
				for (int tp = 0; startPos < tmp.length; tp++, startPos ++) {
					arrayBiggerThanZero[tp] = sumOfSales[startPos];
				}
				//���� ��� ����

				if (isToday == false) return;
				
				
				String day = "day", dayTmp = "temp";
				ResultSet resultAllSet = null;
				switch (opt) {
				case 0:
					//��ü �
					
				case 1:
					//��ü ����
					resultAllSet = db.exec(
							"select m.menu_price, o.order_count, o.order_date " +
							" from herb_menu as m " +
							" join herb_order as o "
							+ " on " +
							" m.menu_id = o.order_menu_id " +
							" and " +
							"date (o.order_date) <= date (" +
							date +
							")" +
							";");
					break;
				case 2:
					//�Ϻ� �
					
				case 3:
					//�Ϻ� ����
					resultAllSet = db.exec(
							"select m.menu_price, o.order_count, o.order_date " +
							" from herb_menu as m " +
							" join herb_order as o "
							+ " on " +
							" m.menu_id = o.order_menu_id " +
							" and " +
							"date (o.order_date) = date (" +
							date +
							")" +
							";");
					break;
				}
				
				LinkedList<Long []> list = new LinkedList<Long []>();//////////////////
				Long sumOfSalesAll[] = new Long[6];
				
				for (int i=0; i<6; i++) sumOfSalesAll[i] = Long.valueOf(0).longValue();
				boolean continueState = resultAllSet.next();
				while (continueState) {
					dayTmp = dayFormat.format(dayFormat.parse(resultAllSet.getString("order_date"), new ParsePosition(0)));
					if (day.compareTo(dayTmp) != 0) {
						//�Ϸ�� �ð� ������ �����Ͽ� ����Ʈȭ 
						// while�� ó���� ���� ��
						// �Ϸ簡 �ٲ� ������ ����Ʈ�� �迭�ֱ�
							day = dayTmp;
							list.add(sumOfSalesAll);		//���� ó���� ���� �ϳ��� �ִ� �迭�� ���Ե�
							sumOfSalesAll = new Long[6];
							for (int i = 0; i < sumOfSalesAll.length; i++) {
								sumOfSalesAll[i] = Long.valueOf(0).longValue();
							}
					}
					
					int hour = Integer
							.valueOf(time.format(format.parse(resultAllSet.getString("order_date"), new ParsePosition(0))))
							.intValue();
					int timeIndex = 0; // 9~11 : 0, 11~13 : 1, 13~15 : 2, 15~17 : 3,
					// 17~19 : 4, 19~21 : 5		
					
				
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
					
					sumOfSalesAll[timeIndex] += resultAllSet.getInt("m.menu_price") * resultAllSet.getInt("o.order_count");
					
					//�������� ���Ե��� ���� ���� �ֱ�
					if ((continueState = resultAllSet.next()) == false) {
						for (int tmpInd = sumOfSalesAll.length - 1; tmpInd >= 0; tmpInd--) {
							if (sumOfSalesAll[tmpInd] == 0) 
								sumOfSalesAll[tmpInd] = Long.valueOf(-1).longValue();  
							else 
								break;
						}
						list.add(sumOfSalesAll);
					}
						
				}
				if (list.isEmpty() == false) list.removeFirst();		//���� ó���� ���Ե� ���� �ϳ��� �����
				
				//����Ʈ�� ��� �ֹ������� �ֱ� ��
				//�迭�� ����Ʈ�� �ű�
				long sumOfSalesAllArray[] = new long[list.size() * sumOfSalesAll.length];
				int indSum =0;
				int x[] = new int[list.size() * sumOfSalesAll.length];
				for (int i = 0 ; i < list.size(); i++ ) {
					sumOfSalesAll = list.get(i);
					for (int t = 0; t < sumOfSalesAll.length; t++, indSum++) {
						if (sumOfSalesAll[t] == -1) break;
						sumOfSalesAllArray[ indSum ] = sumOfSalesAll[t];
						x[ indSum ] = indSum;
					
					}
				}
				
				indSum --;
				if (opt == 1 || opt == 3) indSum = 1;		//������ư�� Ŭ������ ��� 1���� �ٲ� 
				//������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
				Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
				//		(tmp, arrayBiggerThanZero, tmp[tmp.length-1]+2, arrayBiggerThanZero.length-1 );
						(x, sumOfSalesAllArray, indSum+2, indSum
								);
				
				System.out.println(indSum+"��°, "+x +":" + sumOfSalesAllArray.length);
				long val = appx.getVal();	//������ ��
				System.out.println(val);
				if( val < 0) val =0; //����  ����
				dataSet.addValue(val, category_approximationData, tmp[tmp.length-1] +2 +"��");
				

			} else if (option.compareTo("��") == 0) {

				chart_subTitle1 = "Day";
				chart_subTitle2 = "�Ϻ� ������Ȳ";
				// �Է¹��� ��¥�� �������� 7�� �̳��� �Ǹų����� ���
				LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
				ResultSet resultSet = db
						.exec("select * from herb_order "
								+ "where date(order_date)  >= date(subdate("
								+ date
								+", INTERVAL 7 DAY)) and date(order_date) <= date(" 
								+ date
								+")");

				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
				SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
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
				int timeIndex = 0; // ���簡 �Ͽ����̶� ������ ��� �� : 0, ȭ : 1, �� : 2, �� :
								// 3, �� : 4, �� : 5, �� : 6
				Date now = format.parse(date.substring(1, date.length()-1), new ParsePosition(0));
				long nowTime = now.getTime(); // 1970~ ���� �� ��
				boolean isNearVal= true;
				String targetDate = date.substring(1, date.length()-1);
				ResultSet beforeTest = db.exec("SELECT m.menu_price, o.order_count, o.order_date " +
				" FROM herb_menu as m " +
				" JOIN herb_order as o "
				+ " ON m.menu_id = o.order_menu_id " +
				" AND date (o.order_date) <= date (" +	date +	")" +
				" ORDER BY order_date ASC" +
				" LIMIT 1" +";");
				
				Date firstDay = null;
				boolean isNotGeneratedDay = true;
				if (beforeTest.next()) {
					 firstDay = dayFormat.parse(beforeTest.getString("order_date"), new ParsePosition(0));
					 firstDay.setTime(firstDay.getTime() - 10);
					 if (now.after(firstDay))
						 isNotGeneratedDay = false;
				}
				
				if (
						dayFormat.parse(targetDate, new ParsePosition(0)).getTime() / (3600 * 24000)
						<
						new Date().getTime()/(3600*24000) - 7
						) 
					isNearVal = false;	//������ �ð��ε����� 7�� �̳� �ð����� Ȯ��
						
				
					
				
				
				
				long tomorrowDayTime = ((nowTime/(24000 *3600))*24000*3600) + 24000*3600;	//���� ������ milliseconds
				System.out.println(tomorrowDayTime);
				System.out.println("-------");
				while (i.hasNext()) {
					
					HerbOrderTable hot = (HerbOrderTable) i.next();
					long orderTime= hot.getOrder_date().getTime();
					System.out.println(orderTime);
					
					long diffMillis = tomorrowDayTime - orderTime;
					int diffDay = (int)(diffMillis/(24*60*60*1000));
					System.out.println(diffDay);
					
					if (diffDay >= 6) { // 6�� ��
						timeIndex = 0;
					} else if (diffDay >= 5) {
						timeIndex = 1;
					} else if (diffDay >= 4) {
						timeIndex = 2;
					} else if (diffDay >= 3) {
						timeIndex = 3;
					} else if (diffDay >= 2) {
						timeIndex = 4;
					} else if (diffDay >= 1) {
						timeIndex = 5;
					} else {
						timeIndex = 6;
					}
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

					sumOfSales[timeIndex] += res2.get(0).getMenu_price()* hot.getOrder_count();
				}
		
				
				
				int tmp[] = new int[7];
				int j=0, startPos=-1;
				/* tmp�迭�� ��ǥ�� x�� ���� */
				for (int tp= -6 ; j < tmp.length ; tp++,j++ ) {
					tmp[j] = tp;
				}
				startPos = 0;
				
				//if( startPos == -1) startPos = 0;
				for( j = startPos; j<tmp.length; j++) {
					//System.out.println(j);
					if (tmp[j] == 0) {
						dataSet.addValue(sumOfSales[j], category_sumOfSales, "���س�¥");
						continue;
					}
					dataSet.addValue(sumOfSales[j], category_sumOfSales, (0-tmp[j])+"�� ��");
				}
				
				int size = tmp.length - startPos;
				long arrayBiggerThanZero[] = new long[size];
				
				for (int tp =0; startPos < tmp.length; tp++, startPos ++) {
					arrayBiggerThanZero[tp] = sumOfSales[startPos];
				}
				
				//���� ��� ����
				if (isNearVal == false || isNotGeneratedDay == true) {
					
					return;
				}
				
				ResultSet resultAllSet = null;
				switch (opt) {
				case 0:
					//��ü �
					
				case 1:
					//��ü ����
					resultAllSet = db.exec(
							"select m.menu_price, o.order_count, o.order_date " +
							" from herb_menu as m " +
							" join herb_order as o "
							+ " on " +
							" m.menu_id = o.order_menu_id " +
							" and " +
							"date (o.order_date) <= date (" +
							date +
							")" +
							";");
					break;
				case 2:
					//�Ϻ� �
					
				case 3:
					//�Ϻ� ����
					resultAllSet = db.exec(
							"select m.menu_price, o.order_count, o.order_date " +
							" from herb_menu as m " +
							" join herb_order as o "
							+ " on " +
							" m.menu_id = o.order_menu_id " +
							" and " +
							"date (o.order_date) >= date (subdate(" +
							date +
							", INTERVAL 7 DAY)" +
							")" +
							"and date(o.order_date) <= date(" +
							date +
							");"
							);
					break;
				}
				LinkedList <Long> list = new LinkedList<Long>();
				LinkedList <Long> listInDay = new LinkedList<Long>();
				
				boolean continueState = resultAllSet.next();
				//String day="day", dayTmp="dayTmp";
				long orderDayTmp1 = -1, orderDayTmp2= -1;
				if (continueState)
					orderDayTmp1 = dayFormat.parse(resultAllSet.getString("order_date"), new ParsePosition(0)).getTime()  / (3600 * 24000);
				while (continueState) {
					//��¥�� �޾ƿ���
					orderDayTmp2 = dayFormat.parse(resultAllSet.getString("order_date"), new ParsePosition(0)).getTime() / (3600 * 24000);
					if (orderDayTmp1 != orderDayTmp2) {
						//��¥�� �ٸ��� ����Ʈ�� �ֱ�
						long daySum = 0;
						for (Iterator it = listInDay.iterator(); it.hasNext();) {
							daySum += (long)it.next();
						}
						list.add(daySum);
						listInDay.clear();
						
						//2���̻� ���̳��°�� 0���� ä���
						
						while (orderDayTmp2 - orderDayTmp1 >= 2) {
							orderDayTmp1 ++;
							list.add(Long.valueOf(0));
						}
						orderDayTmp1 = orderDayTmp2;
						
					}
					listInDay.add(((Integer)(resultAllSet.getInt("m.menu_price") * resultAllSet.getInt("o.order_count"))).longValue());
					continueState = resultAllSet.next();
					if (continueState == false) {
						long daySum = 0;
						for (Iterator it = listInDay.iterator(); it.hasNext();) {
							daySum += (long)it.next();
						}
						list.add(daySum);
					}
				}
				//if (list.isEmpty() == false) list.removeFirst();
				int x[] = new int[list.size()];
				for (int x_tmp = 0; x_tmp < x.length; x_tmp ++) {
					x[x_tmp] = x_tmp;
					System.out.println("x: "+x[x_tmp]);
				}
				
				long [] y = new long[ list.size() ];
				int ind=0;
				for (Iterator it = list.iterator(); it.hasNext(); ) {
					y[ind++] = (long)it.next();
					System.out.println(ind-1+": "+y[ind-1]);
				}
				// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
				int indSum = x.length - 1;
				if (opt == 1 || opt == 3) indSum = 1;
				Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
						(x, y, x.length, indSum );
					//	(tmp, arrayBiggerThanZero, 1, 1 );
				//����  ����
				long val = appx.getVal();
				System.out.println(val);
				if( val < 0) val =0;
				dataSet.addValue( val, category_approximationData, "1�� ��");
				
			} else if (option.compareTo("��") == 0) {
				// �ش� ��¥�� �������� �� ��(28)�̳��� �Ǹų����� ���

				chart_subTitle1 = "Week";
				chart_subTitle2 = "�ֺ� ������Ȳ";
				LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
				
				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
				SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date now = format.parse(date.substring(1, date.length()-1), new ParsePosition(0));
				boolean isNearVal= true;
				String targetDate = date.substring(1, date.length()-1);
				ResultSet beforeTest = db.exec("SELECT m.menu_price, o.order_count, o.order_date " +
				" FROM herb_menu as m " +
				" JOIN herb_order as o "
				+ " ON m.menu_id = o.order_menu_id " +
				" AND date (o.order_date) <= date (" +	date +	")" +
				" ORDER BY order_date ASC" +
				" LIMIT 1" +";");
				
				Date firstDay = null;
				boolean isNotGeneratedDay = true;
				if (beforeTest.next()) {
					 firstDay = dayFormat.parse(beforeTest.getString("order_date"), new ParsePosition(0));
					 firstDay.setTime(firstDay.getTime() - 10);
					 if (now.after(firstDay))
						 isNotGeneratedDay = false;
				}
				
				
				
				if (
						dayFormat.parse(targetDate, new ParsePosition(0)).getTime() / (3600 * 24000)
						<
						new Date().getTime()/(3600*24000) - 28
						) 
					isNearVal = false;	//������ �ð��ε����� 28�� �̳� �ð����� Ȯ��
					
				ResultSet resultSet = db
						.exec("select * from herb_order "
								+ "where date(order_date) >= date(subdate(" 
								+ date
								+ ", INTERVAL 28 DAY)) and date(order_date) <= date("
							 + date
							 +")");

				
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
				
				long nowDay = now.getTime() / (3600 * 24000); // 1970~ ���� �� ��
				while (i.hasNext()) {
					HerbOrderTable hot = (HerbOrderTable) i.next();
					long orderDay = hot.getOrder_date().getTime()
							/ (3600 * 24000);

					
					if (nowDay - orderDay > 21) { // 21�� ��
						timeIndex = 0;
					} else if (nowDay - orderDay > 14) {
						timeIndex = 1;
					} else if (nowDay - orderDay > 7) {
						timeIndex = 2;
					} else {
						timeIndex = 3;
					}
				
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

					sumOfSales[timeIndex] += res2.get(0).getMenu_price() * hot.getOrder_count();
				}

				
				int tmp[] = new int[4];
				int j=0, startPos=-1;
				/* tmp�迭�� ��ǥ�� x�� ���� */
				for (int tp= -3 ; j < tmp.length ; tp++,j++ ) {
					tmp[j] = tp;
				}
				
				startPos = 0;
				
				//if( startPos == -1) startPos = 0;
				for( j = startPos; j<tmp.length; j++) {
					if (tmp[j] == 0) {
						dataSet.addValue(sumOfSales[j], category_sumOfSales, "���� ����");
						continue;
					}
					dataSet.addValue( sumOfSales[j], category_sumOfSales
							, (0-tmp[j]) +"���� ��");
				}
				
				int size = tmp.length - startPos;
				long arrayBiggerThanZero[] = new long[size];
				
				for (int tp =0; startPos < tmp.length; tp++, startPos ++) {
					arrayBiggerThanZero[tp] = sumOfSales[startPos];
				}
				//���� ��� ����
				if (isNearVal == false || isNotGeneratedDay == true) return;
				
				ResultSet resultAllSet = null;
				switch (opt) {
				case 0:
					//��ü �
					
				case 1:
					//��ü ����
					resultAllSet = db.exec(
							"select m.menu_price, o.order_count, o.order_date " +
							" from herb_menu as m " +
							" join herb_order as o "
							+ " on " +
							" m.menu_id = o.order_id " +
							" and " +
							"date (o.order_date) <= date (" +
							date +
							") ORDER BY o.order_date DESC" +
							";");
					break;
				case 2:
					//�Ϻ� �
					
				case 3:
					//�Ϻ� ����
					resultAllSet = db.exec(
							"select m.menu_price, o.order_count, o.order_date " +
							" from herb_menu as m " +
							" join herb_order as o "
							+ " on " +
							" m.menu_id = o.order_id " +
							" and " +
							"date (o.order_date) >= date (subdate(" +
							date +
							", INTERVAL 28 DAY)" +
							")" +
							"and date(o.order_date) <= date(" +
							date +
							") ORDER BY o.order_date DESC" +
							";"
							);
					break;
				}
				LinkedList <Long> list = new LinkedList<Long>();
				LinkedList <Long> listInDay = new LinkedList<Long>();	//�ο� �ϳ��� �Ǹž�
				
				
				boolean continueState = resultAllSet.next();
				if (continueState) {
					nowDay = dayFormat.parse(resultAllSet.getString("order_date"), new ParsePosition(0)).getTime();
					//listInDay.add(((Integer)(resultAllSet.getInt("m.menu_price") * resultAllSet.getInt("o.order_count"))).longValue());
				}
				//nowDay : ������ ��¥(��)
				nowDay = dayFormat.parse(date.substring(1, date.length()-1), new ParsePosition(0)).getTime() / (3600 * 24000);
			
				while (continueState) {
					//��¥�� �޾ƿ���
					long orderDay = dayFormat.parse(resultAllSet.getString("order_date"), new ParsePosition(0)).getTime()
							/ (3600 * 24000);
					
					if ( nowDay - orderDay >= 7) {
						
						//��¥�� 7�� ���̳��� ����Ʈ�� �ֱ�
						
						long daySum = 0;
						for (Iterator it = listInDay.iterator(); it.hasNext();) {
							daySum += (long)it.next();
						}
						list.addFirst(daySum);
						listInDay.clear();
						//2���Ϻ��� �� ���̳��°�� 0���� ä���
						while (nowDay - orderDay > 13) {
							nowDay -= 7;
							list.addFirst(Long.valueOf(0));
						}
						nowDay -= 7;
						//nowDay = orderDay;
					}
					listInDay.add(((Integer)(resultAllSet.getInt("m.menu_price") * resultAllSet.getInt("o.order_count"))).longValue());
					continueState = resultAllSet.next();
					if (continueState == false) {
						long daySum = 0;
						for (Iterator it = listInDay.iterator(); it.hasNext();) {
							daySum += (long)it.next();
						}
						list.addFirst(daySum);
					}
				}
				//if (list.isEmpty() == false)list.removeFirst();
				int x[] = new int[list.size()];
				for (int x_tmp = 0; x_tmp < x.length; x_tmp ++) {
					x[x_tmp] = x_tmp;
				}
				
				long [] y = new long[ list.size() ];
				int ind=0;
				
				for (Iterator it = list.iterator(); it.hasNext(); ) {
					y[ind++] = (long)it.next();
					
				}
				// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
				int indSum = x.length - 1;
				if (opt == 1 || opt == 3) indSum = 1;
				
				Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
						(x, y, x.length, indSum );
				
				//����  ����
				long val = appx.getVal();
				System.out.println(val);
				if( val < 0) val =0;
				dataSet.addValue( val, category_approximationData
						, "1���� ��");
				
			} else if (option.compareTo("��") == 0) {
				// ���� ��¥�� �������� �� �� �̳��� ������ ���


				chart_subTitle1 = "Month";
				chart_subTitle2 = "���� ������Ȳ";
				LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
				

				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
				SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date now = format.parse(date.substring(1, date.length()-1), new ParsePosition(0));
				long nowTime = now.getTime(); // 1970~ ���� �� ��
				boolean isNearVal= true;
				String targetDate = date.substring(1, date.length()-1);
				ResultSet beforeTest = db.exec("SELECT m.menu_price, o.order_count, o.order_date " +
				" FROM herb_menu as m " +
				" JOIN herb_order as o "
				+ " ON m.menu_id = o.order_menu_id " +
				" AND date (o.order_date) <= date (" +	date +	")" +
				" ORDER BY order_date ASC" +
				" LIMIT 1" +";");
				
				Date firstDay = null;
				boolean isNotGeneratedDay = true;
				if (beforeTest.next()) {
					 firstDay = dayFormat.parse(beforeTest.getString("order_date"), new ParsePosition(0));
					 firstDay.setTime(firstDay.getTime() - 10);
					 if (now.after(firstDay))
						 isNotGeneratedDay = false;
				}
				
				
				
				if (
						dayFormat.parse(targetDate, new ParsePosition(0)).getTime() / (3600 * 24000)
						<
						new Date().getTime()/(3600*24000) - 365
						) 
					isNearVal = false;	//������ �ð��ε����� 1���̳� �ð����� Ȯ��
						
				ResultSet resultSet = db
						.exec("select * from herb_order "
								+ "where date(order_date) >= date(subdate("
								+ date
								+", INTERVAL 1 YEAR)) and date(order_date) <= date("
								+ date
								+")");
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
				//now = new Date();
				long nowDay = now.getTime() / (3600 * 24000); // 1970~ ���� �� ��
				while (i.hasNext()) {
					HerbOrderTable hot = (HerbOrderTable) i.next();
					long orderDay = hot.getOrder_date().getTime()
							/ (3600 * 24000);

					if (nowDay - orderDay > 30 * 11) { // 6�� ��
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

					sumOfSales[timeIndex] += res2.get(0).getMenu_price() * hot.getOrder_count();
				}
				
				int tmp[] = new int[12];
				int j=0, startPos=-1;
				/* tmp�迭�� ��ǥ�� x�� ���� */
				for (int tp= -11 ; j < tmp.length ; tp++,j++ ) {
					tmp[j] = tp;
				}
				startPos = 0;
				
				/*
				 0�� �ƴ� ���� ������ ���� �����͸� ��ǥ�� ��� ���� 
				for ( j=0; j<tmp.length; j++) {
					if (sumOfSales[j] > 0 ) {
						startPos = j;
						break;
					}
				}
				if( startPos == -1) startPos = 0;
				*/
				for( j = startPos; j<tmp.length; j++) {
					if (tmp[j] == 0) {
						dataSet.addValue(sumOfSales[j], category_sumOfSales, "���� ��");
						continue;
					}
					dataSet.addValue( sumOfSales[j], category_sumOfSales, (0-tmp[j])+"���� ��");
				}
				
				int size = tmp.length - startPos;
				long arrayBiggerThanZero[] = new long[size];
				
				for (int tp =0; startPos < tmp.length; tp++, startPos ++) {
					arrayBiggerThanZero[tp] = sumOfSales[startPos];
				}
				//���� ��� ����
				if (isNearVal == false || isNotGeneratedDay == true) return;
				
				ResultSet resultAllSet = null;
				switch (opt) {
				case 0:
					//��ü �
					
				case 1:
					//��ü ����
					resultAllSet = db.exec(
							"select m.menu_price, o.order_count, o.order_date " +
							" from herb_menu as m " +
							" join herb_order as o "
							+ " on " +
							" m.menu_id = o.order_id " +
							" and " +
							"date (o.order_date) <= date (" +
							date +
							") ORDER BY o.order_date DESC" +
							";");
					break;
				case 2:
					//�Ϻ� �
					
				case 3:
					//�Ϻ� ����
					resultAllSet = db.exec(
							"select m.menu_price, o.order_count, o.order_date " +
							" from herb_menu as m " +
							" join herb_order as o "
							+ " on " +
							" m.menu_id = o.order_id " +
							" and " +
							"date (o.order_date) >= date (subdate(" +
							date +
							", INTERVAL 1 YEAR)" +
							")" +
							"and date(o.order_date) <= date(" +
							date +
							") ORDER BY o.order_date DESC" +
							";"
							);
					break;
				}
				LinkedList <Long> list = new LinkedList<Long>();
				LinkedList <Long> listInDay = new LinkedList<Long>();	//�ο� �ϳ��� �Ǹž�
				
				
				boolean continueState = resultAllSet.next();
				if (continueState) {
					nowDay = dayFormat.parse(resultAllSet.getString("order_date"), new ParsePosition(0)).getTime();
					//listInDay.add(((Integer)(resultAllSet.getInt("m.menu_price") * resultAllSet.getInt("o.order_count"))).longValue());
				}
				//nowDay : ������ ��¥(��)
				nowDay = dayFormat.parse(date.substring(1, date.length()-1), new ParsePosition(0)).getTime() / (3600 * 24000);
			
				while (continueState) {
					//��¥�� �޾ƿ���
					long orderDay = dayFormat.parse(resultAllSet.getString("order_date"), new ParsePosition(0)).getTime()
							/ (3600 * 24000);
					
					if ( nowDay - orderDay >= 30) {
						
						//��¥�� 30�� ���̳��� ����Ʈ�� �ֱ�
						
						long daySum = 0;
						for (Iterator it = listInDay.iterator(); it.hasNext();) {
							daySum += (long)it.next();
						}
						list.addFirst(daySum);
						listInDay.clear();
						//�δ޺��� �� ���̳��°�� 0���� ä���
						while (nowDay - orderDay >= 60) {
							nowDay -= 30;
							list.addFirst(Long.valueOf(0));
						}
						nowDay -= 30;
						//nowDay = orderDay;
					}
					listInDay.add(((Integer)(resultAllSet.getInt("m.menu_price") * resultAllSet.getInt("o.order_count"))).longValue());
					continueState = resultAllSet.next();
					if (continueState == false) {
						long daySum = 0;
						for (Iterator it = listInDay.iterator(); it.hasNext();) {
							daySum += (long)it.next();
						}
						list.addFirst(daySum);
					}
				}
				//if (list.isEmpty() == false)list.removeFirst();
				int x[] = new int[list.size()];
				for (int x_tmp = 0; x_tmp < x.length; x_tmp ++) {
					x[x_tmp] = x_tmp;
				}
				
				long [] y = new long[ list.size() ];
				int ind=0;
				
				for (Iterator it = list.iterator(); it.hasNext(); ) {
					y[ind++] = (long)it.next();
					System.out.println(y[ind-1]);
				}
				// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
				int indSum = x.length - 1;
				if (opt == 1 || opt == 3) indSum = 1;
				
				// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
				Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
						(x, y, x.length, indSum );
						//(tmp, arrayBiggerThanZero, 1, arrayBiggerThanZero.length-1 );
					//	(tmp, arrayBiggerThanZero, 1, 1 );
				//����  ����
				long val = appx.getVal();
				if( val < 0) val =0;
				dataSet.addValue( val, category_approximationData, "1���� ��");
				
				
			} else if (option.compareTo("�ϳ�") == 0) {
				// ���� ��¥�� �������� 5 �� �̳��� ������ ���
				/*
				 * LinkedList<HerbOrderTable> res = testDB.selectHerbOrderTable(
				 * "select * from herb_order " +
				 * "where date(order_date) >= date(subdate(now(), INTERVAL 5 YEAR)) and date(order_date) <= date(now())"
				 * );
				 */
				chart_subTitle1 = "Year";
				chart_subTitle2 = "�⵵�� ������Ȳ";
				
				LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
			

				String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
				SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date now = format.parse(date.substring(1, date.length()-1), new ParsePosition(0));
				long nowTime = now.getTime(); // 1970~ ���� �� ��
				boolean isNearVal= true;
				String targetDate = date.substring(1, date.length()-1);
				ResultSet beforeTest = db.exec("SELECT m.menu_price, o.order_count, o.order_date " +
				" FROM herb_menu as m " +
				" JOIN herb_order as o "
				+ " ON m.menu_id = o.order_menu_id " 
				+ " AND date (o.order_date) <= date (" +	date +	")"
				+ " ORDER BY order_date ASC" 
				+ " LIMIT 1" 
				+ ";");
				
				Date firstDay = null;
				boolean isNotGeneratedDay = true;
				if (beforeTest.next()) {
					 firstDay = dayFormat.parse(beforeTest.getString("order_date"), new ParsePosition(0));
					 firstDay.setTime(firstDay.getTime() - 10);
					 if (now.after(firstDay))
						 isNotGeneratedDay = false;
					 
				}
				
				ResultSet resultSet = db
						.exec("select * from herb_order "
								+ "where date(order_date) >= date(subdate("
								+ date
								+", INTERVAL 5 YEAR)) and date(order_date) <= date(" 
								+ date
								+")");
				
				if (
						dayFormat.parse(targetDate, new ParsePosition(0)).getTime() / (3600 * 24000)
						<
						dayFormat.parse(dayFormat.format(new Date()), new ParsePosition(0)).getTime()/(3600*24000) - 365*5
						) 
					isNearVal = false;	//������ �ð��ε����� 1���̳� �ð����� Ȯ��
				
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
				// now = new Date();
				long nowDay = now.getTime() / (3600 * 24000); // 1970~ ���� �� ��
				while (i.hasNext()) {
					HerbOrderTable hot = (HerbOrderTable) i.next();
					long orderDay = hot.getOrder_date().getTime()
							/ (3600 * 24000);

				
					if (nowDay - orderDay > 365 * 4) { // �� ��
						System.out.println(nowDay+":1: "+orderDay);
						timeIndex = 0;
					} else if (nowDay - orderDay > 365 * 3) {
						System.out.println(nowDay+":2: "+orderDay);
						timeIndex = 1;
					} else if (nowDay - orderDay > 365 * 2) {
						System.out.println(nowDay+":3: "+orderDay);
						timeIndex = 2;
					} else if (nowDay - orderDay > 365 * 1) {
						System.out.println(nowDay+":4: "+orderDay);
						timeIndex = 3;
					} else {
						System.out.println(nowDay+":5: "+orderDay);
						System.out.println(date);
						System.out.println(hot.getOrder_date());
						timeIndex = 4;
					}
					
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
					if (resultSet != null) resultSet.close();
					if (resultSet2 != null) resultSet2.close();

					sumOfSales[timeIndex] += res2.get(0).getMenu_price() * hot.getOrder_count();
				}
				
				int tmp[] = new int[5];
				int j=0, startPos=-1;
				/* tmp�迭�� ��ǥ�� x�� ���� */
				for (int tp= -4 ; j < tmp.length ; tp++,j++ ) {
					tmp[j] = tp;
				}
				
				/* 0�� �ƴ� ���� ������ ���� �����͸� ��ǥ�� ��� ���� */
				for ( j=0; j<tmp.length; j++) {
					if (sumOfSales[j] > 0 ) {
						startPos = j;
						break;
					}
				}
				if( startPos == -1) startPos = 0;
				for( j = startPos; j<tmp.length; j++) {
					if (tmp[j] == 0) {
						dataSet.addValue(sumOfSales[j], category_sumOfSales, "���س⵵");
						continue;
					}
					dataSet.addValue(sumOfSales[j], category_sumOfSales, (0-tmp[j])+"�� ��");
				}
				
				int size = tmp.length - startPos;
				long arrayBiggerThanZero[] = new long[size];
				
				for (int tp =0; startPos < tmp.length; tp++, startPos ++) {
					arrayBiggerThanZero[tp] = sumOfSales[startPos];
				}
				//���� ��� ����
				if (isNearVal == false || isNotGeneratedDay == true) return;
				
				ResultSet resultAllSet = null;
				switch (opt) {
				case 0:
					//��ü �
					
				case 1:
					//��ü ����
					resultAllSet = db.exec(
							"select m.menu_price, o.order_count, o.order_date " +
							" from herb_menu as m " +
							" join herb_order as o "
							+ " on " +
							" m.menu_id = o.order_id " +
							" and " +
							"date (o.order_date) <= date (" +
							date +
							") ORDER BY o.order_date DESC" +
							";");
					break;
				case 2:
					//�Ϻ� �
					
				case 3:
					//�Ϻ� ����
					resultAllSet = db.exec(
							"select m.menu_price, o.order_count, o.order_date " +
							" from herb_menu as m " +
							" join herb_order as o "
							+ " on " +
							" m.menu_id = o.order_id " +
							" and " +
							"date (o.order_date) >= date (subdate(" +
							date +
							", INTERVAL 5 YEAR)" +
							")" +
							"and date(o.order_date) <= date(" +
							date +
							") ORDER BY o.order_date DESC" +
							";"
							);
					break;
				}
				LinkedList <Long> list = new LinkedList<Long>();
				LinkedList <Long> listInDay = new LinkedList<Long>();	//�ο� �ϳ��� �Ǹž�
				
				
				boolean continueState = resultAllSet.next();
				if (continueState) {
					nowDay = dayFormat.parse(resultAllSet.getString("order_date"), new ParsePosition(0)).getTime();
					//listInDay.add(((Integer)(resultAllSet.getInt("m.menu_price") * resultAllSet.getInt("o.order_count"))).longValue());
				}
				//nowDay : ������ ��¥(��)
				nowDay = dayFormat.parse(date.substring(1, date.length()-1), new ParsePosition(0)).getTime() / (3600 * 24000);
			
				while (continueState) {
					//��¥�� �޾ƿ���
					long orderDay = dayFormat.parse(resultAllSet.getString("order_date"), new ParsePosition(0)).getTime()
							/ (3600 * 24000);
					
					if ( nowDay - orderDay >= 365) {
						
						//��¥�� 30�� ���̳��� ����Ʈ�� �ֱ�
						
						long daySum = 0;
						for (Iterator it = listInDay.iterator(); it.hasNext();) {
							daySum += (long)it.next();
						}
						list.addFirst(daySum);
						listInDay.clear();
						//�δ޺��� �� ���̳��°�� 0���� ä���
						while (nowDay - orderDay >= 730) {
							nowDay -= 365;
							list.addFirst(Long.valueOf(0));
						}
						nowDay -= 365;
						//nowDay = orderDay;
					}
					listInDay.add(((Integer)(resultAllSet.getInt("m.menu_price") * resultAllSet.getInt("o.order_count"))).longValue());
					continueState = resultAllSet.next();
					if (continueState == false) {
						long daySum = 0;
						for (Iterator it = listInDay.iterator(); it.hasNext();) {
							daySum += (long)it.next();
						}
						list.addFirst(daySum);
					}
				}
				//if (list.isEmpty() == false)list.removeFirst();
				int x[] = new int[list.size()];
				for (int x_tmp = 0; x_tmp < x.length; x_tmp ++) {
					x[x_tmp] = x_tmp;
				}
				
				long [] y = new long[ list.size() ];
				int ind=0;
				
				for (Iterator it = list.iterator(); it.hasNext(); ) {
					y[ind++] = (long)it.next();
					System.out.println(y[ind-1]);
				}
				// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
				int indSum = x.length - 1;
				if (opt == 1 || opt == 3) indSum = 1;
				
				// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
				Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
						(x, y, x.length, indSum );
				// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
				//Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
						//(tmp, arrayBiggerThanZero, 1, arrayBiggerThanZero.length-1 );
					//	(tmp, arrayBiggerThanZero, 1, 1 );
				//����  ����
				long val = appx.getVal();
				if( val < 0) val =0;
				dataSet.addValue(val, category_approximationData, "1�� ��");
				
				
				//���� ��� ��
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ChartPanel getHistogramChart_HistogramChart() {
		ChartPanel chartPanel_HistogramChart = null;
		
		
		//final XYSeriesCollection datasetC = new XYSeriesCollection(series);
		 //dataSet.setAutoWidth(true);
	     //dataSet.setIntervalWidth(0.2);//set width here
		
	    
	    //return dataset;
	     
	    // chartFactory.createBar
	     /*
		chart = ChartFactory.createXYBarChart("Sales Monitoring",
				chart_subTitle2, false, "Sale", (IntervalXYDataset) dataset,
				org.jfree.chart.plot.PlotOrientation.VERTICAL, true, true,
				false);
				*/
	    
	    
	     //XYSeries Dataset�� CategoryDataset���� ���� �ʿ�.
	     
	     chart = ChartFactory.createBarChart("�Ǹ� ��Ȳ",	//title 
	    		 chart_subTitle2, //categoryAxisLabel
	    		 "�� ����",	//valueAxisLabel
	    		 dataSet,		//dataset
	    		 PlotOrientation.VERTICAL,	//orientation
	    		 true,		//legend
	    		 true, 		//tooltips
	    		 false);	//url
		chart.setBorderVisible(false);
		
		subTitle = new TextTitle(chart_subTitle2);

		chart.setBackgroundPaint(Color.WHITE);
		chart.addSubtitle(subTitle);
		chartPanel_HistogramChart = new ChartPanel(chart);
		
		CategoryPlot plot = (CategoryPlot) chartPanel_HistogramChart.getChart().getPlot();
		Font labelFont = null;
		//labelFont = chart.getTitle().getFont();
		
		//labelFont = chart.getTitle().getFont();
		labelFont = new Font("����", Font.PLAIN, 20);
		chart.getTitle().setFont(new Font("����", labelFont.getStyle(), labelFont.getSize()));
		
		//������ ����

		labelFont = plot.getDomainAxis().getLabelFont();

		plot.getDomainAxis().setLabelFont(new Font("����", labelFont.getStyle(), labelFont.getSize()));

		 

		//������ ���� ���� ���̺�

		labelFont = plot.getDomainAxis().getTickLabelFont();

		plot.getDomainAxis().setTickLabelFont(new Font("����", labelFont.getStyle(), labelFont.getSize()));

		 

		//������ ����

		labelFont = plot.getRangeAxis().getLabelFont();

		plot.getRangeAxis().setLabelFont(new Font("����", labelFont.getStyle(), labelFont.getSize()));

		 

		//������ ���� ���� ���̺�

		labelFont = plot.getRangeAxis().getTickLabelFont();

		plot.getRangeAxis().setTickLabelFont(new Font("����", labelFont.getStyle(), labelFont.getSize()));
		 

		//����        

		chart.getLegend().setItemFont(new Font("����", Font.PLAIN, 20));
		//chartPanel_HistogramChart.setSize(1200, 800);
		
		return chartPanel_HistogramChart;
	}
}
