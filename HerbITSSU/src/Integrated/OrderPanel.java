package Integrated;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import AnimationComponent.AniButton;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.slidinglayout.SLSide;



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
	
	private AniButton payButton = new AniButton("�����ϱ�");
	private AniButton cancel = new AniButton("�������");
	private final SLPanel basePanel = new SLPanel();
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
	private JPanel categoryPanel, menuPanel;					//PIXEL ����
	private JPanel cardCash;					//��ư�ΰ��� �г�
	private JPanel pane;
	
	
	public OrderPanel( OrderSystem os ) {
		this.os = os;
		
		categoryPanel = new JPanel();
		menuPanel = new JPanel();
		cardCash = new JPanel();
		pane = new JPanel();
		
		this.setLayout(new BorderLayout());
		pane.setLayout(new BorderLayout());
		//pane.add(basePanel, BorderLayout.CENTER);
		//pane.add(cardCash, BorderLayout.SOUTH);
		
		JSplitPane splitPane = new JSplitPane(currentNumber, categoryPanel, basePanel);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerSize(1);
		splitPane.setDividerLocation(100);
		splitPane.setLeftComponent(categoryPanel);
		splitPane.setRightComponent(basePanel);
		
		this.add(splitPane, BorderLayout.CENTER);
		
		
		/* ���̺� ���̾ƿ� ���� ���� */
		orderListTable = new JTable();
		orderListTableModel = new DefaultTableModel(data, columnNames);
		orderListTable.setModel(orderListTableModel);
		
		orderListTableScroll = new JScrollPane(orderListTable);
		orderListTableScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		orderListTableScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		/* �ȼ��۾� �ʿ��Ѻκ� ���� PIXEL*/
		menuPanel.setLayout( new ModifiedFlowLayout() );
		cardCash.setLayout(new FlowLayout());
		/* �ȼ��۾� �ʿ��Ѻκ� �� PIXEL*/
		
		menuListScroll = new JScrollPane(menuPanel);
		menuListScroll
			.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		menuListScroll
			.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		for( int tmp=0; tmp<menuList.length; tmp++)
		{
			categoryPanel.add(new JButton("Cate-" + menuList[tmp]));
		}
		
		for( int tmp=0; tmp<menuList.length; tmp++)
		{
			menuBtn[tmp] = new JButton(menuList[tmp]);
			menuBtn[tmp].addActionListener(new menuListener());
			/* �ȼ��۾� �ʿ��� �κ� 2 */
			menuPanel.add(menuBtn[tmp]);
			/* �ȼ��۾� �ʿ��Ѻκ� 2�� */
		}
		
		payButton.setAction(payAction);
		//cardCash.add(payButton);
		menuPanel.add(payButton);
		
		cancel.setAction(payBackAction);
		//cardCash.add(cancel);
		payPane.add(cancel);
		
		orderListTableModel.setDataVector(data, columnNames);
		
		payCfg = new SLConfig(basePanel)
		.gap(10, 10)
		.row(1f).col(2f).col(1f)
		.place(0, 0, orderListTableScroll)
		.place(0, 1, payPane);

		payBackCfg = new SLConfig(basePanel)
		.gap(10, 10)
		.row(1f).col(1f).col(2f)
		.place(0, 0, menuListScroll)
		.place(0, 1, orderListTableScroll);
		
		basePanel.setTweenManager(SLAnimator.createTweenManager());
		basePanel.initialize(payBackCfg);
		
	}
	public void dataUpdate() {			//������ ����Ǿ���� �κ�
		
		
	}
	
	private final Runnable payAction = new Runnable() {
		@Override
		public void run() {
			basePanel.createTransition()
				.push(new SLKeyframe(payCfg, 0.6f)
				.setEndSide(SLSide.LEFT, menuListScroll)
				.setStartSide(SLSide.RIGHT, payPane)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
				}}))
				.play();
		}
	};
	
	private final Runnable payBackAction = new Runnable() {
		@Override
		public void run() {
			basePanel.createTransition()
				.push(new SLKeyframe(payBackCfg, 0.6f)
				.setStartSide(SLSide.LEFT, menuListScroll)
				.setEndSide(SLSide.RIGHT, payPane)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
				}}))
				.play();
		}
	};
	
	public void payActionPush(){
		this.payButton.getAniAction().run();
	}
	
	public void cancelPayAction(){
		this.cancel.getAniAction().run();
	}
	
	public static class Accessor extends SLAnimator.ComponentAccessor{
		
	}
}
