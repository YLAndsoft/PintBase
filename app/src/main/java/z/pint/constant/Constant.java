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
     * 显示关注数据标识
     */
    public static final int VIEW_ATTENTION = 1;
    /**
     * 显示粉丝数据标识
     */
    public static final int VIEW_FANS = 2;

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
            "http://img4.imgtn.bdimg.com/it/u=2780812232,2684717238&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=1469859027,540410113&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=592436607,884287772&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2983938786,515044916&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3759866168,1196951677&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=938785118,2357251497&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1471147182,1024138520&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=4111194153,239002974&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3529653024,2678054048&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1714799252,3086292637&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1340165995,1760703310&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2902337451,862734463&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4195864225,1028453985&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1769658390,3063989160&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=630557444,36393826&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2234788172,890314053&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2595196314,2331799035&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=4216217861,2664964801&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3579911808,3344834464&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=320788609,3625248713&fm=27&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2860022117,1288532080&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3248177378,2820118878&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2510926102,204021980&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=1901597061,2553885137&fm=27&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1599875269,1970806622&fm=27&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2349192151,3307870600&fm=27&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=3783963656,1001974911&fm=27&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=1630136267,2922565794&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2177944544,48840392&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2261235992,288318752&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1391141900,2870148546&fm=27&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=252947432,3784613369&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1641621028,2960189600&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1787490582,4228817920&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=234560653,800062660&fm=27&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=4259654474,2415365759&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1020092825,3393099193&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1020092825,3393099193&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=928552729,150994061&fm=27&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=4230928273,3599386546&fm=27&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=924427432,4036562115&fm=27&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=1964877883,4238919556&fm=27&gp=0.jpg",
    };


}


