package Integrated;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
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
	
	StorePanel storePanel;			//������� ��
	OrderPanel orderPanel;		//�ֹ� ��
	AdminPanel adminPanel;			//�����ڸ޴� ��
	
	
	




	
	public void sync() {
		storePanel.dataUpdate();
		orderPanel.dataUpdate();
		adminPanel.dataUpdate();
	}
	
	public OrderSystem() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension di = tk.getScreenSize();
		
		db = new DBGenerator();
		
		setTitle("Cafe in Herb");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setUndecorated(true);
		setScreen (this, true, (int)di.getWidth(), (int)di.getHeight());
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

		setVisible(true);				//ȭ�鿡 ���̵��� �����Ѵ�.
		
	}
	
	HerbPane createTabbedPane() {
		HerbPane pane = new HerbPane();		//HerbPane�� �ֹ�����, �������, �������� ���� �߰��Ѵ�.
		storePanel = new StorePanel(this);
		pane.addTab("�������", storePanel);				//������� ���� �߰�
		orderPanel = new OrderPanel(this);
		pane.addTab("�ֹ�����", orderPanel);				//�ֹ����� ���� �߰�
		adminPanel = new AdminPanel(this);
		pane.addTab("�����ڸ޴�", adminPanel);				//�������� ���� �߰�
		return pane;
	}
	
    private static void setScreen (Window window, boolean full, int size_X, int size_Y) {
        DisplayMode dm;
        GraphicsConfiguration gc;
        GraphicsDevice gd;
        GraphicsEnvironment ge;

        dm = new DisplayMode(size_X, size_Y, 32, DisplayMode.REFRESH_RATE_UNKNOWN);
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        gc = gd.getDefaultConfiguration();
        if (full) {
          if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(window);
            if (gd.isDisplayChangeSupported()) {
              gd.setDisplayMode(dm);
              return ; // finish method
            }

          }
        } // end if (full)

        gd.setFullScreenWindow(null);
        window.setSize(size_X, size_Y);
      } // close setScreen

}
