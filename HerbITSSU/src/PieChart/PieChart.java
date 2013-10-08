package PieChart;

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
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.TableOrder;

import Database.DBGenerator;
import Database.HerbMenuTable;
import Database.HerbOrderTable;

/*�޴��� �Ǹŷ��� �׸��� ������Ʈ */
public class PieChart{
	  private JFreeChart chart = null;
	  
	  private String menu, chart_subTitle1, chart_subTitle2;
	  private String date, subDate;
	  private DBGenerator db;
	  private String category_sumOfSales = "���� �հ�";
	  private DefaultCategoryDataset dataSet;
	 
	  
	  public PieChart(DBGenerator db){
		  dataSet = new DefaultCategoryDataset();
		  this.db = db;
		  /* �Ǹŷ��� �޴��� ���� �κ� */
		  /*
		  myDataset.setValue(10, category_sumOfSales
				  , menuList[0]);
		  myDataset.setValue(50, category_sumOfSales
				  , menuList[13]);
				  */
	  }
	  public void setTime(String year, String month, String day) {
		  this.date = "\""+year+"-"
					+ month+"-"
					+ day
					+" 00:00:00"
					+"\"";
		  this.subDate = date.substring(1, date.length()-1);
	  }
	  public void setData(String option) {
		  try {
				if (option.compareTo("�ð�") == 0) {
					/*
					 * LinkedList<HerbOrderTable> res = testDB.selectHerbOrderTable(
					 * "select * from herb_order " +
					 * "where date(order_date) = date(now())" );
					 */

					LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
					ResultSet resultSet = db.exec("select * from herb_order "
							+ "where date(order_date) = date("
							+ date
							+ ")");

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

						/* �� ���� ����� �ſ� ����. ������ */
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
					/*
					series.add( 9, sumOfSales[0]);
					series.add(11, sumOfSales[1]);
					series.add(13, sumOfSales[2]);
					series.add(15, sumOfSales[3]);
					series.add(17, sumOfSales[4]);
					series.add(19, sumOfSales[5]);
					*/
					format = new SimpleDateFormat("kk");
					String nowHour = format.format(format.parse(subDate, new ParsePosition(0)));
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
					
					
					
					int tmp[] = new int[pos + 1];
					int j=0, startPos=-1;
					/* tmp�迭�� ��ǥ�� x�� ���� */
					for (int tp= 9 ; j < tmp.length ; tp+=2 ,j++ ) {
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
							//dataSet.addValue(sumOfSales[j], category_sumOfSales, "����");
							continue;
						}
						dataSet.addValue(sumOfSales[j], category_sumOfSales, tmp[j]+"��" );
					}
					
					int size = tmp.length - startPos;
					long arrayBiggerThanZero[] = new long[size];
					
					for (int tp =0; startPos < tmp.length; tp++, startPos ++) {
						arrayBiggerThanZero[tp] = sumOfSales[startPos];
					}
					//���� ��� ����
					// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
					//Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
						//	(tmp, arrayBiggerThanZero, tmp[tmp.length-1]+2, arrayBiggerThanZero.length-1 );
						//	(tmp, arrayBiggerThanZero, tmp[tmp.length-1]+2, 1 );
					
					//����  ����
					//long val = appx.getVal();
					//if( val < 0) val =0;
					//dataSet.addValue(val, category_approximationData, tmp[tmp.length-1] +2 +"��");
					
					chart_subTitle1 = "Hour";
					chart_subTitle2 = "�ð��� ������Ȳ";

				} else if (option.compareTo("��") == 0) {

					// ���� ��¥�� �������� 7�� �̳��� �Ǹų����� ���
					/*
					 * LinkedList<HerbOrderTable> res = testDB.selectHerbOrderTable(
					 * "select * from herb_order " +
					 * "where date(order_date) >= date(subdate(now(), INTERVAL 7 DAY)) and date(order_date) <= date(now())"
					 * );
					 */
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
					Date now = format.parse(subDate, new ParsePosition(0));
					long nowTime = now.getTime(); // 1970~ ���� �� ��
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

						sumOfSales[timeIndex] += res2.get(0).getMenu_price();
					}
			
					
					
					int tmp[] = new int[7];
					int j=0, startPos=-1;
					/* tmp�迭�� ��ǥ�� x�� ���� */
					for (int tp= -6 ; j < tmp.length ; tp++,j++ ) {
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
						//System.out.println(j);
						if (tmp[j] == 0) {
							dataSet.addValue(sumOfSales[j], category_sumOfSales, "������");
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
					// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
				//	Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
					//		(tmp, arrayBiggerThanZero, 1, arrayBiggerThanZero.length-1 );
						//	(tmp, arrayBiggerThanZero, 1, 1 );
					//����  ����
					//long val = appx.getVal();
					//if( val < 0) val =0;
					//dataSet.addValue( val, category_approximationData, "1�� ��");
					
					chart_subTitle1 = "Day";
					chart_subTitle2 = "�Ϻ� ������Ȳ";
				} else if (option.compareTo("��") == 0) {
					// ���� ��¥�� �������� �� ��(28)�̳��� �Ǹų����� ���
					
					LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
					ResultSet resultSet = db
							.exec("select * from herb_order "
									+ "where date(order_date) >= date(subdate(" 
									+ date
									+ ", INTERVAL 28 DAY)) and date(order_date) <= date("
								 + date
								 +")");

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
					Date now = format.parse(subDate, new ParsePosition(0));
					long nowDay = now.getTime() / (3600 * 24000); // 1970~ ���� �� ��
					while (i.hasNext()) {
						HerbOrderTable hot = (HerbOrderTable) i.next();
						long orderDay = hot.getOrder_date().getTime()
								/ (3600 * 24000);

						
						if (nowDay - orderDay > 21) { // 6�� ��
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

						sumOfSales[timeIndex] += res2.get(0).getMenu_price();
					}

					
					int tmp[] = new int[4];
					int j=0, startPos=-1;
					/* tmp�迭�� ��ǥ�� x�� ���� */
					for (int tp= -3 ; j < tmp.length ; tp++,j++ ) {
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
					// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
					//Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
							//(tmp, arrayBiggerThanZero, 1, arrayBiggerThanZero.length-1 );
						//	(tmp, arrayBiggerThanZero, 1, 1 );
					//����  ����
					//long val = appx.getVal();
					//if( val < 0) val =0;
					//dataSet.addValue( val, category_approximationData
						//	, "1���� ��");
					
					chart_subTitle1 = "Week";
					chart_subTitle2 = "�ֺ� ������Ȳ";
				} else if (option.compareTo("��") == 0) {
					// ���� ��¥�� �������� �� �� �̳��� ������ ���

					/*
					 * LinkedList<HerbOrderTable> res = testDB.selectHerbOrderTable(
					 * "select * from herb_order " +
					 * "where date(order_date) >= date(subdate(now(), INTERVAL 1 YEAR)) and date(order_date) <= date(now())"
					 * );
					 */
					LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
					ResultSet resultSet = db
							.exec("select * from herb_order "
									+ "where date(order_date) >= date(subdate("
									+ date
									+", INTERVAL 1 YEAR)) and date(order_date) <= date("
									+ date
									+")");

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
					Date now = format.parse(subDate, new ParsePosition(0));
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

						sumOfSales[timeIndex] += res2.get(0).getMenu_price();
					}
					
					int tmp[] = new int[12];
					int j=0, startPos=-1;
					/* tmp�迭�� ��ǥ�� x�� ���� */
					for (int tp= -11 ; j < tmp.length ; tp++,j++ ) {
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
							dataSet.addValue(sumOfSales[j], category_sumOfSales, "���ؿ�(12��)");
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
					// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
					//Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
							//(tmp, arrayBiggerThanZero, 1, arrayBiggerThanZero.length-1 );
						//	(tmp, arrayBiggerThanZero, 1, 1 );
					//����  ����
					//long val = appx.getVal();
					//if( val < 0) val =0;
					//dataSet.addValue( val, category_approximationData, "1���� ��");
					
					
					chart_subTitle1 = "Month";
					chart_subTitle2 = "���� ������Ȳ";
				} else if (option.compareTo("�ϳ�") == 0) {
					// ���� ��¥�� �������� 5 �� �̳��� ������ ���
					/*
					 * LinkedList<HerbOrderTable> res = testDB.selectHerbOrderTable(
					 * "select * from herb_order " +
					 * "where date(order_date) >= date(subdate(now(), INTERVAL 5 YEAR)) and date(order_date) <= date(now())"
					 * );
					 */
					LinkedList<HerbOrderTable> res = new LinkedList<HerbOrderTable>();
					ResultSet resultSet = db
							.exec("select * from herb_order "
									+ "where date(order_date) >= date(subdate("
									+ date
									+", INTERVAL 5 YEAR)) and date(order_date) <= date(" 
									+ date
									+")");

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
					Date now = format.parse(subDate, new ParsePosition(0));
					long nowDay = now.getTime() / (3600 * 24000); // 1970~ ���� �� ��
					while (i.hasNext()) {
						HerbOrderTable hot = (HerbOrderTable) i.next();
						long orderDay = hot.getOrder_date().getTime()
								/ (3600 * 24000);

					
						if (nowDay - orderDay > 365 * 4) { // 6�� ��
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

						sumOfSales[timeIndex] += res2.get(0).getMenu_price();
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
					// ������ ���� n : 1 ���� 4���� ( x�� ������ 5���� ���)
				//	Nth_InterpolatingPolynomial appx = new Nth_InterpolatingPolynomial
							//(tmp, arrayBiggerThanZero, 1, arrayBiggerThanZero.length-1 );
						//	(tmp, arrayBiggerThanZero, 1, 1 );
					//����  ����
					//long val = appx.getVal();
					//if( val < 0) val =0;
					//dataSet.addValue(val, category_approximationData, "1�� ��");
					
					
					//���� ��� ��
					
					chart_subTitle1 = "Year";
					chart_subTitle2 = "�⵵�� ������Ȳ";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	  }
	  public ChartPanel getPieChart_HistogramChart(){
	   ChartPanel chartPanel_PieChart = null;
	   PieDataset pieDataSet = new CategoryToPieDataset(dataSet, TableOrder.BY_ROW, 0);
	   chart = ChartFactory.createPieChart("�Ǹ� ��Ȳ", pieDataSet, true, true, false);
	   //subTitle = new TextTitle("���� ������Ȳ");
	   
	   
	   
	   chartPanel_PieChart = new ChartPanel(chart);
	   
	   PiePlot plot = (PiePlot) chartPanel_PieChart.getChart().getPlot();
		Font labelFont = null;
		labelFont = chart.getTitle().getFont();
		
		labelFont = chart.getTitle().getFont();
		chart.getTitle().setFont(new Font("����", labelFont.getStyle(), labelFont.getSize()));
		
		//���̺� ����

		labelFont = plot.getLabelFont();

		plot.setLabelFont(new Font("����", labelFont.getStyle(), labelFont.getSize()));

		 
		//����        

		chart.getLegend().setItemFont(new Font("����", Font.PLAIN, 10));
	   return chartPanel_PieChart;
	  }
}