package JythonObjectFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import interfaces.BuildingType;
import interfaces.HerbAprioriType;

/* 자이썬 클래스를 생성하고 자이썬 클래스 내 메소드를 통해 메시지 출력 */

public class JythonDriver {

	private HerbAprioriType apriori;
	
	public JythonDriver(String selectionStr) {

	    // Obtain an instance of the object factory
	    JythonObjectFactory factory = JythonObjectFactory.getInstance();
	
	    // Call the createObject() method on the object factory by
	    // passing the Java interface and the name of the Jython module
	    // in String format. The returning object is casted to the the same
	    // type as the Java interface and stored into a variable.
	    if(selectionStr.equalsIgnoreCase("Building"))
	    {
		    BuildingType building = (BuildingType) factory.createObject(
		        BuildingType.class, "Building");
		    // Populate the object with values using the setter methods
		    building.setBuildingName("BUIDING-A");
		    building.setBuildingAddress("100 MAIN ST.");
		    building.setBuildingId(1);
		    System.out.println(building.getBuildingId() + " " + building.getBuildingName() + " " +
		        building.getBuildingAddress());
		
	    }else if(selectionStr.equalsIgnoreCase("Apriori"))
	    {
		    //HerbAprioriTest
		    System.out.println("#######HerbApriori Test#######");
		    apriori = (HerbAprioriType) factory.createObject(HerbAprioriType.class, "HerbApriori");
		    apriori.getResult();
	    }
    
    }
	
	public String getAprioriString(){
		return apriori.getResultString();
	}

}