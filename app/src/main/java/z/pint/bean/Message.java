package z.pint.bean;

import java.io.Serializable;

/**
 * Created by DN on 2018/7/12.
 */

public class Message implements Serializable{

    private int messageID;//消息ID
    private int messageType;//消息类型
    private int addUserID;//添加消息用户ID
    private int notifyUserID;//通知消息用户ID：被关注人的ID，
    private String messageTime;//消息时间
    private String userName;//用户昵称
    private String userHead;//用户头像
    private String appName;//应用名称
    private String messageContent;//消息内容
    private String messageTitle;//消息标题
    private String messageImage;//消息图片
    public Message(){}
    public Message(int messageType){
        this.messageType = messageType;
    }
    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getAddUserID() {
        return addUserID;
    }

    public void setAddUserID(int addUserID) {
        this.addUserID = addUserID;
    }

    public int getNotifyUserID() {
        return notifyUserID;
    }

    public void setNotifyUserID(int notifyUserID) {
        this.notifyUserID = notifyUserID;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageImage() {
        return messageImage;
    }

    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
    }
}
