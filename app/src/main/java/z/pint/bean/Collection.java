package z.pint.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 收藏
 * Created by DN on 2018/6/19.
 */
@Table(name = "COLLECTION")//表名
public class Collection implements Serializable{
    @Column(name = "collectionID", isId = true, autoGen = true)
    private int collectionID;//收藏ID
    @Column(name = "worksID",property = "NOT NULL")
    private int worksID;//作品ID
    @Column(name = "userID",property = "NOT NULL")
    private int userID;//作品用户ID
    @Column(name = "userHead",property = "NOT NULL")
    private String userHead;//作品用户头像
    @Column(name = "userName",property = "NOT NULL")
    private String userName;//作品用户昵称
    @Column(name = "worksReleaseTime")
    private String worksReleaseTime;//作品发布时间
    @Column(name = "worksImag")
    private String worksImag;//作品图片
    @Column(name = "worksDescribe")
    private String worksDescribe;//作品描述
    @Column(name = "worksLikeNumber")
    private int worksLikeNumber;//作品点赞数
    @Column(name = "worksCommentNumber")
    private int worksCommentNumber;	//作品评论数
    @Column(name = "collectionTime")
    private String collectionTime;//收藏时间


    public String getWorksImag() {
        return worksImag;
    }

    public void setWorksImag(String worksImag) {
        this.worksImag = worksImag;
    }

    public int getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(int collectionID) {
        this.collectionID = collectionID;
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

    public String getWorksReleaseTime() {
        return worksReleaseTime;
    }

    public void setWorksReleaseTime(String worksReleaseTime) {
        this.worksReleaseTime = worksReleaseTime;
    }

    public String getWorksDescribe() {
        return worksDescribe;
    }

    public void setWorksDescribe(String worksDescribe) {
        this.worksDescribe = worksDescribe;
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

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }
}
