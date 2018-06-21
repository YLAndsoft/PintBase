package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import f.base.BaseFragmentActivity;
import z.pint.R;
import z.pint.adapter.ClassifyViewPagerAdapter;
import z.pint.fragment.PersonalFragment;
import z.pint.fragment.SearchItemFragment;
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
    String tabName []= {"个人","作品"};
    private List<Fragment> fragments = new ArrayList<>();
    private ClassifyViewPagerAdapter classViewPagerAdapter;


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
    public void setListener() {
        userinfo_titile_toback.setOnClickListener(this);
        user_edit.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {
        List<String> list = new ArrayList<>();
        list.add(tabName[0]);
        list.add(tabName[1]);
        PersonalFragment sif = new PersonalFragment();
        UserWorksFragment uwf = new UserWorksFragment();
        fragments.add(sif);
        fragments.add(uwf);
        classViewPagerAdapter = new ClassifyViewPagerAdapter(getSupportFragmentManager(), list, fragments);
        userinfo_pager.setAdapter(classViewPagerAdapter);
        userinfo_pager.setOffscreenPageLimit(tabName.length);//依据传过来的tab页的个数来设置缓存的页数
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
                startActivity(new Intent(mContext,EditInfoActtivity.class));
                break;
        }
    }
}
