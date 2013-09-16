package PieChart;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PiePopupFrame extends JFrame implements ActionListener
{
	public PiePopupFrame(String title, String label){
		super(title);
		this.setLayout(new BorderLayout());
		this.setSize(400,300);
		
		//â�ݱ� ��ư Ŭ���� ������ ����
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//��ư����
		JButton btn = new JButton("Ȯ��");
		btn.addActionListener(this);

		//�����ӿ� �߰�
		this.add("Center",new JLabel(label,JLabel.CENTER));
		this.add("South",btn);

		//â���̱�
		this.setVisible(true);
	}

	//�������̵�
	public void actionPerformed(ActionEvent ae){
		
		this.setVisible(false);
		this.dispose();
	}
}