package Database;
import java.util.Date;

public class HerbMenuTable {
	private int menu_id;			//primary, auto increase
    private String menu_name;
    private int  menu_price;
    private Date menu_reg_date;
    private String menu_category;
	
	public int getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public int getMenu_price() {
		return menu_price;
	}
	public void setMenu_price(int menu_price) {
		this.menu_price = menu_price;
	}
	public Date getMenu_reg_date() {
		return menu_reg_date;
	}
	public void setMenu_reg_date(Date date) {
		this.menu_reg_date = date;
	}
    public String toString() { 
    	String result = null;
        result = menu_id + "|";
        result += menu_name + "|";
        result += menu_price + "|";
        result += menu_reg_date + "|";
        result += menu_category;
        return result;
    }
	public String getMenu_category() {
		return menu_category;
	}
	public void setMenu_category(String menu_category) {
		this.menu_category = menu_category;
	}
}
