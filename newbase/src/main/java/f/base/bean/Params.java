package f.base.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by DN on 2018/6/26.
 */

public class Params<T> implements Serializable{
    private String URL;//地址
    private Map<String,String> map;//map参数
    private Class<T> clazz;//需要解析的数据类型
    private Object obj;//保存网络数据结果
    private boolean isList = false;//是否集合数据
    private boolean isRefresh = false;//是否是刷新请求
    private boolean isLoad = false;//是否是加载更多

    public Params(){}

    public Params(String URL, Map<String,String> map){
        this.URL = URL;
        this.map = map;
    }
    public Params(String URL, Map<String,String> map,Class<T> clazz,boolean isList){
        this.URL = URL;
        this.map = map;
        this.clazz = clazz;
        this.isList = isList;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
