package z.pint.utils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import z.pint.bean.Works;
import z.pint.constant.HttpConfig;

/**
 * 统计
 * Created by DN on 2018/6/29.
 */

public class StatisticsUtils {


    /**
     * 点赞统计
     * @param works
     * @param isLikes
     */
    public static void isLikes(Context mContext,boolean isLikes,Works works){
        if(!isLikes){
            likesStatistics(mContext,works,HttpConfig.ADD_STATE);
        }else{
            likesStatistics(mContext,works,HttpConfig.DELETE_STATE);
        }
    }

    /**
     * 关注统计
     * @param isAttention
     * @param works
     */
    public static void isAttention(Context mContext,boolean isAttention,Works works){
        if(!isAttention){
            atStatistics(mContext,works,HttpConfig.ADD_STATE);
        }else{
            atStatistics(mContext,works,HttpConfig.DELETE_STATE);
        }
    }

    private static void likesStatistics(Context mContext,Works works, int stateAction){
        if(null==works){return;}
        int userID = (int) SPUtils.getInstance(mContext).getParam("userID", 0);
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID,userID+"");
        map.put(HttpConfig.WORKS_ID,works.getWorksID()+"");
        map.put(HttpConfig.ACTION_STATE,stateAction+"");
        XutilsHttp.xUtilsPost(HttpConfig.getLiksData, map, new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                Log.e("Statistics点赞结果：",result+"");
            }
            @Override
            public void onFail(String result) {
                Log.e("Statistics点赞异常：",result.toString());
            }
        });
    }

    private static void atStatistics(Context mContext,Works works,int stateAction){
        if(null==works){return;}
        int userID = (int) SPUtils.getInstance(mContext).getParam("userID", 0);
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID,userID+"");//用户ID
        map.put(HttpConfig.CONVER_ATTENTION_ID,works.getUserID()+"");//被关注人用户ID
        map.put(HttpConfig.ACTION_STATE,stateAction+"");
        XutilsHttp.xUtilsPost(HttpConfig.getAttentionData, map, new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                Log.e("Statistics关注结果：",result+"");
            }
            @Override
            public void onFail(String result) {
                Log.e("Statistics关注异常：",result.toString());
            }
        });
    }

    /**
     * 增加评论
     * @param mContext
     * @param worksID
     * @param content
     */
    public static void addComment(Context mContext,String worksID,String content){
        if(StringUtils.isBlank(worksID)){return;}
        int userID = (int) SPUtils.getInstance(mContext).getParam("userID", 0);
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID,userID+"");//用户ID
        map.put(HttpConfig.WORKS_ID,worksID+"");//作品ID
        map.put(HttpConfig.ACTION_STATE,HttpConfig.ADD_STATE+"");
        map.put(HttpConfig.COMMENT_CONTENT,content+"");//评论内容
        XutilsHttp.xUtilsPost(HttpConfig.getCommentData, map, new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                Log.e("Statistics评论结果：",result+"");
            }
            @Override
            public void onFail(String result) {
                Log.e("Statistics评论异常：",result.toString());
            }
        });
    }


}
