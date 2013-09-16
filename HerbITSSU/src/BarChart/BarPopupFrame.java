package BarChart;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Database.DBGenerator;


@SuppressWarnings("serial")
public class BarPopupFrame extends JFrame implements ActionListener
{
	JButton btn[] = new JButton[5];
	JButton btn_2nd = new JButton("확인");
	String indexStringOfUnits[] = {"시간", "일", "주", "월", "일년"};
	JPanel targetPanel;
	JLabel label1, label2;
	int unitIndex;
	DBGenerator db;
	LinkedList<String> yearList, monthList, dayList; 
	JPanel datePanel = new JPanel(new GridLayout(1, 4, 25, 25));
	@SuppressWarnings("rawtypes")
	JComboBox yearBox, monthBox, dayBox;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BarPopupFrame(String title, String label, JPanel targetPanel, DBGenerator db){
		super(title);
		this.setLayout(new GridLayout(4,2));
		this.setSize(400,300);
		
		//창닫기 버튼 클릭시 프레임 종료
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//버튼생성
		btn[0] = new JButton("시간 단위");
		btn[0].addActionListener(this);
		btn[1] = new JButton("일 단위");
		btn[1].addActionListener(this);
		btn[2] = new JButton("주 단위");
		btn[2].addActionListener(this);
		btn[3] = new JButton("월 단위");
		btn[3].addActionListener(this);
		btn[4] = new JButton("일년 단위");
		btn[4].addActionListener(this);
		btn_2nd.addActionListener(this);
		//프레임에 추가
		label += "\r\n";
		label1 = new JLabel(label, JLabel.CENTER);
		label2 = new JLabel();
		//this.add(new JLabel(label,JLabel.CENTER));
		//this.add(new JLabel());
		this.add(label1);
		this.add(label2);
		this.add(btn[0]);
		this.add(btn[1]);
		this.add(btn[2]);
		this.add(btn[3]);
		this.add(btn[4]);
		
		this.targetPanel = targetPanel;
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
		
		datePanel.add( yearBox );
		datePanel.add( monthBox );
		datePanel.add( dayBox );
		datePanel.add( btn_2nd );
		//창보이기
		this.setVisible(true);
	}

	//오버라이딩
	//기준을 정하고 확인을 누르면 기준을 넘겨서 차트 생성
	//단위 선택 후에는 리턴
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		
		if( source == btn_2nd) 
		{
			HistogramChart barC = new HistogramChart(db);
			//기준을 정했을 경우
			if (unitIndex > 2) {
				//월, 일년 단위
				barC.setTime(
						(String)yearBox.getSelectedItem(), "1", "1");
				//System.out.println(yearBox.getSelectedItem());
			}
			barC.setTime((String)yearBox.getSelectedItem(),
					(String)monthBox.getSelectedItem(), 
					(String)dayBox.getSelectedItem());
			
			
			barC.setData( indexStringOfUnits[ unitIndex ] );
			
			targetPanel.removeAll();
			targetPanel.add(barC.getHistogramChart_HistogramChart());
			
			targetPanel.setVisible(false);
			targetPanel.setVisible(true);
			this.dispose();			//선택창을 닫기
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
		
		
		this.remove(btn[0]);
		this.remove(btn[1]);
		this.remove(btn[2]);
		this.remove(btn[3]);
		this.remove(btn[4]);
		this.remove(label1);
		this.remove(label2);
		
		this.setSize(400, 150);
		this.setTitle("기준일 선택");
		this.setLayout(new GridLayout(2, 1));
		this.setVisible(false);
		this.add( new JLabel("출력할 기준일을 선택하세요", JLabel.CENTER));
		this.add(datePanel);
		this.setVisible(true);
		unitIndex = btnIndex;		//단위를 저장
		
	}
}