package Integrated;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.slidinglayout.SLSide;

import AnimationComponent.AniButton;

import java.awt.event.*;



class OrderPanel extends JPanel {
	
	class menuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton tmp1 = (JButton)e.getSource();
			String name = tmp1.getText();
			
			String price = "0";
			int numberOfItem =0 , sumOfEachItemPrice =0 ;		//�ܰ�, �޴��� �հ�
			/* �ݺ��ϸ� ������ �޾ƿ´�. */
			for (int tmp=0; tmp<menuList.length; tmp++ ) {
				if (menuList[tmp].compareTo(name)==0)
				{
					price = priceList[tmp];
				}
			}
			/* �ݺ��ϸ� ������ �޴��� �հ踦 �޾ƿ´�. */
			boolean sameMenuPickFlag = false;
			int pos = currentNumber;
			for (int tmp=0; tmp<currentNumber; tmp++) {
				if (name.compareTo(data[tmp][0])==0) {
					numberOfItem = Integer.valueOf(data[tmp][1]).intValue()
							+ 1;
					sumOfEachItemPrice = Integer.valueOf(data[tmp][3]).intValue()
							+ Integer.valueOf(price).intValue();
					pos = tmp;
					sameMenuPickFlag = true;
				}
			}
			if (sameMenuPickFlag == false) {
				numberOfItem = 1;
				sumOfEachItemPrice = Integer.valueOf(price).intValue();
				currentNumber ++;
			}
			/* �޾ƿ��� �� */
			/* ���̺� ���� */
			/* data[][0]: �޴���, data[][1]: �ֹ�����, data[][2]: �ܰ�, data[][3]: �޴��ֹ��������հ�*/
			data[pos][0] = name;
			data[pos][1] = Integer.toString(numberOfItem);
			data[pos][2] = price;
			data[pos][3] = Integer.toString(sumOfEachItemPrice);
			
			orderListTableModel.setDataVector(data, columnNames);
			
			
		}	
	}
	
	OrderSystem os;
	
	private AniButton payButton = new AniButton("����");
	private AniButton cancel = new AniButton("���");
	private final SLPanel basePanel;
	private SLConfig payCfg,payBackCfg;
	private PayPane payPane = new PayPane("����â ����");
	
	private JTable orderListTable;
	private DefaultTableModel orderListTableModel;
	private JScrollPane orderListTableScroll;
	private String data[][] = new String[30][4];		//�ֹ��� ����� �����ͼ�, 30�� 4��
	int currentNumber = 0;							//�ֹ����� �޴� ��
	final int maximumNumber = 60; 						//�� ���� �� �ֹ������� �޴� ��
	
	private String menuList[] = {				//��ư�� �� �޴���
			"Ŀ��1", "Ŀ��2", "Ŀ��3", "Ŀ��4", "Ŀ��5", "Ŀ��6", "Ŀ��7", "Ŀ��8", "Ŀ��9"
			,"Ŀ��10", "Ŀ��11", "Ŀ��12", "Ŀ��13", "Ŀ��14", "Ŀ��15", "Ŀ��16", "Ŀ��17", "Ŀ��18", "Ŀ��19", "Ŀ��20"
	};
	private JScrollPane menuListScroll;
	private String priceList[] = {
			"1100", "1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900",
			"2000", "2100", "2200", "2300", "2400", "2500", "2600", "2700", "2800", "2900", "3000"
	};
	private JButton menuBtn[] = new JButton[menuList.length];
	JButton cash, card;
	private String columnNames[] = { "�޴���", "����", "�ܰ�", "�޴��� �հ�"};
	/* data[][0]: �޴���, data[][1]: �ֹ�����, data[][2]: �ܰ�, data[][3]: �޴��ֹ��������հ�*/
	private JPanel leftPanel;					//PIXEL ����
	private JPanel cardCash;					//��ư�ΰ��� �г�
	
	
	public OrderPanel( OrderSystem os , SLPanel basePanel, SLConfig payCfg, SLConfig payBackCfg) {
		this.os = os;
		this.basePanel = basePanel;
		this.payCfg = os.getPayCfg();
		this.payBackCfg = os.getPayBackCfg();
		
		/* ���̺� ���̾ƿ� ���� ���� */
		orderListTable = new JTable();
		orderListTableModel = new DefaultTableModel(data, columnNames);
		orderListTable.setModel(orderListTableModel);
		
		orderListTableScroll = new JScrollPane(orderListTable);
		orderListTableScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		orderListTableScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		/* ���̺� ���̾ƿ� ���� �� */
		this.setLayout(new BorderLayout());
		this.add(orderListTableScroll, BorderLayout.EAST);
		leftPanel = new JPanel();
		cardCash = new JPanel();
		this.add(cardCash, BorderLayout.SOUTH);
		/* �ȼ��۾� �ʿ��Ѻκ� ���� PIXEL*/
		leftPanel.setLayout( new ModifiedFlowLayout() );
		cardCash.setLayout(new FlowLayout());
		/* �ȼ��۾� �ʿ��Ѻκ� �� PIXEL*/
		
		menuListScroll = new JScrollPane(leftPanel);
		menuListScroll
			.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		menuListScroll
			.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(menuListScroll, BorderLayout.CENTER);
		for( int tmp=0; tmp<menuList.length; tmp++)
		{
			menuBtn[tmp] = new JButton(menuList[tmp]);
			menuBtn[tmp].addActionListener(new menuListener());
			/* �ȼ��۾� �ʿ��� �κ� 2 */
			leftPanel.add(menuBtn[tmp]);
			/* �ȼ��۾� �ʿ��Ѻκ� 2�� */
		}
		
		payButton.setAction(payAction);
		cardCash.add(payButton);
		
		cancel.setAction(payBackAction);

		/*
		cancel = new JButton("���");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for(int x=0 ; x < data.length ; x++) {
					data[x][0] = "";
					data[x][1] = "";
					data[x][2] = "";
					data[x][3] = "";
				}
				currentNumber=0;
				orderListTableModel.setDataVector(data, columnNames);
			}
			
		});*/
		cardCash.add(cancel);
		/* �ȼ��۾� �ʿ��� �κ� 3 ��*/
		
		orderListTableModel.setDataVector(data, columnNames);
		
	}
	public void dataUpdate() {			//������ ����Ǿ���� �κ�
		
		
	}
	
	private final Runnable payAction = new Runnable() {
		@Override
		public void run() {
			PayPane.payWorking = true; // ����â�� ��µǾ��ִ� ���� 
				//Ÿ Pane���� ���ٽ� flag�� Ȱ��
			basePanel.createTransition()
				.push(new SLKeyframe(payCfg, 0.6f)
				.setStartSide(SLSide.RIGHT, payPane)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
				}}))
				.play();
		}
	};
	
	private final Runnable payBackAction = new Runnable() {
		@Override
		public void run() {
			PayPane.payWorking = false;
			basePanel.createTransition()
				.push(new SLKeyframe(payBackCfg, 0.6f)
				.setEndSide(SLSide.RIGHT, payPane)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
				}}))
				.play();
		}
	};
	
	public PayPane getPayPane(){
		return this.payPane;
	}
	
	public void setCfg(){
		this.payCfg = os.getPayCfg();
		this.payBackCfg = os.getPayBackCfg();
	}
	
	public void payActionPush(){
		this.payButton.getAniAction().run();
	}
	
	public void cancelPayAction(){
		this.cancel.getAniAction().run();
	}
	
}
