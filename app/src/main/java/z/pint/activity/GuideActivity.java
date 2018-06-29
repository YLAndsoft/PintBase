package z.pint.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import f.base.utils.GsonUtils;
import f.base.utils.XutilsHttp;
import f.base.widget.SVP;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import z.pint.MainActivity;
import z.pint.R;
import z.pint.bean.User;
import z.pint.constant.HttpConfig;
import z.pint.utils.DBHelper;
import z.pint.utils.SPUtils;
import z.pint.utils.UserUtils;

/**
 * Created by DN on 2018/6/29.
 */

public class GuideActivity extends Activity {
    @ViewInject(value = R.id.guide_bg)
    private ImageView guide_bg;

    private Context mContext;
    private static final int PHOTO_PERMISS = 111;
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
                            Manifest.permission.READ_PHONE_STATE)
                    .request();
        }else{
            //生成默认用户,判断是否注册过
            boolean isRegister = (boolean) SPUtils.getInstance(mContext).getParam("isRegister", false);
            if(isRegister){startMain();return;}
            initData(mContext);
        }
    }

    public void initData(Context mContext) {
        register(UserUtils.getUser(mContext));
    }

    /**
     * 注册
     * @param user
     */
    private void register(final User user){
        if(null==user){return;}
        Map<String,String> map = new HashMap<>();
        //actionState=0&userInfo=user
        map.put(HttpConfig.ACTION_STATE,HttpConfig.ADD_STATE+"");
        String userJson=new Gson().toJson(user, User.class);
        map.put(HttpConfig.USER_INFO,userJson+"");
        SVP.showWithStatus(mContext,"检测更新中....");
        XutilsHttp.xUtilsPost(HttpConfig.getUserInfoData, map, new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                if(SVP.isShowing(mContext))SVP.dismiss(mContext);
                Log.e("STATE注册成功：",result);
                //成功，
                SPUtils.getInstance(mContext).setParam("isRegister",true);
                User gsonObject = GsonUtils.getGsonObject(result, User.class);
                //存入数据库
                DBHelper.saveUser(gsonObject);
                startMain();
            }

            @Override
            public void onFail(String result) {
                //失败，直接存入数据库
                if(SVP.isShowing(mContext))SVP.dismiss(mContext);
                SPUtils.getInstance(mContext).setParam("isRegister",false);
                Log.e("STATE注册异常：",result);
                DBHelper.saveUser(user);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
