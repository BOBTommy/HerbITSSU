package BarChart;

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


public class BarPanel implements ActionListener
{
	JButton btn[] = new JButton[5];
	JButton btn_2nd[] = new JButton[4];
	
	String indexStringOfUnits[] = {"시간", "일", "주", "월", "일년"};
	JPanel targetPanel;
	JPanel originalPanel;
	JLabel label1;
	int unitIndex;
	DBGenerator db;
	LinkedList<String> yearList, monthList, dayList; 
	JPanel datePanel = new JPanel(new GridLayout(1, 3, 25, 25));
	//JPanel selectPanel = new JPanel(new GridLayout(4, 1, 0, 0));
	JPanel bottomPanel = new JPanel(new GridLayout(6, 1, 25, 25));
	JComboBox yearBox, monthBox, dayBox;
	Font bigFont = new Font("굴림", Font.PLAIN, 25);
	public BarPanel(String title, String label, JPanel leftPanel, DBGenerator db){
		//super(title);
		this.targetPanel = new JPanel();
		leftPanel.setLayout(new FlowLayout());
		leftPanel.removeAll();
		//leftPanel.setLayout(null);
		//leftPanel.setSize(1057, 900);
		leftPanel.setVisible(false);
		
		leftPanel.add(this.targetPanel);
		leftPanel.setVisible(true);
		originalPanel = leftPanel;
		
		targetPanel.setLayout(new GridLayout(4,1, 20, 20));
		//this.setSize(400,300);
		
		//창닫기 버튼 클릭시 프레임 종료
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//버튼생성
		ImageIcon imageIcon;
		btn[0] = new JButton("시간 단위");
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
		imageIcon = new ImageIcon("image/admin/ordertap_whole_round.png");
		btn_2nd[0] = new JButton(imageIcon);
		btn_2nd[0].setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		btn_2nd[0].setFont(bigFont);
		btn_2nd[0].addActionListener(this);
		imageIcon = new ImageIcon("image/admin/ordertap_whole_straight.png");
		btn_2nd[1] = new JButton(imageIcon);
		btn_2nd[1].setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		btn_2nd[1].setFont(bigFont);
		btn_2nd[1].addActionListener(this);
		imageIcon = new ImageIcon("image/admin/ordertap_part_round.png");
		btn_2nd[2] = new JButton(imageIcon);
		btn_2nd[2].setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		btn_2nd[2].setFont(bigFont);
		btn_2nd[2].addActionListener(this);
		imageIcon = new ImageIcon("image/admin/ordertap_part_straght.png");
		btn_2nd[3] = new JButton(imageIcon);
		btn_2nd[3].setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		btn_2nd[3].setFont(bigFont);
		btn_2nd[3].addActionListener(this);
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
		chooseDay.setFont(bigFont);
		bottomPanel.add(chooseDay);
				
		bottomPanel.add(datePanel);
		bottomPanel.add( btn_2nd[0] );
		bottomPanel.add( btn_2nd[1] );
		bottomPanel.add( btn_2nd[2] );
		bottomPanel.add( btn_2nd[3] );
		
		//창보이기
		datePanel.setBackground(new Color(0x898a8c));
		bottom1P.setBackground(new Color(0x898a8c));
		bottom2P.setBackground(new Color(0x898a8c));
		bottom3P.setBackground(new Color(0x898a8c));
		bottomPanel.setBackground(new Color(0x898a8c));
		targetPanel.setBackground(new Color(0x898a8c));
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
			int opt = -1;
			if (source == btn_2nd[0]) opt = 0;		//전체 곡선
			else if(source == btn_2nd[1]) opt = 1;	//전체 직선
			else if(source == btn_2nd[2]) opt = 2;	//일부 곡선
			else if(source == btn_2nd[3]) opt = 3;	//일부 직선
			
			HistogramChart barC = new HistogramChart(db, opt);
			//기준을 정했을 경우
			
			barC.setTime((String)yearBox.getSelectedItem(),
					(String)monthBox.getSelectedItem(), 
					(String)dayBox.getSelectedItem());
			if (unitIndex > 2) {
				//월, 일년 단위
				barC.setTime(
						(String)yearBox.getSelectedItem(), "12", "31");
				//System.out.println(yearBox.getSelectedItem());
			}
			
			barC.setData( indexStringOfUnits[ unitIndex ] );
			
			originalPanel.removeAll();
			//originalPanel.setLayout(null);					//픽셀작업필요한부분
			//originalPanel.setSize(1200, 800);
			originalPanel.setLayout(null);
			originalPanel.setSize(1054, 600);
			originalPanel.add(barC.getHistogramChart_HistogramChart());
			
			
			//originalPanel.add(barC.getHistogramChart_HistogramChart());
			targetPanel.setVisible(false);
			targetPanel.setVisible(true);
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
		targetPanel.setLayout(new GridLayout(1, 1, 25, 25));
		targetPanel.add(bottomPanel);
		targetPanel.setVisible(true);
		unitIndex = btnIndex;		//단위를 저장
		
	}
}