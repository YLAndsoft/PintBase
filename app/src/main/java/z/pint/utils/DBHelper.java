package z.pint.utils;

import java.util.Date;
import java.util.List;

import f.base.db.DBManager;
import z.pint.bean.Collection;
import z.pint.bean.User;
import z.pint.bean.Works;

/**
 * 对数据库操作辅助类
 * 在建表的时候你的主键设置成自增长，那么你在插入数据的时候直接调replace方法就可以了，
 * 但是saveOrUpdate只能达到插入的效果，达不到更新原有数据的效果
 * 如果在建表的时候你的主键设置成不是自增长，replace方法当然可以插入，
 * saveOrUpdate方法既可以插入也可以达到更新的效果，要注意的是在你更新的时候你的主键要对应的上
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
        //添加/更新
        List<User> userList = DBManager.insert(user, User.class);
        return null!=userList&&userList.size()>0?true:false;
    }

    /**
     * 保存收藏的作品
     * @param works
     * @return
     */
    public static boolean saveCollection(Works works,int collectionUserID){
        if(works==null)return false;
        boolean isExits = selectCollection(works.getWorksID());//查询作品是否已经收藏
        if(isExits)return false;//已经存在，直接返回
        Collection collection = getCollection(works,collectionUserID);//生成收藏类
        if(null==collection)return false;
        List<Collection> userList = DBManager.insert(collection, Collection.class);//保存收藏
        return null!=userList&&userList.size()>0?true:false;
    }

    /**
     * 根据作品ID查询收藏是否存在
     * @param worksID
     * @return boolean
     */
    public static  boolean selectCollection(int worksID){
        List<Collection> collectionList = DBManager.queryClazzKeyValue(Collection.class, "worksID", worksID + "");
        return null==collectionList||collectionList.size()<0?false:true;
    }

    /**
     * 根据作品ID查询收藏是否存在
     * @param worksID
     * @return bean
     */
    public static Collection selectCollectionObject(int worksID){
        List<Collection> collectionList = DBManager.queryClazzKeyValue(Collection.class, "worksID", worksID + "");
        return null==collectionList||collectionList.size()<0?null:collectionList.get(0);
    }
    /**
     * 根据收藏用户ID查询所有收藏
     * @param collUserID
     * @return List
     */
    public static List<Collection> selectCollectionAll(int collUserID){
        List<Collection> collectionList = DBManager.queryClazzKeyValue(Collection.class, "collectionUserID", collUserID + "");
        return null==collectionList||collectionList.size()<0?null:collectionList;
    }

    /**
     * 根据作品ID，取消收藏
     * @param worksID
     * @return
     */
    public static List<Collection> deleteCollection(int worksID){
        Collection collection = selectCollectionObject(worksID);//查询作品
        if(null==collection){return null;}
        List<Collection> coll = DBManager.deleteEntity(Collection.class,collection);
        return coll;
    }




    /**
     * 根据ID得到用户信息
     * @param userID
     * @return
     */
    public static User getUser(int userID){
        List<User> userList = DBManager.queryClazzKeyValue(User.class, "userID", userID);
        return null==userList||userList.size()<=0?null:userList.get(0);
    }
    public static User getUserName(String userName){
        List<User> userList = DBManager.queryClazzKeyValue(User.class, "userName", userName);
        return null==userList||userList.size()<=0?null:userList.get(0);
    }


    private static Collection getCollection(Works works,int collectionUserID){
        try{
            Collection collection = new Collection();
            collection.setCollectionUserID(collectionUserID);
            collection.setUserHead(works.getUserHead());
            collection.setUserName(works.getUserName());
            collection.setCollectionTime(TimeUtils.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            collection.setUserID(works.getUserID());
            collection.setWorksCommentNumber(works.getWorksCommentNumber());
            collection.setWorksImag(works.getWorksImage());
            collection.setWorksDescribe(works.getWorksDescribe());
            collection.setWorksID(works.getWorksID());
            collection.setWorksLikeNumber(works.getWorksLikeNumber());
            collection.setWorksReleaseTime(works.getWorksReleaseTime());
            return collection;
        }catch (Exception ex){
            return null;
        }
    }

}
