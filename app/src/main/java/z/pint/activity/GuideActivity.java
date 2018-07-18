package z.pint.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import f.base.utils.GsonUtils;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import f.base.widget.SVP;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import z.pint.R;
import z.pint.bean.User;
import z.pint.constant.HttpConfig;
import z.pint.utils.DBHelper;
import z.pint.utils.SPUtils;
import z.pint.utils.UserUtils;

/**
 * 启动页
 * Created by DN on 2018/6/29.
 */

public class GuideActivity extends Activity {
    @ViewInject(value = R.id.guide_bg)
    private ImageView guide_bg;

    private Context mContext;
    private static final int PHOTO_PERMISS = 111;
    private static final int ACCESS_FINE_LOCATION_CODE = 222;
    private Geocoder geocoder;//用于获取地区
    private String address;
    private LocationManager locationManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        x.view().inject(this);
        mContext = this;
        guide_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击图片的操作
            }
        });
        initPermission();
    }


    private void initPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            PermissionGen.with(GuideActivity.this)
                    .addRequestCode(PHOTO_PERMISS)
                    .permissions(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE)
                    .request();
        }else{
            initData(mContext);
        }
    }

    public void initData(Context mContext) {
        //生成默认用户,判断是否注册过
        boolean isRegister = (boolean) SPUtils.getInstance(mContext).getParam("isRegister", false);
        loaction();
        if(isRegister){startMain();return;}
        register(UserUtils.getUser(mContext));
    }

    /**
     * 注册
     * @param user
     */
    private void register(final User user){
        if(null==user){return;}
        user.setUserAddress(address);
        Map<String,String> map = new HashMap<>();
        //actionState=0&userInfo=user
        map.put(HttpConfig.ACTION_STATE,HttpConfig.ADD_STATE+"");
        String toJson = new Gson().toJson(user, User.class);
        map.put(HttpConfig.USER_INFO,toJson+"");
        SVP.showWithStatus(mContext,"检测更新中....");
        XutilsHttp.xUtilsPost(HttpConfig.getUserInfoData, map, new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                if(SVP.isShowing(mContext))SVP.dismiss(mContext);
                Log.e("STATE注册成功：",result);
                SPUtils.getInstance(mContext).setParam("isRegister",true);
                //成功，
                User gsonObject = GsonUtils.getGsonObject(result, User.class);
                SPUtils.getInstance(mContext).setParam("userID",gsonObject.getUserID());
                SPUtils.getInstance(mContext).setParam("userName",gsonObject.getUserName());
                SPUtils.getInstance(mContext).setParam("userHead",gsonObject.getUserHead());
                //存入数据库
                boolean b = DBHelper.saveUser(gsonObject);
                startMain();
            }
            @Override
            public void onFail(String result) {
                //失败，直接存入数据库
                if(SVP.isShowing(mContext))SVP.dismiss(mContext);
                //保存登录状态
                SPUtils.getInstance(mContext).setParam("isRegister",false);
                //保存用户ID
                SPUtils.getInstance(mContext).setParam("userID",user.getUserID());
                SPUtils.getInstance(mContext).setParam("userName",user.getUserName());
                SPUtils.getInstance(mContext).setParam("userHead",user.getUserHead());
                Log.e("STATE注册异常：",result);
                boolean b = DBHelper.saveUser(user);
                startMain();
            }
        });

    }

    private void startMain(){
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    @PermissionSuccess(requestCode = PHOTO_PERMISS)
    public void requestPhotoSuccess(){
        //成功之后的处理
        initData(mContext);
    }

    @PermissionFail(requestCode = PHOTO_PERMISS)
    public void requestPhotoFail(){
        //失败之后的处理，我一般是跳到设置界面
        goToSetting(mContext);
    }

    /***
     * 去设置界面
     */
    public static void goToSetting(Context context){
        //go to setting view
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
    /**获取地理位置*/
    private void loaction() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(GuideActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CODE);
                return;
            }
        }
        geocoder = new Geocoder(this);
        //用于获取Location对象，以及其他
        String serviceName = Context.LOCATION_SERVICE;
        //实例化一个LocationManager对象
        locationManager = (LocationManager) this.getSystemService(serviceName);
        String network = LocationManager.NETWORK_PROVIDER;
        /*String gps = LocationManager.GPS_PROVIDER;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);   //高精度
        criteria.setAltitudeRequired(false);    //不要求海拔
        criteria.setBearingRequired(false); //不要求方位
        criteria.setCostAllowed(false); //不允许有话费
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT); */  //低功耗
        //从可用的位置提供器中，匹配以上标准的最佳提供器
        Location location1  =locationManager.getLastKnownLocation(network+"");
        if(location1!=null){
            withNewLocation(location1);
        }
    }

    private void withNewLocation(Location location) {
        String mprovince = "";
        String mcity = "";
        double lat = 0;
        double lng = 0;
        List<Address> addList = null;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            Toast.makeText(GuideActivity.this,"无法获取地理信息",Toast.LENGTH_SHORT).show();
        }
        try {
            addList = geocoder.getFromLocation(lat, lng, 1); // 解析经纬度
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address add = addList.get(i);
                mprovince += add.getAdminArea();
                mcity += add.getLocality();
            }
        }
        if (null!=mprovince&&mprovince.trim().length() != 0) {
            address = mprovince;
        }
        if (null!=mcity&&mcity.trim().length() != 0) {
            address = address+mcity;
        }
        if(!StringUtils.isBlank(address)){
            SPUtils.getInstance(mContext).setParam("address",address+"");
        }else{
            SPUtils.getInstance(mContext).setParam("address","火星");
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
