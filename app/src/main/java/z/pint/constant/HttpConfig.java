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

    public static final String getHomeData =  URL_Web+"/getHomeData";//获取首页数据集合接口
    public static final String getRecommendData =  URL_Web+"/getRecommendData";//获取推荐页面集合接口
    public static final String getCommentData =  URL_Web+"/getWorksComment";//作品评论接口
    public static final String getLiksData =  URL_Web+"/getLiksData";//点赞接口
    public static final String getRecommendItemData =  URL_Web+"/getRecommendItemData";//获取分类对应的数据
    public static final String getClassifyNameData =  URL_Web+"/classifyname";//获取分类集合
    public static final String getAttentionData = URL_Web+"/getAttentionData";//关注接口
    public static final String getFansData = URL_Web+"/getFansData";//关注接口
    public static final String getUserInfoData = URL_Web+"/getUserInfoData";//注册接口
    public static final String getWorksAllData = URL_Web+"/getWorksAllData";//根据ID获取所有作品
    public static final String getSearchData = URL_Web+"/getSearchData";//搜索接口


    //
    /**
     * 访问网络所需的参数名称
     */
    public static final String USER_ID ="userID";
    public static final String APPNAME ="appName";
    public static final String START ="start";
    public static final String NUM ="num";
    public static final String FLAG ="flag";
    public static final String USER_INFO ="userInfo";
    public static final String WORKS_ID ="worksID";
    public static final String CLASSIFY_ID ="classifyID";
    public static final String CLASSIFY_NAME ="classifyname";
    public static final String CONVER_ATTENTION_ID ="converAttentionID";
    public static final String ACTION_STATE = "actionState";//动作状态0-查询 1-添加 /*2-删除 3-修改*/
    public static final String SEARCH_CONTENT ="searchContent";



    public static final int SELECT_STATE = 0;//查询
    public static final int ADD_STATE = 1;//增加
    public static final int DELETE_STATE = 2;//修改
    public static final int UP_STATE = 3;//删除





}
