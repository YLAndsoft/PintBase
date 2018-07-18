package z.pint.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import f.base.BaseDialog;
import f.base.BaseFragment;
import f.base.bean.Params;
import f.base.utils.RandomUtils;
import f.base.widget.SVP;
import z.pint.R;
import z.pint.activity.CollectionActivity;
import z.pint.activity.HelperActivity;
import z.pint.activity.MyWorksActivity;
import z.pint.activity.SettingActivity;
import z.pint.activity.UserFAActivity;
import z.pint.activity.UserInfoActivity;
import z.pint.bean.EventBusEvent;
import z.pint.bean.User;
import z.pint.bean.Works;
import z.pint.constant.Constant;
import z.pint.constant.HttpConfig;
import z.pint.utils.DBHelper;
import z.pint.utils.EventBusUtils;
import z.pint.utils.SPUtils;
import z.pint.utils.ViewUtils;

/**
 * 个人界面
 * Created by DN on 2018/6/19.
 */

public class UserFragment extends BaseFragment {


    private User userInfo;
    private int userID;
    private List<Works> worksList;
    private BaseDialog clearCacheDialog;
    @Override
    public int bindLayout() {
        return R.layout.fragment_user_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        //注册EventBus
        EventBusUtils.register(this);
        ViewUtils.setTextView(user_fansTitle,getResources().getString(R.string.user_fansTitle));
        ViewUtils.setTextView(user_attentionTile,getResources().getString(R.string.user_attentionTile));
        ViewUtils.setTextView(user_worksTitle,getResources().getString(R.string.user_worksTitle));
        ViewUtils.setTextView(user_collectionTitle,getResources().getString(R.string.user_collectionTitle));
        ViewUtils.setTextView(user_help_Title,getResources().getString(R.string.user_help_Title));
        ViewUtils.setTextView(user_clearCacheTitle,getResources().getString(R.string.user_clearCacheTitle));
        ViewUtils.setTextView(user_settingTilte,getResources().getString(R.string.user_settingTilte));
        ViewUtils.setTextView(user_chatTitle,getResources().getString(R.string.user_chatTitle));
        rl_user.setOnClickListener(this);
        ll_collect.setOnClickListener(this);
        ll_advise.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_clear.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        ll_gz.setOnClickListener(this);
        ll_fs.setOnClickListener(this);
        ll_zp.setOnClickListener(this);
        userID =  SPUtils.getUserID(mContext);
        //刷新操作
        user_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                Params params = getParams();
                params.setRefresh(true);//刷新操作
                getData(params);
            }
        });
    }

    @Override
    protected void initData() {
        if(userInfo==null){
            userInfo = DBHelper.getUser(userID);//查询数据库
        }
        worksList = DBHelper.selectWorksAll();
        if(null!=worksList&&worksList.size()>0){
            ViewUtils.setTextView(user_worksNumber,worksList.size()+"");
        }else{
            ViewUtils.setTextView(user_worksNumber,"0");
        }
        if(userInfo!=null){
            ViewUtils.setImageUrl(mContext,user_head,userInfo.getUserHead(),R.mipmap.default_head_image);
            ViewUtils.setTextView(user_name,userInfo.getUserName());
            ViewUtils.setTextView(user_sign,userInfo.getUserSign());
            ViewUtils.setSex(user_sex,userInfo.getUserSex());
            ViewUtils.setTextView(user_attentionNumber,userInfo.getAttentionNumber()+"");
            ViewUtils.setTextView(user_fansNumber,userInfo.getFansNumber()+"");
        }else{
            ViewUtils.setTextView(user_name,getResources().getString(R.string.error_name));
            ViewUtils.setTextView(user_sign,getResources().getString(R.string.defult_sign));
            ViewUtils.setTextView(user_attentionNumber,"0");
            ViewUtils.setTextView(user_fansNumber,"0");
        }
    }
    /**
     * 接收用户修改之后的信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING,priority = 99)
    public void upUnserInfo(EventBusEvent event) {
        if(event.getCode()== EventBusUtils.EventCode.A){
            if(event!=null){
                User data = (User) event.getData();
                if(data!=null){
                    ViewUtils.setImageUrl(mContext,user_head,data.getUserHead(),R.mipmap.default_head_image);
                    ViewUtils.setTextView(user_name,data.getUserName());
                    ViewUtils.setTextView(user_sign,data.getUserSign());
                    ViewUtils.setSex(user_sex,data.getUserSex());
                    userInfo=data;
                }
            }
        }

    }
    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.rl_user:
                //showToast("跳转到个人信息界面");
                Intent userinfo = new Intent(mContext,UserInfoActivity.class);
                if(userInfo!=null){
                    userinfo.putExtra("userID",userInfo.getUserID()+"");
                }
                startActivity(userinfo);
                break;
            case R.id.ll_gz:
                //showToast("跳转到关注界面");
                Intent attention = new Intent(mContext, UserFAActivity.class);
                attention.putExtra("VIEW_TAG",Constant.VIEW_ATTENTION);
                attention.putExtra("userID",userID);
                startActivity(attention);
                break;
            case R.id.ll_fs:
                //showToast("跳转到粉丝界面");
                Intent fans = new Intent(mContext, UserFAActivity.class);
                fans.putExtra("VIEW_TAG", Constant.VIEW_FANS);
                fans.putExtra("userID",userID);
                startActivity(fans);
                break;
            case R.id.ll_zp:
                //showToast("跳转到作品界面");
                startActivity(new Intent(mContext,MyWorksActivity.class));
                break;
            case R.id.ll_collect:
                //showToast("跳转到收藏界面");
                startActivity(new Intent(mContext,CollectionActivity.class));
                break;
            case R.id.ll_clear:
                clearCache();//清理缓存
                break;
            case R.id.ll_chat:
                toChat();
                break;
            case R.id.ll_advise:
                //showToast("跳转到帮助界面");
                startActivity(new Intent(mContext,HelperActivity.class));
                break;
            case R.id.ll_setting:
                //showToast("跳转到设置界面");
                Intent setting = new Intent(mContext,SettingActivity.class);
                setting.putExtra("userInfo",userInfo);
                startActivity(setting);
                break;

        }
    }

    @Override
    public Params getParams() {
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        map.put(HttpConfig.USER_ID,userID+"");
        return new Params(HttpConfig.getUserInfoData,map,User.class,false);
    }

    @Override
    protected void showError(String result,boolean isRefresh) {
        if(userInfo==null){
            userInfo = DBHelper.getUser(userID);//查询数据库
        }
        if(isRefresh){
            user_refreshLayout.finishRefresh(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    /**
     * 联系开发者
     */
    private void toChat() {
        if (checkApkExist(mContext, Constant.TENCENT_PAKEG)){
            String url1 = Constant.DEVELOPER+getResources().getString(R.string.developerQQ);
            Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
            i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i1.setAction(Intent.ACTION_VIEW);
            mContext.startActivity(i1);
        }else {
            showToast("请安装QQ！");
        }
    }

    /**
     * 清理缓存
     */
    private void clearCache() {
        if(clearCacheDialog==null){
            clearCacheDialog = createDialog(BaseDialog.DIALOG_DEFAULT_STATE,"清理缓存","是否清理缓存?",0, new BaseDialog.OnDialogClickListener() {
                @Override
                public void onLeftClick(int listenerCode) {
                    if(clearCacheDialog!=null&&clearCacheDialog.isShowing()){
                        clearCacheDialog.dismiss();
                    }
                }
                @Override
                public void onRigthClick(String content, int listenerCode) {
                    if(clearCacheDialog!=null&&clearCacheDialog.isShowing())clearCacheDialog.dismiss();
                    SVP.showWithStatus(mContext,"清理中...");
                    //启动定时器
                    mHandler.postDelayed(runnable, RandomUtils.getRandom(1000,4000));
                }
            });
        }
        if(clearCacheDialog.isShowing()){
            clearCacheDialog.dismiss();
            return;
        }
        clearCacheDialog.show();
    }
    @Override
    protected void setData(Object result,boolean isRefresh) {
        if(isRefresh){
            user_refreshLayout.finishRefresh(true);
        }
        userInfo = (User) result;
        initData();
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
     * 检测是否安装QQ
     * @param context
     * @param packageName
     * @return
     */
    private boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    @ViewInject(value = R.id.rl_user)
    private RelativeLayout rl_user;
    @ViewInject(value = R.id.ll_gz)
    private LinearLayout ll_gz;//关注
    @ViewInject(value = R.id.ll_fs)
    private LinearLayout ll_fs;//粉丝
    @ViewInject(value = R.id.ll_zp)
    private LinearLayout ll_zp;//作品
    @ViewInject(value = R.id.ll_collect)
    private LinearLayout ll_collect;//收藏
    @ViewInject(value = R.id.ll_advise)
    private LinearLayout ll_advise;//帮助
    @ViewInject(value = R.id.ll_setting)
    private LinearLayout ll_setting;//设置
    @ViewInject(value = R.id.ll_clear)
    private LinearLayout ll_clear;//清理缓存
    @ViewInject(value = R.id.ll_chat)
    private LinearLayout ll_chat;//联系我们
    @ViewInject(value = R.id.user_head)
    private ImageView user_head;
    @ViewInject(value = R.id.user_name)
    private TextView user_name;
    @ViewInject(value = R.id.user_sex)
    private ImageView user_sex;
    @ViewInject(value = R.id.user_sign)
    private TextView user_sign;
    @ViewInject(value = R.id.user_refreshLayout)
    private SmartRefreshLayout user_refreshLayout;
    @ViewInject(value = R.id.user_worksNumber)
    private TextView user_worksNumber;
    @ViewInject(value = R.id.user_fansNumber)
    private TextView user_fansNumber;
    @ViewInject(value = R.id.user_attentionNumber)
    private TextView user_attentionNumber;

    @ViewInject(value = R.id.user_attentionTile)
    private TextView user_attentionTile;
    @ViewInject(value = R.id.user_fansTitle)
    private TextView user_fansTitle;
    @ViewInject(value = R.id.user_worksTitle)
    private TextView user_worksTitle;
    @ViewInject(value = R.id.user_collectionTitle)
    private TextView user_collectionTitle;
    @ViewInject(value = R.id.user_help_Title)
    private TextView user_help_Title;
    @ViewInject(value = R.id.user_clearCacheTitle)
    private TextView user_clearCacheTitle;
    @ViewInject(value = R.id.user_settingTilte)
    private TextView user_settingTilte;
    @ViewInject(value = R.id.user_chatTitle)
    private TextView user_chatTitle;






}
