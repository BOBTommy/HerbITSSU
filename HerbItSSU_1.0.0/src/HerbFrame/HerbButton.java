package HerbFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class HerbButton extends JButton{

	private Runnable action;
	
	public HerbButton(String name){
		super(name);
		this.addListener();
	}
	
	public void setAction(Runnable action){
		this.action = action;
	}
	
	private void addListener(){
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (action != null) action.run();
			}
		});
	}
	
}
