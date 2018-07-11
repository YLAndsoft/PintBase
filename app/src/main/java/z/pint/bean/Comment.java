package z.pint.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 评论类
 * Created by DN on 2018/6/19.
 */
@Table(name = "COMMENT")//表名
public class Comment implements Serializable {
    @Column(name = "commentID", isId = true, autoGen = true)
    private int commentID; //评论ID
    @Column(name = "userID",property = "NOT NULL")
    private int userID;//用户ID
    @Column(name = "worksID",property = "NOT NULL")
    private int worksID;//作品ID
    @Column(name = "userHead",property = "NOT NULL")
    private String userHead; //评论用户头像
    @Column(name = "userName",property = "NOT NULL")
    private String userName;//评论用户昵称
    @Column(name = "commentTime",property = "NOT NULL")
    private String commentTime;//评论时间
    @Column(name = "commentContent",property = "NOT NULL")
    private String commentContent;//评论内容

    public Comment(){}
    public Comment(String userHead,String userName,String commentContent){
        this.userHead = userHead;
        this.userName = userName;
        this.commentContent = commentContent;
    }
    public Comment(int userID,int worksID,String userHead,String userName,String commentTime,String commentContent){
        this.userID= userID;
        this.worksID = worksID;
        this.userHead = userHead;
        this.userName = userName;
        this.commentTime = commentTime;
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
