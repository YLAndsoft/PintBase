package z.pint.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import f.base.utils.RandomUtils;
import z.pint.R;
import z.pint.bean.User;
import z.pint.constant.Constant;

/**
 * 用户生成工具
 * Created by DN on 2018/6/29.
 */

public class UserUtils {

    /**
     * 生成默认用户
     * @param mContext
     * @return
     */
    public static User getUser(Context mContext){
        User user = new User();
        user.setUserSex(0);//默认性别：0：男，1：女
        user.setUserHead(Constant.USER_HEAD[RandomUtils.getRandom(0,Constant.USER_HEAD.length)]);//默认头像
        user.setUserName(mContext.getResources().getString(R.string.defult_userName));//默认昵称
        user.setUserSign(mContext.getResources().getString(R.string.defult_sign));//默认签名
        user.setUserAddress(mContext.getResources().getString(R.string.defult_address));//默认地区
        try{
            //String imei = getIMEI(mContext);
            //String imsi = getIMSI(mContext);
            String imei = getIMEI(mContext);
            String random = RandomUtils.getRandom(imei, 6);
            int parseInt = Integer.parseInt(random);
            user.setUserID(parseInt);
            user.setImei(imei);
        }catch (Exception ex){
            ex.printStackTrace();//getRandomNumbers
            String randomNumbers = RandomUtils.getRandomNumbers(15);
            user.setUserID(Integer.parseInt(RandomUtils.getRandom(randomNumbers+"",6)));
        }
        try{
            String longToString = TimeUtils.longToString(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss");
            user.setRegistrTime(longToString);
        }catch (Exception ex){
            ex.printStackTrace();//getRandomNumbers
            user.setRegistrTime("");
        }
        return user;
    }

    /**
     * 获取手机IMEI号
     */
    private static String getIMEI(Context context) {
        //是由15位数字组成的”电子串号”，其组成结构为TAC（6位数字）+FAC（两位数字）+SNR（6位数字）+SP （1位数字）
        try{
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            //String random = RandomUtils.getRandom(imei, 6);
            //return Integer.parseInt(imei);
            return telephonyManager != null ? telephonyManager.getDeviceId(): RandomUtils.getRandomNumbers(15);
        }catch (Exception ex){
            ex.printStackTrace();
            //考虑到没给权限的时候，获取IMEI失败，造成异常
            return RandomUtils.getRandomNumbers(15);
        }
    }

    /**
     * 获取手机IMSI号
     */
    private static String getIMSI(Context context){
        //是区别移动用户的标志，储存在SIM卡中，可用于区别移动用户的有效信息。其总长度不超过15位，同样使用0～9的数字
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        return imsi ;
    }
}
