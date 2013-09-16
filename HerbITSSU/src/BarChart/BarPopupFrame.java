package BarChart;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Database.DBGenerator;


public class BarPopupFrame extends JFrame implements ActionListener
{
	JButton btn[] = new JButton[5];
	JPanel targetPanel;
	DBGenerator db;
	public BarPopupFrame(String title, String label, JPanel targetPanel, DBGenerator db){
		super(title);
		this.setLayout(new GridLayout(4,2));
		this.setSize(400,300);
		
		//â�ݱ� ��ư Ŭ���� ������ ����
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//��ư����
		btn[0] = new JButton("�ð� ����");
		btn[0].addActionListener(this);
		btn[1] = new JButton("�� ����");
		btn[1].addActionListener(this);
		btn[2] = new JButton("�� ����");
		btn[2].addActionListener(this);
		btn[3] = new JButton("�� ����");
		btn[3].addActionListener(this);
		btn[4] = new JButton("�ϳ� ����");
		btn[4].addActionListener(this);
		
		//�����ӿ� �߰�
		label += "\r\n";
		this.add(new JLabel(label,JLabel.CENTER));
		this.add(new JLabel());
		this.add(btn[0]);
		this.add(btn[1]);
		this.add(btn[2]);
		this.add(btn[3]);
		this.add(btn[4]);
		
		this.targetPanel = targetPanel;
		this.db = db;
		//â���̱�
		this.setVisible(true);
	}

	//�������̵�
	public void actionPerformed(ActionEvent ae){
		HistogramChart barC = new HistogramChart(db);
		if( ae.getSource() == btn[0] )			//�ð� ����. ���� ���� �����ϰ� ���� �ʿ�, ����� ���� �˻�
		{
			barC.setData("�ð�");
		}
		else if( ae.getSource() == btn[1] )			//�� ����
		{
			barC.setData("��");
		}
		else if( ae.getSource() == btn[2] )			//�� ����
		{
			barC.setData("��");
		}
		else if( ae.getSource() == btn[3] )			//�� ����
		{
			barC.setData("��");
		}
		else if( ae.getSource() == btn[4] )			//�ϳ� ����
		{
			barC.setData("�ϳ�");
		}
		targetPanel.removeAll();
		targetPanel.add(barC.getHistogramChart_HistogramChart());
		
		targetPanel.setVisible(false);
		targetPanel.setVisible(true);
	}
}