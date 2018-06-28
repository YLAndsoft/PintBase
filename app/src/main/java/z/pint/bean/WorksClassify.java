package z.pint.bean;

import java.io.Serializable;

/**
 * Created by DN on 2018/6/21.
 */

public class WorksClassify implements Serializable{
    private int classifyID;//分类ID
    private String classifyName;//分类名称/*
    //private String classifyAppName;//分类应用名称*/

    public int getClassifyID() {
        return classifyID;
    }

    public void setClassifyID(int classifyID) {
        this.classifyID = classifyID;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    /*public String getClassifyAppName() {
        return classifyAppName;
    }

    public void setClassifyAppName(String classifyAppName) {
        this.classifyAppName = classifyAppName;
    }*/
}
