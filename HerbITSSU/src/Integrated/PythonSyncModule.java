package Integrated;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.mysql.jdbc.Statement;

import JythonObjectFactory.JythonDriver;

public class PythonSyncModule extends JFrame{

	private OrderSystem os;
	
	private JPanel baseContainer; // 컨테이너
	private JProgressBar progBar; // 진행바
	private JTextArea textArea;
	private JScrollPane scPane;
	
	private String textStr;
	
	public PythonSyncModule(OrderSystem os) {
		super("Apriori prediction 모듈 동기화 중입니다.");
		this.os = os;
		this.textStr="";
		
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
		JythonDriver driver = new JythonDriver("Apriori");
		textStr += "Result of Apriori Algorithm\n";
		textStr += driver.getAprioriString();
		textArea.setText(textStr);
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
				textStr += "Order ID : " + rs.getInt(1);
				textStr += " Menu : " + MenuList.herbMenu.get(rs.getInt(2));
				textStr += "\n";
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
}
