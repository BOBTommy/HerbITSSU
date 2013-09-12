package Integrated;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Database.DBGenerator;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLPanel;

class OrderSystem extends JFrame{
	Container contentPane;		//�������� �����ϴ� ����Ʈ��
	DBGenerator db;
	
	StorePanel storePanel;			//������ ��
	OrderPanel orderPanel;		//�ֹ� ��
	AdminPanel adminPanel;			//�����ڸ޴� ��
	PayPane payPanel;
	
	private ChangeListener tabListener;		//Sliding Layout �޴� ��ȯ ������
	
	private SLPanel basePanel = new SLPanel();
	private SLConfig payCfg,payBackCfg;
	
	public void sync() {
		storePanel.dataUpdate();
		orderPanel.dataUpdate();
		adminPanel.dataUpdate();
	}
	
	public OrderSystem() {
		db = new DBGenerator();
		
		setTitle("Cafe in Herb");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if( db.closeDB() ){
                	System.out.println("db closed");
                }
                System.exit(0);
            }
        } );
        
		if( db.connectDB() ){
			System.out.println("connected to db");;
		}

		contentPane = getContentPane();
		contentPane.add(basePanel,BorderLayout.CENTER);
		
		HerbPane pane = createTabbedPane();	//������ �����Ͽ� ����Ʈ�ҿ� �����Ѵ�.
		payPanel = orderPanel.getPayPane();
		
		payCfg = new SLConfig(basePanel)
					.gap(10, 10)
					.row(1f).col(5f).col(1f)
					.place(0, 0, pane)
					.place(0, 1, payPanel);
		
		payBackCfg = new SLConfig(basePanel)
					.gap(10, 10)
					.row(1f).col(1f)
					.place(0, 0, pane);
		
		basePanel.setTweenManager(SLAnimator.createTweenManager());
		basePanel.initialize(payBackCfg);
		
		orderPanel.setCfg();
		addListener(pane);
		
		setSize(800, 600);				//����� �����Ѵ�.
		setVisible(true);				//ȭ�鿡 ���̵��� �����Ѵ�.
		
	}
	
	HerbPane createTabbedPane() {
		HerbPane pane = new HerbPane();		//HerbPane�� �ֹ�����, �������, ������ ���� �߰��Ѵ�.
		storePanel = new StorePanel(this);
		pane.addTab("������", storePanel);				//������� ���� �߰�
		orderPanel = new OrderPanel(this , basePanel, payCfg, payBackCfg);
		pane.addTab("�ֹ�����", orderPanel);				//�ֹ����� ���� �߰�
		adminPanel = new AdminPanel(this);
		pane.addTab("�����ڸ޴�", adminPanel);				//������ ���� �߰�
		return pane;
	}
	
	public SLConfig getPayCfg(){
		return this.payCfg;
	}
	
	public SLConfig getPayBackCfg(){
		return this.payBackCfg;
	}
	
	public void addListener(HerbPane pane){
		tabListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				HerbPane sourcePane = (HerbPane) e.getSource();
				int index = sourcePane.getSelectedIndex();
				//0-��� ����, 1-�ֹ� ����, 2- ������ �޴�
				if (index == 1){ //�ֹ� ����, ������ ���� �г��� �ö�� �ִ� ���
					if(PayPane.payWorking)
						orderPanel.payActionPush();
				}else{
					if(PayPane.payWorking){
						orderPanel.cancelPayAction();
						PayPane.payWorking = true;
					}
				}
			}
		};
		pane.addChangeListener(tabListener);
	}
	
}
