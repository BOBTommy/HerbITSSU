package Integrated;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class MenuList {
	
	//새로 필요가 없도록 Menu 구성
	public static Dictionary<Integer, String>herbMenu = new Hashtable<Integer, String>();
	//Integer = menu_id, String = menu_name
	
	public static Dictionary<String, Integer>herbMenuInt = new Hashtable<String, Integer>();
	//String = Key, Integer = value
	
	public static Dictionary<String, String>herbRecMenuList = new Hashtable<String, String>();
	//Ex. 아메리카노 -> 와플
	
	private MenuList(){
		// Menu List 는 생성자를 쓰지 않음
	}
	//Item1 : 주문 아이템, Item2 : 추천 아이템
	public static void addRecommandItem(String item1, String item2){
		ArrayList<Integer>items = new ArrayList<Integer>();
		StringTokenizer token = new StringTokenizer(item2, ",");
		String item = "";
		while(token.hasMoreTokens()){
			item = token.nextToken();
			items.add(Integer.parseInt(item));
		}
		for(int i=0; i<items.size();i++){
			herbRecMenuList.put(MenuList.herbMenu.get(Integer.parseInt(item1)),
					MenuList.herbMenu.get(items.get(i)));
		}
	}
	
}
