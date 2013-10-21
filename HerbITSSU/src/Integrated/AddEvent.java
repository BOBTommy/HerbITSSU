package Integrated;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private String [] categoryS={"�ٵ�/���", "��", "Ƽ��/����", "��/���ټ�", "���̺�", "ȭ��ǰ", "�Ʒθ�/����/����", "������/���", "����/Ȧ��", "��/��Ű","������", "Ŀ��", "���̽�Ŀ��", "����", "�����꽺", "����/����/ĵ��","Ƽ��Ʈ", "����ũ�ƿ�", "����/�ڵ��", "����/�����"};
	private String categoryS2;

	
	public AddEvent(final OrderSystem os,final JPanel pan) {
		this.os=os;
		leftpanel=pan;
		leftpanel.setLayout(new FlowLayout());
		leftpanel.removeAll();
		rs = os.db.exec("select * from herb_menu;");
		
		add = new JButton("���");
		add.setBackground(new Color(255,255,255));
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				nameS=name.getText();
				priceS=price.getText();
				String s= "'"+nameS+"','"+priceS + "'";
				os.db.exec("insert herb_menu("
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
						
				JOptionPane.showMessageDialog(null, "��ϵǾ����ϴ�");
				
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
		nameL=new JLabel("�޴��̸�");
		name=new JTextField(10);
		priceL=new JLabel("����");
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
