package z.pint.utils;

import org.greenrobot.eventbus.EventBus;

import z.pint.bean.EventBusEvent;

/**
 * Created by DN on 2018/7/9.
 */

public class EventBusUtils {
    /**
     * 注册
     * @param subscriber
     */
    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 取消注册
     * @param subscriber
     */
    public static void unregister(Object subscriber) {
        if(EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().unregister(subscriber);
        }
    }


    /**
     * 发送消息
     * @param event
     */
    public static void sendEvent(EventBusEvent event) {
        EventBus.getDefault().post(event);
    }
    /**
     * 发送消息
     * @param event
     */
    public static void sendStickyEvent(EventBusEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 通过code码区分事件类型
     */
    public static final class EventCode {
        public static final int A = 11;//已占用，修改用户信息
        public static final int B = 22;//已占用，发布作品
        public static final int C = 33;
        public static final int D = 44;
        public static final int E = 55;
        // other more
    }
}
