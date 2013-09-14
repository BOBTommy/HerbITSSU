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
	Container contentPane;		//컨텐츠를 포함하는 컨텐트팬
	DBGenerator db;
	
	StorePanel storePanel;			//재고관리 탭
	OrderPanel orderPanel;		//주문 탭
	AdminPanel adminPanel;			//관리자메뉴 탭
	
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
		
		HerbPane pane = createTabbedPane();	//탭팬을 생성하여 컨텐트팬에 부착한다.
		contentPane.add(pane,BorderLayout.CENTER);
		
		setSize(800, 600);				//사이즈를 설정한다.
		setVisible(true);				//화면에 보이도록 설정한다.
		
	}
	
	HerbPane createTabbedPane() {
		HerbPane pane = new HerbPane();		//HerbPane에 주문관리, 매장관리, 고객관리 탭을 추가한다.
		storePanel = new StorePanel(this);
		pane.addTab("재고관리", storePanel);				//매장관리 탭을 추가
		orderPanel = new OrderPanel(this);
		pane.addTab("주문관리", orderPanel);				//주문관리 탭을 추가
		adminPanel = new AdminPanel(this);
		pane.addTab("관리자메뉴", adminPanel);				//고객관리 탭을 추가
		return pane;
	}
}
