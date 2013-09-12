package interfaces;

/*	자이썬 모듈의 인터페이스 */

public interface BuildingType {

    public String getBuildingName();
    public void setBuildingName(String name);
    
    public String getBuildingAddress();
    public void setBuildingAddress(String Address);
    
    public int getBuildingId();
    public void setBuildingId(int ID);

}