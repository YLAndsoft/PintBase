package z.pint.utils;

import java.util.Date;
import java.util.List;

import f.base.db.DBManager;
import z.pint.bean.Collection;
import z.pint.bean.Comment;
import z.pint.bean.Likes;
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
        Collection collection = createCollection(works,collectionUserID);//生成收藏类
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

    /**
     * 修改用户
     * @param user
     * @return
     */
    public static boolean updateUser(User user){
        List<User> update = DBManager.update(user, User.class);
        return null==update||update.size()<=0?false:true;
    }

    /**
     * 创建收藏类
     * @param works
     * @param collectionUserID
     * @return
     */
    private static Collection createCollection(Works works,int collectionUserID){
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

    /**
     * 保存发布的作品
     * @param works
     * @return
     */
    public static boolean saveWorks(Works works){
        if(works==null)return false;
        List<Works> worksList = DBManager.save(works, Works.class);//保存作品
        return null!=worksList&&worksList.size()>0?true:false;
    }

    /**
     * 查询所有作品
     * @return
     */
    public static List<Works> selectWorksAll(){
        List<Works> worksList = DBManager.queryAll(Works.class);
        return null!=worksList&&worksList.size()>0?worksList:null;
    }

    /**
     * 根据ID查询作品
     * @param workID
     * @return
     */
    public static Works queryWorkID(int workID){
        List<Works> worksList = DBManager.queryClazzKeyValue(Works.class, "worksID", workID);
        return null!=worksList&&worksList.size()>0?worksList.get(0):null;
    }

    /**
     * 修改作品表里面的用户昵称
     * @param works
     */
    public static boolean updateWorksUserName(Works works){
        List<Works> worksList = DBManager.updateColumn(works,"userName",Works.class);
        return null!=worksList&&worksList.size()>0?true:false;
    }

    /**
     * 修改作品表里面的用户头像
     * @param works
     */
    public static boolean updateWorksHead(Works works){
        List<Works> worksList = DBManager.updateColumn(works,"userHead",Works.class);
        return null!=worksList&&worksList.size()>0?true:false;
    }
    /**
     * 修改作品表里面的评论数
     * @param works
     */
    public static boolean upWorksCommentNumber(Works works){
        List<Works> worksList = DBManager.updateColumn(works,"worksCommentNumber",Works.class);
        return null!=worksList&&worksList.size()>0?true:false;
    }
    /**
     * 修改作品表里面的点赞数
     * @param works
     */
    public static boolean upWorksLikeNumber(Works works){
        List<Works> worksList = DBManager.updateColumn(works,"worksLikeNumber",Works.class);
        return null!=worksList&&worksList.size()>0?true:false;
    }
    /**
     * 修改作品表里面的点赞数
     * @param works
     */
    public static boolean upisLikeNumber(Works works){
        List<Works> worksList = DBManager.updateColumn(works,"isLikes",Works.class);
        return null!=worksList&&worksList.size()>0?true:false;
    }

    /**
     * 保存评论
     * @param comment
     * @return
     */
    public static boolean saveComment(Comment comment){
        if(comment==null)return false;
        List<Comment> commentList = DBManager.save(comment, Comment.class);//保存评论
        return null!=commentList&&commentList.size()>0?true:false;
    }

    /**
     * 查询所有评论
     * @param
     */
    public static List<Comment> queryCommentAll(){
        List<Comment> comments = DBManager.queryAll(Comment.class);
        return null!=comments&&comments.size()>0?comments:null;
    }

    /**
     * 根据作品ID查评论
     * @param worksID
     */
    public static List<Comment> queryComment(int worksID){
        List<Comment> comments = DBManager.queryClazzKeyValue(Comment.class, "worksID", worksID);
        return null!=comments&&comments.size()>0?comments:null;
    }
    /**
     * 修改评论表里面的用户昵称
     * @param comment
     */
    public static boolean updateCommentUserName(Comment comment){
        List<Comment> worksList = DBManager.updateColumn(comment,"userName",Comment.class);
        return null!=worksList&&worksList.size()>0?true:false;
    }

    /**
     * 修改评论表里面的用户头像
     * @param comment
     */
    public static boolean updateCommentHead(Comment comment){
        List<Comment> worksList = DBManager.updateColumn(comment,"userHead",Comment.class);
        return null!=worksList&&worksList.size()>0?true:false;
    }

    /**
     * 查询所有点赞
     * @param
     */
    public static List<Likes> queryLikesAll(){
        List<Likes> likes = DBManager.queryAll(Likes.class);
        return null!=likes&&likes.size()>0?likes:null;
    }
    /**
     * 根据作品ID查点赞
     * @param worksID
     */
    public static List<Likes> queryLikes(String worksID){
        List<Likes> likes = DBManager.queryClazzKeyValue(Likes.class, "worksID", worksID);
        return null!=likes&&likes.size()>0?likes:null;
    }
    /**
     * 保存点赞
     * @param likes
     * @return
     */
    public static boolean saveLikes(Likes likes){
        if(likes==null)return false;
        List<Likes> likeList = DBManager.save(likes, Likes.class);//保存评论
        return null!=likeList&&likeList.size()>0?true:false;
    }
    /**
     * 修改点赞表里面的用户头像
     * @param liks
     */
    public static boolean updateLikesHead(Likes liks){
        List<Likes> lik = DBManager.updateColumn(liks,"userHead",Likes.class);
        return null!=lik&&lik.size()>0?true:false;
    }
    /**
     * 修改点赞表里面的用户昵称
     * @param lks
     */
    public static boolean updateLikesUserName(Likes lks){
        List<Likes> lk = DBManager.updateColumn(lks,"userName",Likes.class);
        return null!=lk&&lk.size()>0?true:false;
    }


}
