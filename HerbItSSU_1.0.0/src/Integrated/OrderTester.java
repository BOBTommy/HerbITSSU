package Integrated;

import HerbFrame.FirstPanel;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.tweenengine.Tween;

public class OrderTester {
	static OrderSystem os;
	public static void main(String args[] )
	{
		Tween.registerAccessor(OrderSystem.class, new FirstPanel.Accessor());
		SLAnimator.start();
		
		os = new OrderSystem();
	}
}
