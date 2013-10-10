package Database;

import java.util.ArrayList;

public class Order {
	
	private ArrayList<Integer> orderList;
	private int orderId;
	
	public Order(int orderId){
		this.orderId = orderId;
		this.orderList = new ArrayList<Integer>();
	}
	
	public void addOrder(int order){
		
		for(int i=0; i<this.orderList.size(); i++){
			if(this.orderList.get(i).intValue() == order)
				return;
		}
		
		this.orderList.add(order);
		
	}
	
	public int getOrderID(){
		return this.orderId;
	}
	
	public int getOrderCount(){
		return this.orderList.size();
	}
	
	public String toString(){
		String result = "[";
		for(int i=0; i<this.orderList.size(); i++)
		{
			result += this.orderList.get(i);
			if(this.orderList.size()-1 != i)
				result += ",";
		}
		result += "]";
		return result;
	}
	
}
