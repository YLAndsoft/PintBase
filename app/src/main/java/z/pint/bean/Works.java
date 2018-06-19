package z.pint.bean;

import java.io.Serializable;

/**
 * 作品类
 * Created by DN on 2018/6/19.
 */
public class Works implements Serializable{
    private int worksID;//作品ID
    private int userID;//用户ID
    private String userName;//用户昵称
    private String userHead;//用户头像
    private String worksImage;//作品展示图
    private String worksLabel;//作品标签
    private String worksDescribe;//作品描述
    private String worksReleaseTime;//作品发布时间
    private int worksLikeNumber;//作品点赞数
    private int worksCommentNumber;	//作品评论数
    private int worksStrokes;//作品笔画
    private boolean isLikes;//是否已点赞
    private boolean isAttention;//是否已关注
}
