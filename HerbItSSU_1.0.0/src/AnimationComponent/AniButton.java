package AnimationComponent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class AniButton extends JButton{

	private Runnable action;
	
	public AniButton(String name){
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
	
	public Runnable getAniAction(){
		return this.action;
	}
	
}
