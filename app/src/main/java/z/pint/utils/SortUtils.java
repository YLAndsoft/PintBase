package z.pint.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import z.pint.bean.Message;
import z.pint.bean.Works;

/**
 * 对某些类进行排序
 * Created by DN on 2018/7/11.
 */

public class SortUtils {

    /**
     * 对作品时间排序来显示-降序
     * @param worksList
     */
    public static  void sortList(List<Works> worksList) {
        Comparator<Works> itemComparator = new Comparator<Works>() {
            public int compare(Works info1, Works info2){
                Date data1 = TimeUtils.stringToDate(info1.getWorksReleaseTime(),"yyyy-MM-dd HH:mm:ss");
                Date data2 = TimeUtils.stringToDate(info2.getWorksReleaseTime(),"yyyy-MM-dd HH:mm:ss");
                return data2.compareTo(data1);
            }
        };
        Collections.sort(worksList, itemComparator);
    }

    /**
     * 对消息时间排序-降序
     * @param msgList
     */
    public static  void sortMessageList(List<Message> msgList) {
        Comparator<Message> itemComparator = new Comparator<Message>() {
            public int compare(Message info1, Message info2){
                Date data1 = TimeUtils.stringToDate(info1.getMessageTime(),"yyyy-MM-dd HH:mm:ss");
                Date data2 = TimeUtils.stringToDate(info2.getMessageTime(),"yyyy-MM-dd HH:mm:ss");
                return data2.compareTo(data1);
            }
        };
        Collections.sort(msgList, itemComparator);
    }
}
