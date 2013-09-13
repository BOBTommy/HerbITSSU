package Database;

import java.util.Date;

public class HerbOrderTable {
	private int order_id;				//primary
	private int order_menu_id;
	private int order_count;
	private Date order_date;
	public String toString() { 
    	String result = null;
        result = order_id + "|";
        result += order_menu_id + "|";
        result += order_count + "|";
        result += order_date + "|";
        
        return result;
    }
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public int getOrder_id() {
		return this.order_id;
	}
	public void setOrder_menu_id(int order_menu_id) {
		this.order_menu_id = order_menu_id;
	}
	public int getOrder_menu_id() {
		return this.order_menu_id;
	}
	public void setOrder_count(int order_count) {
		this.order_count = order_count;
	}
	public int getOrder_count() {
		return this.order_count;
	}
	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}
	public Date getOrder_date() {
		return this.order_date;
	}
}
