package Integrated;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ModyEvent {
	private JPanel leftpanel;
	final OrderSystem os;
	private JButton mody=new JButton("수정");
	private JButton delet=new JButton("삭제");
	private JButton search=new JButton("찾기");
	private int num;
	private int flag=0;
	java.sql.ResultSet rs;
	private JTextField name=new JTextField(10);
	private JTextField price=new JTextField(10);
	private JLabel nameL=new JLabel("이름");
	private JTextField name2=new JTextField(10);
	private JLabel priceL=new JLabel("가격");
	private JTextField category = new JTextField(10);
	
	public ModyEvent(final OrderSystem os,final JPanel pan)
	{
		this.os=os;
		leftpanel=pan;
		rs = os.db.exec("select * from herb_menu;");
		
		leftpanel.removeAll();
		
		
		
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {	
				if(name.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "이름을 입력해주세요");
				else
					search();
			
			}
		});
		
		leftpanel.removeAll();
		leftpanel.add(nameL);
		leftpanel.add(name);
		leftpanel.add(search);		
		leftpanel.setVisible(false);
		leftpanel.setVisible(true);
	}
	
	private void search()
	{
		num=0;
		String n=name.getText();
		try {
			rs.next();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while(true)
			{
				System.out.println(rs.getString("menu_name"));
				if(rs.getString("menu_name").equals(n))
				{
					num=rs.getInt("menu_id");
					flag=1;
					break;
				}
				else if(rs.next())
					;
				else
				{
					JOptionPane.showMessageDialog(null, "해당이름의 메뉴가 없습니다");
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(flag==1)
			next_step();
	}
	private void next_step()
	{
		leftpanel.removeAll();
		leftpanel.add(nameL);
		leftpanel.add(name);
		leftpanel.add(search);
		if(flag==1)
		{
			leftpanel.add(nameL);
			try {
				name2.setText((rs.getString("menu_name")));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			leftpanel.add(name2);
			try {
				price.setText(rs.getString("menu_price"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			leftpanel.add(price);
			try {
				category.setText(rs.getString("menu_category"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			leftpanel.add(category);
			
			mody.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {	
					if(name.getText().isEmpty())
						JOptionPane.showMessageDialog(null, "이름을 입력해주세요");
					else
					{
						try {
							os.db.exec("update herb_menu set menu_name="
									+ "'"+name2.getText()+"'" + ", menu_price="
									+ "'"+price.getText()+"'" + ", menu_category="
									+"'"+category.getText()+"'"+ "where menu_id="+rs.getInt("menu_id"));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						JOptionPane.showMessageDialog(null, "수정되었습니다");
						leftpanel.removeAll();
						leftpanel.add(nameL);
						name.setText("");
						leftpanel.add(name);
						leftpanel.add(search);		
						leftpanel.setVisible(false);
						leftpanel.setVisible(true);
					}
				
				}
			});
			leftpanel.add(mody);
		
			delet.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {	
					if(name.getText().isEmpty())
						JOptionPane.showMessageDialog(null, "이름을 입력해주세요");
					else

					{
						try {
							os.db.exec("delete from herb_menu where menu_id="+rs.getInt("menu_id"));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(null, "삭제되었습니다");
					leftpanel.removeAll();
					leftpanel.add(nameL);
					name.setText("");
					leftpanel.add(name);
					leftpanel.add(search);		
					leftpanel.setVisible(false);
					leftpanel.setVisible(true);
				}
			});
			leftpanel.add(delet);
			leftpanel.setVisible(false);
			leftpanel.setVisible(true);
		}
	}
}
