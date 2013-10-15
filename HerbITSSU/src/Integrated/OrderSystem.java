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
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Database.DBGenerator;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLPanel;

class OrderSystem extends JFrame{
	Container contentPane;		//컨텐츠를 포함하는 컨텐트팬
	DBGenerator db;
	
	OrderPanel orderPanel;		//주문 탭
	StorePanel storePanel;			//재고관리 탭
	AdminPanel adminPanel;			//관리자메뉴 탭

	
	public void sync() {
		orderPanel.dataUpdate();
		storePanel.dataUpdate();
		adminPanel.dataUpdate();
	}
	
	public OrderSystem() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension di = tk.getScreenSize();
		
		db = new DBGenerator();
		
		setTitle("Cafe in Herb");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setUndecorated(true);
		setScreen (this, false, (int)di.getWidth(), (int)di.getHeight());
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if( db != null && db.closeDB() ){
                	System.out.println("db closed");
                }
                System.exit(0);
            }
        } );
        
		if( db != null && db.connectDB() ){
			System.out.println("connected to db");;
		}

		contentPane = getContentPane();
		
		HerbPane pane = createTabbedPane();	//탭팬을 생성하여 컨텐트팬에 부착한다.
		contentPane.add(pane,BorderLayout.CENTER);

		setVisible(true);				//화면에 보이도록 설정한다.
		
	}
	
	HerbPane createTabbedPane() {
		HerbPane pane = new HerbPane();		//HerbPane에 주문관리, 매장관리, 고객관리 탭을 추가한다.
		
		storePanel = new StorePanel(this);
		pane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5 style='font-size: 18pt;'>재고관리</body></html>", storePanel);				//매장관리 탭을 추가
		orderPanel = new OrderPanel(this,false);
		pane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5 style='font-size: 18pt;'>주문관리</body></html>", orderPanel);				//주문관리 탭을 추가
		adminPanel = new AdminPanel(this);
		pane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5 style='font-size: 18pt;'>관리자메뉴</body></html>", adminPanel);				//고객관리 탭을 추가
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
