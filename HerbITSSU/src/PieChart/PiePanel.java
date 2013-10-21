package PieChart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Database.DBGenerator;
import Integrated.ImagePanel;


public class PiePanel implements ActionListener
{
	JButton btn[] = new JButton[5];
	JButton btn_2nd = new JButton();
	
	String indexStringOfUnits[] = {"시간", "일", "주", "월", "일년"};
	JPanel targetPanel;
	JLabel label1;
	int unitIndex;
	DBGenerator db;
	LinkedList<String> yearList, monthList, dayList; 
	JPanel datePanel = new JPanel(new GridLayout(1, 3, 25, 25));
	//JPanel selectPanel = new JPanel(new GridLayout(4, 1, 0, 0));
	JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 25, 25));
	JPanel bottom_2nd_Panel = new JPanel(new GridLayout(1, 3, 30, 30));
	JPanel originalPanel;
	JComboBox yearBox, monthBox, dayBox;
	Font bigFont = new Font("굴림", Font.PLAIN, 25);
	public PiePanel(String title, String label, JPanel leftPanel, DBGenerator db){
		//super(title);
		this.targetPanel = new ImagePanel("image/admin/background_2.png");
		this.originalPanel = leftPanel;
		leftPanel.removeAll();
		leftPanel.setVisible(false);
		leftPanel.setLayout(null);
		leftPanel.add(this.targetPanel);
		
		
		leftPanel.setVisible(true);
		
		targetPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 200, 50));
		//this.setSize(400,300);
		
		//창닫기 버튼 클릭시 프레임 종료
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//버튼생성
		ImageIcon imageIcon;
		imageIcon = new ImageIcon("image/admin/hour_hour.png");
		btn[0] = new JButton(imageIcon);
		btn[0].setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		btn[0].setFont(bigFont);
		btn[0].addActionListener(this);
		imageIcon = new ImageIcon("image/admin/day_day.png");
		btn[1] = new JButton(imageIcon);
		btn[1].setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		btn[1].setFont(bigFont);
		btn[1].addActionListener(this);
		imageIcon = new ImageIcon("image/admin/week_week.png");
		btn[2] = new JButton(imageIcon);
		btn[2].setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		btn[2].setFont(bigFont);
		btn[2].addActionListener(this);
		imageIcon = new ImageIcon("image/admin/month_month.png");
		btn[3] = new JButton(imageIcon);
		btn[3].setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		btn[3].setFont(bigFont);
		btn[3].addActionListener(this);
		imageIcon = new ImageIcon("image/admin/year_year.png");
		btn[4] = new JButton(imageIcon);
		btn[4].setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		btn[4].setFont(bigFont);
		btn[4].addActionListener(this);
		btn[0].setBackground(new Color(0x898a8c));
		btn[1].setBackground(new Color(0x898a8c));
		btn[2].setBackground(new Color(0x898a8c));
		btn[3].setBackground(new Color(0x898a8c));
		btn[4].setBackground(new Color(0x898a8c));
		imageIcon = new ImageIcon("image/admin/search.png");
		btn_2nd = new JButton(imageIcon);
		btn_2nd.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		btn_2nd.addActionListener(this);
		btn_2nd.setFont(bigFont);
		
		//프레임에 추가
		imageIcon = new ImageIcon("image/admin/choice_unit.png");
		label1 = new JLabel(imageIcon);
		label1.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
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
		// 년도 콤보박스
		int i;
		for( i=2000 ; i<2051; i++) {
			yearList.add(i+"");
		}
		// 월 콤보박스
		for( i=1; i<10; i++) {
			monthList.add("0"+i);
			dayList.add("0"+i);
		}
		while( i<13 ) {
			monthList.add(i+"");
			i++;
		}
		//일 콤보박스
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
		imageIcon = new ImageIcon("image/admin/choice_day.png");
		JLabel chooseDay = new JLabel(imageIcon);
		chooseDay.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		
		targetPanel.setBounds(300, 100, targetPanel.getWidth(), targetPanel.getHeight());
		bottomPanel.add(chooseDay);
				
		bottomPanel.add(datePanel);
		bottom_2nd_Panel.add(new JLabel(""));
		btn_2nd.setBackground(new Color(0x898a8c));
		bottom_2nd_Panel.add(btn_2nd);
		bottom_2nd_Panel.add(new JLabel(""));
		bottom_2nd_Panel.setBackground(new Color(0x898a8c));
		bottomPanel.add( bottom_2nd_Panel );
		
		//창보이기
		datePanel.setBackground(new Color(0x898a8c));
		bottom1P.setBackground(new Color(0x898a8c));
		bottom2P.setBackground(new Color(0x898a8c));
		bottom3P.setBackground(new Color(0x898a8c));
		bottomPanel.setBackground(new Color(0x898a8c));
		
		targetPanel.setVisible(false);
		targetPanel.setVisible(true);
	}

	//오버라이딩
	//기준을 정하고 확인을 누르면 기준을 넘겨서 차트 생성
	//단위 선택 후에는 리턴
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
			//기준을 정했을 경우
			
			pieC.setTime((String)yearBox.getSelectedItem(),
					(String)monthBox.getSelectedItem(), 
					(String)dayBox.getSelectedItem());
			if (unitIndex > 2) {
				//월, 일년 단위
				pieC.setTime(
						(String)yearBox.getSelectedItem(), "12", "31");
				//System.out.println(yearBox.getSelectedItem());
			}
			
			pieC.setData( indexStringOfUnits[ unitIndex ] );
			
			originalPanel.removeAll();
			originalPanel.setLayout(null);
			originalPanel.setSize(1054, 900);
			originalPanel.add(pieC.getPieChart_HistogramChart());
			originalPanel.setBackground(new Color(255, 255, 255));
			originalPanel.repaint();
			//targetPanel.setVisible(false);
			//targetPanel.setVisible(true);
			//this.dispose();			//선택창을 닫기
			return;
		
		}
		
		//단위 선택시
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
		//targetPanel.setLayout(new GridLayout(1, 1, 25, 25));
		targetPanel.add(bottomPanel);
		targetPanel.setVisible(true);
		unitIndex = btnIndex;		//단위를 저장
		
	}
}