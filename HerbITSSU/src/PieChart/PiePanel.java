package PieChart;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Database.DBGenerator;


public class PiePanel implements ActionListener
{
	JButton btn[] = new JButton[5];
	JButton btn_2nd = new JButton();
	
	String indexStringOfUnits[] = {"�ð�", "��", "��", "��", "�ϳ�"};
	JPanel targetPanel;
	JLabel label1;
	int unitIndex;
	DBGenerator db;
	LinkedList<String> yearList, monthList, dayList; 
	JPanel datePanel = new JPanel(new GridLayout(1, 3, 25, 25));
	//JPanel selectPanel = new JPanel(new GridLayout(4, 1, 0, 0));
	JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 25, 25));
	JComboBox yearBox, monthBox, dayBox;
	Font bigFont = new Font("����", Font.PLAIN, 25);
	public PiePanel(String title, String label, JPanel leftPanel, DBGenerator db){
		//super(title);
		this.targetPanel = new JPanel();
		leftPanel.removeAll();
		leftPanel.setVisible(false);
		leftPanel.add(this.targetPanel);
		leftPanel.setVisible(true);
		
		targetPanel.setLayout(new GridLayout(4,1, 20, 20));
		//this.setSize(400,300);
		
		//â�ݱ� ��ư Ŭ���� ������ ����
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//��ư����
		btn[0] = new JButton("�ð� ����");
		btn[0].setFont(bigFont);
		btn[0].addActionListener(this);
		btn[1] = new JButton("�� ����");
		btn[1].setFont(bigFont);
		btn[1].addActionListener(this);
		btn[2] = new JButton("�� ����");
		btn[2].setFont(bigFont);
		btn[2].addActionListener(this);
		btn[3] = new JButton("�� ����");
		btn[3].setFont(bigFont);
		btn[3].addActionListener(this);
		btn[4] = new JButton("�ϳ� ����");
		btn[4].setFont(bigFont);
		btn[4].addActionListener(this);
		btn_2nd = new JButton("Ȯ��");
		btn_2nd.addActionListener(this);
		btn_2nd.setFont(bigFont);
		
		//�����ӿ� �߰�
		label += "\r\n";
		label1 = new JLabel(label, JLabel.CENTER);
		//label2 = new JLabel();
		//this.add(new JLabel(label,JLabel.CENTER));
		//this.add(new JLabel());
		targetPanel.removeAll();
		
		label1.setFont(bigFont);
		
		targetPanel.add(label1);
		//targetPanel.add(label2);
		
		JPanel bottom1P = new JPanel();
		bottom1P.setLayout(new GridLayout(1, 2, 25, 25));
		JPanel bottom2P = new JPanel();
		bottom2P.setLayout(new GridLayout(1, 2, 25, 25));
		JPanel bottom3P = new JPanel();
		bottom3P.setLayout(new GridLayout(1, 2, 25, 25));
		bottom1P.add(btn[0]);
		bottom1P.add(btn[1]);
		bottom2P.add(btn[2]);
		bottom2P.add(btn[3]);
		bottom3P.add(btn[4]);
		bottom3P.add(new JLabel());
		targetPanel.add(bottom1P);
		targetPanel.add(bottom2P);
		targetPanel.add(bottom3P);
		this.db = db;
		
		yearList = new LinkedList<String>();
		monthList = new LinkedList<String>();
		dayList= new LinkedList<String>();
		// �⵵ �޺��ڽ�
		int i;
		for( i=2000 ; i<2051; i++) {
			yearList.add(i+"");
		}
		// �� �޺��ڽ�
		for( i=1; i<10; i++) {
			monthList.add("0"+i);
			dayList.add("0"+i);
		}
		while( i<13 ) {
			monthList.add(i+"");
			i++;
		}
		//�� �޺��ڽ�
		for( i=10; i<32; i++) {
			dayList.add(i+"");
		}
		
		yearBox = new JComboBox( yearList.toArray());
		monthBox = new JComboBox( monthList.toArray());
		dayBox = new JComboBox( dayList.toArray());
		
		SimpleDateFormat year = new SimpleDateFormat("yyyy");
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat day = new SimpleDateFormat("dd");
		String selYear = year.format(new Date());
		String selMonth = month.format(new Date());
		String selDay = day.format(new Date());
		
		yearBox.setSelectedItem(selYear);
		monthBox.setSelectedItem(selMonth);
		dayBox.setSelectedItem(selDay);
		
		yearBox.setFont(bigFont);
		monthBox.setFont(bigFont);
		dayBox.setFont(bigFont);
		datePanel.add( yearBox );
		datePanel.add( monthBox );
		datePanel.add( dayBox );
		//datePanel.add( new JLabel() );
		JLabel chooseDay = new JLabel("����� �������� �����ϼ���", JLabel.CENTER);
		chooseDay.setFont(bigFont);
		bottomPanel.add(chooseDay);
				
		bottomPanel.add(datePanel);
		bottomPanel.add( btn_2nd );
		
		//â���̱�
		targetPanel.setVisible(false);
		targetPanel.setVisible(true);
	}

	//�������̵�
	//������ ���ϰ� Ȯ���� ������ ������ �Ѱܼ� ��Ʈ ����
	//���� ���� �Ŀ��� ����
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		
		if ((source != btn[0])
		&& (source != btn[1])
		&& (source != btn[2])
		&& (source != btn[3])
		&& (source != btn[4]))
		{
			
			PieChart pieC = new PieChart(db);
			//������ ������ ���
			
			pieC.setTime((String)yearBox.getSelectedItem(),
					(String)monthBox.getSelectedItem(), 
					(String)dayBox.getSelectedItem());
			if (unitIndex > 2) {
				//��, �ϳ� ����
				pieC.setTime(
						(String)yearBox.getSelectedItem(), "12", "31");
				//System.out.println(yearBox.getSelectedItem());
			}
			
			pieC.setData( indexStringOfUnits[ unitIndex ] );
			
			targetPanel.removeAll();
			targetPanel.setLayout(new GridLayout(1, 1, 25, 25));
			targetPanel.add(pieC.getPieChart_HistogramChart());
			
			targetPanel.setVisible(false);
			targetPanel.setVisible(true);
			//this.dispose();			//����â�� �ݱ�
			return;
		
		}
		
		//���� ���ý�
		int btnIndex = 0;
		for( int i=0; i<btn.length; i++, btnIndex++ ) {
			if (btnIndex ==3) {
			datePanel.remove(monthBox);
			datePanel.remove(dayBox);
			
			}
			if (source == btn[btnIndex])
				break;
		}
		
		targetPanel.removeAll();
		targetPanel.setVisible(false);
		targetPanel.setLayout(new GridLayout(1, 1, 25, 25));
		targetPanel.add(bottomPanel);
		targetPanel.setVisible(true);
		unitIndex = btnIndex;		//������ ����
		
	}
}