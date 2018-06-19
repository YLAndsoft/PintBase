package z.pint.bean;

import java.io.Serializable;

/**
 * 点赞类
 * Created by DN on 2018/6/19.
 */

public class Likes implements Serializable {
    private int likesID;
    private int userID;//用户ID
    private String userName;//用户昵称
    private String userHead;//用户头像
}
