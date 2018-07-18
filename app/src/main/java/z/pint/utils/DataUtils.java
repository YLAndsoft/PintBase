package z.pint.utils;

import java.util.ArrayList;
import java.util.List;

import z.pint.bean.Comment;
import z.pint.bean.Likes;
import z.pint.bean.Message;
import z.pint.bean.Works;

/**
 * Created by DN on 2018/6/27.
 */

public class DataUtils {
    /**
     * 获取评论假数据
     * @return
     */
    public static List<Comment> getCommentData(){
        List<Comment> list = new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add(new Comment("https://pic.qqtn.com/file/2011/2011-7/20117728281108592238.jpg",i+"号",i+"号的评论"));
        }
        return list;
    }

    /**
     * 获取点赞假数据
     * @return
     */
    public static List<Likes> getLikesData(){
        List<Likes> list = new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add(new Likes(i+"号","https://pic.qqtn.com/file/2011/2011-7/20117728281108592238.jpg"));
        }
        return list;
    }

    /**
     * 得到首页假数据
     * @return
     */
    public static List<Works> getListData() {
        List<Works> worksList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            worksList.add(new Works());
        }
        return worksList;
    }

    /**
     * 得到消息假数据
     * @return
     */
    public static List<Message> getMessageData() {
        List<Message> msgs = new ArrayList<>();
        msgs.add(new Message(1));
        msgs.add(new Message(2));
        msgs.add(new Message(1));
        msgs.add(new Message(2));
        msgs.add(new Message(1));
        msgs.add(new Message(2));
        msgs.add(new Message(1));
        return msgs;
    }

}
