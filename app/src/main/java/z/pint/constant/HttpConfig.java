package z.pint.constant;

/**
 * Created by DN on 2018/6/26.
 */

public class HttpConfig {

    // 链接服务器获取数据的开关 <false:本地> <true:服务器>
    public static final  boolean isInter = false;

    //服务器地址
    private static final String URL_WEB_ZS = "http://120.24.152.185/AppStore2";
    //本地调试地址
    private static final String URL_WEB_CS = "http://192.168.0.200:8081/paintworld";
    //百度云地址
    //private static final String URL_WEB_BDY = "http://120.24.152.185/AppStore2";
    private static final String URL_Web = isInter ? URL_WEB_ZS: URL_WEB_CS;

    public static final String getHomeData =  URL_Web+"/index";//获取首页数据集合接口

    /**
     * 访问网络所需的参数名称
     */
    public static final String USER_ID ="userID";
    public static final String APPNAME ="appName";
    public static final String START ="start";
    public static final String NUM ="num";
    public static final String FLAG ="flag";
    public static final String WORKS_ID ="worksID";
    public static final String CLASSIFY_ID ="classifyID";
    public static final String CLASSIFY_NAME ="classifyname";
    public static final String CONVER_ATTENTION_ID ="converAttentionID";







}
