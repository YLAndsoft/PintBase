package z.pint.utils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import f.base.utils.LogUtils;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import z.pint.R;
import z.pint.bean.Likes;
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
        if(isLikes){
            likesStatistics(mContext,works,HttpConfig.ADD_STATE);
        }
    }

    /**
     * 点赞统计(本地)
     * @param works
     */
    public static void addLikes(Works works){
        boolean b = DBHelper.saveLikes(createLikes(works));
        Log.e("Statistics点赞结果：",b+"");
        if(b){
            //修改点赞表里面的是否点赞
            boolean b1 = DBHelper.upWorksLikeNumber(works);
            works.setLikes(true);
            DBHelper.upisLikeNumber(works);
            Log.e("Statistics添加点赞数量结果：",b1+"");
        }
    }
    private static Likes createLikes(Works works){
        Likes likes = new Likes();
        likes.setUserHead(works.getUserHead()+"");
        likes.setUserName(works.getUserName()+"");
        likes.setWorksID(works.getWorksID());
        likes.setUserID(works.getUserID());
        likes.setAttention(false);
        return likes;
    }

    /**
     * 关注统计
     * @param isAttention
     * @param works
     */
    public static void isAttention(Context mContext,boolean isAttention,Works works){
        if(isAttention){ //true:增加关注
            atStatistics(mContext,works.getUserID(),HttpConfig.ADD_STATE);
        }else{ //false:取消关注
            atStatistics(mContext,works.getUserID(),HttpConfig.DELETE_STATE);
        }
    }
    /**
     * 关注统计
     * @param isAttention
     * @param userID
     */
    public static void isAttention(Context mContext,boolean isAttention,int userID){
        if(isAttention){ //true:增加关注
            atStatistics(mContext,userID,HttpConfig.ADD_STATE);
        }else{ //false:取消关注
            atStatistics(mContext,userID,HttpConfig.DELETE_STATE);
        }
    }

    /**
     * 点赞统计
     * @param mContext
     * @param works
     * @param stateAction
     */
    private static void likesStatistics(Context mContext,Works works, int stateAction){
        if(null==works){return;}
        int userID = (int) SPUtils.getInstance(mContext).getParam("userID", 0);
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID,userID+"");
        map.put(HttpConfig.WORKS_ID,works.getWorksID()+"");
        map.put(HttpConfig.ACTION_STATE,stateAction+"");
        XutilsHttp.xUtilsRequest(HttpConfig.getLiksData, map, new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                Log.e("Statistics点赞结果：",result+"");
            }
            @Override
            public void onFail(String result) {
                Log.e("Statistics点赞异常：",result);
            }
        });
    }

    /**
     * 关注统计
     * @param mContext
     * @param worksUserID
     * @param stateAction
     */
    public static void atStatistics(Context mContext,int worksUserID,int stateAction){
        int userID = SPUtils.getUserID(mContext);
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID,userID+"");//用户ID
        map.put(HttpConfig.CONVER_ATTENTION_ID,worksUserID+"");//被关注人用户ID
        map.put(HttpConfig.ACTION_STATE,stateAction+"");
        XutilsHttp.xUtilsRequest(HttpConfig.getAttentionData, map, new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                Log.e("Statistics关注结果：",result+"");
            }
            @Override
            public void onFail(String result) {
                Log.e("Statistics关注异常：",result);
            }
        });
        if(stateAction==HttpConfig.ADD_STATE){
            //增加关注通知消息
            Map<String,String> mp = new HashMap<>();
            mp.put(HttpConfig.USER_ID,userID+"");//用户ID
            mp.put(HttpConfig.NOTIFY_USER_ID,worksUserID+"");//需要通知的人的用户ID
            mp.put(HttpConfig.APPNAME,mContext.getResources().getString(R.string.http_name)+"");
            XutilsHttp.xUtilsRequest(HttpConfig.addMessage, mp, new XutilsHttp.XUilsCallBack() {
                @Override
                public void onResponse(String result) {
                    Log.e("Statistics添加关注消息结果：",result+"");
                }
                @Override
                public void onFail(String result) {
                    Log.e("Statistics添加关注消息结果：",result+"");
                }
            });
        }
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
                Log.e("Statistics评论异常：",result);
            }
        });
    }


}
