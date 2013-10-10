package Database;

//고객의 주문 상황

public class CustomerOrder {

	private String menuName;
	private int menuCount;
	
	public CustomerOrder(String menuName, int menuCount){
		this.menuName = menuName;
		this.menuCount = menuCount;
	}
	
	public String getMenuName(){
		return this.menuName;
	}
	
	public int getMenuCount(){
		return this.menuCount;
	}
	
	public void addMenuCount(){
		this.menuCount++;
	}
	
}
