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
        public static final int A = 0x111111;
        public static final int B = 0x222222;
        public static final int C = 0x333333;
        public static final int D = 0x444444;
        public static final int E = 0x555555;
        // other more
    }
}
