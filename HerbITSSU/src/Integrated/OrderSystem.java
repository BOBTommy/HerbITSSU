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
		
		HerbPane pane = createTabbedPane();	//������ �����Ͽ� ����Ʈ�ҿ� �����Ѵ�.
		contentPane.add(pane,BorderLayout.CENTER);
		
		setSize(800, 600);				//����� �����Ѵ�.
		setVisible(true);				//ȭ�鿡 ���̵��� �����Ѵ�.
		
	}
	
	HerbPane createTabbedPane() {
		HerbPane pane = new HerbPane();		//HerbPane�� �ֹ�����, �������, ������ ���� �߰��Ѵ�.
		storePanel = new StorePanel(this);
		pane.addTab("������", storePanel);				//������� ���� �߰�
		orderPanel = new OrderPanel(this);
		pane.addTab("�ֹ�����", orderPanel);				//�ֹ����� ���� �߰�
		adminPanel = new AdminPanel(this);
		pane.addTab("�����ڸ޴�", adminPanel);				//������ ���� �߰�
		return pane;
	}
}
