package Database;


//고객의 주문 상황

public class CustomerOrder {

	private String menuName;
	private int menuCount;
	private int price;
	
	public CustomerOrder(String menuName, int menuCount, int price){
		this.menuName = menuName;
		this.menuCount = menuCount;
		this.price = price;
	}
	
	public String getMenuName(){
		return this.menuName;
	}
	
	public int getMenuCount(){
		return this.menuCount;
	}
	
	public int getMenuPrice(){
		return this.price;
	}
	
	public void addMenuCount(){
		this.menuCount++;
	}
	
	public int getTotalPrice() {
		return (this.menuCount * price);
	}
	
}
