package z.pint.bean;

import java.util.List;

/**
 * Created by DN on 2018/7/3.
 */

public class SearchData {
    private List<Works> workses;
    private List<WorksClassify> classifyName;

    public List<Works> getWorks() {
        return workses;
    }

    public void setWorks(List<Works> works) {
        this.workses = works;
    }

    public List<WorksClassify> getWorksClassifies() {
        return classifyName;
    }

    public void setWorksClassifies(List<WorksClassify> worksClassifies) {
        this.classifyName = worksClassifies;
    }
}
