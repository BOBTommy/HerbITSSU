package Database;

import java.util.ArrayList;

import Integrated.MenuList;

public class Recommand {

	private ArrayList<Integer> item1;
	private ArrayList<Integer> item2;
	
	public Recommand(){
		item1 = new ArrayList<Integer>();
		item2 = new ArrayList<Integer>();
	}
	
	public void addItem1(int item1){
		this.item1.add(item1);
	}
	
	public void addItem2(int item2){
		this.item2.add(item2);
	}
	
	public String getRecommand(){
		String result = "";
		result += MenuList.herbMenu.get(item1.get(0));
		for(int i=0; i<item2.size(); i++){
			result += MenuList.herbMenu.get(item2.get(i));
		}
		return result;
	}
	
	
}
