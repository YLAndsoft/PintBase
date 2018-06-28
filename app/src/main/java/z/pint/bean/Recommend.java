package z.pint.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 推荐界面数据实体类
 * Created by DN on 2018/6/19.
 */

public class Recommend implements Serializable{
    private List<WorksClassify> classifyName;//分类集合
    private List<Works> workses;//数据集合

    public List<WorksClassify> getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(List<WorksClassify> classifyName) {
        this.classifyName = classifyName;
    }

    public List<Works> getWorks() {
        return workses;
    }

    public void setWorks(List<Works> works) {
        this.workses = works;
    }
}
