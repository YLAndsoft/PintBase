package f.base.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by DN on 2018/6/26.
 */

public class Params implements Serializable{
    private String URL;//地址
    private Map<String,String> map;//map参数
    private boolean isPost = true;//是否是Post请求 默认为Post
    private boolean isRequest = false;//是否是异步请求，默认为true

    public Params(){}

    public Params(String URL,Map<String,String> map){
        this.URL = URL;
        this.map = map;
    }
    public Params(String URL,Map<String,String> map,boolean isPost,boolean isRequest){
        this.URL = URL;
        this.map = map;
        this.isPost = isPost;
        this.isRequest = isRequest;
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
