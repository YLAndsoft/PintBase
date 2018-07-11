package z.pint.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 作品类
 * Created by DN on 2018/6/19.
 */
@Table(name = "WORKS")//表名
public class Works implements Serializable{
    @Column(name = "_id", isId = true, autoGen = true)
    private int _id;
    @Column(name = "worksID", property = "NOT NULL")
    private int worksID;//作品ID
    @Column(name = "userID",property = "NOT NULL")
    private int userID;//用户ID
    @Column(name = "userName",property = "NOT NULL")
    private String userName;//用户昵称
    @Column(name = "userHead",property = "NOT NULL")
    private String userHead;//用户头像
    @Column(name = "worksImage",property = "NOT NULL")
    private String worksImage;//作品展示图
    @Column(name = "worksLabel")
    private String worksLabel;//作品标签
    @Column(name = "worksDescribe",property = "NOT NULL")
    private String worksDescribe;//作品描述
    @Column(name = "worksReleaseTime")
    private String worksReleaseTime;//作品发布时间
    @Column(name = "worksLikeNumber")
    private int worksLikeNumber;//作品点赞数
    @Column(name = "worksCommentNumber")
    private int worksCommentNumber;	//作品评论数
    @Column(name = "worksStrokes")
    private int worksStrokes;//作品笔画
    @Column(name = "isLikes")
    private boolean isLikes;//是否已点赞
    @Column(name = "classifyID")
    private int classifyID;//
    private boolean isAttention;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getWorksID() {
        return worksID;
    }

    public void setWorksID(int worksID) {
        this.worksID = worksID;
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

    public String getWorksImage() {
        return worksImage;
    }

    public void setWorksImage(String worksImage) {
        this.worksImage = worksImage;
    }

    public String getWorksLabel() {
        return worksLabel;
    }

    public void setWorksLabel(String worksLabel) {
        this.worksLabel = worksLabel;
    }

    public String getWorksDescribe() {
        return worksDescribe;
    }

    public void setWorksDescribe(String worksDescribe) {
        this.worksDescribe = worksDescribe;
    }

    public String getWorksReleaseTime() {
        return worksReleaseTime;
    }

    public void setWorksReleaseTime(String worksReleaseTime) {
        this.worksReleaseTime = worksReleaseTime;
    }

    public int getWorksLikeNumber() {
        return worksLikeNumber;
    }

    public void setWorksLikeNumber(int worksLikeNumber) {
        this.worksLikeNumber = worksLikeNumber;
    }

    public int getWorksCommentNumber() {
        return worksCommentNumber;
    }

    public void setWorksCommentNumber(int worksCommentNumber) {
        this.worksCommentNumber = worksCommentNumber;
    }

    public int getWorksStrokes() {
        return worksStrokes;
    }

    public void setWorksStrokes(int worksStrokes) {
        this.worksStrokes = worksStrokes;
    }

    public boolean isLikes() {
        return isLikes;
    }

    public void setLikes(boolean likes) {
        isLikes = likes;
    }

    public boolean isAttention() {
        return isAttention;
    }

    public void setAttention(boolean attention) {
        isAttention = attention;
    }

    public int getClassifyID() {
        return classifyID;
    }

    public void setClassifyID(int classifyID) {
        this.classifyID = classifyID;
    }
}
