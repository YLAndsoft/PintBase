package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import f.base.BaseFragmentActivity;
import f.base.bean.Params;
import z.pint.R;
import z.pint.adapter.ClassifyViewPagerAdapter;
import z.pint.fragment.DetailsCommentFragment;
import z.pint.fragment.DetialsLaudFragment;
import z.pint.view.CuttingImageView;
import z.pint.view.DetailsPagerSlidingTabStrip;

/**
 * Created by DN on 2018/6/26.
 */

public class WorksDetailsActivity extends BaseFragmentActivity {

    private ClassifyViewPagerAdapter classViewPagerAdapter;
    private DetailsCommentFragment dcf;
    private DetialsLaudFragment dlf;

    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(false, R.color.colorActionBar);
        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_works_details;
    }

    @Override
    public void initView(View view) {
        x.view().inject(this,mContextView);
        details_works_imag.setImageResource(R.mipmap.ova);
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
        details_back.setOnClickListener(this);
        details_attention.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {
        classViewPagerAdapter = new ClassifyViewPagerAdapter(getSupportFragmentManager(), getTabName(), getFragments());
        details_works_pager.setAdapter(classViewPagerAdapter);
        details_works_pager.setOffscreenPageLimit(2);//依据传过来的tab页的个数来设置缓存的页数
        //tabs.setFollowTabColor(true);//设置标题是否跟随
        details_works_tabs.setViewPager(details_works_pager,3,100);
    }

    boolean isAttention = false;
    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.details_back:
                finish();
                break;
            case R.id.details_attention:
                if(!isAttention){
                    isAttention=true;
                    details_attention.setImageResource(R.mipmap.unfollow);
                }else{
                    isAttention=false;
                    details_attention.setImageResource(R.mipmap.follow);
                }
                break;


        }
    }


    private Fragment getDetailsCommentFragment(){
        if(dcf==null)dcf = new DetailsCommentFragment();
        return dcf;
    }
    private Fragment getDetialsLaudFragment(){
        if(dlf==null)dlf= new DetialsLaudFragment();
        return dlf;
    }
    private List<Fragment> getFragments(){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(getDetailsCommentFragment());
        fragments.add(getDetialsLaudFragment());
        return fragments;
    }
    private List<String> getTabName(){
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.details_tabName1));
        list.add(getResources().getString(R.string.details_tabName2));
        return list;
    }

    @ViewInject(value = R.id.details_back)
    private ImageView details_back;//返回按钮
    @ViewInject(value = R.id.details_works_pager)
    private ViewPager details_works_pager;
    @ViewInject(value = R.id.details_works_tabs)
    private DetailsPagerSlidingTabStrip details_works_tabs;
    @ViewInject(value = R.id.details_works_imag)
    private ImageView details_works_imag;//作品展示图
    @ViewInject(value = R.id.details_userHead)
    private CuttingImageView details_userHead;//作品用户头像
    @ViewInject(value = R.id.details_dex)
    private TextView details_dex;//作品描述
    @ViewInject(value = R.id.details_worksStrokes)
    private TextView details_worksStrokes;//作品笔画
    @ViewInject(value = R.id.details_label1)
    private TextView details_label1;//作品标签1
    @ViewInject(value = R.id.details_label2)
    private TextView details_label2;//作品标签2
    @ViewInject(value = R.id.details_label3)
    private TextView details_label3;//作品标签3
    @ViewInject(value = R.id.details_userName)
    private TextView details_userName;//作品用户昵称
    @ViewInject(value = R.id.details_worksTims)
    private TextView details_worksTims;//作品发布时间
    @ViewInject(value = R.id.details_attention)
    private ImageView details_attention;//是否已经关注此作品用户
    @ViewInject(value = R.id.details_Collection)
    private TextView details_Collection;//收藏

}
