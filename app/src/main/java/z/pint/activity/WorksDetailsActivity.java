package z.pint.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import f.base.BaseFragmentActivity;
import f.base.bean.Params;
import f.base.utils.GsonUtils;
import f.base.utils.StringUtils;
import z.pint.R;
import z.pint.adapter.ClassifyViewPagerAdapter;
import z.pint.bean.Collection;
import z.pint.bean.Comment;
import z.pint.bean.EventBusEvent;
import z.pint.bean.Works;
import z.pint.constant.HttpConfig;
import z.pint.fragment.DetailsCommentFragment;
import z.pint.fragment.DetialsLaudFragment;
import z.pint.utils.DBHelper;
import z.pint.utils.EventBusUtils;
import z.pint.utils.SPUtils;
import z.pint.utils.StatisticsUtils;
import z.pint.utils.TimeUtils;
import z.pint.utils.ViewUtils;
import z.pint.view.ButtonPopupWindow;
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
    private int dbUserID;
    private String dbUserName;
    private String dbUserHead;
    private int position;
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
        position = intent.getIntExtra("position",0);
    }
    @Override
    public int bindLayout() {
        return R.layout.activity_works_details;
    }
    @Override
    public void initView(View view) {
        x.view().inject(this,mContextView);
        dbUserID = SPUtils.getUserID(mContext);
        dbUserHead = SPUtils.getUserHead(mContext);
        dbUserName = SPUtils.getUserName(mContext);
    }
    @Override
    public Params getParams() {
        Map<String,String> map  = new HashMap<>();
        map.put(HttpConfig.WORKS_ID,works.getWorksID()+"");
        map.put(HttpConfig.USER_ID,dbUserID+"");
        return new Params(HttpConfig.getWorksDetailsData,map);
    }
    @Override
    protected void setData(String result) {
        if(StringUtils.isBlank(result)){
            setView(works);
            initPager(works);
            return;
        }
        Works gsonObject = GsonUtils.getGsonObject(result, Works.class);
        if(null==gsonObject){
            setView(works);
            initPager(works);
            return;
        }
        setView(gsonObject);
        initPager(gsonObject);
    }

    @Override
    public void initListener() {
        details_back.setOnClickListener(this);
        details_attention.setOnClickListener(this);
        details_ll_comment.setOnClickListener(this);
        details_ll_likes.setOnClickListener(this);
        details_Collection.setOnClickListener(this);
        details_ll_userInfo.setOnClickListener(this);
        send_comment.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {

    }

    private void setView(Works works) {
        if(StringUtils.isBlank(works.getWorksImage())){
            details_works_imag.setVisibility(View.GONE);
            view_image.setVisibility(View.VISIBLE);
            details_worksStrokes.setVisibility(View.GONE);
        }else{
            details_works_imag.setVisibility(View.VISIBLE);
            view_image.setVisibility(View.GONE);
            Glide.with(mContext).load(works.getWorksImage()+"").placeholder(R.mipmap.img_placeholder).error(R.mipmap.bg_man).thumbnail(0.1f).into(details_works_imag);
        }
        ViewUtils.setImageUrl(mContext,details_userHead,works.getUserHead()+"",R.mipmap.default_head_image);
        ViewUtils.setTextView(details_dex,works.getWorksDescribe(),"");
        ViewUtils.setTextView(details_worksStrokes,works.getWorksStrokes()+"笔","0笔");
        ViewUtils.setTextView(details_userName,works.getUserName(),"");
        ViewUtils.setTextView(details_worksTims,works.getWorksReleaseTime(),"");
        //设置标签
        ViewUtils.setLabel(works.getWorksLabel(),details_label1,details_label2,details_label3);
        if(works.getUserID()!=dbUserID){
            ViewUtils.isAttention(works.isAttention(),details_attention);//是否关注
        }else{
            //是自己发布的作品，隐藏关注按钮
            details_attention.setVisibility(View.GONE);
        }
        ViewUtils.isLikes(works.isLikes(),details_toLikes);//是否点赞
        likesTxt.setTextColor(works.isLikes()?getResources().getColor(R.color.details_bg_label_color):getResources().getColor(R.color.gary2));
        boolean b = DBHelper.selectCollection(works.getWorksID());
        ViewUtils.isCollection(b,details_Collection);//是否收藏
    }

    private void initPager(Works works) {
        classViewPagerAdapter = new ClassifyViewPagerAdapter(getSupportFragmentManager(), getTabName(), getFragments());
        details_works_pager.setAdapter(classViewPagerAdapter);
        details_works_pager.setOffscreenPageLimit(getTabName().size());//依据传过来的tab页的个数来设置缓存的页数
        //tabs.setFollowTabColor(true);//设置标题是否跟随
        details_works_tabs.setViewPager(details_works_pager,works.getWorksLikeNumber(),works.getWorksCommentNumber());
    }

    private long collectionTime,attentionTime,likeTime;
    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.details_back:
                finish();
                break;
            case R.id.details_Collection:
                //点击收藏
                if ((System.currentTimeMillis() - collectionTime) > 3000) {
                    collectionTime = System.currentTimeMillis();
                    boolean b = DBHelper.selectCollection(works.getWorksID());
                    if(b){
                        DBHelper.deleteCollection(works.getWorksID());
                        ViewUtils.isCollection(false,details_Collection);//是否收藏
                        showToast("取消收藏");
                    }else{
                        int collectionUserID = (int) SPUtils.getInstance(mContext).getParam("userID",0);
                        DBHelper.saveCollection(works,collectionUserID);
                        ViewUtils.isCollection(true,details_Collection);//是否收藏
                        showToast("收藏成功");
                    }
                }else{
                    showToast("点击不要这么快嘛~");
                }
                break;
            case R.id.details_attention:
                //showToast("点击关注");
                if ((System.currentTimeMillis() - attentionTime) > 3000) {
                    attentionTime = System.currentTimeMillis();
                    boolean isAttention = ViewUtils.isAttention(!works.isAttention(), details_attention);
                    works.setAttention(isAttention);
                    StatisticsUtils.isAttention(mContext,works.isAttention(),works);
                }else{
                    showToast("点击不要这么快嘛~");
                }
                break;
            case R.id.details_ll_comment:
                //showToast("点击评论");
                ll_comment.setVisibility(View.VISIBLE);
                ll_cm_like.setVisibility(View.GONE);
                showInputMethod();//弹窗输入框，
                edit_comment.requestFocus();
                break;
            case R.id.send_comment:
                String trim = edit_comment.getText().toString().trim();
                if(StringUtils.isBlank(trim)){
                    showToast("发送评论内容不能为空!");
                    return;
                }
                edit_comment.setText("");
                hideSoftInput();
                ll_comment.setVisibility(View.GONE);
                ll_cm_like.setVisibility(View.VISIBLE);
                addComment(trim);
                break;
            case R.id.details_ll_likes:
                //showToast("点赞");
                if ((System.currentTimeMillis() - likeTime) > 3000) {
                    likeTime = System.currentTimeMillis();
                    boolean likes = ViewUtils.isLikes(!works.isLikes(), details_toLikes);
                    likesTxt.setTextColor(likes?getResources().getColor(R.color.details_bg_label_color):getResources().getColor(R.color.gary2));
                    int likesNumber = ViewUtils.setLikesNumber(!works.isLikes(), null, works.getWorksLikeNumber());
                    works.setLikes(likes);
                    works.setWorksLikeNumber(likesNumber);
                    details_works_tabs.notifyNumberData(works.getWorksCommentNumber(),works.getWorksLikeNumber());
                    StatisticsUtils.isLikes(mContext,works.isLikes(),works);//作品增加/取消点赞
                }else{
                    showToast("点击不要这么快嘛~");
                }
                break;
            case R.id.details_ll_userInfo:
                Intent userinfo = new Intent(mContext, UserInfoActivity.class);
                userinfo.putExtra("userID",works.getUserID()+"");
                //userinfo.putExtra("works",works);
                startActivity(userinfo);
                break;
        }
    }

    /**
     * 添加评论
     * @param content
     */
    private void addComment(String content) {
        Comment comment = createComment(content);
        if(works.getUserID()==dbUserID){//本地用户，评论保存在本地
            boolean b = DBHelper.saveComment(comment);
            showLog(3,"保存本地评论结果："+b);
            //获取作品
            Works works = DBHelper.queryWorkID(this.works.getWorksID());
            works.setWorksCommentNumber(works.getWorksCommentNumber()+1);
            //通知主界面更新评论数
            EventBusUtils.sendEvent(new EventBusEvent(EventBusUtils.EventCode.C,position,works));
            //修改本地作品评论数
            boolean up = DBHelper.upWorksCommentNumber(works);
            showLog(3,"修改本地评论数量结果："+up);
            details_works_tabs.setViewPager(details_works_pager,works.getWorksLikeNumber(),works.getWorksCommentNumber());
        }else{
            StatisticsUtils.addComment(mContext,works.getWorksID()+"",content);
            //通知主界面更新评论数
            works.setWorksCommentNumber(works.getWorksCommentNumber()+1);
            EventBusUtils.sendEvent(new EventBusEvent(EventBusUtils.EventCode.C,position,works));
            details_works_tabs.setViewPager(details_works_pager,works.getWorksLikeNumber(),works.getWorksCommentNumber());
        }

        //更新评论界面
        dcf.refreshAdapter(comment);

    }

    /**
     * 创建评论
     * @param content
     * @return
     */
    private Comment createComment(String content){
        return new Comment(dbUserID,
        works.getWorksID(),
                dbUserHead,
                dbUserName,
        TimeUtils.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss"),
                content);
    }
    /**
     * 点击软键盘之外的空白处，隐藏软件盘
     * @param ev
     * @return
     */
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }else{ //也有可能是ImageView发送按钮

            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) return true;
        return onTouchEvent(ev);
    }*/
    /**
     * Return whether touch the view.判断点击是否是EditText区域
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right&& event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


    private Fragment getDetailsCommentFragment(){
        if(dcf==null)dcf = new DetailsCommentFragment();
        Bundle b = new Bundle();
        b.putSerializable("works",works);
        dcf.setArguments(b);
        return dcf;
    }
    private Fragment getDetialsLaudFragment(){
        if(dlf==null)dlf= new DetialsLaudFragment();
        Bundle b = new Bundle();
        b.putString("worksID",works.getWorksID()+"");
        dlf.setArguments(b);
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
    @ViewInject(value = R.id.view_image)
    private View view_image;
    @ViewInject(value = R.id.details_ll_userInfo)
    private RelativeLayout details_ll_userInfo;
    @ViewInject(value = R.id.ll_cm_like)
    private LinearLayout ll_cm_like;
    @ViewInject(value = R.id.ll_comment)
    private LinearLayout ll_comment;//评论布局
    @ViewInject(value = R.id.edit_comment)
    private EditText edit_comment;//评论编辑框
    @ViewInject(value = R.id.send_comment)
    private ImageView send_comment;//发送评论

}
