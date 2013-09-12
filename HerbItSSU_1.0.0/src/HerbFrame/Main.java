package HerbFrame;

import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.tweenengine.Tween;

public class Main {

	public static void main(String []args){
		Tween.registerAccessor(FirstPanel.class, new FirstPanel.Accessor());
		SLAnimator.start();
		
		FirstFrame fr = new FirstFrame();
		fr.setSize(800,600);
		fr.setVisible(true);
	}
	
}
