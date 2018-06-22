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

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    public String getRegistrTime() {
        return registrTime;
    }

    public void setRegistrTime(String registrTime) {
        this.registrTime = registrTime;
    }

    public int getAttentionNumber() {
        return attentionNumber;
    }

    public void setAttentionNumber(int attentionNumber) {
        this.attentionNumber = attentionNumber;
    }

    public int getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(int fansNumber) {
        this.fansNumber = fansNumber;
    }

    public boolean isAttention() {
        return isAttention;
    }

    public void setAttention(boolean attention) {
        isAttention = attention;
    }
}
