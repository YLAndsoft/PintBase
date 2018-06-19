package z.pint.bean;

import java.io.Serializable;

/**
 * 用户类
 * Created by DN on 2018/6/19.
 */

public class User implements Serializable{
    private int userID; //用户ID
    private String userName; //用户昵称
    private int userSex;//用户性别 0:男 1:女
    private String userHead;//用户头像
    private String userAddress; //用户地区
    private String userSign;//用户签名
    private String registrTime;//注册时间
    private int attentionNumber;//关注人数
    private int fansNumber;//粉丝数
    private boolean isAttention;//是否已关注
}
