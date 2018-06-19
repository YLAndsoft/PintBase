package z.pint.bean;

import java.io.Serializable;

/**
 * 关注类
 * Created by DN on 2018/6/19.
 */

public class Attention implements Serializable {
    private int _id;//id
    private int userID;//用户ID
    private String userName;//用户昵称
    private String userHead;//用户头像
    private boolean isAttention;//是否已关注
}
