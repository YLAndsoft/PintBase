package z.pint.utils;

import java.util.List;

import f.base.db.DBManager;
import z.pint.bean.User;

/**
 * 对数据库操作辅助类
 * Created by DN on 2018/6/29.
 */

public class DBHelper {
    private DBHelper() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 根据ID判断用户是否存在
     * @param userID
     * @return true:存在 false:不存在
     */
    private static boolean isUserExist(String userID){
        List<User> userList = DBManager.queryClazzKeyValue(User.class, "userID", userID);
        return null==userList||userList.size()<=0?false:true;
    }
    /**
     * 保存用户信息，如果存在，直接替换
     * @param user
     * @return true:成功 false:失败
     */
    public static boolean saveUser(User user){
        if(null==user){return false;}
        if(isUserExist(user.getUserID()+"")){
            //直接替换
            List<User> update = DBManager.update(user, User.class);
            return null!=update&&update.size()>0?true:false;
        }else{
            //添加
            List<User> userList = DBManager.insert(user, User.class);
            return null!=userList&&userList.size()>0?true:false;
        }
    }

    /**
     * 根据ID得到用户信息
     * @param userID
     * @return
     */
    public static User getUser(String userID){
        List<User> userList = DBManager.queryClazzKeyValue(User.class, "userID", userID);
        return null==userList||userList.size()<=0?null:userList.get(0);
    }
    public static User getUserName(String userName){
        List<User> userList = DBManager.queryClazzKeyValue(User.class, "userName", userName);
        return null==userList||userList.size()<=0?null:userList.get(0);
    }




}
