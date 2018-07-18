package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import f.base.BaseActivity;
import f.base.BaseDialog;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import f.base.utils.RandomUtils;
import f.base.widget.SVP;
import z.pint.R;
import z.pint.bean.User;
import z.pint.constant.Constant;
import z.pint.utils.ViewUtils;
import z.pint.view.RecyclerViewDivider;

/**
 * Created by DN on 2018/7/4.
 */

public class SettingActivity extends BaseActivity implements BaseRecyclerHolder.OnViewClickListener,BaseDialog.OnDialogClickListener{
    @ViewInject(value = R.id.settings_recycler)
    private RecyclerView settings_recycler;
    @ViewInject(value = R.id.default_titleName)
    private TextView default_titleName;
    private User user;
    private BaseDialog clearCacheDialog;
    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
        user = (User) intent.getSerializableExtra("userInfo");
    }
    @Override
    public int bindLayout() {
        return R.layout.activity_settings;
    }
    @Override
    public void initView(View view) {
        x.view().inject(this);
    }
    @Override
    public void initListener() {
    }
    @Override
    public void initData(Context mContext) {
        ViewUtils.setTextView(default_titleName,getResources().getString(R.string.setting_titelName));
        settings_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        settings_recycler.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL,1,R.color.gary5));
        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(mContext,getStrings(),R.layout.settings_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder holder, String item, int position) {
                holder.setText(R.id.settings_name,item+"");
                holder.setOnViewClick(R.id.settings_root, item, position,SettingActivity.this);
            }
        };
        settings_recycler.setAdapter(adapter);
    }

    @Override
    public void widgetClick(View v) {
    }

    @Override
    public Params getParams() {
        return null;
    }

    @Override
    protected void setData(String result) {
    }

    private List<String> getStrings(){
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.settings_userInfo));
        list.add(getResources().getString(R.string.settings_clearCache));
        list.add(getResources().getString(R.string.settings_developer));
        //list.add(getResources().getString(R.string.setting_about));
        return list;
    }

    @Override
    public void onViewClick(View view, Object object, int position) {
        //showToast("点击了"+(String)object);
        switch (position){
            case 0:
                Intent intent = new Intent(mContext, EditInfoActtivity.class);
                intent.putExtra("userInfo",user);
                startActivity(intent);
                break;
            case 1:
                if(clearCacheDialog==null){
                    clearCacheDialog = createDialog(BaseDialog.DIALOG_DEFAULT_STATE,"清理缓存","是否清理缓存?",position,SettingActivity.this);
                }
                if(clearCacheDialog.isShowing()){
                    clearCacheDialog.dismiss();
                    return;
                }
                clearCacheDialog.show();
                break;
            case 2:
                if (checkApkExist(mContext, Constant.TENCENT_PAKEG)){
                    String url1 = Constant.DEVELOPER+getResources().getString(R.string.developerQQ);
                    Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i1.setAction(Intent.ACTION_VIEW);
                    mContext.startActivity(i1);
                }else {
                    showToast("请安装QQ！");
                }
                break;
            case 3:
                //showToast("关于");
                break;
        }
    }

    /**
     * 检测是否安装QQ
     * @param context
     * @param packageName
     * @return
     */
    private boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }
    /**
     * 创建dialog
     * @param state
     * @param title
     * @param content
     * @param listenerCode
     * @param listener
     * @return
     */
    private BaseDialog createDialog(int state,String title,String content,int listenerCode,BaseDialog.OnDialogClickListener listener){
        return new BaseDialog(mContext, state, title, content, "否", "是", false,listener ,listenerCode);
    }

    /**
     * 创建定时器
     */
    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(SVP.isShowing(mContext)){
                SVP.dismiss(mContext);
            }
        }
    };


    /**
     * 弹窗左边按钮点击事件
     * @param listenerCode
     */
    @Override
    public void onLeftClick(int listenerCode) {
        if(clearCacheDialog!=null&&clearCacheDialog.isShowing()){
            clearCacheDialog.dismiss();
        }
    }
    /**
     * 弹窗右边按钮点击事件
     * @param listenerCode
     */
    @Override
    public void onRigthClick(String content, int listenerCode) {
        if(listenerCode==1){
            if(clearCacheDialog!=null&&clearCacheDialog.isShowing()){
                clearCacheDialog.dismiss();
            }
            SVP.showWithStatus(mContext,"清理中...");
            //启动定时器
            mHandler.postDelayed(runnable,RandomUtils.getRandom(1000,5000));
        }
    }

}
