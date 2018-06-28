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

}
