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

    public Likes(){}
    public Likes(String userName,String userHead){
        this.userName = userName;
        this.userHead = userHead;
    }

    public int getLikesID() {
        return likesID;
    }

    public void setLikesID(int likesID) {
        this.likesID = likesID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }
}
