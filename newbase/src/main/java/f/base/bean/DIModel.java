package f.base.bean;

public class DIModel {
	 private String name;
	    private String zipcode;

	    public DIModel() {
	        super();
	    }

	    public DIModel(String name, String zipcode) {
	        super();
	        this.name = name;
	        this.zipcode = zipcode;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getZipcode() {
	        return zipcode;
	    }

	    public void setZipcode(String zipcode) {
	        this.zipcode = zipcode;
	    }

	    @Override
	    public String toString() {
	        return "DistrictInfoModel [name=" + name + ", zipcode=" + zipcode + "]";
	    }
}
