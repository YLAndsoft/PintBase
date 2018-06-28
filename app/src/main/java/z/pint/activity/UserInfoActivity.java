package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import f.base.BaseFragmentActivity;
import f.base.bean.Params;
import z.pint.R;
import z.pint.adapter.ClassifyViewPagerAdapter;
import z.pint.bean.User;
import z.pint.fragment.PersonalFragment;
import z.pint.fragment.UserWorksFragment;
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
    @ViewInject(value = R.id.user_edit)
    private ImageView user_edit;
    private PersonalFragment sif = null;
    private UserWorksFragment uwf = null;

    private ClassifyViewPagerAdapter classViewPagerAdapter;
    private User user;

    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
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
        return null;
    }

    @Override
    protected void setData(String s) {

    }

    @Override
    public void initListener() {
        userinfo_titile_toback.setOnClickListener(this);
        user_edit.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {
        classViewPagerAdapter = new ClassifyViewPagerAdapter(getSupportFragmentManager(), getTabName(), getFragments());
        userinfo_pager.setAdapter(classViewPagerAdapter);
        userinfo_pager.setOffscreenPageLimit(2);//依据传过来的tab页的个数来设置缓存的页数
        //tabs.setFollowTabColor(true);//设置标题是否跟随
        userinfo_tabs.setViewPager(userinfo_pager);
    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.userinfo_titile_toback:
                finish();
                break;
            case R.id.user_edit:
                user = new User();
                user.setUserName("小酒窝长睫毛");
                user.setUserAddress("湖北武汉");
                user.setUserHead("");
                user.setUserSex(0);
                user.setUserSign("我是一只小小鸟");
                Intent intent = new Intent(mContext, EditInfoActtivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                break;
        }
    }



    private Fragment getPersonalFragment(){
        if(sif==null){sif = new PersonalFragment();}
        return sif;
    }
    private Fragment getUserWorksFragment(){
        if(uwf==null){uwf = new UserWorksFragment();}
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
