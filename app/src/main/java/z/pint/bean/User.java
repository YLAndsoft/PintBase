package z.pint.bean;

import com.google.gson.annotations.Expose;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 用户类
 * Created by DN on 2018/6/19.
 */
@Table(name = "USER")//表名
public class User implements Serializable{
   /* @Column(name = "_id", isId = true, autoGen = true)
    private int _id;*/
    @Column(name = "userID",isId = true,autoGen = false,property = "NOT NULL")
    private int userID; //用户ID
    @Column(name = "userName",property = "NOT NULL")
    private String userName; //用户昵称
    @Column(name = "userSex",property = "NOT NULL")
    private int userSex;//用户性别 0:男 1:女
    @Column(name = "userHead",property = "NOT NULL")
    private String userHead;//用户头像
    @Column(name = "userAddress",property = "NOT NULL")
    private String userAddress; //用户地区
    @Column(name = "userSign",property = "NOT NULL")
    private String userSign;//用户签名
    @Column(name = "registrTime")
    private String registrTime;//注册时间
    @Column(name = "attentionNumber")
    private int attentionNumber;//关注人数
    @Column(name = "fansNumber")
    private int fansNumber;//粉丝数
    @Column(name = "isAttention")
    private boolean attention;//是否已关注
    @Column(name = "imei")
    private String imei;//imei手机标识

    @Expose //注解：解析的时候不需要转换，否则会报错,被声明为transient的属性不会被序列化
    private transient OnUpdateValueListener listener;

    public User(){}
    public User(String userName,String userHead){
        this.userName = userName;
        this.userHead = userHead;
    }
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

/*    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }*/

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
        if(listener!=null){
            listener.onUpSucces(userName);
        }
        this.userName = userName;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        if(listener!=null){
            listener.onUpSucces(userSex+"");
        }
        this.userSex = userSex;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        if(listener!=null){
            listener.onUpSucces(userHead);
        }
        this.userHead = userHead;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        if(listener!=null){
            listener.onUpSucces(userAddress);
        }
        this.userAddress = userAddress;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        if(listener!=null){
            listener.onUpSucces(userSign);
        }
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
        return attention;
    }

    public void setAttention(boolean attention) {
        this.attention = attention;
    }

    public void setUpdateValueListener(OnUpdateValueListener listener){
        this.listener  =listener;
    }

    /**
     * 监听值发生变化的接口
     */
    public interface  OnUpdateValueListener{
        void onUpSucces(String content);
    }

}
