package Integrated;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
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
	private JButton mody=new JButton("����");
	private JButton delet=new JButton("����");
	private JButton search=new JButton("�����ϱ�");
	private int num;
	private int flag=0;
	java.sql.ResultSet rs;
	private JTextField name=new JTextField(10);
	private JTextField price=new JTextField(10);
	private JLabel nameL=new JLabel("�̸�");
	private JTextField name2=new JTextField(10);
	private JLabel priceL=new JLabel("����");
	private JComboBox<String> category;
	private OrderPanel menupane;
	private JPanel modypane;
	private String [] categoryS={"�ٵ�/���", "��", "Ƽ��/����", "��/���ټ�", "���̺�", "ȭ��ǰ", "�Ʒθ�/����/����", "������/���", "����/Ȧ��", "��/��Ű","������", "Ŀ��", "���̽�Ŀ��", "����", "�����꽺", "����/����/ĵ��","Ƽ��Ʈ", "����ũ�ƿ�", "����/�ڵ��", "����/�����"};


	public ModyEvent(final OrderSystem os,final JPanel pan)
	{
		
		this.os=os;
		leftpanel = pan;
		leftpanel.removeAll();
		menupane = new OrderPanel(this.os,1);
		modypane = new JPanel();
		modypane.setLayout(new FlowLayout());
		leftpanel.setLayout(new BorderLayout());
		leftpanel.add("Center",menupane);
		
		category=new JComboBox<String>(categoryS);
		
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {	
				search();
			}
		});
		mody.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {	
				if(name.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "�̸��� �Է����ּ���");
				else
				{
					try {
						os.db.exec("update herb_menu set menu_name="
								+ "'"+name2.getText()+"'" + ", menu_price="
								+ "'"+price.getText()+"'" + ", menu_category="
								+"'"+(String)category.getSelectedItem()+"'"+ "where menu_id="+rs.getInt("menu_id"));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "�����Ǿ����ϴ�");
					flag=0;
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					modypane.removeAll();
					modypane.add(search);		
					modypane.setVisible(false);
					modypane.setVisible(true);
				}
			
			}
		});
		delet.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {	
				if(name.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "�̸��� �Է����ּ���");
				else

				{
					try {
						os.db.exec("delete from herb_menu where menu_id="+rs.getInt("menu_id"));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(null, "�����Ǿ����ϴ�");
				flag=0;
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				modypane.removeAll();
				modypane.add(search);		
				modypane.setVisible(false);
				modypane.setVisible(true);
			}
		});
		
		modypane.add(search);
		leftpanel.add("South",modypane);
		leftpanel.setVisible(false);
		leftpanel.setVisible(true);
		
	}
	
	private void search()
	{
		num=0;
		String n=menupane.returnName();
		System.out.println(n);
		if(n==null)
		{	
			flag=0;
			JOptionPane.showMessageDialog(null, "�޴��� ������ �ּ���");
		}
		else
		{
			name.setText(n);
			rs = os.db.exec("select * from herb_menu where menu_name = '"
					+ n + "'");
			try {
				rs.next();
				if(rs.wasNull())
				{
					flag=0;
					JOptionPane.showMessageDialog(null, "�޴��� ������ �ּ���");
				}
				else {
					flag=1;
				}
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		n=null;
		if(flag==1)
			next_step();
	}
	private int next_step()
	{
		modypane.removeAll();
		if(flag==1)
		{
			modypane.add(nameL);
			try {
				name2.setText((rs.getString("menu_name")));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modypane.add(name2);
			try {
				price.setText(rs.getString("menu_price"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modypane.add(price);
			try {
				category.setSelectedItem((String)rs.getString("menu_category"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modypane.add(category);
			
			
			modypane.add(mody);
		
			
			modypane.add(delet);
			modypane.setVisible(false);
			modypane.setVisible(true);
		}
		else 
			return 1;
		return 0;
	}
}
