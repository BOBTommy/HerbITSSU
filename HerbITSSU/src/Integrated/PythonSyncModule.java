package Integrated;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Database.Order;
import JythonObjectFactory.JythonDriver;

public class PythonSyncModule extends JFrame{

	private OrderSystem os;
	
	public static String recommandString = "추천 메뉴를 등록하기에는 누적된 주문량이 부족합니다.";
	
	private JPanel baseContainer; // 컨테이너
	private JProgressBar progBar; // 진행바
	private JTextArea textArea;
	private JScrollPane scPane;
	
	private ArrayList<Order> orderList;
	
	private String textStr;
	private boolean flag;
	
	public PythonSyncModule(OrderSystem os) {
		super("Apriori prediction 모듈 동기화 중입니다.");
		this.os = os;
		this.textStr="";
		this.orderList = new ArrayList<Order>();
		
		baseContainer = new JPanel(new BorderLayout());
		this.textArea = new JTextArea();
		scPane = new JScrollPane(this.textArea);
		
		this.setSize(450,200);
		this.updateTextarea();
		this.add(baseContainer);
		baseContainer.add(scPane,BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	public void updateTextarea(){
		this.getFromDB();
		
		flag = false;
		
		flag = this.makeList();
		if( flag ){
			while(true){
				if(new File("C:\\herb.dat").exists())
					break;
			}
			JythonDriver driver = new JythonDriver("Apriori");
			textStr += driver.getAprioriString();
		}
		textStr += "\n동기화가 완료되었습니다. 창을 닫아주세요.";
		textArea.setText(textStr);
		if(flag)
			getData();
	}
	
	public void getData(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("C:\\herb.dat")));
			StringTokenizer token;
			String line = br.readLine();
			while( line != null ){
				token = new StringTokenizer(line, "|");
				String item1 = token.nextToken();
				String item2 = token.nextToken();
				MenuList.addRecommandItem(item1, item2);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	public void getFromDB(){
		ResultSet menuListQuery = this.os.db.exec("SELECT * FROM herb_menu");
		
		try{//herb_menu table 에서 메뉴 내용을 얻어옴
			while(menuListQuery.next()){
				MenuList.herbMenu.put(
						new Integer(menuListQuery.getInt(1)), // menu_id
						menuListQuery.getString(2)); //menu_name
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		
		ResultSet rs = this.os.db.exec("SELECT * FROM herb_order");
		textStr += "Result of query in herb DB\n";
		try{
			//Get All of Order
			while(rs.next()){
				int orderID = rs.getInt(1);
				int menu = rs.getInt(2);
				boolean flag = false; // Flag true = 동일한 주문 ID가 있음,해당 주문에 추가 false = 새 주문
				
				textStr += "Order ID : " + rs.getInt(1);
				for(int i=0; i<this.orderList.size(); i++)
					if(this.orderList.get(i).getOrderID() == orderID){
						flag = true;
						this.orderList.get(i).addOrder(menu);
						break;
					}
				if( !flag ){
					this.orderList.add(new Order(orderID));
					this.orderList.get(this.orderList.size()-1).addOrder(menu);
				}
				
				textStr += " Menu : " + MenuList.herbMenu.get(rs.getInt(2));
				textStr += "\n";
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		for(int i=0; i<this.orderList.size(); i++){
			System.out.println("Order ID : " + this.orderList.get(i).getOrderID() + " " + this.orderList.get(i).toString());
		}
	}
	
	public boolean makeList(){
		//거래량이 3000건 이하면 별로 가치가 없음
		if(this.orderList.size() < 3000)
			return false;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new  File("C:\\apriori.dat")));
			for(int i=0; i<this.orderList.size(); i++){
				if(this.orderList.get(i).getOrderCount() < 2)
					continue;
				writer.write(this.orderList.get(i).toString());
				writer.newLine();
			}
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
}
