package Integrated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import AnimationComponent.AniButton;

public class PayPane extends JPanel{
	
	/*
	 * �����δ� OrderPanel���� �޾ƿ� cancel ��ư�� �ڷΰ��� ����.
	 * �ڷΰ��� Flow : cancel Button Event
	 * ���� ��� Flow : resetTotal in orderPanel -> cancel Button Event
	 */
	
	public static boolean payWorking = false;
	
	private OrderPanel parent;
	private JLabel label = new JLabel();
	private Border blackline;
	private int total;						//�ֹ� �Ѿ�
	
	private JPanel centerPanel;
	private JPanel bottomPanel;
	
	private JLabel dateLabel = new JLabel();	//���� �Ͻ�
	private JLabel totalLabel = new JLabel();	//�� ���� �ݾ�
	private JLabel recommandLabel = new JLabel();	//��õ �޴�
	private JLabel ruleLabel = new JLabel();	//�Ǹ� ����
	
	private JButton cardBtn;
	private JButton cashBtn;
	private JButton backBtn;
	private AniButton cancelBtn;
	
	public PayPane(String text, OrderPanel orderPanel){
		
		this.parent = orderPanel;
		Color FG_COLOR = new Color(0x3B5998);
		label.setForeground(FG_COLOR);
		label.setFont(new Font("Sans", Font.BOLD, 30));
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		blackline = BorderFactory.createLineBorder(Color.black,5);
		this.setBorder(blackline);
		
		this.centerPanel = new JPanel(new GridLayout(4,1,10,10));
		this.bottomPanel = new JPanel(new GridLayout(1,4,10,10));
		
		this.cardBtn = new JButton("ī�����");
		this.cashBtn = new JButton("���ݰ���");
		this.backBtn = new JButton("�������");
		this.total =  0;
		
		updatePanel();
		updateBtnEvent();
	}
	
	private void updatePanel(){
		this.setLayout(new BorderLayout());
		
		this.centerPanel.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));
		updateCenterPanel();
		this.add(this.centerPanel , BorderLayout.CENTER);
		
		this.bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(this.bottomPanel , BorderLayout.SOUTH);
	}
	
	private void updateBtnEvent(){
		
		/*
		 * ���� ��� Flow -> resetTotal -> cancelBtn Push
		 */
		this.backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.resetTotal();
				cancelBtn.getAniAction().run();
			}
		});
	}
	
	public void updateCenterPanel(){
		
		//���� �ð� ������Ʈ
		Date dt = new Date();
		SimpleDateFormat dtForm = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
		String curTime = dtForm.format(dt.getTime());
		dateLabel.setText("���� �Ͻ� : \t\t" + curTime);
		
		//�� ������ ������Ʈ
		String total = Integer.toString(this.total);
		totalLabel.setText("�� ������ : \t" + total);
		
		//�Ǹ� ���� ������Ʈ
		String rule = "<html>�Ǹ� ����<br>"
				+ "1. �ֹ� ������ Ȯ���մϴ�.<br>"
				+ "2. �ֹ� ������ ���� ��õ �޴��� �ֹ� ���θ� ���庾�ϴ�.<br>"
				+ "\tEx. �Ƹ޸�ī�뿡�� ������ ���� ��ô� ���� ������ ��Ű���?<br>"
				+ "3. �� �ݾ��� �����帮�� ���� ����(ī��, ����)�� ���� ���ϴ�.<br>"
				+ "4. ������ �����ϰ� ���ǥ�� �߱��� �帮��, ��ٸ��� �մ��� ���� ���,"
				+ " �̸� ���ظ� ���մϴ�.<br>"
				+ "5. �ֹ��� ���� ������ Ȯ���մϴ�."
				+ "</html>";
		ruleLabel.setText(rule);
		
		this.centerPanel.add(dateLabel);
		this.centerPanel.add(totalLabel);
		//��õ �޴�
		this.centerPanel.add(ruleLabel);
	}
	
	public void addCancelButton(AniButton cancelBtn){
		this.cancelBtn = cancelBtn;
		addBottomBtns();
		this.bottomPanel.add(cancelBtn);
	}
	
	private void addBottomBtns(){
		this.bottomPanel.add(cardBtn);
		this.bottomPanel.add(cashBtn);
		this.bottomPanel.add(backBtn);
	}
	
	public void setTotal(int total){
		this.total = total;
		this.totalLabel.setText("�� ������ : " + Integer.toString(this.total));
	}

}
