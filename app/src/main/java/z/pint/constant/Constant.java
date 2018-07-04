package z.pint.constant;

import android.Manifest;
import android.os.Environment;

import z.pint.R;

/**
 * Created by DN on 2018/6/22.
 */

public class Constant {
    //内存卡的读写权限
    public static final String[] per = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static final String environment = Environment.getExternalStorageDirectory().getPath();

    public static final String DEVELOPER = "mqqwpa://im/chat?chat_type=wpa&uin=";
    public static final String TENCENT_PAKEG = "com.tencent.mobileqq";
    /**
     * 随机用户头像
     */
    public static final String [] USER_HEAD={
            "https://img2.woyaogexing.com/2018/03/30/6d32f03b6d7c06ce!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/85f492bb1950a2a8!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/507000999d798068!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/7cb680d539ff731f!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/8133ed4f8d8977c1!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/b68a17a793e175aa!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/d84b67479ea640a3!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/1376dd33640e6402!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/d52711278f4bc736!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/34ac1e2b28385421!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/6b89cbc5de42d49a!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/bafdcf21e5e6206a!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/599face01ee8fa38!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/01001e4c42ad2992!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/7dbb4a5c78176797!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/d244fe29b68956db!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/43caee57249905cb!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/1fa0f72f9c0fbed1!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/567d8502fefd01d9!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/489f8f887e12de6f!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/b3bdb650d57bc77a!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/bf21dca373d13f4e!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/b99173e21d52e408!250x250.jpg",
            "https://img2.woyaogexing.com/2018/03/30/50f35a9bfa171c5a!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/5307233653d60fb9!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/6da86eef49b72ea1!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/0d5f97d3ae8f7896!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/2faa029fa0ef3289!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/7760a85a816cf1b0!400x400_big.jpg",
            "https://img2.woyaogexing.com/2018/03/30/30b07428c19919b3!400x400_big.jpg",
    };

    /**
     * 显示关注数据标识
     */
    public static final int VIEW_ATTENTION = 1;
    /**
     * 显示粉丝数据标识
     */
    public static final int VIEW_FANS = 2;

}


