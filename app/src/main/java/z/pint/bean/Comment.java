package z.pint.bean;

import java.io.Serializable;

/**
 * 评论类
 * Created by DN on 2018/6/19.
 */

public class Comment implements Serializable {
    private int commentID; //评论ID
    private int userID;//用户ID
    private String userHead; //评论用户头像
    private String userName;//评论用户昵称
    private String commentTime;//评论时间
    private String commentContent;//评论内容
}
