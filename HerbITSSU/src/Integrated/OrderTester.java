package Integrated;

import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.tweenengine.Tween;

public class OrderTester {
	static OrderSystem os;
	public static void main(String args[] )
	{
		Tween.registerAccessor(OrderPanel.class, new OrderPanel.Accessor());
		SLAnimator.start();
		
		os = new OrderSystem();
	}
}
