package PieChart;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.TableOrder;

/*�޴��� �Ǹŷ��� �׸��� ������Ʈ */
public class PieChart{
	  private JFreeChart chart = null;
	  private TextTitle subTitle = null;
	  private String menuList[] = {				//��ư�� �� �޴���
				"coffee1", "coffee2", "coffee3", "coffee4", "coffee5", "coffee6", "coffee7", "coffee8", "coffee9"
				, "coffee10", "coffee11", "coffee12", "coffee13", "coffee14", "coffee15", "coffee16", "coffee17", 
				"coffee18", "coffee19", "coffee20"
		};
	  private DefaultCategoryDataset myDataset;
	  public PieChart(){
		  myDataset = new DefaultCategoryDataset();
		  
		  /* �Ǹŷ��� �޴��� ���� �κ� */ 
		  myDataset.setValue(10, "", menuList[0]);
		  myDataset.setValue(50, "", menuList[13]);
	  }
	  public ChartPanel getPieChart_HistogramChart(){
	   ChartPanel chartPanel_PieChart = null;
	   
	   chart = ChartFactory.createPieChart("Sales Monitoring", new CategoryToPieDataset(myDataset, TableOrder.BY_ROW, 0), true, true, false);
	   subTitle = new TextTitle("���� ������Ȳ");
	   chart.setBorderVisible(false);
	   chart.setBackgroundPaint(Color.WHITE);
	   chart.addSubtitle(subTitle);
	   chartPanel_PieChart = new ChartPanel(chart);
	   return chartPanel_PieChart;
	  }
}