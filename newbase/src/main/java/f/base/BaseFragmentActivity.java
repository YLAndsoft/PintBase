package f.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import org.xutils.x;
import java.util.List;

import f.base.bean.Params;
import f.base.utils.NetworkUtils;
import f.base.utils.XutilsHttp;
import f.base.widget.SystemBarTintManager;


/**
 * Created by DN on 2017/7/22.
 */

public abstract class BaseFragmentActivity extends FragmentActivity implements View.OnClickListener{
    protected Context mContext;
    private FragmentManager fm;
    private Fragment mFragment;
    /** 是否沉浸状态栏**/
    private boolean isSetStatusBar = true;
    /** 是否允许全屏 **/
    private boolean mAllowFullScreen = true;
    /** 是否禁止旋转屏幕 **/
    private boolean isAllowScreenRoate = true;
    /** 是否设置状态栏颜色*/
    private boolean isSetActionBarColor = true;
    /** 当前Activity渲染的视图View **/
    protected View mContextView = null;
    private int mResColor = R.color.transparent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        this.fm = getSupportFragmentManager();
        Intent intent = getIntent();
        initParms(intent);
        try{
            mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
        }catch (Exception ex){
            showToast("绑定布局异常！");
            ex.printStackTrace();
            return;
        }
        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        if (isSetStatusBar) {
            steepStatusBar();
        }
        if (isSetActionBarColor) {
            setActionBarColor(mResColor);
        }
        setContentView(mContextView);
        if (isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        initView(mContextView);
        initListener();
        initData(this);
        Params params = getParams();
        getData(params);
    }
    /**
     * 选择Fragment
     * @param flResId
     * @param fragment
     */
    FragmentTransaction ft;
    protected void switchFragment(int flResId, Fragment fragment) {
        try {
            if ((flResId == 0) || (this.fm == null) || (fragment == null)) {
                return;
            }
            if ((fragment != null) && (this.mFragment != fragment)) {
                ft = this.fm.beginTransaction();
                if (this.mFragment == null) {
                    ft.add(flResId, fragment).commit();
                }else if (!fragment.isAdded()) {
                    ft.hide(this.mFragment).add(flResId, fragment).commit();
                }else {
                    ft.hide(this.mFragment).show(fragment).commit();
                }
                this.mFragment = fragment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 保存Fragment
     * @param flResId
     * @param listFragments
     */
    List<Fragment> listFragments;
    protected void setFragments(int [] flResId, List<Fragment> listFragments) {
        try {
            if ((flResId==null) || (this.fm == null) || (listFragments == null)) {
                return ;
            }
            ft = this.fm.beginTransaction();
            for(int i= 0;i<listFragments.size();i++){
                ft.add(flResId[i], listFragments.get(i));
            }
            ft.commit();
            this.listFragments = listFragments;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 显示Fragment
     * @param flResId
     * @param fragments
     */
    protected void showFragment(int flResId, Fragment fragments) {
        try {
            if ((flResId==0) || (this.fm == null) || (fragments == null)) {
                return ;
            }
            if ((fragments != null) && (this.listFragments != null)) {
                ft = this.fm.beginTransaction();
                for(int i = 0;i<listFragments.size();i++){
                    if(listFragments.get(i)==fragments){
                        ft.show(fragments);
                    }else{
                        ft.hide(listFragments.get(i));
                    }
                }
                ft.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 替换
     * @param flResId
     * @param fragment
     */
    protected void replaceFragment(int flResId, Fragment fragment) {
        if ((flResId == 0) || (this.fm == null) || (fragment == null)) {
            return;
        }
        FragmentTransaction ft = this.fm.beginTransaction();
        ft.replace(flResId, fragment);
        ft.commit();
    }
    /**
     * [初始化参数]
     * @param intent
     */
    public abstract void initParms(Intent intent);

    /**
     * [绑定布局]
     * @return
     */
    public abstract int bindLayout();


    /**
     * [初始化控件]
     * @param view
     */
    public abstract void initView(final View view);


    /**
     * [设置监听]
     */
    public abstract void initListener();

    /**
     * [业务操作]
     * @param mContext
     */
    public abstract void initData(Context mContext);

    public void toBack(View v)
    {
        finish();
    }

    private void setActionBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//通知栏所�?颜色
        }
    }
    /**
     * [沉浸状�?�栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航�?
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /**
     * [吐出Toast]
     * @param msg
     */
    protected void showToast(String msg){
        if(null!=this){
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * [打印日志]
     * @param msg
     */
    protected final String TAG = "FragmentActivityLog信息：";
    protected void showLog(int level,String msg){
        if(!BaseApplication.isLog){return;}
        switch (level){
            case Config.LEVEL_1:
                Log.v(TAG,msg);
                break;
            case Config.LEVEL_2:
                Log.d(TAG,msg);
                break;
            case Config.LEVEL_3:
                Log.e(TAG,msg);
                break;
            default:
                Log.i(TAG,msg);
                break;
        }
    }
    /**
     * [是否允许全屏]
     *
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    /**
     * [是否设置沉浸状�?�栏]
     *
     * @param isSetStatusBar
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }
    /**
     * [是否允许屏幕旋转]
     *
     * @param isAllowScreenRoate
     */
    public void setScreenRoate(boolean isAllowScreenRoate) {
        this.isAllowScreenRoate = isAllowScreenRoate;
    }

    public void setSetActionBarColor(boolean isSetActionBarColor,int color) {
        this.isSetActionBarColor = isSetActionBarColor;
        this.mResColor = color;
    }
    /** View点击 **/
    public abstract void widgetClick(View v);



    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    public void showInputMethod(){
        if (getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(),0);
        }
    }


    /**
     * 获取参数
     * @return
     */
    public abstract Params getParams();


    /**
     * 展示网络数据
     */
    protected abstract void setData(String result);

    /**
     * 获取网络数据
     * @param params
     */
    private void getData(Params params) {
        if(null==params){return;}
        if(!NetworkUtils.isConnected(mContext)){return;}//网络未连接
        XutilsHttp.xUtilsPost(params.getURL(), params.getMap(), new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                setData(result);
            }
            @Override
            public void onFail(String result) {
                setData(result);
            }
        });
    }

}
