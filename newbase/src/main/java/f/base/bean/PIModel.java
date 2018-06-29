package f.base.bean;

import java.util.List;


/**
 * 存省 和市的list 和区的
 */
public class PIModel {
	  private String name;
	    private List<CIModel> cityList;

	    public PIModel() {
	        super();
	    }

	    public PIModel(String name, List<CIModel> cityList) {
	        super();
	        this.name = name;
	        this.cityList = cityList;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public List<CIModel> getCityList() {
	        return cityList;
	    }

	    public void setCityList(List<CIModel> cityList) {
	        this.cityList = cityList;
	    }

	    @Override
	    public String toString() {
	        return "ProvinceInfoModel [name=" + name + ", cityList=" + cityList + "]";
	    }
}
