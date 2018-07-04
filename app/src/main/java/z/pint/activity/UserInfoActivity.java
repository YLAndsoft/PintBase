package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import z.pint.bean.User;
import z.pint.bean.Works;
import z.pint.constant.HttpConfig;
import z.pint.fragment.PersonalFragment;
import z.pint.fragment.UserWorksFragment;
import z.pint.utils.DBHelper;
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
    private int userID;

    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
        userID =  intent.getIntExtra("userID",0);

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView(View view) {
        x.view().inject(this,mContextView);
    }
    @Override
    public Params getParams() {
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        if(userID==0){
            return null;
        }else{
            map.put(HttpConfig.USER_ID,userID+"");
        }
        return new Params(HttpConfig.getUserInfoData, map);
    }
    @Override
    protected void setData(String result) {
        if(StringUtils.isBlank(result)){
            int dbuserID = (int) SPUtils.getInstance(mContext).getParam("userID",0);
            if(userID==dbuserID)user = DBHelper.getUser(userID + ""); //确定是否是自己用户ID，是的情况就去查询本地数据库
            return;
        }
        user = GsonUtils.getGsonObject(result, User.class);
        if(null!=user){
            ViewUtils.setImageUrl(mContext,userinfo_head,user.getUserHead(),R.mipmap.default_head_image);
            ViewUtils.setTextView(userinfo_name,user.getUserName(),getString(R.string.defult_userName));
            ViewUtils.setTextView(userinfo_attention,user.getAttentionNumber()+"关注",0+"关注");
            ViewUtils.setTextView(userinfo_fans,user.getFansNumber()+"粉丝",0+"粉丝");
            ViewUtils.setSex(user_sex,user.getUserSex());
            ViewUtils.setTextView(userinfo_titile_name,user.getUserName(),"");
            classViewPagerAdapter = new ClassifyViewPagerAdapter(getSupportFragmentManager(), getTabName(), getFragments());
            userinfo_pager.setAdapter(classViewPagerAdapter);
            userinfo_pager.setOffscreenPageLimit(2);//依据传过来的tab页的个数来设置缓存的页数
            //tabs.setFollowTabColor(true);//设置标题是否跟随
            userinfo_tabs.setViewPager(userinfo_pager);
        }
    }

    @Override
    public void initListener() {
        userinfo_titile_toback.setOnClickListener(this);
        user_edit.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {

    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.userinfo_titile_toback:
                finish();
                break;
            case R.id.user_edit:
                Intent intent = new Intent(mContext, EditInfoActtivity.class);
                intent.putExtra("userInfo",user);
                startActivity(intent);
                break;
        }
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
        Bundle bundle = new Bundle();
        bundle.putString("userID",user.getUserID()+"");
        uwf.setArguments(bundle);
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
