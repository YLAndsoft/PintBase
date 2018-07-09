package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import f.base.BaseFragmentActivity;
import f.base.bean.Params;
import f.base.utils.GsonUtils;
import f.base.utils.StringUtils;
import z.pint.R;
import z.pint.adapter.ClassifyViewPagerAdapter;
import z.pint.bean.EventBusEvent;
import z.pint.bean.User;
import z.pint.bean.Works;
import z.pint.constant.Constant;
import z.pint.constant.HttpConfig;
import z.pint.fragment.PersonalFragment;
import z.pint.fragment.UserWorksFragment;
import z.pint.utils.DBHelper;
import z.pint.utils.EventBusUtils;
import z.pint.utils.SPUtils;
import z.pint.utils.ViewUtils;
import z.pint.view.PagerSlidingTabStrip;

/**
 * Created by DN on 2018/6/20.
 */

public class UserInfoActivity extends BaseFragmentActivity {
    @ViewInject(value = R.id.userinfo_tabs)
    private PagerSlidingTabStrip userinfo_tabs;
    @ViewInject(value = R.id.userinfo_pager)
    private ViewPager userinfo_pager;
    @ViewInject(value = R.id.userinfo_titile_toback)
    private ImageView userinfo_titile_toback;
    @ViewInject(value = R.id.userinfo_titile_name)
    private TextView userinfo_titile_name;
    @ViewInject(value = R.id.user_edit)
    private ImageView user_edit;
    @ViewInject(value = R.id.userinfo_head)
    private ImageView userinfo_head;
    @ViewInject(value = R.id.userinfo_name)
    private TextView userinfo_name;
    @ViewInject(value = R.id.userinfo_attention)
    private TextView userinfo_attention;
    @ViewInject(value = R.id.userinfo_fans)
    private TextView userinfo_fans;
    @ViewInject(value = R.id.user_sex)
    private ImageView user_sex;

    private PersonalFragment sif;
    private UserWorksFragment uwf;
    private ClassifyViewPagerAdapter classViewPagerAdapter;
    private User user;
    private String userID;//传递过来的用户ID
    private int viewTag;
    private Intent intent;

    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
        this.intent = intent;
        viewTag = intent.getIntExtra("viewTag",0);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView(View view) {
        x.view().inject(this,mContextView);
        EventBusUtils.register(this);
        if(viewTag== Constant.VIEW_OTHER){
            userID =  intent.getStringExtra("userID");
        }else{
            int dbuserID = (int) SPUtils.getInstance(mContext).getParam("userID",0);
            userID = dbuserID+"";
        }
    }
    @Override
    public Params getParams() {
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        if(StringUtils.isBlank(userID)){
            return null;
        }else{
            map.put(HttpConfig.USER_ID,userID+"");
        }
        return new Params(HttpConfig.getUserInfoData, map);
    }
    @Override
    protected void setData(String result) {
        if(StringUtils.isBlank(result)) return;
        user = GsonUtils.getGsonObject(result, User.class);
        setView(user);
        initData(mContext );
    }

    /**
     * 设置View控件的值
     */
    private void setView(User u){
        if(null!=u){
            ViewUtils.setImageUrl(mContext,userinfo_head,u.getUserHead(),R.mipmap.default_head_image);
            ViewUtils.setTextView(userinfo_name,u.getUserName(),getString(R.string.defult_userName));
            ViewUtils.setTextView(userinfo_titile_name,u.getUserName(),getString(R.string.defult_userName));
            ViewUtils.setTextView(userinfo_attention,u.getAttentionNumber()+"关注",0+"关注");
            ViewUtils.setTextView(userinfo_fans,u.getFansNumber()+"粉丝",0+"粉丝");
            ViewUtils.setSex(user_sex,u.getUserSex());
            ViewUtils.setTextView(userinfo_titile_name,u.getUserName(),"");
            if(viewTag== Constant.VIEW_OTHER){
                if(u.isAttention()){
                    user_edit.setImageResource(R.mipmap.follow_1);//修改为已添加关注图标
                }else{
                    user_edit.setImageResource(R.mipmap.follow_0);//修改为添加关注图标
                }
            }else{
                //自己本人
                user_edit.setImageResource(R.mipmap.edit_userinfo);//修改为编辑资料图标
            }
        }
    }
    @Override
    public void initListener() {
        userinfo_titile_toback.setOnClickListener(this);
        user_edit.setOnClickListener(this);
    }

    /**
     * 接收用户修改之后的信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING,priority = 100)
    public void upUnserInfo(EventBusEvent event) {
        //Log.e("TAG", "PostThread: " + Thread.currentThread().getName());
        if(event!=null){
            User data = (User) event.getData();
            if(data!=null){
                setView(data);
                sif.setUserInfo(data);
                //showToast("修改成功！");
            }
        }
    }

    @Override
    public void initData(Context context) {
        if(user!=null){
            classViewPagerAdapter = new ClassifyViewPagerAdapter(getSupportFragmentManager(), getTabName(), getFragments());
            userinfo_pager.setAdapter(classViewPagerAdapter);
            userinfo_pager.setOffscreenPageLimit(getTabName().size());//依据传过来的tab页的个数来设置缓存的页数
            //tabs.setFollowTabColor(true);//设置标题是否跟随
            userinfo_tabs.setViewPager(userinfo_pager);
        }
    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.userinfo_titile_toback:
                finish();
                break;
            case R.id.user_edit:
                if(viewTag== Constant.VIEW_ME) {
                    //跳转至修改信息界面
                    if(user!=null){
                        Intent intent = new Intent(mContext, EditInfoActtivity.class);
                        intent.putExtra("userInfo",user);
                        startActivity(intent);
                    }
                }else{
                    if(user.isAttention()){
                        user.setAttention(false);
                        showToast("取消关注");
                        user_edit.setImageResource(R.mipmap.follow_0);//修改为添加关注图标
                    }else{
                        showToast("添加关注");
                        user.setAttention(true);
                        user_edit.setImageResource(R.mipmap.follow_1);//修改为添加关注图标
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    private Fragment getPersonalFragment(){
        if(sif==null){
            sif = new PersonalFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo",user);
        sif.setArguments(bundle);
        return sif;
    }
    private Fragment getUserWorksFragment(){
        if(uwf==null){
            uwf = new UserWorksFragment();
        }
        if(user!=null){
            Bundle bundle = new Bundle();
            bundle.putString("userID",user.getUserID()+"");
            uwf.setArguments(bundle);
        }
        return uwf;
    }
    private List<String> getTabName(){
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.userinfo_tab_name1));
        list.add(getResources().getString(R.string.userinfo_tab_name2));
        return list;
    }
    private List<Fragment> getFragments(){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(getPersonalFragment());
        fragments.add(getUserWorksFragment());
        return fragments;
    }

}
