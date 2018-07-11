package z.pint.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import f.base.BaseFragment;
import f.base.bean.Params;
import z.pint.R;
import z.pint.activity.CollectionActivity;
import z.pint.activity.MyWorksActivity;
import z.pint.activity.SettingActivity;
import z.pint.activity.UserFAActivity;
import z.pint.activity.UserInfoActivity;
import z.pint.bean.EventBusEvent;
import z.pint.bean.User;
import z.pint.constant.Constant;
import z.pint.constant.HttpConfig;
import z.pint.utils.DBHelper;
import z.pint.utils.EventBusUtils;
import z.pint.utils.SPUtils;
import z.pint.utils.ViewUtils;

/**
 * Created by DN on 2018/6/19.
 */

public class UserFragment extends BaseFragment {
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
    @ViewInject(value = R.id.user_head)
    private ImageView user_head;
    @ViewInject(value = R.id.user_name)
    private TextView user_name;
    @ViewInject(value = R.id.user_sex)
    private ImageView user_sex;
    @ViewInject(value = R.id.user_sign)
    private TextView user_sign;

    private User userInfo;
    private int userID;
    @Override
    public int bindLayout() {
        return R.layout.fragment_user_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        EventBusUtils.register(this);
        rl_user.setOnClickListener(this);
        ll_collect.setOnClickListener(this);
        ll_advise.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_gz.setOnClickListener(this);
        ll_fs.setOnClickListener(this);
        ll_zp.setOnClickListener(this);
        userID = (int) SPUtils.getInstance(mContext).getParam("userID", 0);
    }

    @Override
    protected void initData() {
        if(userInfo==null){
            userInfo = DBHelper.getUser(userID);//查询数据库
        }
        ViewUtils.setImageUrl(mContext,user_head,userInfo.getUserHead(),R.mipmap.default_head_image);
        ViewUtils.setTextView(user_name,userInfo.getUserName(),getResources().getString(R.string.defult_userName));
        ViewUtils.setTextView(user_sign,userInfo.getUserSign(),getResources().getString(R.string.defult_sign));
        ViewUtils.setSex(user_sex,userInfo.getUserSex());
    }
    /**
     * 接收用户修改之后的信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING,priority = 99)
    public void upUnserInfo(EventBusEvent event) {
        //Log.e("TAG", "PostThread: " + Thread.currentThread().getName());
        if(event.getCode()== EventBusUtils.EventCode.A){
            if(event!=null){
                User data = (User) event.getData();
                if(data!=null){
                    ViewUtils.setImageUrl(mContext,user_head,data.getUserHead(),R.mipmap.default_head_image);
                    ViewUtils.setTextView(user_name,data.getUserName(),getResources().getString(R.string.defult_userName));
                    ViewUtils.setTextView(user_sign,data.getUserSign(),getResources().getString(R.string.defult_sign));
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
            case R.id.ll_advise:
                showToast("跳转到帮助界面");
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
        return new Params(HttpConfig.getUserInfoData,map);
    }

    @Override
    protected void showError(String result) {
        if(userInfo==null){
            userInfo = DBHelper.getUser(userID);//查询数据库
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Override
    protected void setData(Object result,boolean isRefresh) {
        userInfo = (User) result;
        initData();
    }

}
