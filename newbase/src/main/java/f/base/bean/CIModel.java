package f.base.bean;

import java.util.List;

public class CIModel {
	 private String name;
	    private List<DIModel> districtList;

	    public CIModel() {
	        super();
	    }

	    public CIModel(String name, List<DIModel> districtList) {
	        super();
	        this.name = name;
	        this.districtList = districtList;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public List<DIModel> getDistrictList() {
	        return districtList;
	    }

	    public void setDistrictList(List<DIModel> districtList) {
	        this.districtList = districtList;
	    }

	    @Override
	    public String toString() {
	        return "CityInfoModel [name=" + name + ", districtList=" + districtList
	                + "]";
	    }
}
