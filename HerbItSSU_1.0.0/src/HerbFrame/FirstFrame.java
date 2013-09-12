package HerbFrame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.slidinglayout.SLSide;

public class FirstFrame extends JFrame{

	private SLPanel panel_1 = new SLPanel();
	private HerbButton go2 = new HerbButton("Go to Next Frame");
	private HerbButton back2 = new HerbButton("Back to the Before frame");
	private SLConfig mainCfg,p1Cfg;
	private FirstPanel p1 = new FirstPanel("Before");
	private FirstPanel p2 = new FirstPanel("After");
	
	public FirstFrame(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.DARK_GRAY);
		this.setTitle("Herb Frame Test");
		this.setLayout(new BorderLayout());
		
		this.add(panel_1,BorderLayout.CENTER);
		
		go2.setAction(p1Action);
		back2.setAction(p1BackAction);
		
		mainCfg = new SLConfig(panel_1)
					.gap(10, 10)
					.row(1f).col(1f)
					.beginGrid(0, 0).row(8f).row(1f).col(1f)
					.place(0, 0, p1)
					.place(1, 0, go2)
					.endGrid();
		p1Cfg= new SLConfig(panel_1)
					.gap(10,10)
					.row(1f).col(1f)
					.beginGrid(0, 0).row(8f).row(1f).col(1f)
					.place(0, 0, p2)
					.place(1, 0, back2)
					.endGrid();
		
		panel_1.setTweenManager(SLAnimator.createTweenManager());
		panel_1.initialize(mainCfg);
	}
	
	private final Runnable p1Action =  new Runnable() {
		@Override
		public void run() {
			panel_1.createTransition()
			.push(new SLKeyframe(p1Cfg, 0.6f)
				.setEndSide(SLSide.TOP, p1)
				.setStartSide(SLSide.BOTTOM, p2)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
				}}))
			.play();
		}
	};
	
	private final Runnable p1BackAction =  new Runnable() {
		@Override
		public void run() {
			panel_1.createTransition()
			.push(new SLKeyframe(mainCfg, 0.6f)
				.setEndSide(SLSide.TOP, p2)
				.setStartSide(SLSide.BOTTOM, p1)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
				}}))
			.play();
		}
	};
	
}
