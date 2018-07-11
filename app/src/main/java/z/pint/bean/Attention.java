package z.pint.bean;

import java.io.Serializable;

/**
 * 关注类
 * Created by DN on 2018/6/19.
 */

public class Attention implements Serializable {
    private int _id;//id
    private int userID;//用户ID
    private int converAttentionID;//被关注人ID
    private String userName;
    private String userHead;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getConverAttentionID() {
        return converAttentionID;
    }

    public void setConverAttentionID(int converAttentionID) {
        this.converAttentionID = converAttentionID;
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
