package z.pint.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 点赞类
 * Created by DN on 2018/6/19.
 */
@Table(name = "LIKES")//表名
public class Likes implements Serializable {
    @Column(name = "likesID", isId = true, autoGen = true)
    private int likesID;
    @Column(name = "userID",property = "NOT NULL")
    private int userID;//用户ID
    @Column(name = "userName",property = "NOT NULL")
    private String userName;//用户昵称
    @Column(name = "userHead",property = "NOT NULL")
    private String userHead;//用户头像
    @Column(name = "worksID",property = "NOT NULL")
    private int worksID;//作品ID
    private boolean attention;//是否关注


    public Likes(){}
    public Likes(String userName,String userHead){
        this.userName = userName;
        this.userHead = userHead;
    }

    public boolean isAttention() {
        return attention;
    }

    public int getWorksID() {
        return worksID;
    }

    public void setWorksID(int worksID) {
        this.worksID = worksID;
    }

    public void setAttention(boolean attention) {
        this.attention = attention;
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
