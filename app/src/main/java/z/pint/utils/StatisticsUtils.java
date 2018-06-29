package z.pint.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

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
    public static void isLikes(boolean isLikes,Works works){
        if(!isLikes){
            likesStatistics(works,HttpConfig.ADD_STATE);
        }else{
            likesStatistics(works,HttpConfig.DELETE_STATE);
        }
    }

    /**
     * 评论统计
     * @param isAttention
     * @param works
     */
    public static void isAttention(boolean isAttention,Works works){
        if(!isAttention){
            atStatistics(works,HttpConfig.ADD_STATE);
        }else{
            atStatistics(works,HttpConfig.DELETE_STATE);
        }
    }

    private static void likesStatistics(Works works,int stateAction){
        if(null==works){return;}
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID,works.getUserID()+"");
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

    private static void atStatistics(Works works,int stateAction){
        if(null==works){return;}
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID,1+"");//用户ID
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

}
