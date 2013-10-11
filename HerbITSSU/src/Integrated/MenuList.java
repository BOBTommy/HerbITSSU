package Integrated;

import java.util.Dictionary;
import java.util.Hashtable;

public class MenuList {
	
	//새로 필요가 없도록 Menu 구성
	public static Dictionary<Integer, String>herbMenu = new Hashtable<Integer, String>();
	//Integer = menu_id, String = menu_name
	
	public static Dictionary<String, Integer>herbMenuInt = new Hashtable<String, Integer>();
	//String = Key, Integer = value
	
	
	private MenuList(){
		// Menu List 는 생성자를 쓰지 않음
	}
	
}
