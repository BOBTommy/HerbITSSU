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
		
		//창닫기 버튼 클릭시 프레임 종료
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//버튼생성
		JButton btn = new JButton("확인");
		btn.addActionListener(this);

		//프레임에 추가
		this.add("Center",new JLabel(label,JLabel.CENTER));
		this.add("South",btn);

		//창보이기
		this.setVisible(true);
	}

	//오버라이딩
	public void actionPerformed(ActionEvent ae){
		
		this.setVisible(false);
		this.dispose();
	}
}