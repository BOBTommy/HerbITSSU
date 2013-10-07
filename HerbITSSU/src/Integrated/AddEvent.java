package Integrated;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddEvent {
	private JPanel leftpanel;
	final OrderSystem os;
	private JButton add;
	private int num;
	java.sql.ResultSet rs;
	private JTextField name;
	private JTextField price;
	private JComboBox<String> category;
	private JLabel nameL;
	private JLabel priceL;
	private String nameS;
	private String priceS;
	private String [] categoryS={"바디/헤어", "컵", "티슈/여성", "팩/베겟속", "베이비", "화장품", "아로마/오일/램프", "방향제/향수", "향초/홀더", "비누/쿠키","직원용", "커피", "아이스커피", "음료", "과일쥬스", "빙수/와플/캔디","티포트", "테이크아웃", "워머/핸드밀", "원두/허브차"};
	private String categoryS2;

	
	public AddEvent(final OrderSystem os,final JPanel pan) {
		this.os=os;
		leftpanel=pan;
		leftpanel.setLayout(new FlowLayout());
		leftpanel.removeAll();
		rs = os.db.exec("select * from herb_menu;");
		num=0;
		try {
			while(rs.next()){	
				num++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		num++;
		add = new JButton("등록");
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				nameS=name.getText();
				priceS=price.getText();
				String s= "'" + Integer.toString(num)+"','"+nameS+"','"+priceS + "'";
				os.db.exec("insert herb_menu("
						+ " menu_id," 
						+ " menu_name,"
						+ " menu_price,"
						+ " menu_reg_date,"
						+ " menu_category"
						+ ") "
						+ "values("
						+  s +","
						+ " now(),"
						+ "'"+(String)category.getSelectedItem()+"'"
						+ ")"
					);
				num++;
		
				JOptionPane.showMessageDialog(null, "등록되었습니다");
				leftpanel.removeAll();
				leftpanel.add(nameL);
				leftpanel.add(name);
				name.setText("");
				leftpanel.add(priceL);
				leftpanel.add(price);
				price.setText("");
				leftpanel.add(add);
				leftpanel.add(category);
				
				leftpanel.setVisible(false);
				leftpanel.setVisible(true);
			}
		});
		nameL=new JLabel("메뉴이름");
		name=new JTextField(10);
		priceL=new JLabel("가격");
		price=new JTextField(10);
		category=new JComboBox<String>(categoryS);
		
		leftpanel.removeAll();
		leftpanel.add(nameL);
		leftpanel.add(name);
		leftpanel.add(priceL);
		leftpanel.add(price);
		leftpanel.add(add);
		leftpanel.add(category);
		
		leftpanel.setVisible(false);
		leftpanel.setVisible(true);
		
	}

	
}
