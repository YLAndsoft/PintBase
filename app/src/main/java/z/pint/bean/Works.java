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
    private int classifyID;//

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
