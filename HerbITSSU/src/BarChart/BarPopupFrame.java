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
		
		//프레임에 추가
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
		//창보이기
		this.setVisible(true);
	}

	//오버라이딩
	public void actionPerformed(ActionEvent ae){
		HistogramChart barC = new HistogramChart(db);
		if( ae.getSource() == btn[0] )			//시간 단위. 추후 선택 가능하게 수정 필요, 현재는 당일 검색
		{
			barC.setData("시간");
		}
		else if( ae.getSource() == btn[1] )			//일 단위
		{
			barC.setData("일");
		}
		else if( ae.getSource() == btn[2] )			//주 단위
		{
			barC.setData("주");
		}
		else if( ae.getSource() == btn[3] )			//월 단위
		{
			barC.setData("월");
		}
		else if( ae.getSource() == btn[4] )			//일년 단위
		{
			barC.setData("일년");
		}
		targetPanel.removeAll();
		targetPanel.add(barC.getHistogramChart_HistogramChart());
		
		targetPanel.setVisible(false);
		targetPanel.setVisible(true);
	}
}