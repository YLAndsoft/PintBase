package z.pint.activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import f.base.BaseFragmentActivity;
import f.base.bean.Params;
import z.pint.R;
import z.pint.adapter.ClassifyViewPagerAdapter;
import z.pint.bean.Works;
import z.pint.fragment.DetailsCommentFragment;
import z.pint.fragment.DetialsLaudFragment;
import z.pint.utils.StatisticsUtils;
import z.pint.utils.ViewUtils;
import z.pint.view.CuttingImageView;
import z.pint.view.DetailsPagerSlidingTabStrip;

/**作品详情
 * Created by DN on 2018/6/26.
 */

public class WorksDetailsActivity extends BaseFragmentActivity {
    private ClassifyViewPagerAdapter classViewPagerAdapter;
    private DetailsCommentFragment dcf;
    private DetialsLaudFragment dlf;
    private Works works;
    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(false, R.color.colorActionBar);
        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        //获取首页传递过来的作品
        works = (Works) intent.getSerializableExtra("works");
    }
    @Override
    public int bindLayout() {
        return R.layout.activity_works_details;
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
    protected void setData(String result) {
    }

    @Override
    public void initListener() {
        details_back.setOnClickListener(this);
        details_attention.setOnClickListener(this);
        details_ll_comment.setOnClickListener(this);
        details_ll_likes.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {
        if(works==null){return; }
        Glide.with(context).load(works.getWorksImage()+"").placeholder(R.mipmap.img_placeholder).error(R.mipmap.bg_man).thumbnail(0.1f).into(details_works_imag);
        ViewUtils.setImageUrl(mContext,details_userHead,works.getUserHead()+"",R.mipmap.default_head_image);
        ViewUtils.setTextView(details_dex,works.getWorksDescribe(),"");
        ViewUtils.setTextView(details_worksStrokes,works.getWorksStrokes()+"笔","0笔");
        ViewUtils.setTextView(details_userName,works.getUserName(),"");
        ViewUtils.setTextView(details_worksTims,works.getWorksReleaseTime(),"");
        //设置标签
        ViewUtils.setLabel(works.getWorksLabel(),details_label1,details_label2,details_label3);
        ViewUtils.isAttention(works.isAttention(),details_attention);//是否关注
        ViewUtils.isLikes(works.isLikes(),details_toLikes);//是否点赞
        likesTxt.setTextColor(works.isLikes()?getResources().getColor(R.color.details_bg_label_color):getResources().getColor(R.color.gary2));
        initPager();
    }

    private void initPager() {
        classViewPagerAdapter = new ClassifyViewPagerAdapter(getSupportFragmentManager(), getTabName(), getFragments());
        details_works_pager.setAdapter(classViewPagerAdapter);
        details_works_pager.setOffscreenPageLimit(getTabName().size());//依据传过来的tab页的个数来设置缓存的页数
        //tabs.setFollowTabColor(true);//设置标题是否跟随
        details_works_tabs.setViewPager(details_works_pager,works.getWorksLikeNumber(),works.getWorksCommentNumber());
    }

    private long mkeyTime;
    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.details_back:
                finish();
                break;
            case R.id.details_attention:
                //showToast("点击关注");
                if ((System.currentTimeMillis() - mkeyTime) > 3000) {
                    mkeyTime = System.currentTimeMillis();
                    boolean isAttention = ViewUtils.isAttention(!works.isAttention(), details_attention);
                    works.setAttention(isAttention);
                    StatisticsUtils.isAttention(isAttention,works);
                }else{
                    showToast("点击不要这么快嘛~");
                }
                break;
            case R.id.details_ll_comment:
                showToast("点击评论");
                //details_works_tabs.notifyNumberData(12,12);
                break;
            case R.id.details_ll_likes:
                //showToast("点赞");
                //details_works_tabs.notifyNumberData(11,11);
                if ((System.currentTimeMillis() - mkeyTime) > 3000) {
                    mkeyTime = System.currentTimeMillis();
                    boolean likes = ViewUtils.isLikes(!works.isLikes(), details_toLikes);
                    likesTxt.setTextColor(likes?getResources().getColor(R.color.details_bg_label_color):getResources().getColor(R.color.gary2));
                    int likesNumber = ViewUtils.setLikesNumber(!works.isLikes(), null, works.getWorksLikeNumber());
                    works.setLikes(likes);
                    works.setWorksLikeNumber(likesNumber);
                    details_works_tabs.notifyNumberData(works.getWorksCommentNumber(),works.getWorksLikeNumber());
                    StatisticsUtils.isLikes(works.isLikes(),works);//作品点赞/取消点赞
                }else{
                    showToast("点击不要这么快嘛~");
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
    @ViewInject(value = R.id.details_ll_comment)
    private LinearLayout details_ll_comment;//评论按钮
    @ViewInject(value = R.id.details_toLikes)
    private ImageView details_toLikes;//点赞图标
    @ViewInject(value = R.id.likesTxt)
    private TextView likesTxt;//点赞
    @ViewInject(value = R.id.details_ll_likes)
    private LinearLayout details_ll_likes;//点赞按钮

}
