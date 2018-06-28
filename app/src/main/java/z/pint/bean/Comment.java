package z.pint.bean;

import java.io.Serializable;

/**
 * 评论类
 * Created by DN on 2018/6/19.
 */

public class Comment implements Serializable {
    private int commentID; //评论ID
    private int userID;//用户ID
    private int worksID;//作品ID
    private String userHead; //评论用户头像
    private String userName;//评论用户昵称
    private String commentTime;//评论时间
    private String commentContent;//评论内容

    public Comment(String userHead,String userName,String commentContent){
        this.userHead = userHead;
        this.userName = userName;
        this.commentContent = commentContent;
    }

    public int getWorksID() {
        return worksID;
    }

    public void setWorksID(int worksID) {
        this.worksID = worksID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
